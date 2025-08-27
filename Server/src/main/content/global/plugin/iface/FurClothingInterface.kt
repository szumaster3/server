package content.global.plugin.iface

import core.api.*
import core.game.container.access.InterfaceContainer
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.colorize
import shared.consts.Components
import shared.consts.Items

private val POLAR_CAMO_TOP = Item(Items.POLAR_CAMO_TOP_10065)
private val POLAR_CAMO_LEGS = Item(Items.POLAR_CAMO_LEGS_10067)

private val WOOD_CAMO_TOP = Item(Items.WOOD_CAMO_TOP_10053)
private val WOOD_CAMO_LEGS = Item(Items.WOOD_CAMO_LEGS_10055)

private val JUNGLE_CAMO_TOP = Item(Items.JUNGLE_CAMO_TOP_10057)
private val JUNGLE_CAMO_LEGS = Item(Items.JUNGLE_CAMO_LEGS_10059)

private val DESERT_CAMO_TOP = Item(Items.DESERT_CAMO_TOP_10061)
private val DESERT_CAMO_LEGS = Item(Items.DESERT_CAMO_LEGS_10063)

private val LARUPIA_HAT = Item(Items.LARUPIA_HAT_10045)
private val LARUPIA_TOP = Item(Items.LARUPIA_TOP_10043)
private val LARUPIA_LEGS = Item(Items.LARUPIA_LEGS_10041)

private val GRAAHK_HEADDRESS = Item(Items.GRAAHK_HEADDRESS_10051)
private val GRAAHK_TOP = Item(Items.GRAAHK_TOP_10049)
private val GRAAHK_LEGS = Item(Items.GRAAHK_LEGS_10047)

private val KYATT_HAT = Item(Items.KYATT_HAT_10039)
private val KYATT_TOP = Item(Items.KYATT_TOP_10037)
private val KYATT_LEGS = Item(Items.KYATT_LEGS_10035)

private val GLOVES_OF_SILENCE = Item(Items.GLOVES_OF_SILENCE_10075)
private val SPOTTED_CAPE = Item(Items.SPOTTED_CAPE_10069)
private val SPOTTIER_CAPE = Item(Items.SPOTTIER_CAPE_10071)

private val OPTIONS = arrayOf("Buy 10", "Buy 5", "Buy 1", "Value")
private const val FUR_CLOTHING_COMPONENT_ID = Components.CUSTOM_FUR_CLOTHING_477

private val ITEMS =
    arrayOf(
        POLAR_CAMO_TOP, POLAR_CAMO_LEGS, null, WOOD_CAMO_TOP, WOOD_CAMO_LEGS, null, JUNGLE_CAMO_TOP, JUNGLE_CAMO_LEGS, null, DESERT_CAMO_TOP, DESERT_CAMO_LEGS,
        null, null, null, null, null, null, null, null, null, null, null,
        LARUPIA_HAT, LARUPIA_TOP, LARUPIA_LEGS, null, GRAAHK_HEADDRESS, GRAAHK_TOP, GRAAHK_LEGS, null, KYATT_HAT, KYATT_TOP, KYATT_LEGS,
        null, null, null, null, null, null, null, null, null, null, null,
        null, GLOVES_OF_SILENCE, null, null, null, SPOTTED_CAPE, null, null, null, SPOTTIER_CAPE, null,
        null, null, null, null, null, null, null, null, null, null, null,
    )

/**
 * Handles custom fur clothing interface.
 */
class FurClothingInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(FUR_CLOTHING_COMPONENT_ID) { player, _ ->
            InterfaceContainer.generateItems(player, ITEMS, OPTIONS, FUR_CLOTHING_COMPONENT_ID, 26, 6, 11, 5000)
            repositionChild(player, FUR_CLOTHING_COMPONENT_ID, 26, 32, 97)

