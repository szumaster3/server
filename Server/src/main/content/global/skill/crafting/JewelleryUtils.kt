package content.global.skill.crafting

import core.api.*
import core.game.component.Component
import core.game.interaction.Clocks
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds
import kotlin.math.min

/**
 * Handles jewellery crafting functionality.
 */
object JewelleryUtils {

    private val mouldComponentMap = mapOf(
        CraftingDefinition.RING_MOULD     to intArrayOf(20, 22, 24, 26, 28, 30, 32, 35),
        CraftingDefinition.NECKLACE_MOULD to intArrayOf(42, 44, 46, 48, 50, 52, 54),
        CraftingDefinition.AMULET_MOULD   to intArrayOf(61, 63, 65, 67, 69, 71, 73),
        CraftingDefinition.BRACELET_MOULD to intArrayOf(80, 82, 84, 86, 88, 90, 92)
    )

    /**
     * Opens the gold jewellery crafting interface.
     */
    @JvmStatic
    fun open(player: Player) {
        openInterface(player, Components.CRAFTING_GOLD_446)

        val moulds = listOf(
            CraftingDefinition.RING_MOULD     to 14,
            CraftingDefinition.NECKLACE_MOULD to 36,
            CraftingDefinition.AMULET_MOULD   to 55,
            CraftingDefinition.BRACELET_MOULD to 74
        )

        for ((mould, component) in moulds) {
            sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, component, inInventory(player, mould))
        }

        for ((mould, components) in mouldComponentMap) {
            val visible = inInventory(player, mould)
            components.forEach { sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, it, !visible) }
        }

        for (item in CraftingDefinition.JewelleryItem.values()) {
            val hasAllItems = allInInventory(player, *item.items)
            val hasMould = inInventory(player, mouldFor(item.name))
            val meetsRequirements = getStatLevel(player, Skills.CRAFTING) >= item.level

            val itemToSend = when {
                hasAllItems && hasMould && meetsRequirements -> item.sendItem
                hasMould -> getPlaceholder(item.name)
                else -> -1
            }

            if (itemToSend != -1) {
                sendItemZoomOnInterface(player, Components.CRAFTING_GOLD_446, item.componentId, itemToSend)
                sendInterfaceConfig(player, Components.CRAFTING_GOLD_446, item.componentId + 1, false)
            }
        }
    }

    private fun getPlaceholder(name: String) = when (name.lowercase()) {
        "ring"           -> Items.RING_PICTURE_1647
        "necklace"       -> Items.NECKLACE_PICTURE_1666
        "amulet", "ammy" -> Items.AMULET_PICTURE_1685
        "bracelet"       -> Items.BRACELET_PICTURE_11067
        else -> -1
    }

    @JvmStatic
    fun make(player: Player, data: CraftingDefinition.JewelleryItem, amount: Int) {
        if (!clockReady(player, Clocks.SKILLING)) return

        val amt = data.items.minOfOrNull { amountInInventory(player, it) } ?: 0
        if (amt == 0) {
            sendMessage(player, "You don't have the required items to make this item.")
            return
        }

        val finalAmount = min(amount, amt)

        if (getStatLevel(player, Skills.CRAFTING) < data.level) {
            sendMessage(player, "You need a crafting level of ${data.level} to craft this.")
            return
        }

        closeInterface(player)
        delayClock(player, Clocks.SKILLING, 5)
        handleJewelleryCrafting(player, data, finalAmount)
    }

    private val mouldMap = mapOf(
        "ring"     to CraftingDefinition.RING_MOULD,
        "necklace" to CraftingDefinition.NECKLACE_MOULD,
        "amulet"   to CraftingDefinition.AMULET_MOULD,
        "bracelet" to CraftingDefinition.BRACELET_MOULD
    )

    /**
     * Gets the correct mould id based on the item name.
     */
    private fun mouldFor(name: String) = mouldMap.entries.firstOrNull { name.lowercase().contains(it.key) }?.value ?: -1

    /**
     * Handles crafting the jewellery.
     */
    private fun handleJewelleryCrafting(player: Player, type: CraftingDefinition.JewelleryItem, amount: Int) {
        var remaining = amount

        queueScript(player, 1, QueueStrength.WEAK) { stage ->
            if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) {
                return@queueScript stopExecuting(player)
            }

            if (getStatLevel(player, Skills.CRAFTING) < type.level) {
                sendMessage(player, "You need a Crafting level of ${type.level} to make this.")
                return@queueScript stopExecuting(player)
            }

            val reqItems = type.items
            if (!allInInventory(player, *reqItems)) {
                sendMessage(player, "You have run out of materials.")
                return@queueScript stopExecuting(player)
            }

            when (stage) {
                0 -> {
                    animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                    playAudio(player, Sounds.FURNACE_2725)
                    delayScript(player, 3)
                }
                else -> {
                    delayClock(player, Clocks.SKILLING, 3)
                    if (!removeItem(player, reqItems)) {
                        return@queueScript stopExecuting(player)
                    }

                    addItem(player, type.sendItem, 1)
                    rewardXP(player, Skills.CRAFTING, type.experience)

                    remaining--
                    if (remaining > 0) {
                        setCurrentScriptState(player, 0)
                        delayScript(player, 3)
                    } else {
                        stopExecuting(player)
                    }
                }
            }

            return@queueScript true
        }
    }
}
