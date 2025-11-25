package content.region.misthalin.dialogue

import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.addDialogueAction
import core.api.closeDialogue
import core.api.openNpcShop
import core.api.sendNPCDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.NPCs

class FishmongerPlugin : InteractionListener {

    override fun defineListeners() {
        on(NPCs.FISHMONGER_1369, IntType.NPC, "talk-to") { player, _ ->
            sendNPCDialogue(player, NPCs.FISHMONGER_1369, "Welcome, ${FremennikTrials.getFremennikName(player)}. My fish is fresher than any in Miscellania.", FaceAnim.FRIENDLY)
            addDialogueAction(player
            ) { p,button ->
                if(button != 0) {
                    closeDialogue(p)
                    openNpcShop(p, NPCs.FISHMONGER_1369)
                }
            }
            return@on true
        }
    }
}