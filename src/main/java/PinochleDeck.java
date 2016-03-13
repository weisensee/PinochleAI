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

    public PinochleDeck() {
        // initialize array of cards
        cards = new ArrayList<Card>(48);

        // let the Card class setup each card in the deck
        for (int i = 0; i < 48; i++)
            cards.add(i, new Card(i % 24));

    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    // returns the Hand corresponding to the player num
    public Hand getHand(int num) {

        int start = num * 12;
        int end = start + 12;

        // TODO: test and remove debug statement
        System.out.println("DEBUG: getHand in PinochleDeck passed:" + num + " retrieving cards from: " + start + " to " + end);
        List<Card> temp = cards.subList(start, end);

        System.out.println("Printing sublist: ");
        for (Card c : temp)
            System.out.print(" " + c.getString());
        for (Card c : temp)
            System.out.print(" " + c.getInt());
        return new Hand(temp);

    }
}
