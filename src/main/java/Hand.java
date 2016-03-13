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
    ArrayList<Card> CARDS;

    // creates a new hand from one that is dealt/passed
    public Hand(List<Card> cardsDealt) {
        CARDS = new ArrayList<Card>(cardsDealt);
    }

    // TODO: impement function to return string of hand for printing

}
