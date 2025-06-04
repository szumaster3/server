package content.region.karamja.quest.junglepotion.dialogue

import core.api.sendDialogue
import core.api.sendDialogueLines
import core.api.sendDialogueOptions
import core.api.setTitle
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.game.world.map.Location

class JogreCavernDialogue(player: Player? = null) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        if (args.size > 1) {
            sendDialogue(player, "You attempt to climb the rocks back out.")
            stage = 13
            return true
        }
        sendDialogueLines(player, "You search the rocks... You find an entrance into some caves.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> {
                setTitle(player, 2)
                sendDialogueOptions(player, "Would you like to enter the caves?", "Yes, I'll enter the cave.", "No thanks, I'll give it a miss.")
                stage++
            }

            1 -> when (buttonId) {
                1 -> {
                    sendDialogueLines(player, "You decide to enter the caves. You climb down several steep rock", "faces into the cavern below.")
                    stage = 10
                }

                2 -> {
                    sendDialogue(player, "You decide to stay where you are!")
                    stage = 11
                }
            }

            10 -> {
                end()
                player.properties.teleportLocation = Location.create(2830, 9520, 0)
            }

            11 -> end()
            13 -> {
                end()
                player.properties.teleportLocation = Location.create(2823, 3120, 0)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = JogreCavernDialogue(player)

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("jogre_dialogue"))
}
