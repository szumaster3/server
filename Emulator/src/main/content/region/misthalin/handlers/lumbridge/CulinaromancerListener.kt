package content.region.misthalin.handlers.lumbridge

import core.api.*
import core.api.quest.getQuestPoints
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.shops.Shop
import core.game.shops.ShopItem
import core.game.system.task.Pulse
import core.game.world.GameWorld
import org.rs.consts.Items
import java.lang.Integer.min

class CulinaromancerListener : LoginListener {
    override fun login(player: Player) {
        if (getQuestPoints(player) >= 18) {
            setVarbit(player, LumbridgeUtils.rfdChestVarbit, 5)

            setAttribute(player, "culino-tier", player.questRepository.points / 18)

            val restockPulse =
                object : Pulse(100) {
                    override fun pulse(): Boolean {
                        getShop(player, false).restock()
                        getShop(player, true).restock()
                        return false
                    }
                }
            GameWorld.Pulser.submit(restockPulse)

            player.logoutListeners["culino-restock"] = { restockPulse.stop() }
        }
    }

    companion object {
        private val foodShops = HashMap<Int, Shop>()
        private val gearShops = HashMap<Int, Shop>()

        @JvmStatic
        fun openShop(
            player: Player,
            food: Boolean,
        ) {
            getShop(player, food).openFor(player)
        }

        fun getShop(
            player: Player,
            food: Boolean,
        ): Shop {
            val uid = player.details.uid
            val points = player.questRepository.points
            val tier = (points / 18)

            if (tier != getAttribute(player, "culino-tier", 0)) {
                foodShops.remove(uid)
                gearShops.remove(uid)
            }
            return if (food) {
                val shop = foodShops[uid] ?: Shop("Culinaromancer's Chest Tier $tier", generateFoodStock(points), false)
                foodShops[uid] = shop
                shop
            } else {
                val shop = gearShops[uid] ?: Shop("Culinaromancer's Chest Tier $tier", generateGearStock(points), false)
                gearShops[uid] = shop
                shop
            }
        }

        private fun generateFoodStock(points: Int): Array<ShopItem> {
            val stock = Array(foodStock.size) { ShopItem(0, 0) }
            val maxQty =
                when (val qpTier = (points / 18) - 1) {
                    0, 1, 2, 3, 4 -> 1 + qpTier
                    else -> qpTier + (qpTier + (qpTier - 5))
                }
            for ((index, item) in foodStock.withIndex()) {
                stock[index].itemId = item.id
                stock[index].amount = if (item.id == Items.PIZZA_BASE_2283) 1 else maxQty
            }
            return stock
        }

        private fun generateGearStock(points: Int): Array<ShopItem> {
            val stock = Array(gearStock.size) { ShopItem(0, 0) }
            val qpTier = (points / 18)
            for ((index, item) in stock.withIndex()) item.itemId = gearStock[index]

            for (i in 0 until min(qpTier, 10)) {
                stock[i].amount = 30
                stock[i + 10].amount = 5
            }
            stock[9].amount = 1
            return stock
        }

        private val gearStock =
            arrayOf(
                Items.GLOVES_7453,
                Items.GLOVES_7454,
                Items.GLOVES_7455,
                Items.GLOVES_7456,
                Items.GLOVES_7457,
                Items.GLOVES_7458,
                Items.GLOVES_7459,
                Items.GLOVES_7460,
                Items.GLOVES_7461,
                Items.GLOVES_7462,
                Items.WOODEN_SPOON_7433,
                Items.EGG_WHISK_7435,
                Items.SPORK_7437,
                Items.SPATULA_7439,
                Items.FRYING_PAN_7441,
                Items.SKEWER_7443,
                Items.ROLLING_PIN_7445,
                Items.KITCHEN_KNIFE_7447,
                Items.MEAT_TENDERISER_7449,
                Items.CLEAVER_7451,
            )

        private val foodStock =
            arrayOf(
                Item(Items.CHOCOLATE_BAR_1973, 1),
                Item(Items.CHEESE_1985, 1),
                Item(Items.TOMATO_1982, 1),
                Item(Items.COOKING_APPLE_1955, 1),
                Item(Items.GRAPES_1987, 1),
                Item(Items.POT_OF_FLOUR_1933, 1),
                Item(Items.PIZZA_BASE_2283, 1),
                Item(Items.EGG_1944, 1),
                Item(Items.BUCKET_OF_MILK_1927, 1),
                Item(Items.POT_OF_CREAM_2130, 1),
                Item(Items.PAT_OF_BUTTER_6697, 1),
                Item(Items.SPICE_2007, 1),
                Item(Items.PIE_DISH_2313, 1),
                Item(Items.CAKE_TIN_1887, 1),
                Item(Items.BOWL_1923, 1),
                Item(Items.JUG_1935, 1),
                Item(Items.EMPTY_POT_1931, 1),
                Item(Items.EMPTY_CUP_1980, 1),
                Item(Items.BUCKET_1925, 1),
            )
    }
}
