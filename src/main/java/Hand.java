import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Hand.java
 * Luke Weisensee
 *
 * Stores and Manages a player's hand: a list of cards
 */
public class Hand {
    ArrayList<Card> cardsInHand;

    // creates a new hand from one that is dealt/passed
    public Hand(List<Card> cardsDealt) {
        cardsInHand = new ArrayList<Card>(cardsDealt);
    }

}
