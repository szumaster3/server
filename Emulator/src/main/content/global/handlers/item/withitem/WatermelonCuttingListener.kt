package content.global.handlers.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

class WatermelonCuttingListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles cutting the watermelon.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.WATERMELON_5982) { player, _, _ ->
            if (!inInventory(player, Items.WATERMELON_5982)) return@onUseWith true

            animate(player, Animations.CUT_WATERMELON_2269)
            val duration = animationDuration(Animation.create(Animations.CUT_WATERMELON_2269))
            queueScript(player, duration) {
                if (removeItem(player, Items.WATERMELON_5982)) {
                    repeat(3) {
                        val slice = Item(Items.WATERMELON_SLICE_5984)
                        if (!player.inventory.add(slice)) {
                            GroundItemManager.create(slice, player)
                        }
                    }
                    sendMessage(player, "You slice the watermelon into three slices.")
                }
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }
    }
}
