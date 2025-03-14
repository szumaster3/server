package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.Context;

/**
 * The type Build item context.
 */
public final class BuildItemContext implements Context {

    private final Player player;

    private final Item item;

    private final int oldAmount;

    /**
     * Instantiates a new Build item context.
     *
     * @param player the player
     * @param item   the item
     */
    public BuildItemContext(Player player, Item item) {
        this(player, item, 0);
    }

    /**
     * Instantiates a new Build item context.
     *
     * @param player    the player
     * @param item      the item
     * @param oldAmount the old amount
     */
    public BuildItemContext(Player player, Item item, int oldAmount) {
        this.player = player;
        this.item = item;
        this.oldAmount = oldAmount;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets item.
     *
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets old amount.
     *
     * @return the old amount
     */
    public int getOldAmount() {
        return oldAmount;
    }

}