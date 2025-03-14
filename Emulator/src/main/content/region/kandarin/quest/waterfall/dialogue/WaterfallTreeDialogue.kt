package content.region.kandarin.quest.waterfall.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation

class WaterfallTreeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("waterfall_tree_dialogue"))
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            100 -> end()
            0 -> {
                options("Climb down anyway", "Back away")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        player.packetDispatch.sendMessage("You climb down the tree")
                        player.animate(Animation(828))
                        player.pulseManager.run(
                            object : Pulse(4, player) {
                                override fun pulse(): Boolean {
                                    player.packetDispatch.sendMessage("and lose your grip.")
                                    player.packetDispatch.sendMessage("You get washed away in the current.")
                                    player.teleport(Location(2527, 3413))
                                    player.impactHandler.manualHit(player, 8, HitsplatType.NORMAL)
                                    return true
                                }
                            },
                        )
                    }

                    2 -> {
                        player.dialogueInterpreter.sendDialogue("You leave the tree alone.")
                        stage = 100
                    }
                }
        }
        return true
    }

    override fun open(vararg args: Any): Boolean {
        player.dialogueInterpreter.sendDialogue(
            "It would be difficult to get down this tree without using a rope",
            "on it first.",
        )
        stage = 0
        return true
    }
}
