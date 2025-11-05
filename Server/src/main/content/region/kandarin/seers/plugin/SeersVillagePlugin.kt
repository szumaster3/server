package content.region.kandarin.seers.plugin

import core.api.openInterface
import core.api.sendMessage
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.shops.Shops.Companion.openId
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Scenery

class SeersVillagePlugin : InteractionListener {
    companion object {
        private const val COURTHOUSE_STAIRS = Scenery.STAIRS_26017
        private const val CAGE = Scenery.CAGE_6836
        private const val CRATE = Scenery.CRATE_6839
        private const val TICKET_MERCHANT = NPCs.TICKET_MERCHANT_694
    }

    override fun defineListeners() {
        /*
         * Handles interaction with the courthouse stairs.
         */

        on(Scenery.STAIRS_26017, IntType.SCENERY, "climb-down") { player, _ ->
            sendMessage(player, "Court is not in session.")
            return@on true
        }

        /*
         * Handles interaction with the crate inside Seers' Village.
         */

        on(Scenery.CRATE_6839, IntType.SCENERY, "buy") { player, _ ->
            openId(player, 93)
            return@on true
        }

        /*
         * Handles trade interaction with the Ranging Guild Ticket Merchant.
         */

        on(NPCs.TICKET_MERCHANT_694, IntType.NPC, "trade") { player: Player, _: Node ->
            openInterface(player, Components.RANGING_GUILD_TICKET_EXCHANGE_278)
            return@on true
        }

        /*
         * Handles player interaction with the Forester NPC.
         */

        on(NPCs.FORESTER_231, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "He doesn't seem interested in talking to you.")
            return@on true
        }
    }
}
