import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Game.java
 * Lucas Weisensee
 *
 * Game info for one Pinochle Game
 *     -gameState
 *     -Port
 *     -Game ID
 *     -Game name
 *     -Client list
 */
public class Game {
    private static int QUEUE_MAX = 100;         // max queue of players joining games
    private int MAX_PLAYERS = 4;            // player cap
    private int WINNING_SCORE = 1500;       // score required to win
    private GameState gameState;            // this Game's current state
    private int PORT;                       // port to connect on
    private String NAME;                    // this Game's name
    private int GAME_ID;                    // Game's unique ID # (> 0)
    private PlayerProfile[] PLAYERS;        // clients in this Game
    private int[] SCORES;                   // teams scores
    private BlockingQueue joiningGameQueue;  // blocking queue of players waiting to join another game

    // Constructor, takes the new game's ID
    public Game(int gameId, BlockingQueue joiningQueue) {
        GAME_ID = gameId;
        PLAYERS = new PlayerProfile[MAX_PLAYERS];

        // initiate each player position to null
        for (int i = 0; i < MAX_PLAYERS; i++) {
            PLAYERS[i] = null;
        }

        // set scores to zero
        SCORES = new int[2];    // one score for each team
        SCORES[0] = 0;
        SCORES[1] = 0;

        // save blocking queue address
        this.joiningGameQueue = joiningQueue;
    }

    // adds player to game and removes them from the queue
    public int addPlayer(PlayerProfile toAdd) {
        // if game is full, print and return error
        if (1 == gameFull()) {
            System.err.println("ERROR: unable to add player, game full. gameID:" + GAME_ID +
                    " Player:" + toAdd.NAME + " ID:" + toAdd.ID);
            return -1;
        }

        // if there's room in the game, add the new player to the first available spot
        for (int i = 0; i < MAX_PLAYERS; i++)   // for each player position
            if (PLAYERS[i] == null) {           // if the position is available
                PLAYERS[i] = toAdd;             // add the new player to that spot
                break;                          // once the player has a spot, stop looking for a spot
            }

        // remove the player from the game waiting list (joiningGameQueue)
        if (joiningGameQueue.remove(toAdd))
            return 1;

        // function should not reach this point, print error if it does
        System.err.println("ERROR adding new player: space available but not found or player not in queue!");
        return -1;
    }
    // Wait for players to join
    private void waitForPlayers(){
        PlayerProfile[] queueSnapshot = new PlayerProfile[QUEUE_MAX];   // snapshot of current queue

        // while current game is not full, add players trying to join
        while (1 != gameFull()) {
            // retrieve a snapshot of current queued players
            joiningGameQueue.toArray(queueSnapshot);

            // check each player in queue so see if they're trying to join this game
            for (int i = 0; i < QUEUE_MAX; i++){    // for each player in the queue
                if (queueSnapshot[i] != null)       // if we reached the end of the queue
                    i += QUEUE_MAX;                 // quit searching

                else
                    if (GAME_ID == queueSnapshot[i].GAME_ID) {  // if the player is trying to join this game
                        addPlayer(queueSnapshot[i]);            // add them to this game
                        if (1 == gameFull())                    // if the game is full, stop looking for new players
                            break;
                    }
            }
        }

        // Game should be full now
        System.out.println("Game Server finished filling game");
        for (int i = 0; i < MAX_PLAYERS; i++) {
            System.out.println("Player:" + i + " ");

        }

    }

    // If game is not over, we're ready to play
    // Returns 1 if game is ready to play, 0 otherwise
    private int readyToPlay() {
        // each player must be connected
        // if game is not full, return
        if (0 == gameFull())
            return 0;

        // if either player has won, game is over
        if (0 == gameWon())
            return 1;

        // if game is not over, continue
        return 1;
    }

    // Deal out cards
    private void dealCards() {

    }

    // Collect bids and determine bid winner
    private void manageBidding() {

    }

    // Collect and publish meld played and scores
    private void manageMelding() {

    }

    // facilitate trick picking
    private void manageTricks() {

    }

    // Checks if someone's won, Returns 0 if no one has
    // Runs when someone wins the game, handles the end game
    private int gameWon() {
        // If no team has won, return 0
        if (SCORES[0] < WINNING_SCORE || SCORES[1] < WINNING_SCORE)
            return 0;

        // if someone won
        // TODO: establish endgame sequence
        System.out.println("Game won, team 1:" + SCORES[0] + " team 1:" + SCORES[1]);
        return 1;       // return 1 if game is over
    }

    // Starts the game
    public void launch() {
        // Wait for players to join
        waitForPlayers();

        // If game is not over, continue
        while(1 == readyToPlay()) {
            // Deal out cards
            dealCards();

            // Collect bids and determine bid winner
            manageBidding();

            // Collect and publish meld played and scores
            manageMelding();

            // facilitate trick picking
            manageTricks();
        }
    }

    // returns 1 if game is full, 0 if there's space available
    public int gameFull() {
        for (int i = 0; i < MAX_PLAYERS; i++)
            if (PLAYERS[i] == null)
                return 0;

        // no empty spaces found: return 1 ("game is full")
        return 1;
    }

    // returns the game's name as a string
    public String getName() {
        return NAME;
    }

    // returns the game's ID number as int
    public int getId() {
        return GAME_ID;
    }
}
