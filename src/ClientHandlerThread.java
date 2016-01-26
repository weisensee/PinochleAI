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
    private Socket socket = null;

    public ClientHandlerThread(Socket newClient) {
        this.socket = newClient;
    }

    public void run() {
        System.out.println("new thread started");

        try {
            // initiate stream readers
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

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
