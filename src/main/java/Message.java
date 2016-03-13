
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
    private static final int GAME_STATE = 4;
    private static final int GAME_STATUS = 5;
    private static final int HAND_DEALT = 6;
    private static final int MAX_BID = 7;

    // Status number for communicating game status
    private static final int GAME_OVER = 1;              // game is over
    private static final int WAITING_FOR_PLAYERS = 3;    // waiting for other players to join game
    private static final int GAME_STARTING = 4;          // waiting for other players to join game


    protected int TYPE;   // message type identifier
    protected String MSG; // message body

    // default constructor
    public Message(){
        MSG = " ";
        TYPE = -1;
    }

    // constructs Message of the given type with the given object
    private static Message parseMessage(int type, Object toSerialize) {
        Message temp = new Message();
        temp.TYPE = type;
        temp.MSG = serialize(toSerialize);
        return temp;
    }

    // returns String version of this message
    public String getJsonString() {
        return serialize(this);
    }

    private static String serialize(Object toSerialize) {
        Gson gson = new Gson();
        return gson.toJson(toSerialize);
    }

    //******************* Object Getters *******************
    // returns the player profile contained in the message (if it has one)
    public PlayerProfile getPlayerProfile() {
        Gson gson = new Gson();
        return gson.fromJson(MSG, PlayerProfile.class);
    }

    // returns the game list contained in the message
    public ArrayList<Game> getGameList() {
        Gson gson = new Gson();
        Type GameListType = new TypeToken<ArrayList<Game>>(){}.getType();
        return gson.fromJson(MSG, GameListType);
    }

    // returns the GameState contained in the message
    public GameState getGameState() {
        Gson gson = new Gson();
        return gson.fromJson(MSG, GameState.class);
    }
    // returns the Game contained in the message
    public Game getGame() {
        Gson gson = new Gson();
        return gson.fromJson(MSG, Game.class);
    }

    // returns the max bid contained in the message
    public int getMaxBid() {
        if (isMaxBid())
            return Integer.parseInt(MSG);
        else {
            System.err.println("ERROR: requesting max bid when message is not maxbid type");
            return -1;
        }
    }
    // returns the dealt hand contained in the message
    public Hand getHandDealt() {
        if (isHandDealt()) {
            Gson gson = new Gson();
            return gson.fromJson(MSG, Hand.class);
        }
        else {
            System.err.println("ERROR: requesting hand dealt when message is not Hand type");
            return null;
        }
    }
//
//    // parses the object and type passed and returns the Message they create
//    private static Message parseMessage(int type, Object toSerialize) {
//        Message toMake = new Message();
//        toMake.setType(type);
//        toMake.setMsg(serialize(toSerialize));
//
//        return toMake;
//    }

    //******************* MESSAGE CREATORS ****************
    // creates Message holding a Game's status code
    private void createStatusMsg(int status) {
        TYPE = GAME_STATUS;
        MSG = serialize(status);
    }
    public void createPlayerProfileMsg(PlayerProfile profile) {
        TYPE = PLAYER_PROFILE;
        MSG = serialize(profile);
    }
    public void createWaitingForPlayersMsg() {
        createStatusMsg(WAITING_FOR_PLAYERS);
    }
    public void createMaxBidMsg(int maxBid) {
        TYPE = MAX_BID;
        MSG = serialize(maxBid);
    }
    public void createGameListMsg(Game[] activeGames) {
        TYPE = GAME_LIST;
        MSG = serialize(activeGames);
    }
    public void createGameStateMsg(GameState gameState) {
        TYPE = GAME_STATE;
        MSG = serialize(gameState);
    }
    public void createGameMsg(Game game) {
        TYPE = GAME;
        MSG = serialize(game);
    }

    public static Message handDealtMsg(Hand handDealt) {return parseMessage(HAND_DEALT, handDealt);}
    public static Message readyToPlayMsg() {return parseMessage(GAME_STATUS, GAME_STARTING);}

    //******************* STATUS CHECKING FUNCTIONS ***********
    public boolean isGameState() {return TYPE == GAME_STATE;}
    public boolean isGame() {return TYPE == GAME;}
    public boolean isHandDealt() {return TYPE == HAND_DEALT;}
    public boolean isMaxBid() {return TYPE == MAX_BID;}
    public boolean isPlayerProfile() {return TYPE == PLAYER_PROFILE;}
    public boolean isGameStatus() {return TYPE == GAME_STATUS;}
    public boolean gameIsStarting() {return MSG.equals(GAME_STARTING);}
    public boolean waitingForPlayers() {return WAITING_FOR_PLAYERS == Integer.parseInt(MSG);}
    public boolean isValidJson() {// returns true if Message is a valid json message
        return MSG.length() > 0;}
//    private void setType(int type) { TYPE = type;}
//    private void setMsg(String msg) {MSG = msg;}
}
