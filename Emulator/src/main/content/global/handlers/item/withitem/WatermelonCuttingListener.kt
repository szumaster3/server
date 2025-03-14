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
        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.WATERMELON_5982) { player, _, _ ->
            if (inInventory(player, Items.WATERMELON_5982)) {
                animate(player, Animations.CUT_WATERMELON_2269)
                queueScript(player, animationDuration(Animation.create(Animations.CUT_WATERMELON_2269))) {
                    if (removeItem(player, Items.WATERMELON_5982)) {
                        for (i in 0..2) {
                            if (!player.inventory.add(Item(Items.WATERMELON_SLICE_5984))) {
                                GroundItemManager.create(Item(Items.WATERMELON_SLICE_5984), player)
                            }
                        }
                        sendMessage(player, "You slice the watermelon into three slices.")
                    }
                    return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }
    }
}
