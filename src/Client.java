import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

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
    public Game PINOCHLE_GAME;                  // game data for current game
    public BufferedReader inputStream;         // server socket reading stream
    public DataOutputStream outputStream;       // server socket writing stream

    // ::ABSTRACT FUNCTIONS::
    public abstract Card getNextPlay();                         // gets the next play from the player
    public abstract void processNewPlay(GameState currentGame); // allows the player to handle the most recent play
    public abstract void placeMaxBid();                         // bid on hand by placing max bid
    public abstract int pickGameToJoin(String query);           // return the clients choice of game to join

        // Starts the Client with given arguments
    // sets up connection then joins/creates and plays the game
    public void run(int port) {
        // setup connections and global variables
        initiateConnection(port);

        // join or create a new game
        joinOrCreateGame();

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

    // respond to server's query: join existing game or create new game
    public void joinOrCreateGame() {
        String defaultQuery = "SERVER QUERY: Which Game would you like to join?";   // what the server should be asking
        String query = "control";

        // Retrieve query from server
        try {
            query = inputStream.readLine();
        } catch (IOException e) {
            System.err.println("ERROR reading join/create query from server:" + e);
            e.printStackTrace();
        }

        // check that query format is correct
        if (!query.contains(defaultQuery))
            System.err.println("ERROR: join/create query not as expected");

        // get answer from client
        int answer = pickGameToJoin(query);

        // Send answer to server
        System.out.println("Sending game choice to server: " + answer);
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println((answer));
        printWriter.flush();
    }

    // play pinochle with current game
    // high level
    public void playGame() {
        // collect cards from server/dealer
        getGameData();

        // bid on hand by placing max bid
        placeMaxBid();

        // play meld from hand for points
        playMeld();

        // play through tricks
        playTricks();
    }


//        String string = "testing1234";
//        System.out.println("sending: " + string);
//
//        try {
//
//            // bind socket
//            server = new Socket(InetAddress.getByName("0.0.0.0"), PORT_NUMBER);
//
//            // initiate stream readers
//            DataInputStream inputStream = new DataInputStream(SERVER.getInputStream());
//            DataOutputStream outputStream = new DataOutputStream(SERVER.getOutputStream());
//
//            // write to stream
//            PrintWriter printWriter = new PrintWriter(outputStream);
//            printWriter.println();
//            printWriter.flush();
//
//            // read from stream
//            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//            String string2 = in.readLine();
//            System.out.println("recieved from server:" + string2);
//
//
//
//        } catch (IOException e) {
//            System.out.println("error:" + e.toString());
//            e.printStackTrace();
//        }

//        Card toPlay = getNextPlay();
//        playCard(toPlay);

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

    // collect cards from server/dealer
    public void getGameData() {

    }



    // play through tricks
    public void playTricks() {

    }

}
