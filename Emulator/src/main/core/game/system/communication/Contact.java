package core.game.system.communication;

/**
 * The type Contact.
 */
public final class Contact {

    private final String username;

    private int worldId;

    private ClanRank rank = ClanRank.ANY_FRIEND;

    /**
     * Instantiates a new Contact.
     *
     * @param username the username
     */
    public Contact(String username) {
        this.username = username;
    }

    /**
     * Gets world id.
     *
     * @return the world id
     */
    public int getWorldId() {
        return worldId;
    }

    /**
     * Sets world id.
     *
     * @param worldId the world id
     */
    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    /**
     * Gets rank.
     *
     * @return the rank
     */
    public ClanRank getRank() {
        return rank;
    }

    /**
     * Sets rank.
     *
     * @param rank the rank
     */
    public void setRank(ClanRank rank) {
        this.rank = rank;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

}