package content.region.kandarin.quest.elena.dialogue

import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TedRehnisonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.PLAGUE_CITY) == 9) {
            playerl(FaceAnim.NEUTRAL, "Hi, I hear a woman called Elena is staying here.").also { stage++ }
        } else if (getQuestStage(player, Quests.PLAGUE_CITY) > 9) {
            npcl(FaceAnim.FRIENDLY, "Any luck finding Elena yet?").also { stage++ }
        } else {
            npcl(FaceAnim.FRIENDLY, "Go away. We don't want any.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            9 ->
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Yes she was staying here, but slightly over a week ago she was getting ready to go back. However she never managed to leave. My daughter Milli was playing near the west wall when she saw some shadowy figures jump",
                        ).also { stage++ }

                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "out and grab her. Milli is upstairs if you wish to speak to her.",
                        ).also { stage = END_DIALOGUE }
                }

            in 10..98 ->
                when (stage) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Not yet...").also { stage++ }
                    2 -> npcl(FaceAnim.FRIENDLY, "I wish you luck, she did a lot for us.").also { stage = END_DIALOGUE }
                }

            in 99..100 ->
                when (stage) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, she is safe at home now.").also { stage++ }
                    2 ->
                        npcl(FaceAnim.FRIENDLY, "That's good to hear, she helped us a lot.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TED_REHNISON_721)
}
