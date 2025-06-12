package content.region.kandarin.seers_village.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.shops.Shops.Companion.openId
import org.rs.consts.*

class SeersVillagePlugin : InteractionListener {
    companion object {
        private const val COURTHOUSE_STAIRS = Scenery.STAIRS_26017
        private const val CAGE = Scenery.CAGE_6836
        private const val CRATE = Scenery.CRATE_6839
        private const val TICKET_MERCHANT = NPCs.TICKET_MERCHANT_694
    }

    override fun defineListeners() {
        on(COURTHOUSE_STAIRS, IntType.SCENERY, "climb-down") { player, _ ->
            sendMessage(player, "Court is not in session.")
            return@on true
        }

        on(CRATE, IntType.SCENERY, "buy") { player, _ ->
            openId(player, 93)
            return@on true
        }

        on(TICKET_MERCHANT, IntType.NPC, "trade") { player: Player, _: Node ->
            openInterface(player, Components.RANGING_GUILD_TICKET_EXCHANGE_278)
            return@on true
        }
    }
}
