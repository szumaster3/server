package content.global.plugins.item.withnpc

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class RopeOnLadyKeliPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles using rope on Lady Keli (Prince ali rescue quest).
         */

        onUseWith(IntType.NPC, Items.ROPE_954, NPCs.LADY_KELI_919) { player, used, _ ->
            if (getQuestStage(player, Quests.PRINCE_ALI_RESCUE) in 40..50 && getAttribute(player, "guard-drunk", false)
            ) {
                if (removeItem(player, used.asItem())) {
                    sendDialogue(player, "You overpower Keli, tie her up, and put her in a cupboard.")
                    setQuestStage(player, Quests.PRINCE_ALI_RESCUE, 50)
                    setAttribute(player, "keli-gone", getWorldTicks() + 350)
                }
            } else {
                if (getQuestStage(player, Quests.PRINCE_ALI_RESCUE) == 40) {
                    sendMessage(player, "You need to do something about the guard first.")
                }
            }
            return@onUseWith true
        }
    }
}
