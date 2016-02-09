/**
 * Card.java
 * Lucas Weisensee
 *
 * Card is a single card from the deck
 *     -has a suit
 *     -has a value
 */
public class Card {
    private int value;      // this card's value

    // initialize the card with the card corresponding to the integer passed in
    public Card(int _value) {
        // check that value is legal
        if (_value >= 48 || _value < 0)
            System.err.println("ERROR initializing Card: value outside of bounds: " + _value);

        // initialize new card's value
        value = _value;
    }

    // returns the string representation of the given card
    public String getString() {
        return Integer.toString(value);
    }
}
