package content.region.fremennik.waterbirth.plugin

import content.region.fremennik.waterbirth.dialogue.BardurDialogue
import content.region.fremennik.waterbirth.dialogue.BardurExchangeDialogue
import content.region.fremennik.waterbirth.npc.BardurNPC
import core.api.openDialogue
import core.api.isQuestComplete
import core.api.sendMessage
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.plugin.ClassScanner
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class BardurPlugin : InteractionListener {

    override fun defineListeners() {
        ClassScanner.definePlugin(BardurDialogue())
        ClassScanner.definePlugin(BardurNPC())

        /*
         * Handles interaction with Bardur NPCs.
         */

        onUseWith(IntType.NPC, exchangeItemIDs, NPCs.BARDUR_2879) { player, _, node ->
            val npc = node.asNpc()
            if (npc.inCombat()) {
                sendMessage(player, "He's busy right now.")
                return@onUseWith false
            }

            if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                sendNPCDialogue(player, NPCs.BARDUR_2879, "I do not trust you Outlander, I will not accept your gifts, no matter what your intention...")
            } else {
                npc.impactHandler.disabledTicks = 60
                openDialogue(player, BardurExchangeDialogue())
            }
            return@onUseWith true
        }

    }

    companion object {
        val exchangeItemIDs =
            intArrayOf(Items.FREMENNIK_HELM_3748, Items.FREMENNIK_BLADE_3757, Items.FREMENNIK_SHIELD_3758)
    }
}