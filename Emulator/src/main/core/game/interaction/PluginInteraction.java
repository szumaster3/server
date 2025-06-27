package core.game.interaction;

import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;

/**
 * Handles player interactions with nodes, items, and NPCs.
 */
public abstract class PluginInteraction implements Plugin<Object> {

    protected int[] ids;
    protected Item item;

    /**
     * @param ids entity IDs this interaction applies to.
     */
    public PluginInteraction(int... ids) {
        this.ids = ids;
    }

    /**
     * @param item item this interaction applies to.
     */
    public PluginInteraction(Item item) {
        this.item = item;
    }

    /**
     * @return true if handled, otherwise false.
     */
    public boolean handle(Player player, Node node) {
        return false;
    }

    /**
     * @return true if handled, otherwise false.
     */
    public boolean handle(Player player, NodeUsageEvent event) {
        return false;
    }

    /**
     * @return true if handled, otherwise false.
     */
    public boolean handle(Player player, NPC npc, Option option) {
        return false;
    }

    /**
     * @return true if handled, otherwise false.
     */
    public boolean handle(Player player, Item item, Option option) {
        return false;
    }

    /**
     * @param ids sets new interaction ids.
     **/
    public void setIds(int[] ids) {
        this.ids = ids;
    }

    /**
     * @param item sets new interaction item.
     */
    public void setItem(Item item) {
        this.item = item;
    }
}
