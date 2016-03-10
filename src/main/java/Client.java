import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    public static int DEFAULT_PORT = 9999;      // default port for Server
    public int PORT_NUMBER;                     // Port number to listen for new connections on
    public String SERVER_ADDRESS = "0.0.0.0";   // default server address, localhost
    public Socket SERVER;                       // game server's socket
    public Game GAME;                  // game data for current game
    public BufferedReader inputStream;          // server socket reading stream
    public DataOutputStream outputStream;       // server socket writing stream
    public PlayerProfile PROFILE;               // Local players profile info
    public List<Card> HAND;                     // Local player's hand
    public GameState GAMESTATE;                 // most recent game's state

    // ::ABSTRACT FUNCTIONS::
    public abstract Card getNextPlay();                         // gets the next play from the player
    public abstract void processNewPlay(GameState currentGame); // allows the player to handle the most recent play
    public abstract void placeMaxBid();                         // bid on hand by placing max bid
    public abstract int pickGameToJoin(ArrayList<Game> gameList);       // return the clients choice of game to join
    public abstract void setNameAndId();                        // sets up the global PlayerProfile info

    // Client constructor
    public Client() {
        // initiate PlayerProfile
        PROFILE = new PlayerProfile();
    }

    // Starts the Client with given arguments
    // sets up connection then joins/creates and plays the game
    public void run(int port) {
        // setup connections and global variables
        initiateConnection(port);

        // Set name and ID
        setNameAndId();

        // join or create a new game
        joinOrCreateGame();

        // Wait for game to be ready
        waitForReadyGame();

        // play game with server
        playGame();

    }

    // Initiates the global settings and variables then sets up the connection to server
    public void initiateConnection(int port) {
        // set port for Client to use
        PORT_NUMBER = port;
        System.out.println("Starting Client on port:" + PORT_NUMBER);

        // Setup socket and stream readers
        try {
            // bind server socket
            SERVER = new Socket(InetAddress.getByName(SERVER_ADDRESS), PORT_NUMBER);

            // initiate stream readers
            inputStream = new BufferedReader(new InputStreamReader(SERVER.getInputStream()));
            outputStream = new DataOutputStream(SERVER.getOutputStream());

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

        // create response message for server
        Message gameChoice = new Message();
        gameChoice.setOwner(PROFILE);

        // Send PROFILE JSON object as string to server
        printToOutputStream(gameChoice.getString());

        // verify game is joined
        Message gameJoined = getNewMessage("waiting for game joined confirmation");
        if (gameJoined.isGameState()) {
            GAMESTATE = gameJoined.getGameState();
            System.out.println("Game Joined: " + GAMESTATE.getGameId());
        }
    }

    // Returns a ArrayList containing all active games
    // Returns null on failure
    private ArrayList<Game> getActiveGameList() {
        Message gameChoice = getNewMessage("reading game list from server");
        return gameChoice.getGameList();                    // extract gamelist array
    }

    // Waiting room for game to be ready
    // TODO: implement chat feature
    public void waitForReadyGame() {
        boolean ready = false;
        System.out.println("Waiting for enough players to join...");

        // wait for game to be ready before joining
        while(!ready) {
            Message msg = getNewMessage("game status update in waiting queue");

            // if message is status message respond accordingly
            if (msg.isGameStatus()) {
                if (msg.getGameStatus() == Settings.GAME_STARTING)
                    ready = true;   // Time to play!
                else if (msg.getGameStatus() == Settings.WAITING_FOR_PLAYERS)
                    System.out.print(" ...still waiting... ");
            }

            // if not status message, throw error
            else {
                System.err.println("Non-status message received in waitForReadyGame");
                break;
            }
        }
    }

    // returns the next message from the server
    // prints the purpose in the error if one should occur
    public Message getNewMessage(String purpose) {
        Message toReturn = null;
        try {
            toReturn = new Message(inputStream.readLine());
        } catch (IOException e) {
            System.err.println("Error in getNewMessage for: " + purpose);
            System.err.println(e);
            System.exit(-1);        // quit on error!
        }

        return toReturn;
    }

    // Convert string to JSONObject Map
    public HashMap getHashFromString(String toConvert) {
        JSONObject jsonObj = new JSONObject();

        // convert to JSONObject
        try {
            JSONParser parser = new JSONParser();      // create parser for JSON-simple
            Object temp = parser.parse(toConvert);   // parse gameList obj from server
            jsonObj = (JSONObject)temp;
        } catch (ParseException e) {
            System.err.println("ERROR parsing active game list from server" + e);
            e.printStackTrace();
        }

        // Create HashMap from JSONObject
        HashMap<String, Integer> gameListHashMap = new HashMap();     // create HashMap to return
        Set<String> keySet = jsonObj.keySet();                          // get list of keys in JSONObject
        Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()) {
            String currentKey = iterator.next();    // key for current item in JSONObj
            gameListHashMap.put(currentKey, Integer.parseInt(jsonObj.get(currentKey).toString()));  // add <str, int>

            // TODO: remove debugging statements
            System.out.println("getHashFromString added ID:" + gameListHashMap.get(currentKey) + " named:" + currentKey);
        }

        // return newly created JSONObject
        return gameListHashMap;
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
        Message handDealt = getNewMessage("get dealt hand");


        System.out.println("Recieved Dealt Hand: " + handDealt.toString());

        // convert dealt hand String to JSON


        // Add cards to HAND list

    }


    // Prints String argument to output stream and flushes stream
    // TODO: have the same communication protocol in Client and Client handler!
    private void printToOutputStream(String toPrint) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(toPrint);
        printWriter.flush();
    }


    // play through tricks
    public void playTricks() {

    }

}
