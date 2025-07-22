package content.minigame.mta.plugin

import core.api.*
import core.game.node.entity.player.Player
import org.rs.consts.Items

/**
 * Handles the interaction with progress hat.
 */
object ProgressHat {

    /**
     * An array of progress hat item ids.
     */
    val hatIds = intArrayOf(
        Items.PROGRESS_HAT_6885,
        Items.PROGRESS_HAT_6886,
        Items.PROGRESS_HAT_6887
    )

    /**
     * Updates the player progress hat based on their total pizazz points.
     */
    @JvmStatic
    fun progress(player: Player) {
        val totalPoints = MTAZone.getTotalPoints(player)

        val hatToAdd = when {
            totalPoints > 600 -> Items.PROGRESS_HAT_6887
            totalPoints in 301..600 -> Items.PROGRESS_HAT_6886
            else -> Items.PROGRESS_HAT_6885
        }

        hatIds.forEach { hat ->
            if (inInventory(player, hat)) {
                sendMessage(player, "The hat shifts unexpectedly.")
                removeItem(player, hat)
            }
        }
        addItem(player, hatToAdd, 1)
        sendMessage(player, "Your progress hat updates to reflect your achievements.")
    }

    /**
     * Resets the player pizazz points stored in varbits.
     */
    @JvmStatic
    fun resetProgress(player: Player): Boolean {
        MTAZone.pizazzVarbitIds.forEach { varbitId ->
            val currentPoints = getVarbit(player, varbitId)
            if (currentPoints > 0) {
                setVarbit(player, varbitId, 0, true)
            }
        }
        sendDialogueLines(
            player,
            "The hat whispers as you destroy it. You can get another from the",
            "Entrance Guardian."
        )
        return true
    }
}
