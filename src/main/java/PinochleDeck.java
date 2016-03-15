import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PinochleDeck.java
 * Luke Weisensee
 *
 * Stores and Manages a pinochle deck
 * --48 Cards
 * --two of each card
 * --9,10,J,Q,K,A
 */

public class PinochleDeck {
    ArrayList<Card> cards;

    // Default constructor
    public PinochleDeck() {
        // initialize array of cards
        cards = new ArrayList<Card>(48);

        // let the Card class setup each card in the deck
        for (int i = 0; i < 48; i++)
            cards.add(i, new Card(i % 24)); // %24 because there's two of each card in Pinochle

    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    // returns the Hand corresponding to the player num
    public Hand getHand(int num) {

        int start = num * 12;
        int end = start + 12;

        List<Card> temp = cards.subList(start, end);
        return new Hand(temp);

    }
}
