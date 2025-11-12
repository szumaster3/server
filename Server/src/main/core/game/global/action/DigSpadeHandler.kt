package core.game.global.action

import content.global.plugins.item.SpadeDigUtils.runListener
import core.api.log
import core.game.node.entity.player.Player
import core.game.system.communication.CommunicationInfo
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.Log
import shared.consts.Animations

/**
 * The `DigSpadeHandler` object manages the logic for digging actions.
 */
object DigSpadeHandler {
    /**
     * A map holding the actions for each location where a digging action is registered.
     */
    private val ACTIONS: MutableMap<Location, DigAction> = HashMap()

    /**
     * The animation used for the digging action when the player uses a spade.
     */
    val ANIMATION: Animation = Animation.create(Animations.DIG_SPADE_830)

    /**
     * Handles the digging action for a player.
     *
     * @param player The player who is performing the digging action.
     * @return `true` if the digging action is successful and a listener or action was triggered, otherwise `false`.
     */
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

    /**
     * Registers a new digging action for a specific location.
     *
     * @param location The location where the digging action will be triggered.
     * @param action The `DigAction` to be executed when a player digs at the specified location.
     * @return `true` if the action was successfully registered, otherwise `false`.
     */
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
