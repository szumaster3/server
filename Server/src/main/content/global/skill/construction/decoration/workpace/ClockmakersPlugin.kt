package content.global.skill.construction.decoration.workpace

import content.global.skill.construction.BuildingUtils
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Items
import shared.consts.Scenery

class ClockmakersPlugin : InteractionListener {

    override fun defineListeners() {
        on(CLOCKMAKER_FURNITURE_IDS, IntType.SCENERY, "craft") { player, node ->
            val unlockLevel = node.id - Scenery.CLOCKMAKER_S_BENCH_13709
            val productList =
                when (node.id) {
                    Scenery.CLOCKMAKER_S_BENCH_13712 ->
                        listOf(
                            Products.CLOCKWORK,
                            Products.TOY_DOLL,
                            Products.TOY_MOUSE,
                            Products.SEXTANT,
                            Products.WATCH
                        )

                    else -> enumValues<Products>().take(unlockLevel + 2)
                }

            rollItem(productList)
            setTitle(player, productList.size)
            sendOptions(
                player,
                "What would you like to craft?",
                *productList.map { getItemName(it.itemId) }.toTypedArray()
            )

            addDialogueAction(player) { _, buttonID ->
                val index = buttonID - 2
                if (index in productList.indices) {
                    craftProduct(player, productList[index])
                }
            }

            return@on true
        }
    }

    private fun rollItem(productList: List<Products>) {
        val toyHorseyProduct = productList.find { it == Products.TOY_HORSEY } ?: return
        toyHorseyProduct.itemId = TOY_HORSEY_IDS.random()
    }

    private fun craftProduct(player: Player, product: Products) {
        if (getStatLevel(player, Skills.CRAFTING) < product.craftingLevel) {
            sendMessage(player, "You need level ${product.craftingLevel} crafting to make that.")
            return
        }

        if (product == Products.WOODEN_CAT) {
            val ingredientID = anyInInventory(player, *WOODEN_CAT_SPECIAL_IDS)
            if (!inInventory(player, BuildingUtils.PLANK.id)) {
                sendMessage(player, "You need a plank and fur to make that.")
                return
            }
            removeItem(player, BuildingUtils.PLANK)
            removeItem(player, ingredientID)
        } else {
            if (!allInInventory(player, *product.materials)) {
                sendMessage(player, "You need the required materials to make that.")
                return
            }
            product.materials.forEach { removeItem(player, it) }
        }

        if (product.itemId == 0) {
            return
        }

        animate(player, BuildingUtils.BUILD_MID_ANIM)
        addItem(player, product.itemId, 1)
        rewardXP(player, Skills.CRAFTING, 15.0)
        val item = getItemName(product.itemId).lowercase()
        sendMessage(player, "You made a $item.")
        closeDialogue(player)
    }

    private enum class Products(var itemId: Int, val craftingLevel: Int, vararg val materials: Int) {
        TOY_HORSEY(0, 10, BuildingUtils.PLANK.id),
        WOODEN_CAT(Items.WOODEN_CAT_10892, 10, BuildingUtils.PLANK.id),
        CLOCKWORK(Items.CLOCKWORK_8792, 8, Items.STEEL_BAR_2353),
        TOY_DOLL(Items.TOY_DOLL_7763, 18, BuildingUtils.PLANK.id, Items.CLOCKWORK_8792),
        TOY_MOUSE(Items.TOY_MOUSE_7767, 33, BuildingUtils.PLANK.id, Items.CLOCKWORK_8792),
        WATCH(Items.WATCH_2575, 28, Items.CLOCKWORK_8792, Items.STEEL_BAR_2353),
        SEXTANT(Items.SEXTANT_2574, 23, Items.STEEL_BAR_2353),
    }

    companion object {
        private val TOY_HORSEY_IDS = listOf(
            Items.TOY_HORSEY_2520,
            Items.TOY_HORSEY_2522,
            Items.TOY_HORSEY_2524,
            Items.TOY_HORSEY_2526,
        )
        private val CLOCKMAKER_FURNITURE_IDS = intArrayOf(
            Scenery.CLOCKMAKER_S_BENCH_13709,
            Scenery.CLOCKMAKER_S_BENCH_13710,
            Scenery.CLOCKMAKER_S_BENCH_13711,
            Scenery.CLOCKMAKER_S_BENCH_13712,
        )
        private val WOODEN_CAT_SPECIAL_IDS = intArrayOf(
            Items.BEAR_FUR_949,
            Items.FUR_6814,
            Items.GREY_WOLF_FUR_959,
        )
    }
}