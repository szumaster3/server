package content.region.kandarin.camelot.dialogue

import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents Sir Gawain dialogue.
 */
@Initializable
class SirGawainDialogue(player: Player? = null) : Dialogue(player) {
    val STAGE_LE_FAYE_END = 20
    val STAGE_PROGRESS = 15

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> {
                    val title = if (player!!.isMale) "sir" else "madam"
                    npcl(FaceAnim.HAPPY, "Good day to you $title!")
                    val questStage = getQuestStage(player, Quests.MERLINS_CRYSTAL)
                    stage = when (questStage) {
                        0 -> 1
                        10 -> 10
                        in 20..30 -> 20
                        in 40..100 -> 40
                        else -> END_DIALOGUE
                    }
                }

                1 -> {
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Good day.", END_DIALOGUE),
                        Topic(FaceAnim.NEUTRAL, "Know you of any quests sir knight?", 2),
                    )
                }

                2 -> {
                    npcl(FaceAnim.NEUTRAL, "The king is the man to talk to if you want a quest.")
                    stage = END_DIALOGUE
                }

                10 -> {
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Good day.", END_DIALOGUE),
                        Topic(FaceAnim.NEUTRAL, "Any ideas on how to get Merlin out of that crystal?", 11),
                        Topic(FaceAnim.NEUTRAL, "Do you know how Merlin got trapped?", STAGE_PROGRESS),
                    )
                }

                11 -> {
                    npcl(FaceAnim.NEUTRAL, "I'm a little stumped myself. We've tried opening it with anything and everything!")
                    stage = END_DIALOGUE
                }

                15 -> {
                    npcl(FaceAnim.ANGRY, "I would guess this is the work of the evil Morgan Le Faye!")
                    stage++
                }

                16 -> {
                    playerl(FaceAnim.NEUTRAL, "And where could I find her?")
                    stage++
                }

                17 -> {
                    npc("She lives in her stronghold to the south of here,", "guarded by some renegade knights led by Sir Mordred.")
                    setQuestStage(player!!, Quests.MERLINS_CRYSTAL, 20)
                    player!!.getQuestRepository().syncronizeTab(player)
                    stage++
                }

                18 -> {
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Any idea how to get into Moran Le Faye's stronghold?", STAGE_LE_FAYE_END),
                        Topic(FaceAnim.NEUTRAL, "Thank you for the information.", 25),
                    )
                }

                STAGE_LE_FAYE_END -> {
                    npcl(FaceAnim.NEUTRAL, "No, you've got me stumped there...")
                    stage = END_DIALOGUE
                }

                25 -> {
                    npcl(FaceAnim.NEUTRAL, "It is the least I can do.")
                    stage = END_DIALOGUE
                }

                30 -> {
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Any idea how to get into Moran Le Faye's stronghold?", STAGE_LE_FAYE_END),
                        Topic(FaceAnim.NEUTRAL, "Hello again.", END_DIALOGUE),
                    )
                }

                40 -> {
                    playerl(FaceAnim.NEUTRAL, "Any ideas on finding Excalibur?")
                    stage++
                }

                41 -> {
                    npcl(FaceAnim.NEUTRAL, "Unfortunately not, adventurer.")
                    stage = END_DIALOGUE
                }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Good day to you sir!").also {
                    if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 || isQuestComplete(player!!, Quests.HOLY_GRAIL)) {
                        stage = 1
                    } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                        stage = 10
                    }
                }

                1 -> showTopics(
                    Topic(FaceAnim.NEUTRAL, "Good day.", END_DIALOGUE),
                    Topic(FaceAnim.NEUTRAL, "Know you of any quests sir knight?", 5),
                )
                5 -> npcl(FaceAnim.NEUTRAL, "I think you've done the main quest we were on right now...").also { stage = END_DIALOGUE }
                10 -> playerl(FaceAnim.NEUTRAL, "I seek the Grail in the name of Camelot!").also { stage++ }
                11 -> npcl(FaceAnim.NEUTRAL, "The Grail? That is truly a noble quest indeed. None but Galahad have come close.").also { stage++ }
                12 -> playerl(FaceAnim.NEUTRAL, "Galahad? Who is he?").also { stage++ }
                13 -> npcl(FaceAnim.NEUTRAL, "He used to be one of the Knights of the Round Table, but he mysteriously disappeared many years ago.").also { stage++ }
                14 -> playerl(FaceAnim.NEUTRAL, "Why would he quit being a Knight?").also { stage++ }
                15 -> npcl(FaceAnim.NEUTRAL, "That is a good question.").also { stage++ }
                16 -> npcl(FaceAnim.NEUTRAL, "I'm afraid I don't have the answer.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SirGawainDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_GAWAIN_240)
}
