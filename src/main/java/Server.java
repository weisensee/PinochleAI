import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Server.java: Game Server for card playing AI
 * Lucas Weisensee
 *
 * Manages active games
 * Connects players to preferred Game
 */
public class Server {
    // Port number for Server to listen on
    private static int PORT_NUMBER = 9999;      // sets the DEFAULT PORT;

    public static void main(String[] args){
        Server GameServer = new Server();
        GameServer.run(9999);
        // could be good to have this editable on the commandline
        // with: Integer.parseInt(args[1])
    }

    // Start new Server with the given arguments
    // -- port: port number to host on
    private void run(int port) {
        PORT_NUMBER = port; //set Server's listen port
        System.out.println("Starting Server on port:" + PORT_NUMBER);
        BlockingQueue joiningGameQueue = new ArrayBlockingQueue(Settings.QUEUE_MAX);
        BlockingQueue activeGames = new LinkedBlockingQueue(Settings.QUEUE_MAX);

        // Listen for new Client connections
        try {
            // create Server socket and accept connection
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

            // Listen until an error or Server is closed
            while (true) {
                // spin off thread to handle new Client
                new ClientHandlerThread(serverSocket.accept(), joiningGameQueue, activeGames).start();

            }
        } catch (IOException e) {
            System.err.println("***Could not listen on port: " + PORT_NUMBER);
            System.err.println("ERROR:" + e);
            System.exit(-1);
        }

    }
}
