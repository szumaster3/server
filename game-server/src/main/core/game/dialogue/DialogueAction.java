package core.game.dialogue;

import core.game.node.entity.player.Player;

/**
 * The interface Dialogue action.
 */
public interface DialogueAction {

    /**
     * Handle.
     *
     * @param player   the player
     * @param buttonId the button id
     */
    void handle(Player player, int buttonId);

}
