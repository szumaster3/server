package content.region.misthalin.varrock.quest.firedup.plugin

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.NPCs

class AllFiredUpListener : InteractionListener {

    override fun defineListeners() {
        on(NPCs.SQUIRE_FYRE_8043, IntType.NPC, "Information") { _, _ ->
            return@on true
        }

        on(NPCs.ORRY_8045, IntType.NPC, "Information") { _, _ ->
            return@on true
        }

        on(NPCs.WALDO_8049, IntType.NPC, "Information") { _, _ ->
            return@on true
        }

        on(NPCs.GJALP_8051, IntType.NPC, "Information") { _, _ ->
            return@on true
        }
    }
}