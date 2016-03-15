/**
 * GameState.java
 * Lucas Weisensee
 *
 * The GameState holds the current state of the Game with:
 *     -bid winner
 *     -bid ammount
 *     -meld ammounts
 *     -cards played
 *     -current dealer
 *     -game id
 *
 */
public class GameState {
    private int GAME_ID;
    private int DEALER;
    public int BID_WINNER;
    public int BID_AMOUNT;
    public int[] MELD;
    private int playPosition;
    private int[] CARDS_PLAYED;


    // Default constructor--initializes values
    public GameState() {
        DEALER = 0;
        GAME_ID = -1;
        BID_AMOUNT = 0;
        BID_WINNER = -1;
        MELD = new int[4];
        CARDS_PLAYED = new int[48];

        // initialize played cards array
        for (int i = 0; i < 48; i++) {
            CARDS_PLAYED[i] = 0;
        }

        // initialize play position to zero
        playPosition = 0;

    }

    // return stored char array representing the played cards
    public int[] getCardsPlayed() {
        return CARDS_PLAYED;
    }

    // returns game's id
    public int getGameId() {
        return GAME_ID;
    }

    // sets game's id
    public void setGameId(int newId) {
        GAME_ID = newId;
    }
    // add played card to played list and increment counter
    public void playCard(Card toPlay) {
        CARDS_PLAYED[playPosition] = toPlay.getInt();
        playPosition++;
    }

    // Returns how much meld the specified player had this round
    public int getMeld(int player) {
        return MELD[player];
    }

    // returns the player num of the bid winner
    public int getBidWinner() {
        return BID_WINNER;
    }

    // returns the winning bid ammount
    public int getBid() {
        return BID_AMOUNT;
    }

    // set meld made by specific player
    public void setMeld(int player, int meld) {
        if (player > 3 || player < 0 || meld < 0 || meld > 5000)
            System.err.println("argument in setmeld is Out of legal bounds");

        else
            MELD[player] = meld;
    }

    // set bid won
    public void setBid(int newBid) {
        if (newBid < 0)
            System.err.println("argument in setBid is Out of legal bounds");

        else
            BID_AMOUNT = newBid;
    }

    // set meld made by specific player
    public void setBidWinner(int winner) {
        if (winner < -1 || winner > 3 ) /* -1 is default value*/
            System.err.println("argument in setBidWinner is Out of legal bounds");

        else
            BID_WINNER = winner;
    }

    // copy played cards into card string and array
    public void setCardsPlayed(String cardStr) {
        char[] played = cardStr.toCharArray();
        playPosition = 0;
        for (int i = 0; i < 48; i++) {
            if (played[i] != (char)-1)  // if there is a card there (all cards are initialized to -1)
                playCard(new Card(played[i]));

            else    // quit when all cards have been copied
                break;
        }
    }


    public int getDealer() {return DEALER;}

    // updates the GameState's data with new data only if said data exists/is valid
    // returns -1 if null gameState is passed in
    public int update(GameState newInfo) {
        // quit if update is null
        if (newInfo == null) {
            System.err.println("ERROR: null game received in Game->update()");
            return -1;
        }

        // if other values are valid, update local data
        if (newInfo.getGameId() != GAME_ID)
            this.GAME_ID = newInfo.getGameId();

        if (newInfo.getDealer() >= 0)
            this.DEALER = newInfo.getDealer();

        if (playPosition != newInfo.getPlayPosition())
            playPosition = newInfo.getPlayPosition();

        if (newInfo.getBidWinner() != BID_WINNER)
            this.BID_WINNER = newInfo.getBidWinner();

        if (newInfo.getBid() != BID_AMOUNT)
            this.BID_AMOUNT = newInfo.getBid();

        if (newInfo.getMeld() != null)
            this.MELD = newInfo.getMeld();

        if (newInfo.getCardsPlayed() != null)
            this.CARDS_PLAYED = newInfo.getCardsPlayed();

        return 1;
    }

    public int[] getMeld() {return MELD;}
    public int getPlayPosition() {return playPosition;}
    public String print() { return this.toString();} // TODO: implement a more elequent display (java gui?)
}
