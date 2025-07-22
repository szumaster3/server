package core.game.component;

import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * Represents a plugin handler for interface components.
 */
public abstract class ComponentPlugin implements Plugin<Object> {

    /**
     * Handles an interaction event on a component.
     *
     * @param player    the player interacting with the component
     * @param component the component being interacted with
     * @param opcode    the opcode of the action (e.g., button click type)
     * @param button    the specific button ID that was interacted with
     * @param slot      the slot index involved in the interaction, if applicable
     * @param itemId    the item ID involved in the interaction, if applicable
     * @return {@code true} if the interaction was successfully handled, {@code false} otherwise
     */
    public abstract boolean handle(final Player player, Component component, final int opcode, final int button, int slot, int itemId);

    /**
     * Called when the component is opened by the player.
     *
     * @param player    the player opening the component
     * @param component the component being opened
     */
    public void open(Player player, Component component) {
    }

    /**
     * Dispatches a generic event to the plugin.
     *
     * @param identifier a string identifier for the event
     * @param args       additional arguments passed to the event
     * @return the event result, or {@code null} if not handled
     */
    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

}