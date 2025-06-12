package content.region.island.miscellania.plugin

import content.region.fremennik.miscellania.dialogue.FarmerFromundDialogue
import content.region.fremennik.miscellania.dialogue.FishermanFrodiDialogue
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class MiscellaniaPlugin : InteractionListener {

    override fun defineListeners() {
        on(NPCs.FISHERMAN_FRODI_1397, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, FishermanFrodiDialogue())
            return@on true
        }

        on(NPCs.FARMER_FROMUND_3917, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, FarmerFromundDialogue())
            return@on true
        }
    }
}
