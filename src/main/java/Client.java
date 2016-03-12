
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

/**
 * Client.java
 * Lucas Weisensee
 *
 * Connects to AiServer and plays a Game
 */

public abstract class Client {
    // ::GLOBAL VARIABLES::
    public PlayerProfile PROFILE;               // Local players profile info
    public PlayerProfile SERVER;               // Severs connection info
    public Game GAME;                           // game data for current game
    public List<Card> HAND;                     // Local player's hand
    public GameState GAMESTATE;                 // most recent game's state

    // ::ABSTRACT FUNCTIONS::
    public abstract Card getNextPlay();                         // gets the next play from the player
    public abstract void processNewPlay(GameState currentGame); // allows the player to handle the most recent play
    public abstract void placeMaxBid();                         // bid on hand by placing max bid
    public abstract int pickGameToJoin(ArrayList<Game> gameList);       // return the clients choice of game to join
    public abstract PlayerProfile getNameAndId();                        // sets up the global PlayerProfile info

    // Client constructor
    public Client() {
        // initiate PlayerProfile
        PROFILE = new PlayerProfile();
        SERVER = new PlayerProfile();
    }

    // Starts the Client with given arguments
    // sets up connection then joins/creates and plays the game
    public void run(int port) {
        // setup connections and global variables
        initiateConnection(port);

        // Set name and ID
        loginToServer(getNameAndId());

        // join or create a new game
        joinOrCreateGame();

        // Wait for game to be ready
        waitForReadyGame();

        // play game with server
        playGame();

    }

    // logs the player into the server
    private void loginToServer(PlayerProfile newPlayer) {
        Message toSend = new Message();
        toSend.createPlayerProfileMsg(newPlayer);
        SERVER.sendMsg(toSend);
    }

    // Initiates the global settings and variables then sets up the connection to server
    public void initiateConnection(int port) {
        System.out.println("Starting Client on port:" + port);

        // Setup socket and stream readers
        try {
            // bind server socket
            SERVER.setSocket(new Socket(InetAddress.getByName(Settings.SERVER_ADDRESS), port));
        } catch (IOException e) {
            System.err.println("ERROR initiating streams:" + e.toString());
            e.printStackTrace();
        }
    }

    // respond to server's query: join an existing game or create new game
    public void joinOrCreateGame() {
        // retrieve active game list
        ArrayList<Game> gameList = getActiveGameList();

        // retrieve client's game choice

        PROFILE.setGameId(pickGameToJoin(gameList));

        // Send PROFILE Message object as JSON string to server
        Message toSend = new Message();
        toSend.createPlayerProfileMsg(PROFILE);
        SERVER.sendMsg(toSend);

        // verify game is joined a GameState from newly joined game
        Message gameJoinedMsg = SERVER.getMessage("waiting for game joined confirmation");
        if (gameJoinedMsg.isGameState()) {
            GAMESTATE = gameJoinedMsg.getGameState();
            System.out.println("Game Joined: " + GAMESTATE.getGameId());
        }
        else {
            System.err.println("game joined confirmation message not received!");
            System.err.println("Message received: " + gameJoinedMsg);
        }

    }

    // Returns a ArrayList containing all active games
    // Returns null on failure
    private ArrayList<Game> getActiveGameList() {
        Message gameChoice = SERVER.getMessage("reading game list from server");
        return gameChoice.getGameList();                    // extract gamelist array
    }

    // Waiting room for game to be ready
    // TODO: implement chat feature
    public void waitForReadyGame() {
        boolean ready = false;
        System.out.println("Waiting for enough players to join...");

        // wait for game to be ready before joining
        while(!ready) {
            Message msg = SERVER.getMessage("game status update in waiting queue");

            // if message is status message respond accordingly
            if (msg.isGameStatus()) {
                if (msg.gameIsStarting())
                    ready = true;   // Time to play!
                else if (msg.waitingForPlayers())
                    System.out.print(" ...still waiting... ");
            }

            // if not status message, throw error
            else {
                System.err.println("Non-status message received in waitForReadyGame");
                break;
            }
        }
    }

    // play pinochle with current game
    // high level
    public void playGame() {
        // collect cards from server/dealer
        getDealtHand();

        // bid on hand by placing max bid
        placeMaxBid();

        // play meld from hand for points
        playMeld();

        // play through tricks
        playTricks();
    }

    // Returns true if argument is a valid active game choice
    public boolean isAvailableGame(int gameChoice, ArrayList<Game> gameList) {
        // if game choice is out of gameID bounds, return false
        if (0 > gameChoice)
            return false;

        // check that gameList contains game with chosen I

        // TODO: implement more strict check:
        return true;
//        return (gameList.containsValue(gameChoice));
    }

    // returns a new unique player ID number, a positive integer !=0
    // TODO: setup database for userid's to ensure no duplicates
    public int getNewId() {
        Random r = new Random();       // currently returns a randomly generated ID
        return 1+ r.nextInt(99999);       // adds one to avoid returning 0
    }

    // plays all available meld in player's hand
    public void playMeld() {                            // play meld from hand for points
        // if meld can be played, play it
    }

    // Plays the given card in the current Game
    public void playCard(Card toPlay) {

    }

    public void joinGame(String toJoin) {

    }

    public void leaveGame() {
    }

    // TODO: review Game.java and implement dealt hand catcher here THEN TEST!
    // collect cards from server/dealer
    public void getDealtHand() {
        // wait for server to send the game data, dealt cards, etc
        Message handDealt = SERVER.getMessage("get dealt hand");


        System.out.println("Recieved Dealt Hand: " + handDealt.toString());

        // convert dealt hand String to JSON


        // Add cards to HAND list

    }

    // play through tricks
    public void playTricks() {

    }

}
