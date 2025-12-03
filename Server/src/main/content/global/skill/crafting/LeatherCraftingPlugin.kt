package content.global.skill.crafting

import core.api.*
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import kotlin.math.max

/**
 * Handles leather crafting.
 */
class LeatherCraftingPlugin : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, TOOLS, *LEATHER) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            val craft = CraftingDefinition.Leather.forInput(with.id).firstOrNull() ?: return@onUseWith true
            val requiredTool =
                if (craft.type == CraftingDefinition.Leather.Type.STUDDED)
                    Items.STEEL_STUDS_2370
                else
                    Items.NEEDLE_1733

            if (used.id != requiredTool) {
                sendMessage(player, "You need ${if (requiredTool == Items.NEEDLE_1733) "a needle" else "steel studs"} to craft this leather.")
                return@onUseWith true
            }

            if (!inInventory(player, Items.THREAD_1734)) {
                sendDialogue(player, "You need thread to make this.")
                return@onUseWith true
            }

            closeDialogue(player)
            openLeatherInterface(player, craft.type, with.id)
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.LEATHER_CRAFTING_154) { player, _, opcode, buttonID, _, _ ->
            val productId = SOFT_LEATHER_BUTTONS[buttonID] ?: return@on true
            val craft = CraftingDefinition.Leather.forProduct(productId) ?: return@on true
            var amount = 0

            when (opcode) {
                155 -> amount = 1
                196 -> amount = 5
                124 -> amount = amountInInventory(player, craft.input)
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value ->
                        val amt = value as? Int ?: return@sendInputDialogue
                        if (amt <= 0) return@sendInputDialogue
                        handleLeatherCrafting(player, craft, amt)
                    }
                    return@on true
                }
            }

            handleLeatherCrafting(player, craft, amount)
            return@on true
        }
    }

    /**
     * Opens the leather crafting interface.
     */
    private fun openLeatherInterface(player: Player, type: CraftingDefinition.Leather.Type, inputId: Int) {
        if (type == CraftingDefinition.Leather.Type.SOFT) {
            openInterface(player, Components.LEATHER_CRAFTING_154)
            return
        }

        val crafts = LOOKUP_TABLE[type]?.get(inputId) ?: return
        if (crafts.isEmpty()) return

        sendSkillDialogue(player) {
            val items = crafts.map { it.product }.toIntArray()
            withItems(*items)

            create { id, amount ->
                val craft = CraftingDefinition.Leather.forProduct(id)
                if (craft != null) {
                    handleLeatherCrafting(player, craft, amount)
                }
            }

            calculateMaxAmount { crafts.firstOrNull()?.let { max(0, amountInInventory(player, it.input)) } ?: 0 }
        }
    }

    private fun handleLeatherCrafting(player: Player, craft: CraftingDefinition.Leather, amountToMake: Int) {
        var remaining = amountToMake

        queueScript(player, 0, QueueStrength.WEAK) {
            if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) {
                return@queueScript stopExecuting(player)
            }

            if (getStatLevel(player, Skills.CRAFTING) < craft.level) {
                val name = getItemName(craft.product).lowercase()
                sendDialogue(player, "You need a Crafting level of ${craft.level} to make ${if(core.tools.StringUtils.isPlusN(name)) "an" else "a"} $name.")
                return@queueScript stopExecuting(player)
            }

            if (craft.studded) {
                if (!inInventory(player, Items.STEEL_STUDS_2370)) {
                    sendDialogue(player, "You need steel studs to make this.")
                    return@queueScript stopExecuting(player)
                }
            } else {
                if (!inInventory(player, Items.NEEDLE_1733)) {
                    sendDialogue(player, "You need a needle to make this.")
                    return@queueScript stopExecuting(player)
                }
                if (!inInventory(player, Items.THREAD_1734)) {
                    sendDialogue(player, "You need thread to make this.")
                    return@queueScript stopExecuting(player)
                }
            }

            if (!inInventory(player, craft.input, craft.amount)) {
                val name = getItemName(craft.input).lowercase()
                sendDialogue(player, "You need ${craft.amount} ${if (craft.amount == 1) name else "${name}s"} to make this.")
                return@queueScript stopExecuting(player)
            }

            animate(player, Animations.CRAFT_LEATHER_1249)
            delayClock(player, Clocks.SKILLING, 2)

            var removed = removeItem(player, Item(craft.input, craft.amount))
            if (craft.studded) removed = removed && removeItem(player, Item(Items.STEEL_STUDS_2370))

            if (removed) {
                addItem(player, craft.product)
                rewardXP(player, Skills.CRAFTING, craft.xp)

                if (!craft.studded) {
                    CraftingDefinition.decayThread(player)
                    if (CraftingDefinition.isLastThread(player))
                        CraftingDefinition.removeThread(player)
                }

                val made = getItemName(craft.product).lowercase()
                sendMessage(player, if (craft.pair) "You make a pair of $made." else "You make ${if(core.tools.StringUtils.isPlusN(made)) "an" else "a"} $made.")

                craft.diary?.let {
                    finishDiaryTask(player, it.type, it.stage, it.step)
                }
            }

            remaining--

            val hasMaterials =
                inInventory(player, craft.input, craft.amount) &&
                        (!craft.studded || inInventory(player, Items.STEEL_STUDS_2370))

            if (remaining > 0 && hasMaterials) {
                setCurrentScriptState(player, 0)
                delayScript(player, 2)
            } else stopExecuting(player)
        }
    }


    companion object {
        /**
         * The tools needed for crafting.
         */
        val TOOLS = intArrayOf(Items.STEEL_STUDS_2370, Items.NEEDLE_1733)

        /**
         * The leather type.
         */
        val LEATHER = CraftingDefinition.Leather.values().map { it.input }.toIntArray()

        /**
         * Represents buttons map for soft leather crafting.
         */
        private val SOFT_LEATHER_BUTTONS =
            mapOf(
                28 to Items.LEATHER_BODY_1129,
                29 to Items.LEATHER_GLOVES_1059,
                30 to Items.LEATHER_BOOTS_1061,
                31 to Items.LEATHER_VAMBRACES_1063,
                32 to Items.LEATHER_CHAPS_1095,
                33 to Items.COIF_1169,
                34 to Items.LEATHER_COWL_1167
            )

        /**
         * Represents lookup table for leather crafting.
         */
        private val LOOKUP_TABLE: Map<CraftingDefinition.Leather.Type, Map<Int, List<CraftingDefinition.Leather>>> =
            CraftingDefinition.Leather.values().groupBy { it.type }.mapValues { (_, crafts) -> crafts.groupBy { it.input } }
    }
}
