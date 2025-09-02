package content.global.skill.crafting.items.leather

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

/**
 * Handles leather crafting.
 */
class LeatherCraftingListener : InteractionListener, InterfaceListener {
    override fun defineListeners() {

        val LEATHER = LeatherCraft.values().map { it.input }.toIntArray()

        onUseWith(IntType.ITEM, LEATHER) { player, used, with ->
            val craft = LeatherCraft.forInput(with.id)?.firstOrNull() ?: return@onUseWith true

            when (craft.type) {
                LeatherCraft.Type.SOFT -> {
                    if (used.id == Items.NEEDLE_1733) {
                        openInterface(player, with.id, LeatherCraft.Type.SOFT)
                    } else {
                        sendMessage(player, "You need a needle to craft this leather.")
                    }
                }
                LeatherCraft.Type.STUDDED -> {
                    if (used.id == Items.STEEL_STUDS_2370) {
                        openInterface(player, with.id, LeatherCraft.Type.STUDDED)
                    } else {
                        sendMessage(player, "You need steel studs to craft this leather.")
                    }
                }
                else -> {
                    sendMessage(player, "You cannot craft this type of leather with that item.")
                }
            }

            return@onUseWith true
        }
    }

    /**
     * Opens the leather crafting interface for a leather type and input item.
     */
    private fun openInterface(player: Player, inputId: Int, type: LeatherCraft.Type) {
        val craft = LeatherCraft.forInput(inputId).firstOrNull { it.type == type } ?: return
        openLeatherInterface(player, craft.type)
    }

    /**
     * Opens the crafting interface depends on leather type.
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
                        submitIndividualPulse(
                            player,
                            LeatherCraftingPulse(player, Item(craft.input), craft, amount)
                        )
                    } else player.debug("Invalid leather item selected.")
                }
                calculateMaxAmount {
                    LeatherCraft.values().firstOrNull { it.type == type }?.let { amountInInventory(player, it.input) }
                        ?: 0
                }
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.LEATHER_CRAFTING_154) { player, _, opcode, buttonID, _, _ ->
            val craft = LeatherCraft.values().firstOrNull { it.product == buttonID } ?: return@on true
            if (buttonID !in 29..34) return@on true
            var amount = when (opcode) {
                155 -> 1
                196 -> 5
                124 -> amountInInventory(player, craft.input)
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value ->
                        submitIndividualPulse(player, LeatherCraftingPulse(player, Item(craft.input), craft, value as Int))
                    }
                    return@on true
                }
                else -> 1
            }
            submitIndividualPulse(player, LeatherCraftingPulse(player, Item(craft.input), craft, amount))
            return@on true
        }
    }
}