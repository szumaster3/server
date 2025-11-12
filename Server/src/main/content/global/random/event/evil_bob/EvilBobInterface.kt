package content.global.random.event.evil_bob

import core.api.closeOverlay
import core.api.queueScript
import core.api.stopExecuting
import core.api.teleport
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.world.map.Location
import shared.consts.Components

/**
 * Represents the Evil Bob random event dialogue.
 * @author szu, Zerken
 */
class EvilBobInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.MACRO_EVIL_BOB_186) { player, _, _, buttonID, _, _ ->
            if (buttonID >= 1) {
                closeOverlay(player)
                queueScript(player, 1, QueueStrength.STRONG) {
                    teleport(player, Location(3422, 4777, 0))
                    return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }
}
