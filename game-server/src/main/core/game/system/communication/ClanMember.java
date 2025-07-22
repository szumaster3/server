package core.game.system.communication;

/**
 * The type Clan member.
 */
public final class ClanMember {

    private ClanRank rank = ClanRank.ANY_FRIEND;

    /**
     * Instantiates a new Clan member.
     *
     * @param rank the rank
     */
    public ClanMember(ClanRank rank) {
        this.rank = rank;
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

}