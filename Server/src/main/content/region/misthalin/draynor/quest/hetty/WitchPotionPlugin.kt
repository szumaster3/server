package content.region.misthalin.draynor.quest.hetty

import core.api.getQuestStage
import core.api.sendMessage
import core.api.sendPlayerDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class WitchPotionPlugin: InteractionListener {
    override fun defineListeners() {

        /*
         * Handles drinking from cauldron (Witch potion quest).
         */

        on(Scenery.CAULDRON_2024, IntType.SCENERY, "Drink From") { player, _ ->
            when (getQuestStage(player, Quests.WITCHS_POTION)) {
                20, 100 -> sendPlayerDialogue(player, "As nice as that looks I think I'll give it a miss for now.")
                40 -> player.dialogueInterpreter.open(NPCs.HETTY_307, true, 1)
                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }
    }
}
