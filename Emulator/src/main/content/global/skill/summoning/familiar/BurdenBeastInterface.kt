package content.global.skill.summoning.familiar

import core.api.sendInputDialogue
import core.game.component.Component
import core.game.container.Container
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components

class BurdenBeastInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        on(Components.LORE_BANK_SIDE_665) { player, component, opcode, buttonID, slot, _ ->
            handleBurdenBeast(player, component, slot, buttonID, opcode)
            return@on true
        }
        on(Components.LORE_BANK_671) { player, component, opcode, buttonID, slot, _ ->
            handleBurdenBeast(player, component, slot, buttonID, opcode)
            return@on true
        }
    }


    companion object {
        /**
         * Handles item interactions between the player inventory and the familiar storage.
         */
        private fun handleBurdenBeast(player: Player, component: Component, slot: Int, button: Int, opcode: Int): Boolean {
            val familiarManager = player.familiarManager
            if (!familiarManager.hasFamiliar() || familiarManager.familiar?.isBurdenBeast != true) {
                return false
            }

            val beast = familiarManager.familiar as BurdenBeast
            val withdraw = component.id == Components.LORE_BANK_671
            val container: Container = if (withdraw) beast.container else player.inventory
            val item: Item? = if (slot in 0 until container.capacity()) container.get(slot) else null

            if (item == null && button != 29) {
                return true
            }

            when (opcode) {
                155 -> {
                    if (button == 29) {
                        beast.withdrawAll()
                        return true
                    }
                    item?.let { beast.transfer(it, 1, withdraw) }
                    return true
                }
                196 -> {
                    item?.let { beast.transfer(it, 5, withdraw) }
                    return true
                }
                124 -> {
                    item?.let { beast.transfer(it, 10, withdraw) }
                    return true
                }
                199 -> {
                    item?.let { beast.transfer(it, container.getAmount(it), withdraw) }
                    return true
                }
                234 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value ->
                        item?.let { beast.transfer(it, value.hashCode(), withdraw) }
                        Unit
                    }
                }
                9 -> {
                    item?.definition?.let { player.packetDispatch.sendMessage(it.examine) }
                    return true
                }
            }
            return false
        }
    }
}