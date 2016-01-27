import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * HumanPlayer.java
 * Lucas Weisensee
 *
 * HumanPlayer is a manual player that plays through the console
 *     -players can play other people or AI in the same way an AI can
 */

 /** ::GLOBAL VARIABLES::
public static int DEFAULT_PORT = 9999;      // default port for Server
public int PORT_NUMBER;                     // Port number to listen for new connections on
public String SERVER_ADDRESS = "0.0.0.0";   // default server address, localhost
public Socket SERVER;                       // game server's socket
public Game PINOCHLE_GAME;                  // game data for current game
public DataInputStream inputStream;         // server socket reading stream
public DataOutputStream outputStream;       // server socket writing stream

*/
public class HumanPlayer extends Client {

    // Main class: starts the program with given arguments
    public static void main(String[] args) {
        HumanPlayer Player = new HumanPlayer();
        Player.run(DEFAULT_PORT);
    }

     // sets up the global PlayerProfile info
     public void setNameAndId() {
         // Query player for name, echo what they input
         System.out.println("Please enter the name you'd like to use below:");
         Scanner cin = new Scanner(System.in);
         PROFILE.NAME = cin.nextLine();
         System.out.println("New name saved: " + PROFILE.NAME);

         // Pick a userID for the current player
         // TODO: setup some kind of database for userid's to ensure no duplicates
         Random r = new Random();        // currently returns a randomly generated ID
         PROFILE.ID = r.nextInt(99999);    // adds one to avoid returning 0

     }


     // allows the player to handle the most recent play
     // Updates the player's information with the newest play
     public void processNewPlay(GameState currentGame) {

     }

     // bid on hand by placing max bid
     public void placeMaxBid() {

     }

     // TODO: allow player to choose meld to play
     // play meld from hand for points
     public void playMeld() {
        // TODO: setup to auto-meld until custom melding is an option
     }

     // return the clients choice of game to join
     public int pickGameToJoin(String query) {
         // Ask the player which game they'd like to join
         System.out.println("FROM SERVER:" + query);
         System.out.println("Please enter which game you'd like to join or 0 to create a new one.");

         // get the player's choice
         Scanner in = new Scanner(System.in);
         int answer = in.nextInt();


         // TODO: check that the players choice is a viable one (in the list of available games)
         System.out.println("Returning: " + answer + " as player's choice");
         return answer;
     }

    // returns the player's next play
    public Card getNextPlay() {
        return new Card();
    }


}
