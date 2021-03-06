/**
 * Card.java
 * Lucas Weisensee
 *
 * Card is a single card from the deck
 *     -has a suit
 *     -has a value
 *
 *
 */
public class Card {
    private int value;      // this card's value
    private static char[] SUITS = {'\u2660','\u2666','\u2663', '\u2764'};   // Spade, Diamond, Club, Heart

    // initialize the card with the card corresponding to the integer passed in
    public Card(int _value) {
        // check that value is legal
        if (_value >= 24 || _value < -1) /*-1 is default value*/
            System.err.println("ERROR initializing Card: value outside of bounds: " + _value);

        // initialize new card's value
        value = _value;
    }

    // return the int value of card
    public int getInt() {
        return value;
    }

    // returns the string representation of the given card
    public String toString() {return toString(value);}
    // static wrapper method also allows string to be called from Card class when it'd not instantiated
    public static String toString(int value) {
        return getCardName(value) + getCardSuitStr(value);
    }

    // returns the card's suit as a string representaion
    public static char getCardSuitStr(int value) {
        return SUITS[value % 4];
    }

    // returns the string representation of the card value passed in by argument
    public static String getCardName(int value) {
        int num = value % 6;
        switch (num) {
            case 0:
                return new String("A");
            case 1:
                return new String("10");
            case 2:
                return new String("K");
            case 3:
                return new String("Q");
            case 4:
                return new String("J");
            case 5:
                return new String("9");
        }

        System.err.println("ERROR: card value out of bounds in getCardName:" + value);
        return null;
    }
}
