package content.global.skill.crafting.spinning

import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Scenery

class SpinningListener : InteractionListener {
    override fun defineListeners() {
        on(SPINING_WHEEL, IntType.SCENERY, "spin") { player, _ ->
            openInterface(player, Components.CRAFTING_SPINNING_459)
            return@on true
        }
    }

    companion object {
        val SPINING_WHEEL =
            intArrayOf(
                Scenery.SPINNING_WHEEL_2644,
                Scenery.SPINNING_WHEEL_4309,
                Scenery.SPINNING_WHEEL_8748,
                Scenery.SPINNING_WHEEL_20365,
                Scenery.SPINNING_WHEEL_21304,
                Scenery.SPINNING_WHEEL_25824,
                Scenery.SPINNING_WHEEL_26143,
                Scenery.SPINNING_WHEEL_34497,
                Scenery.SPINNING_WHEEL_36970,
                Scenery.SPINNING_WHEEL_37476,
            )
    }
}
