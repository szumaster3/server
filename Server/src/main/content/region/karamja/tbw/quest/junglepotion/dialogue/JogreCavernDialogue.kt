package content.region.karamja.tbw.quest.junglepotion.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location

/**
 * Represents the Jogre Cavern dialogue.
 *
 * # Relations
 * - [JunglePotion][content.region.karamja.tbw.quest.junglepotion.JunglePotion]
 */
class JogreCavernDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if (args.size > 1) {
            sendDialogue(player, "You attempt to climb the rocks back out.")
            stage = 13
        } else {
            sendDialogueLines(player, "You search the rocks... You find an entrance into some caves.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                setTitle(player, 2)
                sendOptions(
                    player,
                    "Would you like to enter the caves?",
                    "Yes, I'll enter the cave.",
                    "No thanks, I'll give it a miss."
                )
                stage = 1
            }

            1 -> {
                val destination = when (buttonId) {
                    1 -> Location.create(2830, 9520, 0)
                    2 -> null
                    else -> null
                }

                if (destination != null) {
                    sendDialogue(player, "You decide to enter the caves. You climb down several steep rock faces into the cavern below.")
                } else if (buttonId == 2) {
                    sendDialogue(player, "You decide to stay where you are!")
                }

                destination?.location?.let { teleport(player, it, TeleportManager.TeleportType.INSTANT) }
                end()
            }

            13 -> {
                teleport(player, Location.create(2823, 3120, 0), TeleportManager.TeleportType.INSTANT)
                end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = JogreCavernDialogue(player)

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("jogre_dialogue"))
}