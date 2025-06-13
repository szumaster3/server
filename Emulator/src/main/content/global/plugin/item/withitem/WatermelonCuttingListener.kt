package content.global.plugin.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

class WatermelonCuttingListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles cutting the watermelon.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.WATERMELON_5982) { player, _, _ ->
            if (!removeItem(player, Items.WATERMELON_5982)) return@onUseWith true

            animate(player, Animations.CUT_WATERMELON_2269)
            val duration = animationDuration(Animation(Animations.CUT_WATERMELON_2269))

            queueScript(player, duration) {
                addItemOrDrop(player, Items.WATERMELON_SLICE_5984, 3)
                sendMessage(player, "You slice the watermelon into three slices.")
                stopExecuting(player)
            }

            return@onUseWith true
        }
    }
}
