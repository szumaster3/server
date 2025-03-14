package content.global.handlers.item.withnpc

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class RopeOnLadyKeliListener : InteractionListener {
    override fun defineListeners() {
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
