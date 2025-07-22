package content.region.asgarnia.taverley.plugin

import content.region.asgarnia.taverley.dialogue.TegidDialogue
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class TaverleyPlugin : InteractionListener {

    override fun defineListeners() {
        on(NPCs.TEGID_1213, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, TegidDialogue())
            return@on true
        }
    }
}
