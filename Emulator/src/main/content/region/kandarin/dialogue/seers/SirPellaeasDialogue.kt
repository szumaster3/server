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
class SirPellaeasDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 ->
                    npcl(FaceAnim.NEUTRAL, "Greetings to the court of King Arthur!").also {
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

                1 -> playerl(FaceAnim.NEUTRAL, "Hello. I'm looking for a quest. Who should I talk to?").also { stage++ }
                2 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "King Arthur will let you know. I believe he has a quest at the moment.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }

                10 -> playerl(FaceAnim.NEUTRAL, "Any suggestions on freeing Merlin?").also { stage++ }
                11 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "My best guess would be some sort of magic. Unfortunately Merlin is our magic expert.",
                    ).also {
                        stage++
                    }

                12 -> playerl(FaceAnim.NEUTRAL, "Ok, well, thanks anyway.").also { stage = END_DIALOGUE }

                20 -> playerl(FaceAnim.NEUTRAL, "Any suggestions on getting into Mordred's fort?").also { stage++ }
                21 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "My best guess would be using magic. Unfortunately Merlin is our magic expert.",
                    ).also {
                        stage++
                    }

                22 -> playerl(FaceAnim.NEUTRAL, "Ok, well, thanks anyway.").also { stage = END_DIALOGUE }

                40 -> playerl(FaceAnim.NEUTRAL, "Any suggestions on finding Excalibur?").also { stage++ }
                41 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "My best guess would be some sort of spell. Unfortunately Merlin is our magic expert.",
                    ).also {
                        stage = 22
                    }
            }
        } else {
            when (stage) {
                0 ->
                    npcl(FaceAnim.NEUTRAL, "Greetings to the court of King Arthur!").also {
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
                        FaceAnim.NEUTRAL,
                        "You are a very talented Knight indeed to have freed Merlin so quickly. You have all of our gratitude.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                10 -> playerl(FaceAnim.NEUTRAL, "Any suggestions on finding the Grail?").also { stage++ }
                11 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "My best guess would be some sort of spell. Merlin is our magic expert. Ask him?",
                    ).also {
                        stage++
                    }
                12 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Although having said that, I believe Galahad found its location once...",
                    ).also { stage++ }
                13 -> playerl(FaceAnim.NEUTRAL, "Really? Know where I can find him?").also { stage++ }
                14 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I'm afraid not. He left here many moons ago and I know not where he went.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirPellaeasDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_PELLEAS_244)
    }
}
