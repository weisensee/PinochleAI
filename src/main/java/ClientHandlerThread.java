import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.json.simple.JSONObject;

/**
 * ClientHandlerThread.java
 * Luke Weisensee
 *
 * Handles a new Client's connection to the Game's Server
 */
public class ClientHandlerThread extends Thread {
    public PlayerProfile PLAYER;            // current player's profile
    public Socket clientSocket;             // thread's client's socket
    public DataInputStream inputStream;     // input stream from client
    public DataOutputStream outputStream;   // output stream to client
    public BlockingQueue joiningGameQueue;  // blocking queue of players waiting to join another game
    public BlockingQueue activeGames;       // blocking queue of active games
//    public Game GAME;                       // local game that is being played

    public ClientHandlerThread(Socket newClient, BlockingQueue joiningQueue, BlockingQueue gameList) {
        super("ClientHandlerThread");

        // Initialize new player
        this.PLAYER = new PlayerProfile();

        // Save the socket for the current player
        this.clientSocket = newClient;

        // save players joining game queue
        this.joiningGameQueue = joiningQueue;

        // save active game list
        activeGames = gameList;
    }

    // Queries client for game choice and connects them to that game
    public void run() {
        System.out.println("new thread started");
        setupClientStreamReaders();     // initiates stream readers for communicating with client
        // TODO: decide when to start using PLAYER.getSocket() and then quit using 'clientSocket'
//        PLAYER.SOCKET = clientSocket;   // setup current player with the new socket

        // query client for choice (create or join)
        int answer = joinOrCreateQuery();

        // execute decision
        if (0 == answer)
            createNewGame();

        // join existing game if player chose to
        else
            joinGame(answer);
    }

    // Prints String argument to output stream and flushes stream
    // TODO: have the same communication protocol in Client and Client handler!
    private void printToOutputStream(String toPrint) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(toPrint);
        printWriter.flush();
    }

    // Queries client: join existing game or create a new one
    // Sets player profile info TODO: think about setting this up as separate methor or renaming
    // Returns: game ID of answer or 0 for create new
    public int joinOrCreateQuery() {
        int answer = -1;    // initialize to negative, all game IDs are >0
        Message gameListMsg = new Message();

        // generate and and send the active game list
        gameListMsg.createGameListMessage(getActiveGameArray());
        printToOutputStream(gameListMsg.getString());

        // Get JSONObject String of Player's Profile from client
        try {
            // read in message from inputStream
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            Message gameChoice = new Message(in.readLine());

            // TODO: remove debug statement:
            System.out.println("JSON RECEIVED::" + gameChoice.print());

            // if game choice message is invalid throw error and quit
            // TODO: respond appropriately when player disconnects, launch a disconnect function?
            if (!gameChoice.isValidJson())
                throw new IOException("Null message received instead of game choice, player disconnected?");

            // Set local player profile from message
            PLAYER = gameChoice.getOwner();

            // get game choice from message
            answer = gameChoice.getGameId();

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

    public void setupClientStreamReaders() {
        try {
            // initiate stream readers
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.out.println("error:" + e.toString());
            e.printStackTrace();
        }
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
        PLAYER.setSocket(clientSocket);
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

    }
}
