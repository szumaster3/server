package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location

class TroubleBrewingRumEffect(
    val forceChatMessage: String,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        val teleportation: Pulse =
            object : Pulse(6) {
                override fun pulse(): Boolean {
                    player.teleport(TROUBLE_BREWING_MINIGAME)
                    return true
                }
            }

        val mainPulse: Pulse =
            object : Pulse(4) {
                override fun pulse(): Boolean {
                    player.sendChat(forceChatMessage)
                    player.pulseManager.run(teleportation)
                    return true
                }
            }

        player.pulseManager.run(mainPulse)
    }

    companion object {
        private val TROUBLE_BREWING_MINIGAME = Location(3813, 3022)
    }
}
