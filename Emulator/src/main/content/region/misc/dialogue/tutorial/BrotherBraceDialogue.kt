package content.region.misc.dialogue.tutorial

import content.region.misc.handlers.tutorial.TutorialStage
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BrotherBraceDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            60 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Greetings! I'd just like to briefly go over two topics with you: Prayer, and Friend's.",
                )
            62 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Prayers have all sorts of wonderful benefits! From boosting defence and damage, to protecting you from outside damage, to saving items on death!",
                )
            65 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "For your friend and ignore lists, it's quite simple really! Use your friend list to keep track of players who you like, and ignore those you don't!",
                )
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            60 ->
                when (stage++) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Alright, sounds fun!")
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Right, so first thing: Prayer. Prayer is trained by offering bones to the gods, and can grant you many boons!",
                        )
                    2 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 61)
                        TutorialStage.load(player, 61)
                    }
                }

            62 ->
                when (stage++) {
                    0 -> playerl(FaceAnim.AMAZED, "Very cool!")
                    1 -> npcl(FaceAnim.FRIENDLY, "Next up, let's talk about friends.")
                    2 -> {
                        end()
                        setAttribute(player, TutorialStage.TUTORIAL_STAGE, 63)
                        TutorialStage.load(player, 63)
                    }
                }

            65 -> {
                end()
                setAttribute(player, TutorialStage.TUTORIAL_STAGE, 66)
                TutorialStage.load(player, 66)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BROTHER_BRACE_954)
    }
}
