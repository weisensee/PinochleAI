import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Messsage.java
 * Luke Weisensee
 *
 * Stores and parses messages between server and client
 *
 * Implements an abstraction for sending messages as JSON objects:
 *      joined game
 *      end game
 *      waiting for players
 *      pass dealt hand
 *      place bid
 *      play card
 *      card played
 *
 */

// TODO: implement message parsing to and from JSON
public class Message {
    String INITSTRING;       // Original string that message was created from
    boolean ISVALIDJSON;    // is false if message Json was identified as invalid, null, etc
    PlayerProfile OWNER;    // owner who created this message
    JSONObject MSGJSON;     // body of message

    //****DEFAULT TYPE NAMING CONVENTIONS****
    public static final String TYPE_NAME = "Type";
    public static final String ACTIVEGAMECOUNT_NAME = "Active Game Count";
    public static final String GAMELIST_NAME = "Game List";
    public static final String PLAYERID_NAME = "Player ID";
    public static final String PLAYERSTR_NAME = "Player Name";
    public static final String GAMEID_NAME = "GameId Name";
    public static final String PLAYERSOCKET_NAME = "Player Socket";

    // Message Codes
    public static final int GAME_LIST = 1;
    public static final int GAME_STATUS = 2;
    public static final int HAND_DEALT = 3;
    public static final int GAME_STATE = 4;


    // default constructor
    public Message() {
        // initialize json validity to false
        ISVALIDJSON = false;

        // initialize owner profile
        OWNER = new PlayerProfile();

        // initialize message's json object
        MSGJSON = new JSONObject();

    }

    // constructs this message from json object parsed from string argument
    public Message(String newMsg) {
        // Check that passed in Json string is valid
        ISVALIDJSON = isValidJson(newMsg);

        // initialize the message with passed in String
        INITSTRING = newMsg;

        // parse string argument into JSON object
        JSONObject msgJson = parseString(newMsg);

        // set local json obj
        MSGJSON = msgJson;

        // set message owner if the json obj was valid and contains a PlayerProfile
        if (ISVALIDJSON && containsPlayerProfile(MSGJSON))
            OWNER = parsePlayerProfile(MSGJSON);
    }

    // returns true if JSONObject passed in by argument contains a player profile
    public boolean containsPlayerProfile(JSONObject toCheck) {
        // check for PlayerProfile Data
        if (toCheck != null &&  toCheck.containsKey(PLAYERID_NAME) && toCheck.containsKey(PLAYERSTR_NAME))
            return true;

        else                // if no PlayerProfile data is present
            return false;   // return false
    }

    // Returns JSONObject created by parsing String argument
    // returns null if string was not valid
    public JSONObject parseString(String jsonString) {
        JSONObject completeMsgJson = null;

        if (isValidJson(jsonString))
            completeMsgJson = parseStrToJson(jsonString);

        // return null or parsed, valid string
        return completeMsgJson;
    }

    // parses and returns a PlayerProfile from JSONObject argument
    private PlayerProfile parsePlayerProfile(JSONObject jsonMsg) {
        PlayerProfile tempProfile = new PlayerProfile();

        // Set Profile's personal ID number (converting long to int)
        tempProfile.setId(((Long)jsonMsg.get(PLAYERID_NAME)).intValue());

        // Set Profile's current game_id number
        tempProfile.setGameId(((Long)jsonMsg.get(GAMEID_NAME)).intValue());

        // Set Profile's Name
        tempProfile.setName((String)jsonMsg.get(PLAYERSTR_NAME));

        // set Profile's Socket
        // TODO: test if this socket passing scheme even works
        tempProfile.setSocket((Socket)jsonMsg.get(PLAYERSOCKET_NAME));

        // Return the new player profile
        return tempProfile;
    }

    // Puts owner information into JSONObject
    public void parseOwnerToJson() {
        // Set Profile's personal ID number
        MSGJSON.put(PLAYERID_NAME, OWNER.getId());

        // Set Profile's current game_id number
        MSGJSON.put(GAMEID_NAME, OWNER.getGameId());

        // Set Profile's Name
        MSGJSON.put(PLAYERSTR_NAME, OWNER.getName());

        // set Profile's Socket
        // TODO: test if this socket passing scheme even works
        MSGJSON.put(PLAYERSOCKET_NAME, OWNER.getSocket());
    }

    // Sets the current message's owner to the one passed in as argument
    public void setOwner(PlayerProfile newOwner) {
        OWNER = newOwner;
    }

    // Takes JSON string and returns JSONObject of that string
    // Returns null if string parsing fails
    // TODO: determine what type of message is coming in and how they should be parsed!
    private JSONObject parseStrToJson(String toParse) {
        try {
            // convert to JSON
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(toParse);

            // return JSONObject
            return (JSONObject)obj;

            // Handle errors thrown in parsing
        } catch(ParseException pe) {
            System.err.println("Error parsing json string");
            System.err.println(pe);
        }

        // should never return null
        System.out.print("returning null on parse error!!");
        return null;
    }

