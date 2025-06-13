package content.global.plugin.item

import core.api.getUsedOption
import core.api.sendItemDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class CadavaPotionListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles options for Cadava potion (Romeo & Juliet quest).
         */

        on(Items.CADAVA_POTION_756, IntType.ITEM, "drink", "look-at") { player, node ->
            val option = getUsedOption(player)
            sendItemDialogue(
                player,
                node.id,
                if (option == "drink") "You dare not drink." else "This looks very colourful.",
            )
            return@on true
        }
    }
}
