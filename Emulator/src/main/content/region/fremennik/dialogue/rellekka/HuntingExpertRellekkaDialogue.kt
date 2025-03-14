package content.region.fremennik.dialogue.rellekka

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HuntingExpertRellekkaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.HAPPY,
            "Good day, you seem to have a keen eye. " + "Maybe even some hunter's blood in that body of yours?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                showTopics(
                    Topic(FaceAnim.ASKING, "Is there anything you can teach me?", 1),
                    Topic(FaceAnim.NEUTRAL, "Nevermind.", END_DIALOGUE),
                )
            1 -> npcl(FaceAnim.FRIENDLY, "I can teach you how to hunt.").also { stage++ }
            2 -> playerl(FaceAnim.THINKING, "What kind of creatures can I hunt?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Many creatures in many ways. You need to make some traps " + "and catch birds!",
                ).also { stage++ }
            4 -> playerl(FaceAnim.HALF_ASKING, "Birds?").also { stage++ }
            5 -> npcl(FaceAnim.ANNOYED, "Yes, birds! Like the ones here!").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "Look. Just... Get some hunting gear and go set up some traps.",
                ).also { stage++ }
            7 -> playerl(FaceAnim.HALF_ROLLING_EYES, "Is that it?").also { stage++ }
            8 -> npcl(FaceAnim.FURIOUS, "JUST GO DO IT!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HUNTING_EXPERT_5112)
}
