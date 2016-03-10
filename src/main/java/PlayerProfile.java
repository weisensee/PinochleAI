import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
    public DataOutputStream outputStream;// output stream to client
    public PlayerProfile() {
        // initiate with default values
        ID = -1;
        GAME_ID = -1;
        NAME = "default";
        SOCKET = null;
    }

    // initializes a PlayerProfile with the attributes from the JSONObject argument
    public PlayerProfile(JSONObject newProfile) {
        this();
        parseJsonObj(newProfile);
    }

    // Sends message passed in as argument to player
    public int sendMsg(Message toSend) {
        try {
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println(toSend);
            printWriter.flush();
            return 1;
        } catch (Exception e) {
            System.err.println("ERROR Sending message in PlayerProfile->sendMsg");
            System.err.println(e);
            e.printStackTrace();
            return -1;
        }
    }

    // Returns new PlayerProfile parsed from JSONObject argument
    public PlayerProfile parseJsonObj(JSONObject newProfile) {
        // Create new PlayerProfile
        PlayerProfile tempNewProfile = new PlayerProfile();

        // Add each data value to appropriate component if it's value is not 'null'
        if (newProfile.containsKey("NAME") && null != newProfile.get("NAME"))
            setName(newProfile.get("NAME").toString());

        if (newProfile.containsKey("GAME_ID") && null != newProfile.get("GAME_ID"))
            setGameId(Integer.parseInt(newProfile.get("GAME_ID").toString()));

        if (newProfile.containsKey("ID") && null != newProfile.get("ID"))
            setId(Integer.parseInt(newProfile.get("ID").toString()));

        if (newProfile.containsKey("SOCKET") && null != newProfile.get("SOCKET")) {
            System.out.println("JSON OBJ conains 'SOCKET' entry: " + newProfile.get("SOCKET").toString());
        }

        // return new PlayerProfille
        return tempNewProfile;
    }

    // sets the PlayerProfile's SOCKET to the one passed in as argument
    public void setSocket(Socket newSocket) {
        SOCKET = newSocket;

        // setup output stream for future messages
        if (SOCKET != null) {
            try {
                outputStream = new DataOutputStream(SOCKET.getOutputStream());
            } catch (IOException e) {
                System.err.println("ERROR setting up output stream in setSocket in PlayerProfile");
                System.err.println(e);
                e.printStackTrace();
            }
        }
    }

    // TODO: eliminate this function and fix errors using Message wrapper class
    // Returns PlayerProfile parsed from JSON String argument
    public PlayerProfile parseJsonString(String jsonString) {
        JSONObject tempJsonObj = new JSONObject();

        // parse JSON String
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);  // parse the string
            tempJsonObj = (JSONObject)obj;          // cast object as JSONObject

        } catch (ParseException e) {
            System.err.println("ERROR Parsing JSON String:" + e);
            e.printStackTrace();
        }

        // Create PlayerProfile from new JSONObject
        return parseJsonObj(tempJsonObj);

    }

    // TODO: eliminate this function and fix errors using Message wrapper class
    // Parses String representation of JSONObject into current player profile
    public void setPlayerProfile(String jsonString) {
        // convert to PlayerProfile
        PlayerProfile tempProfile = parseJsonString(jsonString);

        // copy data to local profile
        setGameId(tempProfile.getGameId());
        setName(tempProfile.getName());
        setId(tempProfile.getId());
        setSocket(tempProfile.getSocket());
    }

    // returns Player's Profile info as JSONObject
    public JSONObject getJsonObj() {
        JSONObject jsonObj = new JSONObject();  // new json object
        jsonObj.put("ID", ID);                  // add each piece of data
        jsonObj.put("NAME", NAME);
        jsonObj.put("GAME_ID", GAME_ID);
        jsonObj.put("SOCKET", SOCKET);

        return jsonObj;
    }

    // returns Player's profile info as String in JSON format
    public String getJsonString() {
        return getJsonObj().toJSONString();
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
