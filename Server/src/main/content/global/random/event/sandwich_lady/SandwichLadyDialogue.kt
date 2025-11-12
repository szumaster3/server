package content.global.random.event.sandwich_lady

import content.data.GameAttributes
import core.api.getAttribute
import core.api.openInterface
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueFile
import core.game.node.entity.combat.ImpactHandler
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.timer.impl.AntiMacro
import core.tools.END_DIALOGUE
import shared.consts.Components

/**
 * Represents the Sandwich Lady dialogue.
 * @author Vexia, Ceikry
 */
class SandwichLadyDialogue(val isChoice: Boolean) : DialogueFile() {
    private val sandwichInterface = Components.MACRO_SANDWICH_TRAY_297

    override fun handle(componentID: Int, buttonID: Int) {
        val assigned = getAttribute(player!!, GameAttributes.S_LADY_ITEM, 0)
        val choice = getAttribute(player!!, GameAttributes.S_LADY_ITEM_VALUE, 0)
        if (!isChoice) {
            when (stage) {
                0 -> npc("Have a ${ItemDefinition.forId(assigned).name.lowercase()}, dear.").also { stage++ }
                1 -> {
                    end()
                    openInterface(player!!, sandwichInterface)
                }
            }
        } else {
            when (stage) {
                0 -> if (choice != assigned) {
                    npc!!.sendChat("That's not what I said you could have!")
                    player!!.impactHandler.manualHit(npc, 3, ImpactHandler.HitsplatType.NORMAL)
                    AntiMacro.terminateEventNpc(player!!)
                } else {
                    npc("Here you are, dear. I hope you enjoy it!")
                    if (!player!!.inventory.add(Item(assigned))) {
                        GroundItemManager.create(Item(assigned), player!!)
                    }
                    AntiMacro.terminateEventNpc(player!!)
                    stage = END_DIALOGUE
                }
            }
        }
    }
}
