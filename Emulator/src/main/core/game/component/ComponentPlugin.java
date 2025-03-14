package core.game.component;

import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * The type Component plugin.
 */
public abstract class ComponentPlugin implements Plugin<Object> {

    /**
     * Handle boolean.
     *
     * @param player    the player
     * @param component the component
     * @param opcode    the opcode
     * @param button    the button
     * @param slot      the slot
     * @param itemId    the item id
     * @return the boolean
     */
    public abstract boolean handle(final Player player, Component component, final int opcode, final int button, int slot, int itemId);

    /**
     * Open.
     *
     * @param player    the player
     * @param component the component
     */
    public void open(Player player, Component component) {
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

}
