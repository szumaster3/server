package content.minigame.troublebrewing

import core.api.inInventory
import core.game.node.entity.player.Player
import org.rs.consts.Items
import org.rs.consts.Quests

object MosleUtils {

    /**
     * Checks whether the player can understand the pirate language.
     *
     * A player is considered to understand the pirate language if:
     * - They meet the requirements for the "Rocking Out" quest, OR
     * - They have the "Book o' Piracy" item in their inventory.
     *
     * @param player The player whose status is being checked.
     * @return `true` if the player can understand pirate language, `false` otherwise.
     */
    fun canUnderstandPirateLanguage(player: Player): Boolean {
        if (core.api.quest.hasRequirement(player, Quests.ROCKING_OUT)) {
            return true
        }
        if (inInventory(player, Items.BOOK_O_PIRACY_7144)) {
            return true
        }

        return false
    }
}