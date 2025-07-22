package core.game.interaction;

import core.cache.def.impl.SceneryDefinition;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Plugin;
import core.tools.Log;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.log;

/**
 * The {@code OptionHandler} class provides an abstraction for handling various interaction options in the game.
 * Implementations of this class define how a player interacts with different nodes and objects.
 */
public abstract class OptionHandler implements Plugin<Object> {

    /**
     * Handles a specific interaction option selected by the player.
     *
     * @param player The player performing the interaction.
     * @param node   The node that the player is interacting with.
     * @param option The option selected by the player.
     * @return {@code true} if the interaction is successful, otherwise {@code false}.
     */
    public abstract boolean handle(Player player, Node node, String option);

    /**
     * Determines whether the interaction should be delayed.
     *
     * @param player The player interacting.
     * @return {@code true} if the interaction is delayed, otherwise {@code false}.
     */
    public boolean isDelayed(Player player) {
        return true;
    }

    /**
     * Determines whether the player should walk to the node before interacting.
     *
     * @param player The player performing the action.
     * @param node   The node being interacted with.
     * @return {@code true} if the player should walk to the node, otherwise {@code false}.
     */
    public boolean isWalk(final Player player, final Node node) {
        return false;
    }

    /**
     * Determines whether walking is required for the interaction.
     *
     * @return {@code true} if walking is required, otherwise {@code false}.
     */
    public boolean isWalk() {
        return true;
    }

    /**
     * Gets the destination location for the interaction.
     *
     * @param n    The primary node.
     * @param node The target node.
     * @return The destination {@link Location}, or {@code null} if not applicable.
     */
    public Location getDestination(Node n, Node node) {
        return null;
    }

    /**
     * Retrieves the valid child object IDs for a given wrapper ID.
     *
     * @param wrapper The wrapper ID to check.
     * @return An array of valid child object IDs.
     */
    public int[] getValidChildren(int wrapper) {
        final SceneryDefinition definition = SceneryDefinition.forId(wrapper);
        final List<Integer> list = new ArrayList<>(20);
        if (definition.childrenIds == null) {
            log(this.getClass(), Log.ERR, "Null child wrapper in option handler wrapperId=" + wrapper);
            return new int[]{wrapper};
        }
        for (int child : definition.childrenIds) {
            if (child != -1 && !list.contains(child)) {
                list.add(child);
            }
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Fires an event associated with this option handler.
     *
     * @param identifier The event identifier.
     * @param args       Additional arguments for the event.
     * @return The result of the event execution, or {@code null} if not applicable.
     */
    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}