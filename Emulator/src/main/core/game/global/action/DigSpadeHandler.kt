package core.game.global.action

import content.global.handlers.item.SpadeDigUtils.runListener
import core.api.log
import core.game.node.entity.player.Player
import core.game.system.communication.CommunicationInfo
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.Log
import org.rs.consts.Animations

object DigSpadeHandler {
    private val ACTIONS: MutableMap<Location, DigAction> = HashMap()

    val ANIMATION: Animation = Animation.create(Animations.DIG_SPADE_830)

    @JvmStatic
    fun dig(player: Player): Boolean {
        val action = ACTIONS[player.location]
        player.animate(ANIMATION)
        player.lock(1)

        if (runListener(player.location, player)) {
            return true
        }

        if (action != null) {
            Pulser.submit(
                object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        action.run(player)
                        return true
                    }
                },
            )
            return true
        }
        return false
    }

    @JvmStatic
    fun register(
        location: Location,
        action: DigAction,
    ): Boolean {
        if (ACTIONS.containsKey(location)) {
            log(CommunicationInfo::class.java, Log.ERR, "Already contained dig reward for location $location.")
            return false
        }
        ACTIONS[location] = action
        return true
    }
}
