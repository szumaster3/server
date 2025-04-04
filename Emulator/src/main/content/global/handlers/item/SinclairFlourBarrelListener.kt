package content.global.handlers.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player

import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Vars
import core.game.node.entity.player.link.diary.DiaryType

class SinclairFlourBarrelListener : InteractionListener {

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
        addItem(player, Items.POT_OF_FLOUR_1933)
        sendMessage(player, "You take some flour from the barrel.")
        updateDiaryProgress(player)
        sendMessage(player, "There's still plenty of flour left.")
    }

    private fun updateDiaryProgress(player: Player) {
        if (!hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, DIARY_TASK_INDEX, DIARY_TASK_ID)) {
            val currentFlourCount = getVarbit(player, Vars.VARBIT_FLOUR_BIN_STORAGE_4920)
            if (currentFlourCount >= MAX_FLOUR_COUNT) {
                setVarbit(player, Vars.VARBIT_FLOUR_BIN_STORAGE_4920, DIARY_TASK_ID)
            } else {
                setVarbit(player, Vars.VARBIT_FLOUR_BIN_STORAGE_4920, currentFlourCount + 1)
            }
        }
    }

}