package content.global.skill.crafting.casting.gold

import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

/**
 * Handles jewellery crafting functionality.
 */
object Jewellery {

    const val RING_MOULD: Int = Items.RING_MOULD_1592
    const val AMULET_MOULD: Int = Items.AMULET_MOULD_1595
    const val NECKLACE_MOULD: Int = Items.NECKLACE_MOULD_1597
    const val BRACELET_MOULD: Int = Items.BRACELET_MOULD_11065
    const val GOLD_BAR: Int = Items.GOLD_BAR_2357
    const val PERFECT_GOLD_BAR: Int = Items.PERFECT_GOLD_BAR_2365
    const val SAPPHIRE: Int = Items.SAPPHIRE_1607
    const val EMERALD: Int = Items.EMERALD_1605
    const val RUBY: Int = Items.RUBY_1603
    const val DIAMOND: Int = Items.DIAMOND_1601
    const val DRAGONSTONE: Int = Items.DRAGONSTONE_1615
    const val ONYX: Int = Items.ONYX_6573

    private val mouldComponentMap = mapOf(
        RING_MOULD to intArrayOf(20, 22, 24, 26, 28, 30, 32, 35),
        NECKLACE_MOULD to intArrayOf(42, 44, 46, 48, 50, 52, 54),
        AMULET_MOULD to intArrayOf(61, 63, 65, 67, 69, 71, 73),
        BRACELET_MOULD to intArrayOf(80, 82, 84, 86, 88, 90, 92)
    )

    /**
     * Represents a jewellery item craftable with gold.
     */
    enum class JewelleryItem(val level: Int, experience: Int, val componentId: Int, val sendItem: Int, vararg val items: Int) {
        // Rings.
        GOLD_RING(5, 15, 19, Items.GOLD_RING_1635, GOLD_BAR),
        SAPPHIRE_RING(20, 40, 21, Items.SAPPHIRE_RING_1637, SAPPHIRE, GOLD_BAR),
        EMERALD_RING(27, 55, 23, Items.EMERALD_RING_1639, EMERALD, GOLD_BAR),
        RUBY_RING(34, 70, 25, Items.RUBY_RING_1641, Items.RUBY_1603, GOLD_BAR),
        PERFECT_RING(40, 70, 25, Items.PERFECT_RING_773, RUBY, Items.PERFECT_GOLD_BAR_2365),
        DIAMOND_RING(43, 85, 27, Items.DIAMOND_RING_1643, DIAMOND, GOLD_BAR),
        DRAGONSTONE_RING(55, 100, 29, Items.DRAGONSTONE_RING_1645, DRAGONSTONE, GOLD_BAR),
        ONYX_RING(67, 115, 31, Items.ONYX_RING_6575, Items.ONYX_6573, GOLD_BAR),

        // Necklaces.
        GOLD_NECKLACE(6, 20, 41, Items.GOLD_NECKLACE_1654, GOLD_BAR),
        SAPPHIRE_NECKLACE(22, 55, 43, Items.SAPPHIRE_NECKLACE_1656, SAPPHIRE, GOLD_BAR),
        EMERALD_NECKLACE(29, 60, 45, Items.EMERALD_NECKLACE_1658, EMERALD, GOLD_BAR),
        RUBY_NECKLACE(40, 75, 47, Items.RUBY_NECKLACE_1660, RUBY, GOLD_BAR),
        PERFECT_NECKLACE(40, 75, 47, Items.PERFECT_NECKLACE_774, RUBY, Items.PERFECT_GOLD_BAR_2365),
        DIAMOND_NECKLACE(56, 90, 49, Items.DIAMOND_NECKLACE_1662, DIAMOND, GOLD_BAR),
        DRAGONSTONE_NECKLACE(72, 105, 51, Items.DRAGON_NECKLACE_1664, DRAGONSTONE, GOLD_BAR),
        ONYX_NECKLACE(82, 120, 53, Items.ONYX_NECKLACE_6577, ONYX, GOLD_BAR),
        SLAYER_RING(75, 15, 34, Items.RING_OF_SLAYING8_13281, Items.ENCHANTED_GEM_4155, GOLD_BAR),

        // Amulets.
        GOLD_AMULET(8, 30, 60, Items.GOLD_AMULET_1673, GOLD_BAR),
        SAPPHIRE_AMULET(24, 63, 62, Items.SAPPHIRE_AMULET_1675, SAPPHIRE, GOLD_BAR),
        EMERALD_AMULET(31, 70, 64, Items.EMERALD_AMULET_1677, EMERALD, GOLD_BAR),
        RUBY_AMULET(50, 85, 66, Items.RUBY_AMULET_1679, RUBY, GOLD_BAR),
        DIAMOND_AMULET(70, 100, 68, Items.DIAMOND_AMULET_1681, DIAMOND, GOLD_BAR),
        DRAGONSTONE_AMULET(80, 150, 70, Items.DRAGONSTONE_AMMY_1683, DRAGONSTONE, GOLD_BAR),
        ONYX_AMULET(90, 165, 72, Items.ONYX_AMULET_6579, ONYX, GOLD_BAR),

