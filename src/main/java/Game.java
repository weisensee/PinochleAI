import java.net.Socket;
import java.util.ArrayList;
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
    private int MAX_PLAYERS = 4;            // player cap
    private GameState GAMESTATE;            // this Game's current state
    private int PORT;                       // port to connect on
    private String NAME;                    // this Game's name
    private int GAME_ID;                    // Game's unique ID # (> 0)
    private PlayerProfile[] PLAYERS;        // clients in this Game
    private Hand[] HANDS;            // cards each player holds
    private int[] SCORES;                   // teams scores
    private BlockingQueue joiningGameQueue; // blocking queue of players waiting to join another game
    public static final int WINNING_SCORE = 1500;   // score required to win

    // Constructor, takes the new game's ID
    public Game(int gameId, BlockingQueue joiningQueue) {
        GAME_ID = gameId;
        PLAYERS = new PlayerProfile[MAX_PLAYERS];
        GAMESTATE = new GameState();

        // initiate each player position to null
        for (int i = 0; i < MAX_PLAYERS; i++) {
            PLAYERS[i] = null;
        }

        // set scores to zero
        SCORES = new int[2];    // one score for each team
        SCORES[0] = 0;
        SCORES[1] = 0;

        // save blocking queue address
        if (joiningQueue != null)
            this.joiningGameQueue = joiningQueue;

        // initialize players hands?
    }

    // Starts the game
    // from this point on the Game class is responsible for managing all aspects of the game's progression
    public void launch() {
        // Wait for players to join
        waitForPlayers();

        // If game is not over, continue
        while(!readyToPlay()) {
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

    // Wait for players to join the game
    private void waitForPlayers() {
        PlayerProfile[] queueSnapshot = new PlayerProfile[Settings.QUEUE_MAX];   // pointer for current queue snapshot

        // while current game is not full, add players trying to join
        while (!gameFull()) {
            // retrieve a snapshot of current queued players
            joiningGameQueue.toArray(queueSnapshot);

            // check each player in queue so see if they're trying to join this game
            for (int i = 0; i < Settings.QUEUE_MAX; i++) {   // for each player in the queue
                if (queueSnapshot[i] == null)               // if we reached the end of the queue
                    i += Settings.QUEUE_MAX;                // quit searching

                else if (GAME_ID == queueSnapshot[i].getGameId()) {  // if the player is trying to join this game
                    addPlayer(queueSnapshot[i]);                // add them to this game
                    if (gameFull())                        // if the game is full, stop looking for new players
                        break;
                }
            }
            // TODO: if all players have left, close this game!!
            // if there's space left and no players in queue
            sendStatusToPlayers(Settings.WAITING_FOR_PLAYERS); // tell active players we're waiting
        }
    }

    // adds player to game and removes them from the queue
    public int addPlayer(PlayerProfile toAdd) {
        // if game is full, print and return error
        if (gameFull()) {
            System.err.println("ERROR: unable to add player, game full. gameID:" + GAME_ID +
                    " Player:" + toAdd.getName() + " ID:" + toAdd.getId());
            return -1;
        }

        // if there's room in the game, add the new player to the first available spot
        for (int i = 0; i < MAX_PLAYERS; i++)   // for each player position
            if (PLAYERS[i] == null) {           // if the position is available
                PLAYERS[i] = toAdd;             // add the new player to that spot
                break;                          // once the player has a spot, stop looking for a spot
            }

        // remove the player from the game waiting list (joiningGameQueue)
        if (joiningGameQueue.remove(toAdd)) {
            // if player was added successfully, update them with game status
            Message gameInfo = new Message();
            GAMESTATE.setGameId(GAME_ID);
            gameInfo.createGameStateMessage(GAMESTATE);
            toAdd.sendMsg(gameInfo);
            return 1;
        }

        // function should not reach this point, print error if it does
        System.err.println("ERROR adding new player: space available but not found or player not in queue!");
        return -1;
    }

    // Updates all players with status of current game
    private void sendStatusToPlayers(int status) {
        Message statusMsg = new Message();
        statusMsg.createStatusMsg(status);

        for (int i = 0; i < 4; i++)
            if (PLAYERS[i] != null)
                PLAYERS[i].sendMsg(statusMsg);
    }

    // Returns true if game is ready to play, false otherwise
    private boolean readyToPlay() {
        // each player must be connected
        // if game is not full, return
        if (!gameFull())
            return false;

        // if either player has won, game is over
        if (gameWon())
            return false;

        // if game is not over, continue
        return true;
    }

    // Send String argument to all active players
    public void sendToAllPlayers(String toSend) {
        // for each current player
//        for (int i = 0; i < MAX_PLAYERS; i++)
//            if (PLAYERS[i] != null)
//                PLAYERS[i].send
    }

    // TODO: test that this works/is dealing out cards to players
    // Deal out cards
    // notify players that game is starting by dealing out hands
    private void dealCards() {
        // initialize new deck of cards
        PinochleDeck deck = new PinochleDeck();

        // shuffle and deal cards locally
        deck.shuffle();

        // save then send each player's hand
        for(int i = 0; i < MAX_PLAYERS; i++) {
            // save hand locally
            HANDS[i] = deck.getHand(i);

            // send dealt hands
            Message handDealt = new Message();
            handDealt.handDealt(HANDS[i]);
            PLAYERS[i].sendMsg(handDealt);
        }


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
    private boolean gameWon() {
        // If no team has won, return 0
        if (SCORES[0] < WINNING_SCORE || SCORES[1] < WINNING_SCORE)
            return false;

        // if someone won
        // TODO: establish endgame sequence
        System.out.println("Game won, team 1:" + SCORES[0] + " team 1:" + SCORES[1]);
        return true;       // return 1 if game is over
    }


    // returns true if game is full, false if there's space available
    public boolean gameFull() {
        for (int i = 0; i < MAX_PLAYERS; i++)
            if (PLAYERS[i] == null)
                return false;

        // no empty spaces found: return 1 ("game is full")
        return true;
    }

    // Sets the game's name with String argument
    public void setName(String newName) {
        NAME = newName;
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
