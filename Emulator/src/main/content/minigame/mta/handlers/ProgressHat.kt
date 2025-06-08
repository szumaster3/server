package content.minigame.mta.handlers

import core.api.*
import core.game.node.entity.player.Player
import org.rs.consts.Items

/**
 * Handles the interaction with progress hat item.
 */
object ProgressHat {

    /**
     * Mapping hat item id to pizazz point slot indices.
     */
    private val hatIndexMap = mapOf(
        Items.PROGRESS_HAT_6885 to 0,
        Items.PROGRESS_HAT_6886 to 1,
        Items.PROGRESS_HAT_6887 to 2
    )

    /**
     * List of all progress hat ids
     */
    val hats = hatIndexMap.keys.toIntArray()

    /**
     * Get update based on pizazz points.
     *
     * @param player The player whose progress hat is being updated.
     */
    @JvmStatic
    fun progress(player: Player) {
        val activityData = player.getSavedData().activityData
        val g = activityData.getPizazzPoints(0)
        val a = activityData.getPizazzPoints(2)
        val t = activityData.getPizazzPoints(1)
        val e = activityData.getPizazzPoints(3)
        val totalPoints = g + a + t + e

        when {
            totalPoints > 600 -> addItem(player, Items.PROGRESS_HAT_6887)
            totalPoints in 301..599 -> addItem(player, Items.PROGRESS_HAT_6886)
            else -> addItem(player, Items.PROGRESS_HAT_6885)
        }

        hats.forEach { currentHat ->
            if (removeItem(player, currentHat)) {
                sendMessage(player, "The hat shifts unexpectedly.")
                addItem(player, currentHat, 1)
            }
        }
    }

    /**
     * Handles destroy for progress hat.
     *
     * @param player The player destroying the hat.
     * @return `true` progress reset.
     */
    @JvmStatic
    fun resetProgress(player: Player): Boolean {
        val activityData = player.getSavedData().activityData

        hatIndexMap.values.forEach { pizazzSlot ->
            activityData.decrementPizazz(pizazzSlot, activityData.getPizazzPoints(pizazzSlot))
        }

        sendDialogueLines(
            player,
            "The hat whispers as you destroy it. You can get another from the",
            "Entrance Guardian."
        )
        return true
    }
}