import java.util.ArrayList;
import java.util.HashMap;

/*
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

    public int pickGameToJoin(HashMap gameList) { // return the clients choice of game to join
        return 0;
    }
    public void placeMaxBid(){}                // bid on hand by placing max bid
    public int pickGameToJoin(ArrayList<Game> gameList) {   // return the clients choice of game to join
        // TODO: should return the game # this player is supposed to join
        return 0;
    }

    public void setNameAndId(){}                       // sets up the global PlayerProfile info


    // returns the player's next play
    public Card getNextPlay() {
        return new Card(0);
    }

    // Updates the player's information with the newest play
    public void processNewPlay(GameState currentGame) {

    }
}
