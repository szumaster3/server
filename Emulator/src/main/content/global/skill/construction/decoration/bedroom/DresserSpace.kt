package content.global.skill.construction.decoration.bedroom

import core.api.animate
import core.api.openInterface
import core.api.sendString
import core.api.ui.sendInterfaceConfig
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Animations
import org.rs.consts.Scenery

class DresserSpace : InteractionListener {
    private val dresserSpaceFurniture =
        intArrayOf(
            Scenery.SHAVING_STAND_13162,
            Scenery.SHAVING_STAND_13163,
            Scenery.DRESSER_13164,
            Scenery.DRESSER_13165,
            Scenery.DRESSER_13166,
            Scenery.DRESSER_13167,
            Scenery.DRESSER_13168,
        )

    override fun defineListeners() {
        on(dresserSpaceFurniture, IntType.SCENERY, "preen") { player, node ->
            animate(player, Animations.PREEN_OPTION_TO_CHECK_SELF_IN_MIRROR_POH_3670)
            if (player.appearance.isMale) {
                openInterface(player, 596)
                sendString(player, node.name, 596, 64)
                sendInterfaceConfig(player, 592, 197, true)
            } else {
                openInterface(player, 592)
                sendString(player, node.name, 592, 18)
                sendInterfaceConfig(player, 592, 202, true)
            }
            return@on true
        }
    }
}
