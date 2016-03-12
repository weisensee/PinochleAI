
import java.io.*;
import java.net.Socket;
import java.util.HashSet;

/**
 * PlayerProfile.java
 * Lucas Weisensee
 *
 * PlayerProfile is the personal identity of a player.
 * The programs use it to keep track of players, how to contact them and who they are.
 *     -name
 *     -player ID
 *     -socket
 *
 */
public class PlayerProfile {
    // TODO: make socket private and setup getter/setter
    public Socket SOCKET;   // players connection socket
    private String NAME;     // player's chosen name
    private int ID;         // player's unique ID number
    private int GAME_ID;    // Game ID of game player wants to join
    public DataOutputStream outputStream;   // output stream to client
    public DataInputStream inputStream;     // input stream from client
    public PlayerProfile() {
        // initiate with default values
        ID = -1;
        GAME_ID = -1;
        NAME = "default";
        SOCKET = null;
    }


    // Sends message passed in as argument to player
    public int sendMsg(Message toSend) {
        // TODO: implement "try with resources" try (resource) {...} -->need later java version
        try {
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println(toSend.getJsonString());
            printWriter.flush();
            return 1;
        } catch (Exception e) {
            System.err.println("ERROR Sending message in PlayerProfile->sendMsg: " + toSend);
            System.err.println(e);
            e.printStackTrace();
            return -1;
        }
    }

    // returns the next message from the Player's socket
    public Message getMessage(String purpose) {
        // read in message from inputStream
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            return new Message(in.readLine());
        } catch (Exception e) {
            System.err.println("ERROR retrieving message in PlayerProfile->getMessage for: " + purpose);
            System.err.println(e);
            e.printStackTrace();
            return null;
        }

    }

    // sets the PlayerProfile's SOCKET to the one passed in as argument
    public void setSocket(Socket newSocket) {
        SOCKET = newSocket;

        // setup output stream for future messages
        if (SOCKET != null) {
            try {
                outputStream = new DataOutputStream(SOCKET.getOutputStream());
                inputStream = new DataInputStream(SOCKET.getInputStream());
            } catch (IOException e) {
                System.err.println("ERROR setting up stream readers and writers in setSocket in PlayerProfile");
                System.err.println(e);
                e.printStackTrace();
            }
        }
    }

    // returns the player's name as String
    public String getName() {
        return NAME;
    }

    // returns the players unique ID as int
    public int getId() {
        return ID;
    }

    // returns the players specified game ID
    public int getGameId() {
        return GAME_ID;
    }

    // sets the players unique player ID
    public void setId(int newId) {
        ID = newId;
    }

    // sets the players chosen game ID
    public void setGameId(int chosenId) {
        GAME_ID = chosenId;
    }

    // sets the players name
    public void setName(String newName) {
        NAME = newName;
    }

    // returns the player profile's socket object
    public Socket getSocket() {
        return SOCKET;
    }

    // returns true if player profile is set at default settings
    public boolean isDefault() {
        return ID == -1;
    }
}
