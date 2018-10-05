import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class that implements a player for the ConnectN (and possibly other) games.
 * <p>
 * You do not need to modify this class (except to fix one checkstyle error). But you are encouraged
 * to study it since it does some of the things that you need to do to complete ConnectN.
 *
 * @see <a href="https://cs125.cs.illinois.edu/MP/3/">MP3 Documentation</a>
 */
public class Player {
    private static int globalID = 0;

    /** This player's name. */
    private String name;

    /**
     * Get the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the player's name.
     *
     * @param setName the player's new name
     */
    public void setName(final String setName) {
        this.name = setName;
    }

    /** The number of games this player has won. */
    private int score;

    /**
     * Get this player's score.
     *
     * @return this player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add one to this player's score.
     */
    public void addScore() {
        score += 1;
    }

    /** This player's ID. Used internally by {@link #equals(Object) equals()}. */
    private final int id;

    /**
     * Get the ID of this player.
     *
     * Used during serialization, so do not remove.
     *
     * @return the ID of this player.
     */
    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    /**
     * Create a new player with the given name.
     * <p>
     * Each player's score begins at zero, and each receives a monotonically increasing unique ID.
     *
     * @param setName the name for the new player
     */
    public Player(final String setName) {
        this.name = setName;
        this.score = 0;
        this.id = Player.globalID++;
    }

    /**
     * Create a copy of an existing player.
     * <p>
     * This copy constructor creates a copy of an existing player, so that player information can be
     * exposed without allowing the copy to modify the state of the original player.
     * <p>
     * The id is copied, meaning that the copy will initially be equal to the original. However,
     * future modifications to the score field will not change the copy and will result in it being
     * considered non-equal to the original.
     *
     * @param other the other
     */
    public Player(final Player other) {
        this(other.name, other.score, other.id);
    }

    /**
     * Create a new player from a full set of fields.
     * <p>
     * This is entirely for deserialization by the web frontend. Unlike the other constructors, it
     * does not increment the player count, since the assumption is that you are recovering an
     * existing player.
     *
     * @param setName the name for the new player
     * @param setScore the score for the new player
     * @param setID the ID for the new player
     */
    @JsonCreator
    public Player(@JsonProperty("name") final String setName,
            @JsonProperty("score") final int setScore,
            @JsonProperty("id") final int setID) {
        this.name = setName;
        this.score = setScore;
        this.id = setID;
    }

    /*
     * The following methods were auto-generated by IntelliJ. We use the id field for
     * equals and hash, implementing entity semantics.
     */

    /**
     * Define the hash code for the Player class.
     * <p>
     * This method should only use the id field of the instance. This implements what is known as
     * <i>entity</i> semantics. Two players with the same name and same score may not be equal if
     * they were created at different times. Note that IntelliJ can auto-generate this and
     * {@link #equals(Object) equals}.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /**
     * Define equality for the Player class.
     * <p>
     * This method should only use the id field of the instance. This implements what is known as
     * <i>entity</i> semantics. Two players with the same name and same score may not be equal if
     * they were created at different times. Note that IntelliJ can auto-generate this and
     * {@link #hashCode() hashCode()}.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Player other = (Player) obj;
        return id == other.id;
    }
}