    // Returns True if current, locally defined JsonString is a valid Json object
    public boolean isValidJson() {
        return ISVALIDJSON;
    }

    // Checks the String argument for validity, returns true if is a valid Json object
    public boolean isValidJson(String toCheck) {
        // check for null string
        if (null == toCheck)
            return false;

        // TODO: implement more rigorous json checking here

        // if passed in json string object seems valid return true
        return true;
    }

    // creates a message containing the hand dealt
    public void handDealt(Hand hand) {
        // TODO: implement hand parsed to json, utilize Hand class
    }

    // creates a message containing a game's info

    // returns a string representaion of the message
    public String print() {
        return INITSTRING;
    }

    // returns the message's game id
    // eg. that of the game the player is playing/has chosen
    public int getGameId() {
        // if Owner information has been added, return it
        if (!OWNER.isDefault())
            return OWNER.getGameId();

        else {
            System.err.println("GameId requested from message without Owner information");
            return -1;
        }
    }

    // returns owner's player profile
    public PlayerProfile getOwner() {
        return OWNER;
    }

    // creates JSON message from gamelist passed in as array
    // sets active game count and adds each game to the json object
    public void createGameListMessage(Game[] gameArray) {
//            MSGJSON.put(gameArray[i].getName(), gameArray[i].getId());

        // set message type to game list
        setType(GAMELIST_NAME);

        // add each game's data to local JSON object
        for(int i = 0; i < Settings.MAX_GAMES; i++) {
            if (gameArray[i] != null) {
                MSGJSON.put(i, gameArray[i].getId());
                MSGJSON.put(gameArray[i].getId(), gameArray[i].getName());
            }
            else {
                MSGJSON.put(ACTIVEGAMECOUNT_NAME, i);    // if all games are added, add game count
                break;
            }
        }
    }

    // get Game list message
    public ArrayList<Game> getGameList() {
        int activeGameCount = ((Long)MSGJSON.get(ACTIVEGAMECOUNT_NAME)).intValue();
        ArrayList<Game> gameList = new ArrayList<Game>(activeGameCount);

        // add each game in the JSON object to the list
        for (int i = 0; i < activeGameCount; i++) {
            Game game = new Game(((Long)MSGJSON.get(Integer.toString(i))).intValue(), null);
            game.setName((String)MSGJSON.get(Integer.toString(game.getId())));
            gameList.add(game);
        }

        // return completed list
        return gameList;
    }

    // sets the type of current message to the type passed in as argument
    private void setType(String type) {
        MSGJSON.put(TYPE_NAME, type);
    }

    // Creates a game status message for all players and observers
    public void createGameStateMessage(GameState gameState) {
        // Set type of message
        MSGJSON.put(TYPE_NAME, GAME_STATE);

        // copy over values
        MSGJSON.put("bid winner", gameState.getBidWinner());
        for (int i = 0; i < 4; i++)
            MSGJSON.put("meld"+i, gameState.getMeld(i));
        MSGJSON.put("played", gameState.getCardsPlayed());
        MSGJSON.put("bid", gameState.getBid());
        MSGJSON.put("game id", gameState.getGameId());

    }

    // returns a gameState from a gamestate message
    public GameState getGameState() {
        GameState toReturn = new GameState();

        toReturn.setGameId(Integer.parseInt((String)MSGJSON.get("game id")));
        toReturn.setBid(Integer.parseInt((String)MSGJSON.get("bid")));
        toReturn.setBidWinner((Integer)MSGJSON.get("bid winner"));
        toReturn.setCardsPlayed((String)MSGJSON.get("played"));
        for(int i = 0; i < 4; i++)
            toReturn.setMeld(i, (Integer)MSGJSON.get("meld"+i));

        return toReturn;
    }

    // create a status message with the argument passed in as the status
    public void createStatusMsg(int status) {
        // set type to status message
        MSGJSON.put(TYPE_NAME, GAME_STATUS);

        // copy the status
        MSGJSON.put(GAME_STATUS, status);
    }

    // returns the game's status code, returns -1 if not a status message
    public int getGameStatus() {
        if (isGameStatus())
            return (Integer)MSGJSON.get(GAME_STATUS);

        else
            return -1;
    }

    // Returns true is message is a gameState
    public boolean isGameState() {
        return GAME_STATE == MSGJSON.get(TYPE_NAME);
    }

    // Return true if message is a hand dealt
    public boolean isHandDealt() {
        return HAND_DEALT == MSGJSON.get(TYPE_NAME);
    }

    // returns true if message is a game status
    public boolean isGameStatus() {
        return GAME_STATUS == MSGJSON.get(TYPE_NAME);
    }

    // returns true if message is an active game list
    public boolean isGameList() {
        return GAME_LIST == MSGJSON.get(TYPE_NAME);
    }

    // returns local json object as a string, adds owner if possible
    public String getString() {
        // if the ownwer is not default, send it along in message
        if (!OWNER.isDefault())
            parseOwnerToJson();

        // convert json object and return it
        return MSGJSON.toString();
    }

}
