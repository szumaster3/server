package content.minigame.gnomecook.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class GnomeBowlListener : InteractionListener {

    private val sceneryIDs = intArrayOf(Scenery.GNOME_COOKER_17131, Scenery.RANGE_2728)

    override fun defineListeners() {
        val cookingMap = mapOf(
            Items.RAW_GNOMEBOWL_2178 to Items.HALF_BAKED_BOWL_2177,
            Items.HALF_MADE_BOWL_9558 to Items.UNFINISHED_BOWL_9560,
            Items.HALF_MADE_BOWL_9559 to Items.TANGLED_TOADS_LEGS_2187,
            Items.HALF_MADE_BOWL_9561 to Items.UNFINISHED_BOWL_9562,
            Items.HALF_MADE_BOWL_9563 to Items.UNFINISHED_BOWL_9564
        )

        cookingMap.forEach { (raw, cooked) ->
            onUseWith(IntType.SCENERY, raw, *sceneryIDs) { player, used, _ ->
                if (!inInventory(player, used.id)) return@onUseWith false
                lock(player, 2)
                animate(player, Animations.HUMAN_MAKE_PIZZA_883)
                queueScript(player, 2, QueueStrength.SOFT) {
                    removeItem(player, used)
                    addItem(player, cooked, 1)

                    if (cooked > 2180) {
                        addItem(player, Items.GNOMEBOWL_MOULD_2166, 1)
                    }
                    rewardXP(player, Skills.COOKING, 30.0)
                    return@queueScript stopExecuting(player)
                }
                return@onUseWith true
            }
        }


        val garnishMap = mapOf(
            Pair(Items.UNFINISHED_BOWL_9562, Items.EQUA_LEAVES_2128) to Items.VEG_BALL_2195,
            Pair(Items.UNFINISHED_BOWL_9564, Items.EQUA_LEAVES_2128) to Items.WORM_HOLE_2191,
            Pair(Items.UNFINISHED_BOWL_9560, Items.POT_OF_CREAM_2130) to Items.CHOCOLATE_BOMB_2185,
            Pair(Items.UNFINISHED_BOWL_9560, Items.CHOCOLATE_DUST_1975) to Items.CHOCOLATE_BOMB_2185
        )

        onUseWith(
            IntType.ITEM,
            garnishMap.keys.map { it.first }.toIntArray(),
            *garnishMap.keys.map { it.second }.toIntArray()
        ) { player, used, with ->
            val product = garnishMap[Pair(used.id, with.id)] ?: return@onUseWith false

            if (product == Items.CHOCOLATE_BOMB_2185) {
                val reqCream = Item(Items.POT_OF_CREAM_2130, 2)
                val reqChoc = Item(Items.CHOCOLATE_DUST_1975)

                if (!inInventory(player, reqChoc.id) || !player.inventory.containsItem(reqCream)) {
                    sendDialogue(player, "You don't have enough ingredients to finish that.")
                    return@onUseWith true
                }
                removeItem(player, reqCream)
                removeItem(player, reqChoc)
            }

            removeItem(player, used.asItem())
            removeItem(player, with.asItem())
            addItem(player, product, 1)
            return@onUseWith true
        }
    }
}
