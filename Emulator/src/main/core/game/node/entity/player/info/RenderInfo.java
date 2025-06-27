package core.game.node.entity.player.info;

import core.ServerConstants;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * The Render info.
 */
public final class RenderInfo {

    private final Player player;

    private List<Player> localPlayers = new LinkedList<Player>();

    private List<NPC> localNpcs = new LinkedList<NPC>();

    private final long[] appearanceStamps = new long[ServerConstants.MAX_PLAYERS];

    private Entity[] maskUpdates = new Entity[256];

    private int maskUpdateCount;

    private Location lastLocation;

    private boolean onFirstCycle = true;

    private boolean preparedAppearance;

    /**
     * Instantiates a new Render info.
     *
     * @param player the player
     */
    public RenderInfo(Player player) {
        this.player = player;
    }

    /**
     * Update information.
     */
    public void updateInformation() {
        onFirstCycle = false;
        lastLocation = player.getLocation();
        preparedAppearance = false;
    }

    /**
     * Register mask update.
     *
     * @param entity the entity
     */
    public void registerMaskUpdate(Entity entity) {
        maskUpdates[maskUpdateCount++] = entity;
    }

    /**
     * Gets local npcs.
     *
     * @return the local npcs
     */
    public List<NPC> getLocalNpcs() {
        return localNpcs;
    }

    /**
     * Sets local npcs.
     *
     * @param localNpcs the local npcs
     */
    public void setLocalNpcs(List<NPC> localNpcs) {
        this.localNpcs = localNpcs;
    }

    /**
     * Is on first cycle boolean.
     *
     * @return the boolean
     */
    public boolean isOnFirstCycle() {
        return onFirstCycle;
    }

    /**
     * Sets on first cycle.
     *
     * @param onFirstCycle the on first cycle
     */
    public void setOnFirstCycle(boolean onFirstCycle) {
        this.onFirstCycle = onFirstCycle;
    }

    /**
     * Gets last location.
     *
     * @return the last location
     */
    public Location getLastLocation() {
        return lastLocation;
    }

    /**
     * Sets last location.
     *
     * @param lastLocation the last location
     */
    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    /**
     * Gets local players.
     *
     * @return the local players
     */
    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * Sets local players.
     *
     * @param localPlayers the local players
     */
    public void setLocalPlayers(List<Player> localPlayers) {
        this.localPlayers = localPlayers;
    }

    /**
     * Get appearance stamps long [ ].
     *
     * @return the long [ ]
     */
    public long[] getAppearanceStamps() {
        return appearanceStamps;
    }

    /**
     * Gets mask update count.
     *
     * @return the mask update count
     */
    public int getMaskUpdateCount() {
        return maskUpdateCount;
    }

    /**
     * Sets mask update count.
     *
     * @param maskUpdateCount the mask update count
     */
    public void setMaskUpdateCount(int maskUpdateCount) {
        this.maskUpdateCount = maskUpdateCount;
    }

    /**
     * Get mask updates entity [ ].
     *
     * @return the entity [ ]
     */
    public Entity[] getMaskUpdates() {
        return maskUpdates;
    }

    /**
     * Sets mask updates.
     *
     * @param maskUpdates the mask updates
     */
    public void setMaskUpdates(Entity[] maskUpdates) {
        this.maskUpdates = maskUpdates;
    }

    /**
     * Sets prepared appearance.
     *
     * @param prepared the prepared
     */
    public void setPreparedAppearance(boolean prepared) {
        this.preparedAppearance = prepared;
    }

    /**
     * Prepared appearance boolean.
     *
     * @return the boolean
     */
    public boolean preparedAppearance() {
        return preparedAppearance;
    }
}
