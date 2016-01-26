/**
 * HumanPlayer.java
 * Lucas Weisensee
 *
 * HumanPlayer is a manual player that plays through the console
 *     -players can play other people or AI in the same way an AI can
 *
 */
public class HumanPlayer extends Client {

    // Main class: starts the program with given arguments
    public static void main(String[] args) {
        HumanPlayer Player = new HumanPlayer();
        Player.run(DEFAULT_PORT);
    }

    // returns the player's next play
    public Card getNextPlay() {
        return new Card();
    }

    // Updates the player's information with the newest play
    public void processNewPlay(GameState currentGame) {

    }

}
