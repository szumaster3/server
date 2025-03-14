package content.minigame.mta.handlers

import core.api.addItem
import core.api.removeItem
import core.game.node.entity.player.Player
import org.rs.consts.Items

object ProgressHat {
    val hats = intArrayOf(Items.PROGRESS_HAT_6885, Items.PROGRESS_HAT_6886, Items.PROGRESS_HAT_6887)
    val thresholds = mapOf(Items.PROGRESS_HAT_6885 to 0, Items.PROGRESS_HAT_6886 to 300, Items.PROGRESS_HAT_6887 to 600)

    @JvmStatic
    fun progress(player: Player) {
        val activityData = player.getSavedData().activityData
        val g = activityData.getPizazzPoints(0)
        val a = activityData.getPizazzPoints(2)
        val t = activityData.getPizazzPoints(1)
        val e = activityData.getPizazzPoints(3)

        when {
            (g + a + t + e) > 600 -> addItem(player, hats[2])
            (g + a + t + e) in 301..599 -> addItem(player, hats[1])
            else -> addItem(player, hats[0])
        }
        hats.forEach { currentHat ->
            if (removeItem(player, currentHat)) {
                player.packetDispatch.sendMessage("The hat shifts unexpectedly.")
                addItem(player, currentHat)
            }
        }
    }
}
