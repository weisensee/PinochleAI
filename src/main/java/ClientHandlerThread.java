import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * ClientHandlerThread.java
 * Luke Weisensee
 *
 * Handles a new Client's connection to the Game's Server
 */

public class ClientHandlerThread extends Thread {
    private PlayerProfile PLAYER;            // current player's profile
    public BlockingQueue joiningGameQueue;  // blocking queue of players waiting to join another game
    public BlockingQueue activeGames;       // blocking queue of active games
//    public Game GAME;                       // local game that is being played

    public ClientHandlerThread(Socket newClient, BlockingQueue joiningQueue, BlockingQueue gameList) {
        super("ClientHandlerThread");

        // Initialize new player
        this.PLAYER = new PlayerProfile();

        // Save the socket for the current player
        this.PLAYER.setSocket(newClient);

        // save players joining game queue
        this.joiningGameQueue = joiningQueue;

        // save active game list
        activeGames = gameList;
    }

    // TODO: respond appropriately when player disconnects, launch a disconnect function?
    // Queries client for game choice and connects them to that game
    public void run() {
        System.out.println("new thread started");

        getPlayerInfo();

        // query client for choice (create or join)
        int answer = joinOrCreateQuery();

        // execute decision
        if (0 == answer)
            createNewGame();

        // join existing game if player chose to
        else
            joinGame(answer);
    }

    // gets the player's profile info, return a PlayerProfile object or null if failed
    // TODO: implement loading user data and assigning individual ids
    private void getPlayerInfo() {
        // Get JSONObject String of Player's Profile from client
        try {
            // read in message from inputStream
            Message newPlayer = PLAYER.getMessage("getting player profile");

            // if game choice message is invalid throw error and quit
            // TODO: respond appropriately when player disconnects, launch a disconnect function?
            if (!newPlayer.isValidJson() || ! newPlayer.isPlayerProfile())
                throw new IOException("PlayerProfile not received correctly! ->" + newPlayer);
            else {
                PlayerProfile newProfile = newPlayer.getPlayerProfile();
                PLAYER.setName(newProfile.getName());
                PLAYER.setId(newProfile.getId());
            }

        } catch (IOException e) {   // catch reading IO error
            System.err.println("ERROR reading PlayerProfile from client" + e);
            e.printStackTrace();
        }
    }


    // Queries client: join existing game or create a new one
    // Returns: game ID of answer or 0 for create new
    public int joinOrCreateQuery() {
        int answer = -1;    // initialize to negative, all game IDs are >0

        // generate and and send the current active game list
        Message toSend = new Message();
        toSend.createGameListMsg(getActiveGameArray());
        PLAYER.sendMsg(toSend);

        // Get JSONObject String of Player's Profile from client
        try {
            // read in message from inputStream
            PlayerProfile updatedProfile = new PlayerProfile();
            updatedProfile = PLAYER.getMessage("getting game join choice").getPlayerProfile();

            // get game choice from message
            answer = updatedProfile.getGameId();

            // check that current player's info is correct
            if(!updatedProfile.getName().equals(PLAYER.getName()) || updatedProfile.getId() != PLAYER.getId())
                throw new IOException("Player info mismatch for game choice message");

        } catch (IOException e) {   // catch reading IO error
            System.err.println("ERROR reading join/create answer from client" + e);
            e.printStackTrace();
        }

        // TODO: remove debug statement:
        System.out.println("Client game choice: " + answer);

        // check that answer is valid (all valid choices must be positive (join game), or == 0 (create new)
        if (answer < 0)
            System.err.println("ERROR: Client answer:" + answer + " is not in bounds");
        return answer;
    }

    // generates a JSONObject containing the list of active games
    private Game[] getActiveGameArray() {
        // generate snapshot of current active games
        Game[] activeGameSnapshot = new Game[Settings.MAX_GAMES];
        activeGames.toArray(activeGameSnapshot);

        return activeGameSnapshot;
    }

    // Guides client through new game creation
    public void createNewGame() {
        System.out.println("Client has chosen to create a new game");

        // create game with new gameID and default name
        int newId = getNextGameId();
        Game currentGame = new Game(newId, joiningGameQueue);
        currentGame.setName(getGameName());

        // update players game id and add them to game joining queue
        PLAYER.setGameId(newId);
        joiningGameQueue.add(PLAYER);

        // add to active game list and launch
        activeGames.add(currentGame);
        currentGame.launch();

    }

    // returns the name for the current game
    private String getGameName() {
        // Set game name to [1st player]'s game
        return PLAYER.getName() + "'s Game";
    }


    // Returns the next available game ID
    // TODO: setup a simple database to ensure game IDs are generated sequentially
    public int getNextGameId() {
        Random r = new Random();        // currently returns a randomly generated ID
        return 1 + r.nextInt(99999);    // adds one to avoid returning 0
    }

    // Allows client to join new game
    // TODO: check that players are not waiting in queue for a game that is full/in progress
    public void joinGame(int toJoin) {
        System.out.println("Client has chosen to join existing game: " + toJoin);
        PLAYER.setGameId(toJoin);
        joiningGameQueue.add(PLAYER);
    }
}
