package content.global.skill.summoning

import core.api.*
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import shared.consts.Components

class SummoningInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        on(Components.SUMMONING_POUCHES_669) { player, component, opcode, button, slot, itemId ->
            handleSummoningInterface(player, component, opcode, button, slot, itemId)
        }
        on(Components.SUMMONING_SCROLLS_673) { player, component, opcode, button, slot, itemId ->
            handleSummoningInterface(player, component, opcode, button, slot, itemId)
        }
    }

    private fun handleSummoningInterface(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int
    ): Boolean {
        when (button) {
            17,
            18 -> {
                closeInterface(player)
                SummoningCreator.configure(player, button == 17)
                return true
            }
        }

        when (opcode) {
            155,
            196,
            124,
            199 -> {
                val pouch = getPouch(component, slot)
                SummoningCreator.create(player, getItemAmount(opcode), pouch)
                return true
            }
            234 -> {
                sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                    val pouch = getPouch(component, slot)
                    if (value is Int && value > 0) {
                        SummoningCreator.create(player, value, pouch)
                    } else {
                        sendMessage(
                            player,
                            "Please enter a valid integer amount greater than zero."
                        )
                    }
                }
                return true
            }
            166 ->
                if (slot == 51) {
                    sendMessages(
                        player,
                        "This pouch requires 1 phoenix quill, 1 crimson charm, 165 spirit shards, and completion",
                        "of 'In Pyre Need'."
                    )
                } else {
                    SummoningPouch.forSlot(slot)?.let { SummoningCreator.list(player, it) }
                }
            168 -> sendMessage(player, itemDefinition(SummoningScroll.forId(slot)!!.itemId).examine)
        }

        return true
    }

    private fun getPouch(component: Component, slot: Int) =
        if (component.id == Components.SUMMONING_POUCHES_669) SummoningPouch.forSlot(slot)
        else SummoningScroll.forId(slot)

    private fun getItemAmount(opcode: Int) =
        when (opcode) {
            155 -> 1
            196 -> 5
            124 -> 10
            199 -> 28
            else -> -1
        }
}