package core.game.global.action

import content.global.plugin.item.SpadeDigUtils.runListener
import core.api.log
import core.game.node.entity.player.Player
import core.game.system.communication.CommunicationInfo
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.Log
import org.rs.consts.Animations

/**
 * The `DigSpadeHandler` object manages the logic for digging actions that occur when a player uses a spade in certain locations.
 * It handles the animation of digging, submitting the task for the action to be processed, and registering actions to specific locations.
 */
object DigSpadeHandler {
    /**
     * A map holding the actions for each location where a digging action is registered.
     * Each location corresponds to a specific `DigAction` that will be executed when a player digs there.
     */
    private val ACTIONS: MutableMap<Location, DigAction> = HashMap()

    /**
     * The animation used for the digging action when the player uses a spade.
     */
    val ANIMATION: Animation = Animation.create(Animations.DIG_SPADE_830)

    /**
     * Handles the digging action for a player.
     * This method locks the player for a brief moment, triggers the digging animation,
     * and runs a listener for any custom actions associated with the player's location.
     *
     * If an action is registered for the player's location, the action will be queued to run via a `Pulse`.
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
     * If an action is already registered for the location, it will not be added again, and an error will be logged.
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
