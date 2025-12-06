package content.minigame.blastfurnace.plugin

import core.tools.RandomFunction

/**
 * Tracks the state of the Blast Furnace machine.
 * Handles temperatures, coke usage, ticking, and random breakages.
 */
class BlastState {
    /**
     * If true, components cannot break.
     */
    var disableBreaking = false

    /**
     * If true, force break on the next check.
     */
    var forceBreaking = false

    /**
     * Whether the pot pipe is currently broken.
     */
    var potPipeBroken = false

    /**
     * Whether the pump pipe is currently broken.
     */
    var pumpPipeBroken = false

    /**
     * Whether the conveyor belt is broken.
     */
    var beltBroken = false

    /**
     * Whether the cog is broken.
     */
    var cogBroken = false

    /**
     * Number of ticks since start.
     */
    var ticksElapsed = 0

    /**
     * Current stove temperature (0-100).
     */
    var stoveTemp = 0
        private set

    /**
     * Current furnace temperature (0-100).
     */
    var furnaceTemp = 0
        private set

    /**
     * Amount of coke currently burning in the stove.
     */
    var cokeInStove = 0

    /**
     * Advances the Blast Furnace state by one tick.
     */
    fun tick(pumping: Boolean, pedaling: Boolean) {
        ticksElapsed++

        adjustStoveTemperature()
        adjustFurnaceTemperature(pumping)

        checkForCokeDecrement()
        checkForBreakage(pedaling, pumping)
    }

    /**
     * Increases or decreases stove temperature
     * depending on coke.
     */
    private fun adjustStoveTemperature() {
        if (cokeInStove > 0) {
            stoveTemp = (stoveTemp + 1).coerceAtMost(100)
        } else {
            stoveTemp = (stoveTemp - 1).coerceAtLeast(0)
        }
    }

    /**
     * Adjusts furnace temperature based on stove
     * temperature and pumping status.
     */
    private fun adjustFurnaceTemperature(pumping: Boolean) {
        if (pumping && !pumpPipeBroken && !potPipeBroken && !beltBroken && !cogBroken) {
            when (stoveTemp) {
                in 1..32 -> furnaceTemp += 1
                in 32..66 -> furnaceTemp += 2
                in 67..100 -> furnaceTemp += 3
            }
        } else {
            furnaceTemp--
        }

        furnaceTemp = furnaceTemp.coerceAtLeast(0).coerceAtMost(100)
    }

    /**
     * Randomly checks for component breakages while pumping or pedaling.
     */
    private fun checkForBreakage(pedaling: Boolean, pumping: Boolean) {
        if (disableBreaking) return
        if (pumping && (!potPipeBroken || !pumpPipeBroken)) {
            if (RandomFunction.roll(50) || forceBreaking) {
                if (RandomFunction.nextBool()) {
                    potPipeBroken = true
                } else {
                    pumpPipeBroken = true
                }
            }
        }

        if (pedaling && (!beltBroken || !cogBroken)) {
            if (RandomFunction.roll(50) || forceBreaking) {
                beltBroken = true
            } else if (RandomFunction.roll(50) || forceBreaking) {
                cogBroken = true
            }
        }
    }

    /**
     * Decreases coke every 10 ticks.
     */
    private fun checkForCokeDecrement() {
        if (ticksElapsed % 10 == 0) cokeInStove = (cokeInStove - 1).coerceAtLeast(0)
    }

    /**
     * Adds coke to the stove, up to the maximum limit.
     */
    fun addCoke(amount: Int) {
        cokeInStove += amount.coerceAtMost(BlastUtils.COKE_LIMIT - cokeInStove)
    }
}
