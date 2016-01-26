import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * ClientHandlerThread.java
 * Lucas Weisensee
 *
 * Handles a new Client's connection to the Game's Server
 */
public class ClientHandlerThread extends Thread {
    public Socket clientSocket;             // thread's client's socket
    public DataInputStream inputStream;     // input stream from client
    public DataOutputStream outputStream;   // output stream to client

    public ClientHandlerThread(Socket newClient) {
        this.clientSocket = newClient;
    }

    // Queries client for game choice and connects them to that game
    public void run() {
        System.out.println("new thread started");
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

        // send query
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("SERVER QUERY: Which Game would you like to join?");
        printWriter.flush();

        try {
            // get answer
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            answer = Integer.parseInt(in.readLine());
            System.out.println("Client game choice:" + answer);

        } catch (IOException e) {   // catch reading IO error
            System.err.println("ERROR reading join/create answer from client" + e);
            e.printStackTrace();
        }

        // check that answer is >= 0 (all valid choices must be positive (join game), or == 0 (create new)
        if (answer < 0)
            System.err.println("ERROR: Client answer:" + answer + " is not in bounds");
        return answer;
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
    }

    // Allows client to join new game
    public void joinGame(int toJoin) {
        System.out.println("Client has chosen to join existing game: " + toJoin);

    }



    private void echoTest() {
        // Original demo code for server-client testing
        try {
            // initiate stream readers
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // read from stream
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String string2 = in.readLine();
            System.out.println("Message from Client:" + string2);

            // write to stream
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println("server says hi");
            printWriter.flush();

        } catch (IOException e) {
            System.out.println("error:" + e.toString());
            e.printStackTrace();
        }

    }
}
