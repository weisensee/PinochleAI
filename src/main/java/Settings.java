/**
 * Created by luke on 3/9/16.
 */
public class Settings {
    // Settings for servers and clients
    public static final int QUEUE_MAX = 100;        // max queue of players joining games
    public static final int MAX_GAMES = 100;        // maximum active games on server


    // Status Messages for communicating game status
    public static final int GAME_OVER = 1;                // game is over
    public static final int WAITING_FOR_PLAYERS = 3;        // waiting for other players to join game
    public static final int GAME_STARTING = 4;        // waiting for other players to join game
}
