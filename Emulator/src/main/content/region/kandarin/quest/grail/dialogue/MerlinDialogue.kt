package content.region.kandarin.quest.grail.dialogue

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class MerlinDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MERLIN_249)

        when (stage) {
            0 -> {
                if (isQuestComplete(player!!, Quests.HOLY_GRAIL)) {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Congratulations, brave knight, on aiding Camelot in so many ways! If we ever require help again, I will make sure to call upon you!",
                    )
                    stage = 20
                } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 50 &&
                    player!!.hasItem(Item(Items.HOLY_GRAIL_19, 1))
                ) {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "My magic powers tell me that you have discovered the Grail! Take it to Arthur immediately!",
                    )
                    stage = END_DIALOGUE
                } else {
                    playerl(
                        FaceAnim.NEUTRAL,
                        "Hello. King Arthur has sent me on a quest for the Holy Grail. He thought you could offer some assistance.",
                    ).also {
                        stage++
                    }
                }
            }
            1 ->
                npcl(FaceAnim.NEUTRAL, "Ah yes... the Holy Grail...").also {
                    if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                        setQuestStage(player!!, Quests.HOLY_GRAIL, 20)
                    }
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "That is a powerful artefact indeed. Returning it here would help Camelot a lot.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Due to its nature the Holy Grail is likely to reside in a holy place.",
                ).also { stage++ }
            4 -> playerl(FaceAnim.NEUTRAL, "Any suggestions?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I believe there is a holy island somewhere not far away... I'm not entirely sure... I spent too long inside that crystal! Anyway, go and talk to someone over there.",
                ).also {
                    stage++
                }
            6 -> npcl(FaceAnim.NEUTRAL, "I suppose you could also try speaking to Sir Galahad?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "He returned from the quest many years after everyone else. He seems to know something about it, but he can only speak about those experiences cryptically.",
                ).also {
                    stage++
                }
            8 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Thank you for the advice.", END_DIALOGUE),
                    Topic(FaceAnim.NEUTRAL, "Where can I find Sir Galahad?", 15),
                )
            15 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Galahad now lives a life of religious contemplation. He lives somewhere west of McGrubor's Wood I think.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            20 -> playerl(FaceAnim.NEUTRAL, "Thanks!").also { stage = END_DIALOGUE }
        }
    }
}
