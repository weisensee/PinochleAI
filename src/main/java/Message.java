
import com.google.gson.Gson;

import java.io.IOException;
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
    // Message Type Code
    private static final int PLAYER_PROFILE = 1;
    private static final int GAME_LIST = 2;
    private static final int GAME = 3;
    private static final int GAME_STATUS = 4;
    private static final int HAND_DEALT = 5;
    private static final int GAME_STATE = 6;

    // Status number for communicating game status
    private static final int GAME_OVER = 1;              // game is over
    private static final int WAITING_FOR_PLAYERS = 3;    // waiting for other players to join game
    private static final int GAME_STARTING = 4;          // waiting for other players to join game


    protected int TYPE;   // message type identifier
    protected String MSG; // message body

    // default constructor
    public Message(){}

    // constructs Message of the given type with the given object
    public Message(int type, Object toSerialize) {
        TYPE = type;
        MSG = serialize(toSerialize);
    }

    // Deserializing constructor
    public Message(String newMessage) {
        // deserialize new message
        Gson gson = new Gson();
        Message temp = gson.fromJson(newMessage, Message.class);
        if(temp != null && temp.MSG != null) {
            TYPE = temp.TYPE;
            MSG = temp.MSG;
        }
        else
            System.err.println("ERROR deserializing message, message contains null values:" + temp);
    }

    // returns String version of this message
    public String getJsonString() {
        return serialize(this);
    }

    private static String serialize(Object toSerialize) {
        Gson gson = new Gson();
        return gson.toJson(toSerialize);
    }

    //******************* MESSAGE CREATORS ****************
    // creates Message holding a Game's status code
    private static Message createStatusMsg(int status) {return new Message(GAME_STATUS, status);}
    public static Message createGameListMsg(Game[] activeGames) {return new Message(GAME_LIST, activeGames);}
    public static Message createGameMsg(Game game) {return new Message(GAME, game);}
    public static Message createHandDealtMsg(Hand handDealt) {return new Message(HAND_DEALT, handDealt);}
    public static Message createGameStateMsg(GameState gameState) {return new Message(GAME_STATE, gameState);}
    public static Message createPlayerProfileMsg(PlayerProfile profile) {return new Message(PLAYER_PROFILE, profile);}
    public static Message createWaitingForPlayersMsg() {return createStatusMsg(WAITING_FOR_PLAYERS);}


    //******************* Object Getters *******************
    // returns the player profile contained in the message (if it has one)
    public PlayerProfile getPlayerProfile() {
        Gson gson = new Gson();
        return gson.fromJson(MSG, PlayerProfile.class);
    }

    // returns the game list contained in the message
    public ArrayList<Game> getGameList() {
        return new ArrayList<Game>();
    }

    // returns the GameState contained in the message
    public GameState getGameState() {
        return new GameState();
    }


    //******************* STATUS CHECKING FUNCTIONS ***********
    public boolean isGameState() {return TYPE == GAME_STATE;}
    public boolean isGame() {return TYPE == GAME;}
    public boolean isHandDealt() {return TYPE == HAND_DEALT;}
    public boolean isPlayerProfile() {return TYPE == PLAYER_PROFILE;}
    public boolean isGameStatus() {return TYPE == GAME_STATUS;}
    public boolean gameIsStarting() {return MSG.equals(GAME_STARTING);}
    public boolean waitingForPlayers() {return WAITING_FOR_PLAYERS == Integer.parseInt(MSG);}
    public boolean isValidJson() {// returns true if Message is a valid json message
        return MSG.length() > 0;}
}
