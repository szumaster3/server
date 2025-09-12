package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items
import kotlin.math.max

/**
 * Handles leather crafting.
 */
class LeatherCraftingListener : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        val TOOLS = intArrayOf(Items.STEEL_STUDS_2370, Items.NEEDLE_1733)
        val LEATHER = LeatherProduct.values().map { it.input }.toIntArray()

        onUseWith(IntType.ITEM, TOOLS, *LEATHER) { player, used, with ->

            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val craft = LeatherProduct.forInput(with.id).firstOrNull() ?: return@onUseWith true
            val requiredTool = if (craft.type == LeatherProduct.Type.STUDDED) Items.STEEL_STUDS_2370 else Items.NEEDLE_1733

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

    /**
     * Opens the leather crafting interface.
     */
    private fun openLeatherInterface(player: Player, type: LeatherProduct.Type, inputId: Int) {
        if (type == LeatherProduct.Type.SOFT) {
            openInterface(player, Components.LEATHER_CRAFTING_154)
            return
        }

        val crafts = LOOKUP_TABLE[type]?.get(inputId) ?: return
        if (crafts.isEmpty()) return

        sendSkillDialogue(player) {
            val items = crafts.map { it.product }.toIntArray()
            withItems(*items)

            create { id, amount ->
                val craft = LeatherProduct.forProduct(id)
                if (craft != null) {
                    submitIndividualPulse(
                        player,
                        LeatherCraftingPulse(player, Item(craft.input), craft, amount)
                    )
                }
            }

            calculateMaxAmount {
                crafts.firstOrNull()?.let { max(0, amountInInventory(player, it.input)) } ?: 0
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.LEATHER_CRAFTING_154) { player, _, opcode, buttonID, _, _ ->
            val productId = SOFT_LEATHER_BUTTONS[buttonID] ?: return@on true
            val craft = LeatherProduct.forProduct(productId) ?: return@on true
            var amount = 0
            when (opcode) {
                155 -> amount = 1
                196 -> amount = 5
                124 -> amount = amountInInventory(player, craft.input)
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value ->
                        val amt = value as? Int ?: return@sendInputDialogue
                        if (amt <= 0) return@sendInputDialogue
                        submitIndividualPulse(player, LeatherCraftingPulse(player, Item(craft.input), craft, amt))
                    }
                    return@on true
                }
            }
            submitIndividualPulse(player, LeatherCraftingPulse(player, Item(craft.input), craft, amount))
            return@on true
        }
    }

    companion object {
        /**
         * Represents buttons map for soft leather crafting.
         */
        private val SOFT_LEATHER_BUTTONS = mapOf(
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
        private val LOOKUP_TABLE: Map<LeatherProduct.Type, Map<Int, List<LeatherProduct>>> =
            LeatherProduct.values()
                .groupBy { it.type }
                .mapValues {
                    (_, crafts) -> crafts.groupBy { it.input }
                }
    }
}