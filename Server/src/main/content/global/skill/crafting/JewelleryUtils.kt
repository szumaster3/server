package content.global.skill.crafting

import core.api.*
import core.game.component.Component
import core.game.interaction.Clocks
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds

/**
 * Handles jewellery crafting functionality.
 */
object JewelleryUtils {

    private val mouldComponentMap = mapOf(
        CraftingDefinition.RING_MOULD to intArrayOf(20, 22, 24, 26, 28, 30, 32, 35),
        CraftingDefinition.NECKLACE_MOULD to intArrayOf(42, 44, 46, 48, 50, 52, 54),
        CraftingDefinition.AMULET_MOULD to intArrayOf(61, 63, 65, 67, 69, 71, 73),
        CraftingDefinition.BRACELET_MOULD to intArrayOf(80, 82, 84, 86, 88, 90, 92)
    )

    /**
     * Opens the gold jewellery crafting interface.
     */
    @JvmStatic
    fun open(player: Player) {
        player.interfaceManager.open(Component(Components.CRAFTING_GOLD_446))

        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 14, inInventory(player, CraftingDefinition.RING_MOULD))
        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 36, inInventory(player, CraftingDefinition.NECKLACE_MOULD))
        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 55, inInventory(player, CraftingDefinition.AMULET_MOULD))
        sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, 74, inInventory(player, CraftingDefinition.BRACELET_MOULD))

        for ((mould, components) in mouldComponentMap) {
            val visible = inInventory(player, mould)
            for (component in components) {
                sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, component, !visible)
            }
        }

        for (item in CraftingDefinition.JewelleryItem.values()) {
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
    fun make(player: Player, data: CraftingDefinition.JewelleryItem, amount: Int) {
        if (!clockReady(player, Clocks.SKILLING)) return
        var amount = amount
        var length = 0
        var amt = 0
        amt = if (data.items.contains(CraftingDefinition.GOLD_BAR))
            player.inventory.getAmount(Item(CraftingDefinition.GOLD_BAR))
        else if (data.items.contains(CraftingDefinition.PERFECT_GOLD_BAR)) {
            player.inventory.getAmount(Item(CraftingDefinition.PERFECT_GOLD_BAR))
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
        delayClock(player, Clocks.SKILLING, 5)
        handleJewelleryCrafting(player, null, data, amount)
    }

    /**
     * Determines the correct mould id based on the item name.
     */
    private fun mouldFor(name: String): Int {
        var name = name
        name = name.lowercase()
        if (name.contains("ring")) {
            return CraftingDefinition.RING_MOULD
        }
        if (name.contains("necklace")) {
            return CraftingDefinition.NECKLACE_MOULD
        }
        if (name.contains("amulet")) {
            return CraftingDefinition.AMULET_MOULD
        }
        return if (name.contains("bracelet")) {
            CraftingDefinition.BRACELET_MOULD
        } else -1
    }

    /**
     * Handles crafting the jewellery.
     */
    private fun handleJewelleryCrafting(player: Player, node: Item?, type: CraftingDefinition.JewelleryItem, amount: Int) {
        var remaining = amount
        var ticks = 0

        queueScript(player, 0, QueueStrength.WEAK) { stage ->
            if (remaining <= 0) {
                stopExecuting(player)
                return@queueScript false
            }

            when (stage) {
                0 -> {
                    if (getStatLevel(player, Skills.CRAFTING) < type.level) {
                        sendMessage(player, "You need a Crafting level of ${type.level} to make this.")
                        stopExecuting(player)
                        return@queueScript false
                    }

                    if (ticks % 3 == 0) {
                        animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                        playAudio(player, Sounds.FURNACE_2725)
                    }

                    ticks++
                    delayScript(player, 3)
                }

                else -> {
                    delayClock(player, Clocks.SKILLING, 3)


                    val reqItems = type.items.map { it }.toIntArray()


                    if (!allInInventory(player, *reqItems)) {
                        sendMessage(player, "You have run out of materials.")
                        stopExecuting(player)
                        return@queueScript false
                    }

                    if (!removeItem(player, reqItems)) {
                        stopExecuting(player)
                        return@queueScript false
                    }

                    player.inventory.add(Item(type.sendItem))
                    rewardXP(player, Skills.CRAFTING, type.experience)

                    remaining--

                    if (remaining > 0) {
                        setCurrentScriptState(player, 0)
                        delayScript(player, 3)
                    } else {
                        stopExecuting(player)
                        return@queueScript false
                    }
                }
            }

            return@queueScript true
        }
    }
}