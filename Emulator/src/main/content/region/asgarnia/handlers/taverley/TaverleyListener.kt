package content.region.asgarnia.handlers.taverley

import content.region.asgarnia.dialogue.taverley.TegidDialogue
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class TaverleyListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.TEGID_1213, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, TegidDialogue())
            return@on true
        }
    }
}
