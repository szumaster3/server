package content.region.island.tutorial.dialogue

import content.data.GameAttributes
import content.region.island.tutorial.plugin.TutorialStage
import core.api.getAttribute
import core.api.sendDialogue
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class BrotherBraceDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            60 -> npcl(FaceAnim.FRIENDLY, "Greetings! I'd just like to briefly go over two topics with you: Prayer, and Friend's.")
            62 -> npcl(FaceAnim.FRIENDLY, "Prayers have all sorts of wonderful benefits! From boosting defence and damage, to protecting you from outside damage, to saving items on death!")
            65 -> npcl(FaceAnim.FRIENDLY, "For your friend and ignore lists, it's quite simple really! Use your friend list to keep track of players who you like, and ignore those you don't!")
            in 66..100 -> npcl(FaceAnim.FRIENDLY, "Peace be with you, my child. Can I help you?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            60 -> when (stage++) {
                0 -> playerl(FaceAnim.FRIENDLY, "Alright, sounds fun!")
                1 -> npcl(FaceAnim.FRIENDLY, "Right, so first thing: Prayer. Prayer is trained by offering bones to the gods, and can grant you many boons!")
                2 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 61)
                    TutorialStage.load(player, 61)
                }
            }

            61 -> {
                end()
                sendDialogue(player!!, "You should open the indicated menu to continue.")
            }

            62 -> when (stage++) {
                0 -> playerl(FaceAnim.AMAZED, "Very cool!")
                1 -> npcl(FaceAnim.FRIENDLY, "Next up, let's talk about friends.")
                2 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 63)
                    TutorialStage.load(player, 63)
                }
            }

            65 -> {
                end()
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 66)
                TutorialStage.load(player, 66)
            }

            in 66..100 -> when (stage) {
                0 -> options("Tell me about Prayers again.", "Tell me about my friends list again.", "Nope, I'm ready to move on!").also { stage++ }
                1 -> when (buttonId) {
                    1 -> player("Tell me about Prayers again.").also { stage++ }
                    2 -> player("Tell me about my friends list again.").also { stage = 6 }
                    3 -> player("Nope, I'm ready to move on!").also { stage++ }
                }
                2 -> npcl(FaceAnim.FRIENDLY, "Ah, yes, your prayer list. Prayers can help a lot in combat. Click on the prayer you wish to use to activate it, and click it again to deactivate it.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Active prayers will drain your Prayer Points, which you can recharge by finding an altar or other holy spot and praying there.").also { stage++ }
                4 -> npcl(FaceAnim.FRIENDLY, "As you noticed, most enemies will drop bones when defeated. Burying bones, by clicking them in your inventory, will gain you Prayer experience.").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Do you need anything else?").also { stage = 0 }
                6 -> npcl(FaceAnim.FRIENDLY, "Your friends and ignore lists, yes... I'll tell you a little about each. You can add people to either list by clicking the add button then typing their name into the box that appears.").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "You remove people from the lists in the same way. If you add someone to your ignore list they will not be able to talk to you or send any form of message to you.").also { stage++ }
                8 -> npcl(FaceAnim.FRIENDLY, "Your friends list shows the online status of your friends. Friends in red are offline, friends in green are online and on the same world and friends in yellow are online, but on a different world.").also { stage++ }
                9 -> npcl(FaceAnim.FRIENDLY, "Do you need anything else?").also { stage = 0 }
                10 -> npcl(FaceAnim.FRIENDLY, "Okay then.").also { stage++ }
                11 -> end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BROTHER_BRACE_954)
}
