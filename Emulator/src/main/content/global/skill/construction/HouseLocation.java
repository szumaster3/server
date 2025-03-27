package content.global.skill.construction;

import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.map.Location;
import core.tools.StringUtils;
import org.rs.consts.Scenery;

/**
 * The enum House location.
 */
@SuppressWarnings("all")
public enum HouseLocation {

    /**
     * Nowhere house location.
     */
    NOWHERE(-1, null, 0, 0),

    /**
     * Rimmington house location.
     */
    RIMMINGTON(Scenery.PORTAL_15478, Location.create(2953, 3224, 0), 5000, 1),

    /**
     * Taverly house location.
     */
    TAVERLY(Scenery.PORTAL_15477, Location.create(2893, 3465, 0), 5000, 10),

    /**
     * Pollnivneach house location.
     */
    POLLNIVNEACH(Scenery.PORTAL_15479, Location.create(3340, 3003, 0), 7500, 20),

    /**
     * Rellekka house location.
     */
    RELLEKKA(Scenery.PORTAL_15480, Location.create(2670, 3631, 0), 10000, 30),

    /**
     * Brimhaven house location.
     */
    BRIMHAVEN(Scenery.PORTAL_15481, Location.create(2757, 3178, 0), 15000, 40),

    /**
     * Yanille house location.
     */
    YANILLE(Scenery.PORTAL_15482, Location.create(2544, 3096, 0), 25000, 50);

    private final int portalId;

    private final Location exitLocation;

    private final int cost;

    private final int levelRequirement;

    /**
     * Has level boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasLevel(Player player) {
        return player.getSkills().getStaticLevel(Skills.CONSTRUCTION) >= levelRequirement;
    }

    private HouseLocation(int portalId, Location exitLocation, int cost, int levelRequirement) {
        this.portalId = portalId;
        this.exitLocation = exitLocation;
        this.cost = cost;
        this.levelRequirement = levelRequirement;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return StringUtils.formatDisplayName(name().toLowerCase());
    }

    /**
     * Gets portal id.
     *
     * @return the portal id
     */
    public int getPortalId() {
        return portalId;
    }

    /**
     * Gets exit location.
     *
     * @return the exit location
     */
    public Location getExitLocation() {
        return exitLocation;
    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets level requirement.
     *
     * @return the level requirement
     */
    public int getLevelRequirement() {
        return levelRequirement;
    }

}
