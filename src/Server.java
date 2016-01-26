import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server.java: game Server for card playing AI
 * Lucas Weisensee
 *
 * Manages active games
 * Connects players to preferred game
 */
public class Server {
    // Port number for Server to listen on
    private static int PORT_NUMBER = 9999;    // sets the DEFAULT PORT;

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

        // Listen for new Client connections
        try {
            // Listen until an error or Server is closed
            while (true) {
                // create Server socket and accept connection
                ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

                // spin off thread to handle new Client
                new ClientHandlerThread(serverSocket.accept()).start();

            }
        } catch (IOException e) {
            System.err.println("***Could not listen on port: " + PORT_NUMBER);
            System.err.println("ERROR:" + e);
            System.exit(-1);
        }

    }
}
