package content.region.karamja.handlers

import core.api.getUsedOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.shops.Shops

class TaiBwoWannaiListener : InteractionListener {
    override fun defineListeners() {
        on(GABOOTY_NPC_WRAPPERS, IntType.NPC, "trade-co-op", "trade-drinks") { player, node ->
            if (node.id in GABOOTY_NPC_WRAPPERS) {
                when (getUsedOption(player)) {
                    "trade-co-op" -> Shops.openId(player, 226)
                    "trade-drinks" -> Shops.openId(player, 254)
                }
            }
            return@on true
        }
    }

    companion object {
        private val GABOOTY_NPC_WRAPPERS = intArrayOf(2519, 2520, 2521, 2522)
    }
}
