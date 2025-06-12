package content.region.asgarnia.gwd;

import content.data.God;
import core.game.node.entity.player.Player;

import static core.api.ContentAPIKt.hasGodItem;

/**
 * The enum God wars faction.
 */
public enum GodWarsFaction {

    /**
     * Armadyl god wars faction.
     */
    ARMADYL(6222, 6246, God.ARMADYL),

    /**
     * Bandos god wars faction.
     */
    BANDOS(6260, 6283, God.BANDOS),

    /**
     * Saradomin god wars faction.
     */
    SARADOMIN(6247, 6259, God.SARADOMIN),

    /**
     * Zamorak god wars faction.
     */
    ZAMORAK(6203, 6221, God.ZAMORAK);

    private final int startId;

    private final int endId;

    private final God god;

    GodWarsFaction(int startId, int endId, God god) {
        this.startId = startId;
        this.endId = endId;
        this.god = god;
    }

    /**
     * For id god wars faction.
     *
     * @param npcId the npc id
     * @return the god wars faction
     */
    public static GodWarsFaction forId(int npcId) {
        for (GodWarsFaction faction : values()) {
            if (npcId >= faction.getStartId() && npcId <= faction.getEndId()) {
                return faction;
            }
        }
        return null;
    }

    /**
     * Is protected boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isProtected(Player player) {
        return hasGodItem(player, god);
    }

    /**
     * Gets start id.
     *
     * @return the start id
     */
    public int getStartId() {
        return startId;
    }

    /**
     * Gets end id.
     *
     * @return the end id
     */
    public int getEndId() {
        return endId;
    }
}