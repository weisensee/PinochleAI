import java.util.List;

/**
 * Game.java
 * Lucas Weisensee
 *
 * Game info for one Pinochle Game
 *     -gameState
 *     -Port
 *     -Game ID
 *     -Game name
 *     -Client list
 */
public class Game {
    private GameState gameState;    // this Game's current state
    private int PORT;               // port to connect on
    private String name;            // this Game's name
    private int ID;                 // Game's unique ID # (> 0)
    private List<Client> clients;   // clients in this Game
}
