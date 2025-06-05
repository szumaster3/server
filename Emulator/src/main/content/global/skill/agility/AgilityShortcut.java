package content.global.skill.agility;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.plugin.Plugin;
import org.rs.consts.Items;

import static core.api.ContentAPIKt.inEquipment;
import static core.api.ContentAPIKt.sendMessage;

/**
 * Represents a base class for agility shortcuts in the game.
 *
 * @author Woah
 */
public abstract class AgilityShortcut extends OptionHandler {
    private final int[] ids;
    private final int level;
    private final double experience;
    private final boolean canFail;
    private double failChance;
    private final String[] options;

    /**
     * Constructs a new {@code AgilityShortcut} with failure mechanics.
     *
     * @param ids        the object ids associated with this shortcut
     * @param level      the required agility level
     * @param experience the agility experience rewarded upon success
     * @param canFail    whether the shortcut has a chance to fail
     * @param failChance the probability of failure (0.0 to 1.0)
     * @param options    the interaction options
     */
    public AgilityShortcut(int[] ids, int level, double experience, boolean canFail, double failChance, String... options) {
        this.ids = ids;
        this.level = level;
        this.experience = experience;
        this.canFail = canFail;
        this.failChance = failChance;
        this.options = options;
    }

    /**
     * Constructs a new {@code AgilityShortcut} without failure mechanics.
     *
     * @param ids        the object ids associated with this shortcut
     * @param level      the required agility level
     * @param experience the agility experience rewarded upon success
     * @param options    the interaction options
     */
    public AgilityShortcut(int[] ids, int level, double experience, String... options) {
        this(ids, level, experience, false, 0.0, options);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        configure(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!checkRequirements(player)) {
            return true;
        }
        run(player, node.asScenery(), option, checkFail(player, node.asScenery(), option));
        return true;
    }

    /**
     * Executes the shortcut logic, including animations and movement.
     *
     * @param player  the player using the shortcut
     * @param scenery the obstacle scenery
     * @param option  the chosen interaction option
     * @param failed  whether the attempt failed
     */
    public abstract void run(Player player, Scenery scenery, String option, boolean failed);

    /**
     * Checks if the player meets the shortcut's requirements.
     *
     * @param player the player
     * @return {@code true} if requirements are met, {@code false} otherwise
     */
    public boolean checkRequirements(Player player) {
        if (player.getSkills().getLevel(Skills.AGILITY) < level) {
            sendMessage(player, "You need an agility level of at least " + level + " to negotiate this obstacle.");
            return false;
        }
        if (inEquipment(player, Items.SLED_4084, 1)) {
            sendMessage(player, "You can't do that on a sled.");
            return false;
        }
        return true;
    }

    private boolean checkFail(Player player, Scenery scenery, String option) {
        if (!canFail) {
            return false;
        }
        return AgilityHandler.hasFailed(player, level, failChance);
    }

    /**
     * Registers the shortcut's handlers to the relevant object definitions.
     *
     * @param shortcut the agility shortcut instance
     */
    public void configure(AgilityShortcut shortcut) {
        for (int objectId : shortcut.ids) {
            SceneryDefinition def = SceneryDefinition.forId(objectId);
            for (String option : shortcut.options) {
                def.getHandlers().put("option:" + option, shortcut);
            }
        }
    }

    /**
     * Gets the mirrored direction used when determining object orientation.
     *
     * @param direction the original direction
     * @return the mapped direction
     */
    protected Direction getObjectDirection(Direction direction) {
        return direction == Direction.NORTH ? Direction.EAST : direction == Direction.SOUTH ? Direction.WEST : direction == Direction.EAST ? Direction.NORTH : Direction.SOUTH;
    }

    /**
     * Calculates the destination location when traversing a pipe-type obstacle.
     *
     * @param player  the player
     * @param scenery the scenery
     * @param steps   number of tiles to move
     * @return the resulting location
     */
    public Location pipeDestination(Player player, Scenery scenery, int steps) {
        player.faceLocation(scenery.getLocation());
        int diffX = scenery.getLocation().getX() - player.getLocation().getX();
        diffX = Math.max(-1, Math.min(diffX, 1));
        int diffY = scenery.getLocation().getY() - player.getLocation().getY();
        diffY = Math.max(-1, Math.min(diffY, 1));
        return player.getLocation().transform(diffX * steps, diffY * steps, 0);
    }

    /**
     * Calculates a basic destination by multiplying tile difference by step count.
     *
     * @param player  the player
     * @param scenery the scenery
     * @param steps   number of tiles to move
     * @return the resulting location
     */
    public Location agilityDestination(Player player, Scenery scenery, int steps) {
        player.faceLocation(scenery.getLocation());
        int diffX = scenery.getLocation().getX() - player.getLocation().getX();
        int diffY = scenery.getLocation().getY() - player.getLocation().getY();
        return player.getLocation().transform(diffX * steps, diffY * steps, 0);
    }

    /**
     * Gets the required agility level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the experience awarded upon successful shortcut use.
     *
     * @return the experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Indicates whether the shortcut can fail.
     *
     * @return {@code true} if the shortcut has failure mechanics, {@code false} otherwise
     */
    public boolean isCanFail() {
        return canFail;
    }

    /**
     * Gets the chance of failure.
     *
     * @return the failure chance (from 0.0 to 1.0)
     */
    public double getFailChance() {
        return failChance;
    }

    /**
     * Sets the chance of failure.
     *
     * @param failChance the new failure chance
     */
    public void setFailChance(double failChance) {
        this.failChance = failChance;
    }

    /**
     * Gets the object ids associated with this shortcut.
     *
     * @return the object IDs
     */
    public int[] getIds() {
        return ids;
    }

    /**
     * Gets the interaction options (e.g., "climb", "jump").
     *
     * @return the interaction options
     */
    public String[] getOption() {
        return options;
    }
}