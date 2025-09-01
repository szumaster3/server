package content.global.skill.crafting.items.leather

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

/**
 * Handles opening leather crafting interfaces and submitting crafting pulses.
 */
class LeatherCraftingListener : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        /*
         * Handles open interface when using needle or studs on leather,dragonhide...
         */

        onUseWith(IntType.ITEM, Items.NEEDLE_1733, *LeatherCraft.values().map { it.input }.toIntArray()) { player, used, with ->
            openLeatherInterfaceForType(player, with.id, LeatherCraft.Type.SOFT)
            return@onUseWith true
        }

        /*
         * Handles of types of leather crafting.
         */

        onUseWith(IntType.ITEM, Items.NEEDLE_1733, *LeatherCraft.values().map { it.input }.toIntArray()) { player, used, with ->
            val craft = LeatherCraft.forInput(with.id).firstOrNull { it.type != LeatherCraft.Type.SOFT && it.type != LeatherCraft.Type.STUDDED }
            craft?.let { openLeatherInterface(player, it.type) }
            return@onUseWith true
        }

        /*
         * Handles studdy crafting.
         */
        onUseWith(IntType.ITEM, Items.STEEL_STUDS_2370, *LeatherCraft.values().map { it.input }.toIntArray()) { player, used, with ->
            openLeatherInterfaceForType(player, with.id, LeatherCraft.Type.STUDDED)
            return@onUseWith true
        }
    }

    /**
     * Opens the leather interface for a specific leather type.
     */
    private fun openLeatherInterfaceForType(player: Player, inputId: Int, type: LeatherCraft.Type) {
        val craft = LeatherCraft.forInput(inputId).firstOrNull { it.type == type } ?: return
        openLeatherInterface(player, craft.type)
    }

    /**
     * Opens the crafting interface or skill dialogue depending on the leather type.
     */
    private fun openLeatherInterface(player: Player, type: LeatherCraft.Type) {
        when (type) {
            LeatherCraft.Type.SOFT -> openInterface(player, Components.LEATHER_CRAFTING_154)
            else -> sendSkillDialogue(player) {
                val items = LeatherCraft.values().filter { it.type == type }.map { it.product }.toIntArray()
                withItems(*items)
                create { id, amount ->
                    val craft = LeatherCraft.forProduct(id)
                    if (craft != null) {
                        submitIndividualPulse(player, LeatherCraftingPulse(player, Item(craft.input), craft, amount), type = PulseType.STANDARD)
                    } else player.debug("Invalid leather item selected.")
                }
                calculateMaxAmount {
                    LeatherCraft.values().firstOrNull { it.type == type }?.let { amountInInventory(player, it.input) } ?: 0
                }
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.LEATHER_CRAFTING_154) { player, _, opcode, buttonID, _, _ ->
            val craft = LeatherCraft.values().firstOrNull { it.product == buttonID } ?: return@on true
            val amount = when (opcode) {
                155 -> 1
                196 -> 5
                124 -> amountInInventory(player, craft.input)
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value ->
                        submitIndividualPulse(player, LeatherCraftingPulse(player, Item(craft.input), craft, value as Int), type = PulseType.STANDARD)
                    }
                    return@on true
                }
                else -> 1
            }
            submitIndividualPulse(player, LeatherCraftingPulse(player, Item(craft.input), craft, amount), type = PulseType.STANDARD)
            true
        }
    }
}