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

class BarrelSpace : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.SCENERY, BARRELS) { player, used, _ ->
            val node = used.id as Scenery
            if (removeItem(player, Item(BEER_GLASS))) {
                animate(player, Animation.create(Animations.FILLING_BEER_MUG_FROM_TAP_3661 + (used.id - 13569)))
                sendMessage(
                    player,
                    "You fill up your glass with ${node.name.lowercase().replace("barrel", "").trim()}.",
                )
                addItem(player, getReward(node.id), 1)
            }
            true
        }
    }

    private fun getReward(barrelId: Int): Int {
        return REWARDS[barrelId - 13568] ?: DEFAULT_REWARD
    }

    companion object {
        private val BARRELS = intArrayOf(13568, 13569, 13570, 13571, 13572, 13573)
        private val REWARDS =
            mapOf(
                13568 to 1917,
                13569 to 5763,
                13570 to 1905,
                13571 to 1909,
                13572 to 1911,
                13573 to 5755,
            )
        private const val DEFAULT_REWARD = 1917
        private const val BEER_GLASS = Items.BEER_GLASS_1919
    }
}
