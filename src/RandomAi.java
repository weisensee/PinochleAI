/**
 * RandomAi.java
 * Lucas Weisensee
 *
 * RandomAi is a rudimentary AI which picks randomly from the cards available for each play.
 */
public class RandomAi extends AiPlayer {
    private int num;    // random num to generate from

    // Main class: starts the program with given arguments
    public static void main(String[] args) {
        RandomAi Player = new RandomAi();
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