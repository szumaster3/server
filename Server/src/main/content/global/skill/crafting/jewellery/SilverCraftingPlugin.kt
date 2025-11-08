package content.global.skill.crafting.jewellery

import content.global.skill.crafting.CraftingObjects
import core.api.*
import core.game.dialogue.InputType
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Components
import shared.consts.Items

/**
 * Handles crafting silver products.
 */
class SilverCraftingPlugin : InteractionListener, InterfaceListener {

    private val UNSTRUNG_ID = intArrayOf(Items.UNSTRUNG_SYMBOL_1714, Items.UNSTRUNG_EMBLEM_1720)

    private val OP_MAKE_ONE = 155
    private val OP_MAKE_FIVE = 196
    private val OP_MAKE_ALL = 124
    private val OP_MAKE_X = 199

    override fun defineListeners() {

        /*
         * Handles use silver bar on furnace.
         */

        onUseWith(IntType.SCENERY, Items.SILVER_BAR_2355, *CraftingObjects.FURNACES) { player, _, with ->
            setAttribute(player, "crafting:silver:furnace_id", with)
            openInterface(player, Components.CRAFTING_SILVER_CASTING_438)
            return@onUseWith true
        }

        /*
         * Handles strung silver jewellery.
         */

        onUseWith(IntType.ITEM, Items.BALL_OF_WOOL_1759, *UNSTRUNG_ID) { player, used, with ->
            SilverProduct.forId(with.id)?.let {
                if (removeItem(player, with.id) && removeItem(player, used.id)) {
                    addItem(player, it.strung)
                }
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.CRAFTING_SILVER_CASTING_438) { player, _ ->
            val itemsToSend = listOf(
                Items.HOLY_SYMBOL_1718 to 17,
                Items.UNHOLY_SYMBOL_1724 to 24,
                Items.SILVER_SICKLE_2961 to 31,
                Items.CONDUCTOR_4201 to 38,
                Items.TIARA_5525 to 45,
                Items.SILVTHRILL_ROD_7637 to 53,
                Items.DEMONIC_SIGIL_6748 to 60,
                Items.SILVER_BOLTS_UNF_9382 to 67,
                Items.SILVTHRIL_CHAIN_13154 to 74
            )
            itemsToSend.forEach { (item, index) ->
                sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, index, item)
            }
            return@onOpen true
        }

        on(Components.CRAFTING_SILVER_CASTING_438) { player, _, opcode, buttonID, _, _ ->
            val product = SilverProduct.forButton(buttonID) ?: return@on true

            if (!inInventory(player, product.required)) {
                sendMessage(player, "You need a ${getItemName(product.required).lowercase()} to make this item.")
                return@on true
            }

            if (!hasLevelDyn(player, Skills.CRAFTING, product.level)) {
                sendMessage(player, "You need a crafting level of ${product.level} to make this.")
                return@on true
            }

            when (opcode) {
                OP_MAKE_ONE -> make(player, product, 1)
                OP_MAKE_FIVE -> make(player, product, 5)
                OP_MAKE_ALL -> make(player, product, amountInInventory(player, Items.SILVER_BAR_2355))
                OP_MAKE_X -> sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                    make(player, product, Integer.parseInt(value.toString()))
                }

                else -> return@on true
            }

            return@on true
        }
    }

    private fun make(player: Player, product: SilverProduct, amount: Int) {
        closeInterface(player)
        delayClock(player, Clocks.SKILLING, 3)
        submitIndividualPulse(player, pulse = SilverCraftingPulse(player, product, getAttribute(player, "crafting:silver:furnace_id", core.game.node.scenery.Scenery(-1, -1, 0)), amount))
    }
}