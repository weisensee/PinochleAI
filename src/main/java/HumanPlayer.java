import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;

/**
 * HumanPlayer.java
 * Lucas Weisensee
 *
 * HumanPlayer is a manual player that plays through the console
 *     -players can play other people or AI in the same way an AI can
 *     -currently serving to manually test the Server before connecting up AIs for play
 */
public class HumanPlayer extends Client {

    // Main class: starts the program with given arguments
    public static void main(String[] args) {
        HumanPlayer Player = new HumanPlayer();
        Player.run(Settings.DEFAULT_PORT);
    }

     // sets up the global PlayerProfile info
     public PlayerProfile getNameAndId() {
         // Query player for name, echo what they input
         System.out.println("Please enter the name you'd like to use below:");
         Scanner in = new Scanner(System.in);
         PROFILE.setName(in.nextLine());       // update plater's profile data
         System.out.println("New name saved: " + PROFILE.getName());

         // Pick a userID for the current player
         PROFILE.setId(getNewId());       // adds one to avoid returning 0
         System.out.println(PROFILE.getName() + " got new ID: " + PROFILE.getId());

         return PROFILE;
     }


     // allows the player to handle the most recent play
     // Updates the player's information with the newest play by printing to console
     public void processNewPlay(GameState currentGame) {
        System.out.print(currentGame.print());
     }

     // bid on hand by placing max bid
    // returns -1 on failure or IOException
     public int getMaxBid() {
        System.out.println("Enter your max bid: ");
         try {
             return System.in.read();
         } catch (IOException e) {
             System.err.println("ERROR reading max bid:" + e);
             e.printStackTrace();
             return -1;
         }

     }

     // return the clients choice of game to join
     public int pickGameToJoin(ArrayList<Game> gameList) {
         // parse possible number of available games
         int gameCount = gameList.size();

         // Print out game choices
         System.out.println("\t0\t\tNEW GAME");   // always show the option to create a new game
         for (int i = 0; i < gameCount && gameList.get(i) != null; i++) {
             System.out.println("\t" + (gameList.get(i)).getId() + "\t" + gameList.get(i).getName());   // always show the option to create a new game
         }

         int answer = -1;
         while (!isAvailableGame(answer, gameList)) {
             // Ask the player which game they'd like to join
             System.out.println("Please enter the ID number of the game you'd like to join or '0' to create new game");

             // get the player's choice
             Scanner in = new Scanner(System.in);
             answer = in.nextInt();
         }

         System.out.println("Returning: " + answer + " as player's choice");
         // TODO: if choice = 0 get game name
         return answer;
     }

    // TODO: allow player to choose meld to play
    // play meld from hand for points
    public void playMeld() {
        // TODO: setup to auto-meld until custom melding is easier
    }
    // returns the player's next play
    public Card getNextPlay() {
        // TODO: show the players hand, ask them which card they want to play
        return new Card(-1);
    }


}
