package content.region.kandarin.ardougne.quest.tol.dialogue

import content.region.kandarin.ardougne.quest.tol.plugin.TowerOfLifeUtils
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class NoFingersDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, "Tower of Life")) {
            in 0..1 ->
                when (stage) {
                    START_DIALOGUE ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "You seem to like playing with danger, there.",
                        ).also { stage++ }

                    1 -> npcl(FaceAnim.FRIENDLY, "They don't call me 'No-fingers' for nothing!").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Well, correct me if I'm wrong, but you seem to have all your fingers.",
                        ).also { stage++ }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "It may seem that way mate, but I think you'll find I don't have any.",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.FRIENDLY, "What?").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "These not be fingers. These 'ere be wooden fingers.").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "Wooden fingers?").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "Yep.").also { stage++ }
                    8 -> playerl(FaceAnim.FRIENDLY, "That's not possible, how could you move them?").also { stage++ }
                    9 -> npcl(FaceAnim.FRIENDLY, "Magic wood.").also { stage++ }
                    10 ->
                        playerl(FaceAnim.FRIENDLY, "Yes, well I think I'd better be going now.").also {
                            stage = END_DIALOGUE
                        }
                }

            2 ->
                when (stage) {
                    START_DIALOGUE ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Hello. I was just wondering... I don't suppose you have any spare clothing you could lend me?",
                        ).also { stage++ }

                    1 -> npcl(FaceAnim.FRIENDLY, "I do have many boots.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Ah, thanks.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "But there's no way I'm giving any to you.").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Oh, but why? I could pay you!").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Nope. Only real builders can wear builders' boots, and you're not even close.",
                        ).also { stage = END_DIALOGUE }
                }

            3 ->
                if (getAttribute(player, TowerOfLifeUtils.TOL_TOWER_ACCESS, 0) == 1) {
                    when (stage) {
                        START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi.").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Don't suppose you've seen any boots around here? Size nine, steel toed, brown leather...",
                            ).also { stage++ }

                        2 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Nope, not at all. I'd best be going. Have a nice day. Bye!",
                            ).also { stage = END_DIALOGUE }
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.NO_FINGERS_5590)
}
