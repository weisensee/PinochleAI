import org.json.simple.JSONObject;
import org.json.simple.*;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

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
         PROFILE.setName(cin.nextLine());       // update plater's profile data
         System.out.println("New name saved: " + PROFILE.getName());

         // Pick a userID for the current player
         PROFILE.setId(getNewId());       // adds one to avoid returning 0
         System.out.println(PROFILE.getName() + " got new ID: " + PROFILE.getId());
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
     public int pickGameToJoin(ArrayList<Game> gameList) {
         // parse and print number of available games
         int gameCount = gameList.size();
         System.out.println("Active Game Count is: " + gameCount);

         // Print out game choices
         System.out.println("\t0\t\tNEW GAME");   // always show the option to create a new game
         for (int i = 0; i < gameCount; i++)
             System.out.println("\t" + gameList.get(i).getId() + "\t" + gameList.get(i).getName());   // always show the option to create a new game

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

    // returns the player's next play
    public Card getNextPlay() {
        // TODO: show the players hand, ask them which card they want to play
        return new Card(-1);
    }


}
