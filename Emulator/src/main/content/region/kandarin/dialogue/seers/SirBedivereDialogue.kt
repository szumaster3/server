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
class SirBedivereDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 ->
                    npcl(FaceAnim.NEUTRAL, "May I help you?").also {
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

                1 -> playerl(FaceAnim.NEUTRAL, "I'm really just looking for a quest...").also { stage++ }
                2 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Fortune favours us both then adventurer. I suggest you go and speak to King Arthur.",
                    ).also { stage = END_DIALOGUE }

                10 -> playerl(FaceAnim.NEUTRAL, "Merlin's in a crystal. Little help?").also { stage++ }
                11 -> npcl(FaceAnim.NEUTRAL, "That is what we were hoping for from you, adventurer!").also { stage++ }
                12 -> playerl(FaceAnim.NEUTRAL, "Hmmm. Well, ok, thanks anyway.").also { stage = END_DIALOGUE }

                20 ->
                    playerl(
                        FaceAnim.NEUTRAL,
                        "I don't suppose you have any idea how to break into Mordred's fort do you?",
                    ).also { stage++ }

                21 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I am afraid not. Would that we could! Mordred and his cronies have been thorns in our side for far too long already!",
                    ).also { stage++ }

                22 -> playerl(FaceAnim.NEUTRAL, "Ok. Thanks. See you later!").also { stage++ }
                23 ->
                    npcl(FaceAnim.NEUTRAL, "Take care adventurer, Mordred is an evil and powerful foe.").also {
                        stage = END_DIALOGUE
                    }

                40 -> playerl(FaceAnim.NEUTRAL, "Know anything about Excalibur?").also { stage++ }
                41 -> npcl(FaceAnim.NEUTRAL, "Um... it's a really good sword?").also { stage++ }
                42 -> playerl(FaceAnim.NEUTRAL, "Know where I can find it?").also { stage++ }
                43 -> npcl(FaceAnim.NEUTRAL, "Nope, sorry.").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                0 ->
                    npcl(FaceAnim.FRIENDLY, "May I help you?").also {
                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 ||
                            isQuestComplete(player!!, Quests.HOLY_GRAIL)
                        ) {
                            stage = 2
                        } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                            stage = 10
                        }
                    }
                2 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "All Knights of the Round thank you for your assistance in this trying time for us.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                10 -> npcl(FaceAnim.FRIENDLY, "You are looking for the Grail now adventurer?").also { stage++ }
                11 -> playerl(FaceAnim.FRIENDLY, "Absolutely.").also { stage++ }
                12 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "The best of luck to you! Make the name of Camelot proud, and bring it back to us.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirBedivereDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_BEDIVERE_242)
    }
}
