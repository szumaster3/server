package content.region.kandarin.quest.grail.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.tools.END_DIALOGUE
import core.tools.secondsToTicks
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class SirPercivalDialogue(
    val interactionType: String,
) : DialogueFile() {
    val STAGE_COME_KING = 10
    val STAGE_FATHER_SPEAK = 30
    val STAGE_BEEN_TOLD = 13

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SIR_PERCIVAL_211)

        when (stage) {
            0 -> {
                if (interactionType == "Prod") {
                    sendDialogue(player!!, "You hear a muffled groan. The sack wriggles slightly.")
                    stage = END_DIALOGUE
                } else if (interactionType == "Open") {
                    if (getQuestStage(player!!, Quests.HOLY_GRAIL) != 40) {
                        sendMessage(player!!, "I have no reason to do that.")
                        end()
                        stage = END_DIALOGUE
                    } else {
                        sendDialogueLines(player!!, "You hear muffled noises from the sack. You open the sack.")
                        stage++
                    }
                } else {
                    if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 50) {
                        npcl(FaceAnim.NEUTRAL, "I said I will see you there then!").also { stage = END_DIALOGUE }
                    } else {
                        createSirAndSpeak()
                        stage = 2
                    }
                }
            }

            1 -> {
                createSirAndSpeak()
                stage++
            }

            2 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "How did you end up in a sack?", 3),
                    Topic(FaceAnim.NEUTRAL, "Come with me, I shall make you a king.", STAGE_COME_KING),
                    Topic(FaceAnim.NEUTRAL, "Your father wishes to speak to you.", STAGE_FATHER_SPEAK),
                )

            3 -> npcl(FaceAnim.NEUTRAL, "It's a little embarrassing really.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "After going on a long and challenging quest to retrieve the boots of Arkaneeses, defeating many powerful enemies on the way, I fell into a goblin trap!",
                ).also {
                    stage++
                }
            5 -> npcl(FaceAnim.NEUTRAL, "I've been kept as a slave here for the last 3 months!").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "A day or so ago, they decided it was a fun game to put me in this sack: then they forgot about me!",
                ).also {
                    stage++
                }
            7 -> npcl(FaceAnim.NEUTRAL, "I'm now very hungry, and my bones feel very stiff.").also { stage++ }
            8 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Come with me, I shall make you a king.", STAGE_COME_KING),
                    Topic(FaceAnim.NEUTRAL, "Your father wishes to speak to you.", STAGE_FATHER_SPEAK),
                )

            STAGE_COME_KING -> npcl(FaceAnim.THINKING, "What are you talking about?").also { stage++ }
            11 -> npcl(FaceAnim.THINKING, "The king of where?").also { stage++ }
            12 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "Your father is apparently someone called the Fisher King. He is dying and wishes you to be his heir.",
                ).also {
                    stage++
                }

            STAGE_BEEN_TOLD -> npcl(FaceAnim.SAD, "I have been told that before.").also { stage++ }
            14 -> npcl(FaceAnim.SAD, "I have not been able to find that castle again though...").also { stage++ }
            15 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "Well, I do have the means to get us there - a magic whistle!",
                ).also { stage++ }
            16 -> {
                if (player!!.hasItem(Item(Items.MAGIC_WHISTLE_16, 1))) {
                    sendDialogueLines(
                        player!!,
                        "You give a whistle to Sir Percival. You tell sir Percival what to do",
                        "with the whistle.",
                    ).also {
                        stage++
                    }
                } else {
                    playerl(FaceAnim.NEUTRAL, "Or... I thought I did.")
                    stage = END_DIALOGUE
                }
            }

            17 -> {
                player!!.inventory.remove(Item(Items.MAGIC_WHISTLE_16, 1))
                npcl(FaceAnim.NEUTRAL, "Ok, I will see you there then!").also {
                    setQuestStage(player!!, Quests.HOLY_GRAIL, 50)
                    stage = END_DIALOGUE
                }
            }

            STAGE_FATHER_SPEAK -> npcl(FaceAnim.NEUTRAL, "My father? You have spoken to him recently?").also { stage++ }
            31 ->
                playerl(FaceAnim.NEUTRAL, "He is dying and wishes you to be his heir.").also {
                    stage = STAGE_BEEN_TOLD
                }
        }
    }

    private fun createSirAndSpeak() {
        if (Repository.findNPC(NPCs.SIR_PERCIVAL_211) == null) {
            var perc = NPC(NPCs.SIR_PERCIVAL_211, Location.create(2961, 3505, 0))
            perc.isWalks = false
            perc.init()
            perc.face(player!!)

            queueScript(perc, secondsToTicks(60), QueueStrength.SOFT) { _ ->
                if (perc != null) {
                    perc.clear()
                }
                return@queueScript stopExecuting(perc)
            }
        }

        npcl(FaceAnim.HAPPY, "Wow, thank you! I could hardly breathe in there!")
    }
}
