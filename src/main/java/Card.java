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
        if (_value >= 48 || _value < -1) /*-1 is default value*/
            System.err.println("ERROR initializing Card: value outside of bounds: " + _value);

        // initialize new card's value
        value = _value;
    }

    // return the int value of card
    public int getInt() {
        return value;
    }

    // return the char value of the card
    // TODO: setup char/int conversion
    public char getChar() {
        return (char)value;
    }

    // TODO: return appropriate string to display card suit and value
    // TODO: implement getSuit and getValue helper functions
    // returns the string representation of the given card
    public String getString() {
        return Integer.toString(value);
    }
}
