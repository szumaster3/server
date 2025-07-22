package content.global.skill.construction.decoration.kitchen

import core.api.addItem
import core.api.animate
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

class BarrelPlugin : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.SCENERY, BARRELS) { player, used, _ ->
            val node = used.id as Scenery
            if (removeItem(player, Item(BEER_GLASS))) {
                animate(player, Animation.create(Animations.FILLING_BEER_MUG_FROM_TAP_3661 + (used.id - 13569)))
                sendMessage(
                    player, "You fill up your glass with ${node.name.lowercase().replace("barrel", "").trim()}."
                )
                addItem(player, getReward(node.id), 1)
            }
            return@onUseWith true
        }
    }

    private fun getReward(barrelId: Int): Int = REWARDS[barrelId - 13568] ?: DEFAULT_REWARD

    companion object {
        private val BARRELS = intArrayOf(
            org.rs.consts.Scenery.BEER_BARREL_13568,
            org.rs.consts.Scenery.CIDER_BARREL_13569,
            org.rs.consts.Scenery.ASGARNIAN_ALE_13570,
            org.rs.consts.Scenery.GREENMAN_S_ALE_13571,
            org.rs.consts.Scenery.DRAGON_BITTER_13572,
            org.rs.consts.Scenery.CHEF_S_DELIGHT_13573
        )
        private val REWARDS = mapOf(
            org.rs.consts.Scenery.BEER_BARREL_13568 to Items.BEER_1917,
            org.rs.consts.Scenery.CIDER_BARREL_13569 to Items.CIDER_5763,
            org.rs.consts.Scenery.ASGARNIAN_ALE_13570 to Items.ASGARNIAN_ALE_1905,
            org.rs.consts.Scenery.GREENMAN_S_ALE_13571 to Items.GREENMANS_ALE_1909,
            org.rs.consts.Scenery.DRAGON_BITTER_13572 to Items.DRAGON_BITTER_1911,
            org.rs.consts.Scenery.CHEF_S_DELIGHT_13573 to Items.CHEFS_DELIGHT_5755
        )
        private const val DEFAULT_REWARD = Items.BEER_1917
        private const val BEER_GLASS = Items.BEER_GLASS_1919
    }
}
