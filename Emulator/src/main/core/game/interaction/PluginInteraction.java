package core.game.interaction;

import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;

/**
 * The type Plugin interaction.
 */
public abstract class PluginInteraction implements Plugin<Object> {

    /**
     * The Ids.
     */
    int[] ids;

    /**
     * The Item.
     */
    Item item;

    /**
     * Handle boolean.
     *
     * @param player the player
     * @param node   the node
     * @return the boolean
     */
    public boolean handle(Player player, Node node) {
        return false;
    }

    /**
     * Handle boolean.
     *
     * @param player the player
     * @param event  the event
     * @return the boolean
     */
    public boolean handle(Player player, NodeUsageEvent event) {
        return false;
    }

    /**
     * Handle boolean.
     *
     * @param player the player
     * @param npc    the npc
     * @param option the option
     * @return the boolean
     */
    public boolean handle(Player player, NPC npc, Option option) {
        return false;
    }

    /**
     * Handle boolean.
     *
     * @param player the player
     * @param item   the item
     * @param option the option
     * @return the boolean
     */
    public boolean handle(Player player, Item item, Option option) {
        return false;
    }

    /**
     * Instantiates a new Plugin interaction.
     *
     * @param ids the ids
     */
    public PluginInteraction(int... ids) {
        this.ids = ids;
    }

    /**
     * Instantiates a new Plugin interaction.
     *
     * @param item the item
     */
    public PluginInteraction(Item item) {
        this.item = item;
    }

    /**
     * Sets ids.
     *
     * @param ids the ids
     */
    public void setIds(int[] ids) {
        this.ids = ids;
    }

    /**
     * Sets item.
     *
     * @param item the item
     */
    public void setItem(Item item) {
        this.item = item;
    }
}
