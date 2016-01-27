import java.net.Socket;

/**
 * PlayerProfile.java
 * Lucas Weisensee
 *
 * PlayerProfile is the personal identity of a player.
 * The programs use it to keep track of players, how to contact them and who they are.
 *     -name
 *     -player ID
 *     -socket
 *
 */
public class PlayerProfile {
    public String NAME;
    public int ID;
    public Socket SOCKET;
    public int GAME_ID;

    public PlayerProfile() {
        // initiate with default values
        ID = -1;
        GAME_ID = -1;
        NAME = "default";
        SOCKET = null;
    }


}
