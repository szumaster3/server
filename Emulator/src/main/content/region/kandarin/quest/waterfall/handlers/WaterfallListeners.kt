package content.region.kandarin.quest.waterfall.handlers

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class WaterfallListeners : InteractionListener {
    companion object {
        const val HUDON = NPCs.HUDON_305
        const val TOMBSTONE = Scenery.GLARIAL_S_TOMBSTONE_1992
        const val TOMBSTONE_INTERFACE = Components.GLARIALS_TOMBSTONE_221
        val TOMBSTONE_INTERFACE_CONTENT =
            arrayOf(
                "Here lies Glarial, wife of Baxtorian,",
                "true friend of nature in life and death.",
                "",
                "May she now rest knowing",
                "only visitors with peaceful intent can enter.",
            )
    }

    override fun defineListeners() {
        on(HUDON, IntType.NPC, "talk-to") { player, node ->
            player.dialogueInterpreter.open(HUDON, node.asNpc())
            return@on true
        }

        on(TOMBSTONE, IntType.SCENERY, "read") { player, node ->
            openInterface(player, TOMBSTONE_INTERFACE).also {
                sendString(player, TOMBSTONE_INTERFACE_CONTENT.joinToString(" <br> "), TOMBSTONE_INTERFACE, 2)
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, HUDON) { _, _ ->
            return@setDest Location.create(2512, 3481, 0)
        }
    }
}
