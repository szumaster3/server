package content.region.kandarin.seers_village.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class SinclairFlourBarrelPlugin : InteractionListener {

    companion object {
        private const val DIARY_TASK_INDEX = 0
        private const val DIARY_TASK_ID = 5
        private const val MAX_FLOUR_COUNT = 4
    }

    override fun defineListeners() {
        /*
         * Handles taking flour from the barrel.
         */

        on(Scenery.BARREL_OF_FLOUR_26122, IntType.SCENERY, "take from") { player, _ ->
            takeFlourFromBarrel(player)
            return@on true
        }

        /*
         * Handles using an empty pot on the flour barrel.
         */

        onUseWith(IntType.SCENERY, Items.EMPTY_POT_1931, Scenery.BARREL_OF_FLOUR_26122) { player, _, _ ->
            takeFlourFromBarrel(player)
            return@onUseWith true
        }

    }

    private fun takeFlourFromBarrel(player: Player) {
        if (!removeItem(player, Items.EMPTY_POT_1931)) {
            sendMessage(player, "I need an empty pot to hold the flour in.")
            return
        }
        lock(player, 3)
        animate(player, Animations.MULTI_TAKE_832)
        addItem(player, Items.POT_OF_FLOUR_1933, 1)
        sendMessage(player, "You take some flour from the barrel. There's still plenty of flour left.")
        updateDiaryProgress(player)
    }

    private fun updateDiaryProgress(player: Player) {
        if (!hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, DIARY_TASK_INDEX, DIARY_TASK_ID)) {
            val flourCount = player.getAttribute("diary:seers:sinclair-flour", 0)
            if (flourCount >= MAX_FLOUR_COUNT) {
                setAttribute(player, "/save:diary:seers:sinclair-flour", DIARY_TASK_ID)
                finishDiaryTask(player, DiaryType.SEERS_VILLAGE, DIARY_TASK_INDEX, DIARY_TASK_ID)
            } else {
                setAttribute(player, "/save:diary:seers:sinclair-flour", flourCount + 1)
            }
        }
    }

}