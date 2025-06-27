package core.game.node.entity.impl

import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatPulse
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser

/**
 * Manages the pulses (tasks).
 */
class PulseManager {
    /**
     * A map of currently active pulses, keyed by their respective pulse type.
     */
    private val currentPulses = HashMap<PulseType, Pulse?>()

    /**
     * Runs a pulse with the default pulse type of [PulseType.STANDARD].
     *
     * @param pulse The pulse to run.
     */
    @JvmOverloads
    fun run(pulse: Pulse, pulseType: PulseType = PulseType.STANDARD) {
        val toRemove = ArrayList<PulseType>(currentPulses.size)
        currentPulses.forEach { (key: PulseType?, value: Pulse?) ->
            if (value != null && !value.isRunning) {
                toRemove.add(key)
            }
        }
        for (t in toRemove) currentPulses.remove(t)

        if (currentPulses[PulseType.STRONG] != null) {
            return
        }

        if (!clear(pulseType)) {
            return
        }

        if (pulseType == PulseType.STRONG) {
            clear()
        }

        currentPulses[pulseType] = pulse
        pulse.start()
        if (pulse.isRunning) {
            Pulser.submit(pulse)
        }
    }

    /**
     * Clears all non-strong pulses from the manager, stopping them if necessary.
     */
    fun clear() {
        currentPulses.forEach { (type: PulseType?, pulse: Pulse?) ->
            if (type != PulseType.STRONG && pulse != null) pulse.stop()
        }
    }

    /**
     * Clears the specified pulse type from the manager and stops the pulse if it's running.
     *
     * @param pulseType The pulse type to clear.
     * @return `true` if the pulse was successfully cleared, `false` otherwise.
     */
    fun clear(pulseType: PulseType): Boolean {
        val pulse = currentPulses[pulseType]

        if (pulse != null) {
            pulse.stop()
            currentPulses.remove(pulseType)
        }
        return true
    }

    /**
     * Runs a default unhandled action pulse for the given player, sending a default message when executed.
     *
     * @param player    The player to run the pulse for.
     * @param pulseType The type of pulse to run.
     * @return The created pulse.
     */
    fun runUnhandledAction(player: Player, pulseType: PulseType): Pulse {
        val pulse: Pulse = object : Pulse(1, player) {
            override fun pulse(): Boolean {
                player.packetDispatch.sendMessage("Nothing interesting happens.")
                return true
            }
        }
        run(pulse, pulseType)
        return pulse
    }

    val isMovingPulse: Boolean
        /**
         * Checks if the current pulse is a movement or combat pulse.
         *
         * @return `true` if the current pulse is a movement or combat pulse, `false` otherwise.
         */
        get() {
            if (!hasPulseRunning()) {
                return false
            }

            val current = current
            return current is MovementPulse || current is CombatPulse
        }

    /**
     * Checks if there is any pulse currently running.
     *
     * @return `true` if a pulse is running, `false` otherwise.
     */
    fun hasPulseRunning(): Boolean {
        return current != null && current!!.isRunning
    }

    val current: Pulse?
        /**
         * Gets the current pulse that is running, or `null` if no pulse is running.
         *
         * @return The current pulse, or `null` if none is running.
         */
        get() {
            val types = PulseType.values()
            for (type in types) {
                if (currentPulses[type] != null) {
                    return currentPulses[type]
                }
            }
            return null
        }

    companion object {
        /**
         * Cancels the death task for the given entity, if one exists.
         *
         * @param e The entity whose death task should be canceled.
         */
        @JvmStatic
        fun cancelDeathTask(e: Entity) {
            if (!DeathTask.isDead(e) || e.pulseManager.current == null) {
                return
            }
            e.pulseManager.current!!.stop()
        }
    }
}
