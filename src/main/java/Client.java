
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

/**
 * Client.java
 * Lucas Weisensee
 *
 * Connects to AiServer and plays a Game
 * Parent class for actual players: Human and AI
 */

public abstract class Client {
    // ::GLOBAL VARIABLES::
    public PlayerProfile PROFILE;       // Local player's profile info
    public PlayerProfile SERVER;        // Sever's connection info
    public Game GAME;                   // game data for current game
    public Hand HAND;                   // Local player's hand

    // ::ABSTRACT FUNCTIONS::
    public abstract Card getNextPlay();                         // gets the next play from the player
    public abstract void processNewPlay(GameState currentGame); // allows the player to handle the most recent play
    public abstract int getMaxBid();           // returns the clients chosen max bid
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
        GAME = new Game();
        GAME.setId(PROFILE.getGameId());

        // Send PROFILE Message object as JSON string to server
        Message toSend = new Message();
        toSend.createPlayerProfileMsg(PROFILE);
        SERVER.sendMsg(toSend);

        // verify game is joined a GameState from newly joined game
        Message gameJoinedMsg = SERVER.getMessage("waiting for game joined confirmation");
        if (gameJoinedMsg.isGame()) {
            Game msg = gameJoinedMsg.getGame();
            if (GAME.getId() == msg.getId())
                GAME.update(msg);
            System.out.println("Game Joined: " + GAME.getName() + " with ID:" + GAME.getId());
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

    // Waiting room for enough players to join the game
    // TODO: implement chat feature for players waiting
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
                System.err.println("Non-status message received in waitForReadyGame:" + msg.toString());
                break;
            }
        }
    }

    // plays through one round in the current game
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
    // retrieves the player's max bid and sends it to the server as a message
    private void placeMaxBid() {
        // get bid from player or AI
        int bid = getMaxBid();

        // send "max bid" message to server

    }
    // Returns true if argument is a valid active game choice or new game
    public boolean isAvailableGame(int gameChoice, ArrayList<Game> gameList) {
        // if game choice is out of gameID bounds, return false
        if (0 > gameChoice)
            return false;

        // return true if 'new game' is chosen
        if (gameChoice == 0)
            return true;

        // check list for chosen game
        for(int i = 0; i < gameList.size(); i++)
            if (gameList.get(i).getId() == gameChoice)
                return true;

        // return false if gameChoice is not an available game
        return false;
    }

    // returns a new unique player ID number, a positive integer !=0
    // temporary measure until user management is better established server-side
    // TODO: setup database for userid's to ensure no duplicates
    public int getNewId() {
        Random r = new Random();       // currently returns a randomly generated ID
        return 1+ r.nextInt(99999);       // adds one to avoid returning 0
    }

    // plays all available meld in player's hand
    public void playMeld() {                            // play meld from hand for points
        // if meld can be played, play it!
    }

    // collect cards from server/dealer, save to hand
    public void getDealtHand() {
        // wait for server to send the game data, dealt cards, etc
        Message handDealt = SERVER.getMessage("get dealt hand");
        HAND = handDealt.getHandDealt();

        System.out.println("Recieved Dealt Hand: " + handDealt.toString());
    }

    // play through tricks
    public void playTricks() {

    }

}
