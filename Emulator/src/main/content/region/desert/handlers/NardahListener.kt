package content.region.desert.handlers

import core.api.getUsedOption
import core.api.interaction.openNpcShop
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class NardahListener : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.KAZEMDE_3039, NPCs.ROKUH_3045), IntType.NPC, "talk-to", "trade") { player, node ->
            if (getUsedOption(player) == "trade") {
                openNpcShop(player, node.id)
            }
            if (getUsedOption(player) == "talk-to") {
                player.dialogueInterpreter.open(node.id)
            }
            return@on true
        }
    }
}
