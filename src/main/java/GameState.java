import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.util.ArrayList;

/**
 * GameState.java
 * Lucas Weisensee
 *
 * The GameState holds the current state of the Game with:
 *     -bid winner
 *     -bid ammount
 *     -meld ammounts
 *     -cards played
 */
public class GameState {
    private int GAME_ID;
    public int BID_WINNER;
    public int BID_AMMOUNT;
    public int[] MELD;
    private int playPosition;
    private int[] CARDS_PLAYED;
    char[] charCardsPlayed;

    // Default constructor--initializes values
    public GameState() {
        GAME_ID = -1;
        BID_AMMOUNT = 0;
        BID_WINNER = -1;
        MELD = new int[4];
        CARDS_PLAYED = new int[48];
        charCardsPlayed = new char[48];

        // initialize played cards array
        for (int i = 0; i < 48; i++) {
            CARDS_PLAYED[i] = 0;
            charCardsPlayed[i] = (char)-1;
        }


        playPosition = 0;

    }

    // return stored char array representing the played cards
    public String getCardsPlayed() {
        String temp = new String();
        for (int i = 0; i < 48; i++)
            temp += charCardsPlayed[i];

        return temp;
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
        charCardsPlayed[playPosition] = toPlay.getChar();
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
        return BID_AMMOUNT;
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
            BID_AMMOUNT = newBid;
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






}
