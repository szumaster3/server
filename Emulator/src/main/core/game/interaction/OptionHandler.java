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
 * The type Option handler.
 */
public abstract class OptionHandler implements Plugin<Object> {

    /**
     * Handle boolean.
     *
     * @param player the player
     * @param node   the node
     * @param option the option
     * @return the boolean
     */
    public abstract boolean handle(Player player, Node node, String option);

    /**
     * Is delayed boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isDelayed(Player player) {
        return true;
    }

    /**
     * Is walk boolean.
     *
     * @param player the player
     * @param node   the node
     * @return the boolean
     */
    public boolean isWalk(final Player player, final Node node) {
        return false;
    }

    /**
     * Is walk boolean.
     *
     * @return the boolean
     */
    public boolean isWalk() {
        return true;
    }

    /**
     * Gets destination.
     *
     * @param n    the n
     * @param node the node
     * @return the destination
     */
    public Location getDestination(Node n, Node node) {
        return null;
    }

    /**
     * Get valid children int [ ].
     *
     * @param wrapper the wrapper
     * @return the int [ ]
     */
    public int[] getValidChildren(int wrapper) {
        final SceneryDefinition definition = SceneryDefinition.forId(wrapper);
        final List<Integer> list = new ArrayList<>(20);
        if (definition.configObjectIds == null) {
            log(this.getClass(), Log.ERR, "Null child wrapper in option handler wrapperId=" + wrapper);
            return new int[]{wrapper};
        }
        for (int child : definition.configObjectIds) {
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

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}