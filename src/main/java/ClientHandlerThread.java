import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.json.simple.JSONObject;

/**
 * ClientHandlerThread.java
 * Lucas Weisensee
 *
 * Handles a new Client's connection to the Game's Server
 */
public class ClientHandlerThread extends Thread {
    public PlayerProfile PLAYER;            // current player's profile
    public Socket clientSocket;             // thread's client's socket
    public DataInputStream inputStream;     // input stream from client
    public DataOutputStream outputStream;   // output stream to client
    public BlockingQueue joiningGameQueue; // blocking queue of players waiting to join another game
    public BlockingQueue activeGames;       // blocking queue of active games

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
        PLAYER.SOCKET = clientSocket;   // setup current player with the new socket
        setupClientStreamReaders();     // initiates stream readers for communicating with client

        // query client for choice (create or join)
        int answer = joinOrCreateQuery();

        // execute decision
        if (0 == answer)
            createNewGame();

        // join existing game if player chose to
        else
            joinGame(answer);
    }

    // Queries client: join existing game or create a new one
    // Returns: game ID of answer or 0 for create new
    public int joinOrCreateQuery() {
        int answer = -1;    // initialize to negative, all game IDs are >0

        // generate and active game list
        String gameListString = generateGameList();

        // Send JSON object to client as string
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(gameListString);
        printWriter.flush();

        // TODO: send and recieve JSON object from client, parse below and return apropriate int
        // Get answer from client

        try {
            // get answer (create or join existing)
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            answer = Integer.parseInt(in.readLine());
            System.out.println("Client game choice:" + answer);

        } catch (IOException e) {   // catch reading IO error
            System.err.println("ERROR reading join/create answer from client" + e);
            e.printStackTrace();
        }

        // check that answer is valid (all valid choices must be positive (join game), or == 0 (create new)
        if (answer < 0)
            System.err.println("ERROR: Client answer:" + answer + " is not in bounds");
        return answer;
    }

    // generates a JSONObject containing the list of active games
    private String generateGameList() {
        // create JSON object to add active games to
        JSONObject JsonGameList = new JSONObject();

        // add active game count as attribute
        JsonGameList.put("Active Game Count", activeGames.size());

        // add each active game to the active game list JSON object
        Game[] activeGameSnapshot = new Game[100];
        activeGames.toArray(activeGameSnapshot);
        for(int i = 0; i < 100; i++) {
            if (activeGameSnapshot[i] != null)
                JsonGameList.put(activeGameSnapshot[i].getName(), activeGameSnapshot[i].getId());
            else
                break;
        }

        // return string representation of JSON object
        return JsonGameList.toString();
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

        // create game with new gameID
        int newId = getNextGameId();
        Game currentGame = new Game(newId, joiningGameQueue);

        // update players game id
        PLAYER.GAME_ID = newId;
        getNameAndId();

        // add player to game joining queue
        joiningGameQueue.add(PLAYER);

        // launch the game
        currentGame.launch();

    }

    // queries player for name and id
    // updates the global PLAYER object with new info
    private void getNameAndId() {
        // send query about joining or creating a new game
//        PrintWriter printWriter = new PrintWriter(outputStream);
//        printWriter.println("SERVER QUERY: What is your name?");
//        printWriter.flush();
//
//        try {
//            // get answer (create or join existing)
//            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//            answer = Integer.parseInt(in.readLine());
//            System.out.println("Client game choice:" + answer);
//
//        } catch (IOException e) {   // catch reading IO error
//            System.err.println("ERROR reading join/create answer from client" + e);
//            e.printStackTrace();
//        }

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