            val checkedFurs = arrayListOf<String>()
            for (clothing in FurClothing.values()) {
                if (checkedFurs.contains(clothing.textContent)) continue

                val hasItem = if (clothing.requirements.size == 1) {
                    val req = clothing.requirements[0]
                    inInventory(player, req.id, req.amount)
                } else {
                    clothing.requirements.any { req -> inInventory(player, req.id, req.amount) }
                }

                if (hasItem) {
                    sendString(player, colorize("%G${clothing.textContent}"), FUR_CLOTHING_COMPONENT_ID, clothing.textChildID)
                    checkedFurs.add(clothing.textContent)
                }
            }

            return@onOpen true
        }

        on(FUR_CLOTHING_COMPONENT_ID) { player, _, opcode, _, slotID, _ ->
            val itemMap =
                mapOf(
                    0  to FurClothing.POLAR_KEBBIT_TOP,
                    1  to FurClothing.POLAR_KEBBIT_BOTTOM,
                    3  to FurClothing.COMMON_KEBBIT_TOP,
                    4  to FurClothing.COMMON_KEBBIT_BOTTOM,
                    6  to FurClothing.FELDIP_WEASEL_TOP,
                    7  to FurClothing.FELDIP_WEASEL_LEGS,
                    9  to FurClothing.DESERT_DEVIL_TOP,
                    10 to FurClothing.DESERT_DEVIL_LEGS,
                    22 to FurClothing.LARUPIA_HEAD,
                    23 to FurClothing.LARUPIA_CHEST,
                    24 to FurClothing.LARUPIA_BOTTOM,
                    26 to FurClothing.GRAAHK_HEAD,
                    27 to FurClothing.GRAAHK_CHEST,
                    28 to FurClothing.GRAAHK_BOTTOM,
                    30 to FurClothing.KYATT_HEAD,
                    31 to FurClothing.KYATT_CHEST,
                    32 to FurClothing.KYATT_BOTTOM,
                    45 to FurClothing.GLOVES_SILENCE,
                    49 to FurClothing.SPOT_CAPE,
                    53 to FurClothing.DASH_CAPE
                )
            val clothing = itemMap[slotID] ?: return@on true
            when (opcode) {
                196 -> buy(player, clothing, 1)
                124 -> buy(player, clothing, 5)
                199 -> buy(player, clothing, 10)
                155 -> {
                    val reqs = if (clothing.requirements.size == 1) {
                        "${clothing.requirements[0].amount} ${itemDefinition(clothing.requirements[0].id).name.lowercase()}"
                    } else {
                        clothing.requirements.joinToString(" or ") { "${it.amount} ${itemDefinition(it.id).name.lowercase()}" }
                    }
                    sendMessage(player, "${clothing.product.name} requires $reqs and costs ${clothing.price} coins.")
                }
                9 -> sendMessage(player, itemDefinition(clothing.product.id).examine)
            }
            return@on true
        }
    }

    private fun buy(player: Player, fur: FurClothing, amount: Int) {
        val coins = Item(Items.COINS_995, fur.price * amount)

        val hasReqs = if (fur.requirements.size == 1) {
            val req = fur.requirements[0]
            inInventory(player, req.id, req.amount * amount)
        } else {
            fur.requirements.any { req -> inInventory(player, req.id, req.amount * amount) }
        }

        if (!hasReqs) {
            sendDialogue(player, "You don't have the required furs for that.")
            return
        }

        if (!inInventory(player, coins.id, coins.amount)) {
            sendDialogue(player, "You can't afford that.")
            return
        }

        if (fur.requirements.size == 1) {
            val req = fur.requirements[0]
            removeItem(player, Item(req.id, req.amount * amount), Container.INVENTORY)
        } else {
            val req = fur.requirements.first { r -> inInventory(player, r.id, r.amount * amount) }
            removeItem(player, Item(req.id, req.amount * amount), Container.INVENTORY)
        }

        removeItem(player, coins, Container.INVENTORY)
        addItem(player, fur.product.id, amount)
    }

    /**
     * Represents the custom fur clothing data.
     */
    internal enum class FurClothing(val requirements: List<Item>, val price: Int, val product: Item, val textChildID: Int, val textContent: String) {
        POLAR_KEBBIT_TOP(listOf(Item(Items.POLAR_KEBBIT_FUR_10117, 2)), 20, POLAR_CAMO_TOP, 18, "Polar kebbit"),
        POLAR_KEBBIT_BOTTOM(listOf(Item(Items.POLAR_KEBBIT_FUR_10117, 2), Item(Items.COMMON_KEBBIT_FUR_10121, 2)), 20, POLAR_CAMO_LEGS, 18, "Polar kebbit"),
        COMMON_KEBBIT_TOP(listOf(Item(Items.COMMON_KEBBIT_FUR_10121, 2)), 20, WOOD_CAMO_TOP, 19, "Common kebbit"),
        COMMON_KEBBIT_BOTTOM(listOf(Item(Items.COMMON_KEBBIT_FUR_10121, 2)), 20, WOOD_CAMO_LEGS, 19, "Common kebbit"),
        FELDIP_WEASEL_TOP(listOf(Item(Items.FELDIP_WEASEL_FUR_10119, 2)), 20, JUNGLE_CAMO_TOP, 20, "Feldip weasel"),
        FELDIP_WEASEL_LEGS(listOf(Item(Items.FELDIP_WEASEL_FUR_10119, 2)), 20, JUNGLE_CAMO_LEGS, 20, "Feldip weasel"),
        DESERT_DEVIL_TOP(listOf(Item(Items.DESERT_DEVIL_FUR_10123, 2)), 20, DESERT_CAMO_TOP, 21, "Desert devil"),
        DESERT_DEVIL_LEGS(listOf(Item(Items.DESERT_DEVIL_FUR_10123, 2)), 20, DESERT_CAMO_LEGS, 21, "Desert devil"),
        LARUPIA_HEAD(listOf(Item(Items.LARUPIA_FUR_10095)), 500, LARUPIA_HAT, 15, "Larupia"),
        LARUPIA_CHEST(listOf(Item(Items.TATTY_LARUPIA_FUR_10093), Item(Items.LARUPIA_FUR_10095)), 100, LARUPIA_TOP, 15, "Larupia"),
        LARUPIA_BOTTOM(listOf(Item(Items.TATTY_LARUPIA_FUR_10093), Item(Items.LARUPIA_FUR_10095)), 100, LARUPIA_LEGS, 15, "Larupia"),
        GRAAHK_HEAD(listOf(Item(Items.GRAAHK_FUR_10099)), 750, GRAAHK_HEADDRESS, 16, "Graahk"),
        GRAAHK_CHEST(listOf(Item(Items.TATTY_GRAAHK_FUR_10097), Item(Items.GRAAHK_FUR_10099)), 150, GRAAHK_TOP, 16, "Graahk"),
        GRAAHK_BOTTOM(listOf(Item(Items.TATTY_GRAAHK_FUR_10097), Item(Items.GRAAHK_FUR_10099)), 150, GRAAHK_LEGS, 16, "Graahk"),
        KYATT_HEAD(listOf(Item(Items.KYATT_FUR_10103)), 1000, KYATT_HAT, 17, "Kyatt"),
        KYATT_CHEST(listOf(Item(Items.TATTY_KYATT_FUR_10101), Item(Items.KYATT_FUR_10103)), 200, KYATT_TOP, 17, "Kyatt"),
        KYATT_BOTTOM(listOf(Item(Items.TATTY_KYATT_FUR_10101), Item(Items.KYATT_FUR_10103)), 200, KYATT_LEGS, 17, "Kyatt"),
        GLOVES_SILENCE(listOf(Item(Items.DARK_KEBBIT_FUR_10115, 2)), 600, GLOVES_OF_SILENCE, 22, "Dark kebbit"),
        SPOT_CAPE(listOf(Item(Items.SPOTTED_KEBBIT_FUR_10125, 2)), 400, SPOTTED_CAPE, 23, "Spotted kebbit"),
        DASH_CAPE(listOf(Item(Items.DASHING_KEBBIT_FUR_10127, 2)), 800, SPOTTIER_CAPE, 24, "Dashing kebbit")
    }
}
