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
class SirLucanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> {
                    npcl(FaceAnim.NEUTRAL, "Hello there adventurer.").also {
                        if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 0) {
                            stage = 1
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
                }

                1 -> playerl(FaceAnim.NEUTRAL, "I'm looking for a quest...").also { stage++ }
                2 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Talk to the King then adventurer. He is always looking for men of bravery to aid him in his actions.",
                    ).also { stage = END_DIALOGUE }

                10 -> {
                    playerl(FaceAnim.NEUTRAL, "Any suggestions on freeing Merlin?")
                    stage++
                }

                11 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I've tried all the weapons I can find, yet none are powerful enough to break his crystal prison...",
                    )
                    stage++
                }

                12 -> {
                    playerl(FaceAnim.NEUTRAL, "Excalibur eh? Where would I find that?")
                    stage++
                }

                13 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "The Lady of the Lake is looking after it I believe... but I know not where she resides currently.",
                    )
                    stage++
                }

                14 -> {
                    playerl(FaceAnim.NEUTRAL, "Thanks. I'll try and find someone who does.")
                    stage = END_DIALOGUE
                }

                20 -> playerl(FaceAnim.NEUTRAL, "I need to get into Mordred's Fortress.").also { stage++ }
                21 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "So... you think Mordred's behind this? I'm afraid I don't have any suggestions...",
                    ).also { stage++ }

                22 ->
                    playerl(FaceAnim.NEUTRAL, "Thanks. I'll try and find someone who does.").also {
                        stage =
                            END_DIALOGUE
                    }

                40 -> {
                    playerl(FaceAnim.NEUTRAL, "I need to find Excalibur.").also { stage++ }
                }

                41 -> {
                    npcl(FaceAnim.FRIENDLY, "I'm afraid I don't have any suggestions...").also { stage++ }
                    stage = 14
                }
            }
        } else {
            when (stage) {
                0 ->
                    npcl(FaceAnim.FRIENDLY, "Hello there adventurer.").also {
                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 ||
                            isQuestComplete(player!!, Quests.HOLY_GRAIL)
                        ) {
                            stage = 1
                        } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                            stage = 10
                        }
                    }
                1 -> npcl(FaceAnim.FRIENDLY, "Congratulations on freeing Merlin!").also { stage = END_DIALOGUE }
                10 -> playerl(FaceAnim.FRIENDLY, "I seek the Grail of legend!").also { stage++ }
                11 -> npcl(FaceAnim.FRIENDLY, "I'm afraid I don't have any suggestions...").also { stage++ }
                12 ->
                    playerl(
                        FaceAnim.FRIENDLY,
                        "Thanks. I'll try and find someone who does.",
                    ).also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirLucanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_LUCAN_245)
    }
}
