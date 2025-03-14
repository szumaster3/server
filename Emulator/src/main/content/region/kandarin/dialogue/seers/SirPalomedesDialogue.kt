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
class SirPalomedesDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> {
                    npcl(FaceAnim.NEUTRAL, "Hello there adventurer, what do you want of me?").also {
                        if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 0) {
                            stage = 1
                        } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 10) {
                            stage = 10
                        } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 20 ||
                            getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 30
                        ) {
                            stage = 20
                        } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) >= 40) {
                            stage = 40
                        }
                    }
                }

                1 -> playerl(FaceAnim.NEUTRAL, "I'd like some advice on finding a quest.").also { stage++ }
                2 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I do not know of any myself... but it would perhaps be worth your while asking the King if he has any tasks for you.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                10 ->
                    playerl(
                        FaceAnim.NEUTRAL,
                        "I'd like some advice on breaking that Crystal Merlin's trapped in.",
                    ).also { stage++ }
                11 -> npcl(FaceAnim.NEUTRAL, "Sorry, I cannot help you with that.").also { stage = END_DIALOGUE }
                20 ->
                    playerl(
                        FaceAnim.NEUTRAL,
                        "I'd like some advice on breaking into Mordred's fort.",
                    ).also { stage++ }
                21 -> npcl(FaceAnim.NEUTRAL, "Sorry, I cannot help you with that.").also { stage = END_DIALOGUE }
                40 -> playerl(FaceAnim.NEUTRAL, "I'd like some advice on finding Excalibur.").also { stage = 11 }
            }
        } else {
            when (stage) {
                0 ->
                    npcl(FaceAnim.HALF_GUILTY, "Hello there adventurer, what do you want of me?").also {
                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 ||
                            isQuestComplete(player!!, Quests.HOLY_GRAIL)
                        ) {
                            stage = 1
                        } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                            stage = 10
                        }
                    }

                1 ->
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "After your help freeing Merlin, my help is the least I can offer as a man of honour.",
                    ).also {
                        stage++
                    }
                2 ->
                    playerl(FaceAnim.HALF_GUILTY, "Nothing right now, but I'll bear it in mind. Thanks.").also {
                        stage =
                            END_DIALOGUE
                    }
                10 -> playerl(FaceAnim.FRIENDLY, "I'd like some advice on finding the Grail.").also { stage++ }
                11 -> npcl(FaceAnim.FRIENDLY, "Sorry, I cannot help you with that.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirPalomedesDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_PALOMEDES_3787)
    }
}
