package content.region.kandarin.gnome.quest.waterfall.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation

class WaterfallTreeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        sendDialogueLines(player, "It would be difficult to get down this tree without using a rope", "on it first.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                options("Climb down anyway", "Back away")
                stage = 1
            }
            1 -> when (buttonId) {
                1 -> {
                    end()
                    player.animate(Animation(828))
                    sendMessage(player, "You climb down the tree.")
                    player.pulseManager.run(
                        object : Pulse(4, player) {
                            override fun pulse(): Boolean {
                                sendMessage(player,"and lose your grip.")
                                sendMessage(player,"You get washed away in the current.")
                                teleport(player, Location(2527, 3413))
                                impact(player, 8, HitsplatType.NORMAL)
                                return true
                            }
                        },
                    )
                }

                2 -> sendDialogue(player, "You leave the tree alone.").also { stage++ }
                3 -> end()

            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("waterfall_tree_dialogue"))

}
