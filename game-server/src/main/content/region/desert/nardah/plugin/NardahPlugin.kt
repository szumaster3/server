package content.region.desert.nardah.plugin

import core.api.sendDialogue
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class NardahPlugin: InteractionListener {

    override fun defineListeners() {

        on(NPCs.AWUSAH_THE_MAYOR_3040, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The mayor doesn't seem interested in talking to you right now.")
            return@on true
        }

        on(NPCs.GHASLOR_THE_ELDER_3029, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Good day young ${if (player.isMale) "gentleman" else "lady"}.")
            return@on true
        }
    }
}