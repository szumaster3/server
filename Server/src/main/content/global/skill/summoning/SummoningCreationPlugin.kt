package content.global.skill.summoning

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components

/**
 * Plugin responsible for handling the Summoning creation interface and pouch usage at obelisks.
 */
//@Initializable
class SummoningCreationPlugin : ComponentPlugin() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.SUMMONING_POUCHES_669, this)
        ComponentDefinition.put(Components.SUMMONING_SCROLLS_673, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        when (button) {
            17, 18 -> {
                closeInterface(player)
                SummoningCreator.configure(player, button == 17)
                return true
            }
        }
        when (opcode) {
            155, 196, 124, 199 -> {
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
                        sendMessage(player, "Please enter a valid integer amount greater than zero.")
                    }
                }
                return true
            }
            166 -> if(slot == 51) {
                sendMessages(player, "This pouch requires 1 phoenix quill, 1 crimson charm, 165 spirit shards, and completion", "of 'In Pyre Need'.")
            } else {
                SummoningPouch.forSlot(slot)?.let { SummoningCreator.list(player, it) }
            }
            168 -> sendMessage(player, itemDefinition(SummoningScroll.forId(slot)!!.itemId).examine)
        }
        return true
    }

    /**
     * Gets the pouch or scroll based on component and slot.
     */
    private fun getPouch(component: Component, slot: Int) = if (component.id == Components.SUMMONING_POUCHES_669) {
        SummoningPouch.forSlot(slot)
    } else {
        SummoningScroll.forId(slot)
    }

    /**
     * Check the amount based on click opcode.
     */
    private fun getItemAmount(opcode: Int): Int = when (opcode) {
        155 -> 1
        196 -> 5
        124 -> 10
        199 -> 28
        else -> -1
    }
}
