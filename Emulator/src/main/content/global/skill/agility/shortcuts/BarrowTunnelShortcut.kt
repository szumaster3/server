package content.global.skill.agility.shortcuts

import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.Scenery

class BarrowTunnelShortcut : InteractionListener {
    val SHORTCUTS =
        intArrayOf(
            Scenery.WOODEN_DOORS_30261,
            Scenery.WOODEN_DOORS_30262,
            Scenery.WOODEN_DOORS_30265,
        )

    override fun defineListeners() {
        on(SHORTCUTS, IntType.SCENERY, "open") { player, node ->
            handleShortcut(node.id, player)
            return@on true
        }
    }

    private fun handleShortcut(
        nodeId: Int,
        player: Player,
    ) {
        val destination =
            when (nodeId) {
                Scenery.WOODEN_DOORS_30261, Scenery.WOODEN_DOORS_30262 -> Location(3509, 3448)
                Scenery.WOODEN_DOORS_30265 -> Location(3500, 9812)
                else -> return
            }
        teleport(player, destination)
    }
}