        // Bracelets.
        GOLD_BRACELET(7, 25, 79, Items.GOLD_BRACELET_11069, Items.GOLD_BAR_2357),
        SAPPHIRE_BRACELET(23, 60, 81, Items.SAPPHIRE_BRACELET_11072, SAPPHIRE, GOLD_BAR),
        EMERALD_BRACELET(30, 65, 83, Items.EMERALD_BRACELET_11076, EMERALD, GOLD_BAR),
        RUBY_BRACELET(42, 80, 85, Items.RUBY_BRACELET_11085, RUBY, GOLD_BAR),
        DIAMOND_BRACELET(58, 95, 87, Items.DIAMOND_BRACELET_11092, DIAMOND, GOLD_BAR),
        DRAGONSTONE_BRACELET(74, 110, 89, Items.DRAGON_BRACELET_11115, DRAGONSTONE, GOLD_BAR),
        ONYX_BRACELET(84, 125, 91, Items.ONYX_BRACELET_11130, ONYX, GOLD_BAR);

        val experience: Double = experience.toDouble()

        companion object {
            var productMap = HashMap<Int, JewelleryItem>()

            init {
                val jewelleryArray = values()
                for (jewelleryItem in jewelleryArray) {
                    productMap.putIfAbsent(jewelleryItem.sendItem, jewelleryItem)
                }
            }

            /**
             * Gets a jewellery item by its resulting item id.
             *
             * @param id The product item id.
             * @return The corresponding [JewelleryItem], or `null` if not found.
             */
            @JvmStatic
            fun forProduct(id: Int): JewelleryItem? {
                return productMap[id]
            }
        }
    }

    /**
     * Opens the gold jewellery crafting interface.
     */
    @JvmStatic
    fun open(player: Player) {
        player.interfaceManager.open(Component(Components.CRAFTING_GOLD_446))

        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 14, inInventory(player, RING_MOULD))
        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 36, inInventory(player, NECKLACE_MOULD))
        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 55, inInventory(player, AMULET_MOULD))
        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 74, inInventory(player, BRACELET_MOULD))

        for ((mould, components) in mouldComponentMap) {
            val visible = inInventory(player, mould)
            for (component in components) {
                sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, component, !visible)
            }
        }

        for (item in JewelleryItem.values()) {
            val hasAllItems = allInInventory(player, *item.items)
            val hasMould = inInventory(player, mouldFor(item.name))
            val meetsRequirements = getStatLevel(player, Skills.CRAFTING) >= item.level

            if (hasAllItems && hasMould && meetsRequirements) {
                player.packetDispatch.sendItemZoomOnInterface(item.sendItem, 230, Components.CRAFTING_GOLD_446, item.componentId)
                player.packetDispatch.sendInterfaceConfig(Components.CRAFTING_GOLD_446, item.componentId + 1, false)
            } else if (hasMould) {
                val name = getItemName(item.sendItem).lowercase()
                val placeholder = when {
                    name.contains("ring") -> Items.RING_PICTURE_1647
                    name.contains("necklace") -> Items.NECKLACE_PICTURE_1666
                    name.contains("amulet") || name.contains("ammy") -> Items.AMULET_PICTURE_1685
                    name.contains("bracelet") -> Items.BRACELET_PICTURE_11067
                    else -> -1
                }

                if (placeholder != -1) {
                    player.packetDispatch.sendItemZoomOnInterface(
                        placeholder, 230, Components.CRAFTING_GOLD_446, item.componentId
                    )
                }
            }
            // player.debug("Send: ${item.name}, component: ${item.componentId}, visible: ${hasAllItems && hasMould && meetsLevel}")
        }
    }

    /**
     * Init crafting pulse for a selected jewellery item.
     */
    @JvmStatic
    fun make(player: Player, data: JewelleryItem, amount: Int) {
        var amount = amount
        var length = 0
        var amt = 0
        amt = if (data.items.contains(GOLD_BAR))
            player.inventory.getAmount(Item(GOLD_BAR))
        else if (data.items.contains(PERFECT_GOLD_BAR)) {
            player.inventory.getAmount(Item(PERFECT_GOLD_BAR))
        } else {
            val first = player.inventory.getAmount(Item(data.items[0]))
            val second = player.inventory.getAmount(Item(data.items[1]))
            if (first == second) {
                first
            } else if (first > second) {
                second
            } else {
                first
            }
        }
        if (amount > amt) {
            amount = amt
        }
        for (i in data.items.indices) {
            if (player.inventory.contains(data.items[i], amount)) {
                length++
            }
        }
        if (length != data.items.size) {
            sendMessage(player, "You don't have the required items to make this item.")
            return
        }
        if (getStatLevel(player, Skills.CRAFTING) < data.level) {
            sendMessage(player, "You need a crafting level of " + data.level + " to craft this.")
            return
        }
        val items = arrayOfNulls<Item>(data.items.size)
        for ((index, i) in data.items.indices.withIndex()) {
            items[index] = Item(data.items[i], 1 * amount)
        }
        closeInterface(player)
        submitIndividualPulse(player, JewelleryCraftingPulse(player, null, data, amount))
    }

    /**
     * Determines the correct mould id based on the item name.
     */
    private fun mouldFor(name: String): Int {
        var name = name
        name = name.lowercase()
        if (name.contains("ring")) {
            return RING_MOULD
        }
        if (name.contains("necklace")) {
            return NECKLACE_MOULD
        }
        if (name.contains("amulet")) {
            return AMULET_MOULD
        }
        return if (name.contains("bracelet")) {
            BRACELET_MOULD
        } else -1
    }
}