package content.region.kandarin.dialogue.seers

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SirTristramDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Hail Arthur, King of the Britons!").also { stage++ }
                1 ->
                    playerl(FaceAnim.NEUTRAL, "Um... Hello.").also {
                        if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 0) {
                            stage = 2
                        } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 10) {
                            stage = 10
                        } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 20 ||
                            getQuestStage(
                                player!!,
                                Quests.MERLINS_CRYSTAL,
                            ) == 30
                        ) {
                            stage = 20
                        } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) >= 40) {
                            stage = 40
                        }
                    }

                2 ->
                    playerl(
                        FaceAnim.NEUTRAL,
                        "I'm looking for adventure! More specifically, some sort of quest.",
                    ).also { stage++ }

                3 -> npcl(FaceAnim.NEUTRAL, "...Then hail Arthur, King of Britons, like I just said.").also { stage++ }
                4 -> playerl(FaceAnim.NEUTRAL, "Oh. Ok.").also { stage++ }
                5 ->
                    playerl(FaceAnim.NEUTRAL, "I thought you just had a weird way of saying hello is all.").also {
                        stage = END_DIALOGUE
                    }

                10 -> playerl(FaceAnim.NEUTRAL, "Do you know much about breaking magical crystals?").also { stage++ }
                11 -> npcl(FaceAnim.NEUTRAL, "Funnily enough...").also { stage++ }
                12 -> npcl(FaceAnim.NEUTRAL, "Absolutely nothing.").also { stage++ }
                13 -> playerl(FaceAnim.NEUTRAL, "Ok. Goodbye.").also { stage = END_DIALOGUE }

                20 -> playerl(FaceAnim.NEUTRAL, "I need to get into Mordred's Fort...").also { stage++ }
                21 -> npcl(FaceAnim.NEUTRAL, "Good luck with that!").also { stage = END_DIALOGUE }

                40 -> playerl(FaceAnim.FRIENDLY, "I need to find Excalibur...").also { stage = 21 }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Hail Arthur, King of the Britons!").also { stage++ }
                1 ->
                    playerl(FaceAnim.NEUTRAL, "Um... Hello.").also {
                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 ||
                            isQuestComplete(player!!, Quests.HOLY_GRAIL)
                        ) {
                            stage = 2
                        } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                            stage = 10
                        }
                    }
                2 -> npcl(FaceAnim.NEUTRAL, "Thanks for freeing Merlin.").also { stage++ }
                3 -> playerl(FaceAnim.NEUTRAL, "No problem. It was easy.").also { stage = END_DIALOGUE }
                10 -> playerl(FaceAnim.NEUTRAL, "I am seeking the Grail...").also { stage++ }
                11 -> npcl(FaceAnim.NEUTRAL, "Good luck with that!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirTristramDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_TRISTRAM_243)
    }
}
