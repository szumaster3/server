package content.global.skill.construction.decoration.workshop

import content.global.skill.construction.BuildingUtils
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class ClockmakingSpace : InteractionListener {
    private val sceneryIDs =
        intArrayOf(
            Scenery.CLOCKMAKER_S_BENCH_13709,
            Scenery.CLOCKMAKER_S_BENCH_13710,
            Scenery.CLOCKMAKER_S_BENCH_13711,
            Scenery.CLOCKMAKER_S_BENCH_13712,
        )

    private enum class Products(
        var itemId: Int,
        val craftingLevel: Int,
        vararg val materials: Item,
    ) {
        TOY_HORSEY(0, 10, BuildingUtils.PLANK),
        WOODEN_CAT(Items.WOODEN_CAT_10892, 10, BuildingUtils.PLANK),
        CLOCKWORK(Items.CLOCKWORK_8792, 8, Item(Items.STEEL_BAR_2353)),
        TOY_DOLL(Items.TOY_DOLL_7763, 18, BuildingUtils.PLANK, Item(Items.CLOCKWORK_8792)),
        TOY_MOUSE(Items.TOY_MOUSE_7767, 33, BuildingUtils.PLANK, Item(Items.CLOCKWORK_8792)),
        WATCH(Items.WATCH_2575, 28, Item(Items.CLOCKWORK_8792), Item(Items.STEEL_BAR_2353)),
        SEXTANT(Items.SEXTANT_2574, 23, Item(Items.STEEL_BAR_2353)),
    }

    private val toyHorseyVariants =
        listOf(
            Items.TOY_HORSEY_2520,
            Items.TOY_HORSEY_2522,
            Items.TOY_HORSEY_2524,
            Items.TOY_HORSEY_2526,
        )

    private val woodenCatIngredientIDs =
        intArrayOf(
            Items.BEAR_FUR_949,
            Items.FUR_6814,
            Items.GREY_WOLF_FUR_959,
        )

    override fun defineListeners() {
        on(sceneryIDs, IntType.SCENERY, "craft") { player, node ->
            val unlockLevel = node.id - Scenery.CLOCKMAKER_S_BENCH_13709

            val productList =
                when (node.id) {
                    Scenery.CLOCKMAKER_S_BENCH_13712 -> {
                        listOf(
                            Products.CLOCKWORK,
                            Products.TOY_DOLL,
                            Products.TOY_MOUSE,
                            Products.SEXTANT,
                            Products.WATCH,
                        )
                    }
                    else -> {
                        enumValues<Products>().take(unlockLevel + 2)
                    }
                }

            // Roll toy horsey variant because I cannot find how it was in 2009.
            val toyHorseyProduct = productList.find { it == Products.TOY_HORSEY }
            if (toyHorseyProduct != null) {
                val randomToyHorsey = toyHorseyVariants.random()
                toyHorseyProduct.itemId = randomToyHorsey
            }

            setTitle(player, productList.size)
            sendDialogueOptions(
                player,
                "What would you like to craft?",
                *productList.map { getItemName(it.itemId) }.toTypedArray(),
            )
            addDialogueAction(player) { _, buttonID ->
                if (buttonID - 2 in productList.indices) {
                    craftItem(player, productList[buttonID - 2])
                }
            }
            return@on true
        }
    }

    private fun craftItem(
        player: Player,
        product: Products,
    ) {
        if (getStatLevel(player, Skills.CRAFTING) < product.craftingLevel) {
            sendMessage(player, "You need level ${product.craftingLevel} crafting to make that.")
            return
        }
        if (product == Products.WOODEN_CAT) {
            val ingredientID = woodenCatIngredientIDs.find { inInventory(player, it) }
            if (!inInventory(player, BuildingUtils.PLANK.id) || ingredientID == null) {
                sendMessage(player, "You need a plank and fur to make that.")
                return
            }
            removeItem(player, BuildingUtils.PLANK)
            removeItem(player, ingredientID)
        } else if (!product.materials.all { inInventory(player, it.id) }) {
            sendMessage(player, "You need the required materials to make that.")
            return
        } else {
            product.materials.forEach { player.inventory.remove(it) }
        }
        animate(player, BuildingUtils.BUILD_MID_ANIM)
        addItem(player, product.itemId, 1)
        rewardXP(player, Skills.CRAFTING, 15.0)
        sendMessage(player, "You made a ${getItemName(product.itemId).lowercase()}.")
    }
}
