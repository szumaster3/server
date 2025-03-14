package content.global.activity.oldman

import core.api.getAttribute
import core.game.node.entity.player.Player

object WomTask {
    val TASK = "womt-task"

    val TASK_START = "womt-start"

    val TASK_COMPLETE = "womt-complete"

    val TASK_AMOUNT = "womt-amount"

    val TASK_COUNTER: String = "womt-total"

    val ITEM_DELIVERY = "item-delivery"

    val LETTER_DELIVERY = "letter-delivery"

    fun getTaskCounter(player: Player) {
        getAttribute(player, TASK_COUNTER, -1)
    }

    fun setTaskCounter(player: Player) {
        return player.incrementAttribute(TASK_COUNTER)
    }
}
