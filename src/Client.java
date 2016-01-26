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
    public static int DEFAULT_PORT = 9999;  // default port for Server
    public int PORT_NUMBER;                 // Port number to listen for new connections on


    // gets the next play from the player
    public abstract Card getNextPlay();
    public abstract void processNewPlay(GameState currentGame);

    // Starts the Client with given arguments
    public void run(int port) {
        // set port for Client to use
        PORT_NUMBER = port;

        System.out.println("Starting Client on port:" + PORT_NUMBER);

        String string = "testing1234";
        System.out.println("sending: " + string);

        try {

            // bind socket
            Socket server = new Socket(InetAddress.getByName("0.0.0.0"), PORT_NUMBER);

            // initiate stream readers
            DataInputStream inputStream = new DataInputStream(server.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());

            // write to stream
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println();
            printWriter.flush();

            // read from stream
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String string2 = in.readLine();
            System.out.println("recieved from server:" + string2);



        } catch (IOException e) {
            System.out.println("error:" + e.toString());
            e.printStackTrace();
        }

//        Card toPlay = getNextPlay();
//        playCard(toPlay);
    }

    // Plays the given card in the current Game
    public void playCard(Card toPlay) {

    }

    public void joinGame(String toJoin) {

    }

    public void leaveGame() {
    }


}
