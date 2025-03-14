package content.region.kandarin.quest.grail.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Items.CUP_OF_TEA_712
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GalahadDialogue : DialogueFile() {
    val STAGE_WHY_LEAVE = 40
    val STAGE_CUP_TEA = 5
    var STAGE_GRAIL_WITH_YOU = 50
    var STAGE_HOW_FIND = 35
    var STAGE_WHAT_TALKING = 30
    var STAGE_GET_GOING = 60
    var STAGE_BETTER_GET_ON = 43

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GALAHAD_218)

        when (stage) {
            0 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Welcome to my home. It's rare for me to have guests! Would you like a cup of tea? I'll just put the kettle on.",
                ).also {
                    stage++
                }

            1 -> sendDialogue(player!!, "Brother Galahad hangs a kettle over the fire.").also { stage++ }
            2 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Are you any relation to Brother Galahad?", 3),
                    IfTopic(
                        FaceAnim.NEUTRAL,
                        "I'm on a quest to find the Holy Grail!",
                        20,
                        getQuestStage(player!!, Quests.HOLY_GRAIL) >= 20 &&
                            !isQuestComplete(player!!, Quests.HOLY_GRAIL),
                    ),
                    Topic(FaceAnim.NEUTRAL, "Do you get lonely here on your own?", 10),
                    IfTopic(
                        FaceAnim.NEUTRAL,
                        "I seek an item from the realm of the Fisher King.",
                        70,
                        (getQuestStage(player!!, Quests.HOLY_GRAIL) == 30) &&
                            !player!!.hasItem(Item(Items.HOLY_TABLE_NAPKIN_15, 1)),
                    ),
                )

            3 -> npcl(FaceAnim.FRIENDLY, "I AM Brother Galahad.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Although I've retired as a Knight, and now live as a solitary monk. Also, I prefer to be known as brother rather than sir now. Half a moment, your cup of tea is ready.",
                ).also {
                    stage++
                }

            STAGE_CUP_TEA ->
                sendItemDialogue(player!!, CUP_OF_TEA_712, "Brother Galahad gives you a cup of tea.").also {
                    addItemOrDrop(player!!, CUP_OF_TEA_712)
                    stage = END_DIALOGUE
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sometimes I do, yes. Still not many people to share my solidarity with, as most of the religious men around here are worshippers of Saradomin. Half a moment, your cup of tea is ready.",
                ).also {
                    stage =
                        STAGE_CUP_TEA
                }
            20 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Ah... the Grail... yes... that did fill me with wonder! Oh, that I could have stayed forever! The spear, the food, the people...",
                ).also {
                    stage++
                }

            21 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "So how can I find it?", STAGE_HOW_FIND),
                    Topic(FaceAnim.NEUTRAL, "What are you talking about?", STAGE_WHAT_TALKING),
                    Topic(FaceAnim.NEUTRAL, "Why did you leave?", STAGE_WHY_LEAVE),
                    Topic(FaceAnim.NEUTRAL, "Why didn't you bring the Grail with you?", STAGE_GRAIL_WITH_YOU),
                )

            STAGE_WHAT_TALKING ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The Grail castle... It's... hard to describe with words. It mostly felt like a dream!",
                ).also {
                    stage++
                }

            31 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "So how can I find it?", STAGE_HOW_FIND),
                    Topic(FaceAnim.NEUTRAL, "Why did you leave?", STAGE_WHY_LEAVE),
                    Topic(FaceAnim.NEUTRAL, "Why didn't you bring the Grail with you?", STAGE_GRAIL_WITH_YOU),
                    Topic(FaceAnim.NEUTRAL, "Well, I'd better be going then.", STAGE_GET_GOING),
                )

            STAGE_HOW_FIND ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I did not find it through looking - though admittedly I looked long and hard - eventually, it found me.",
                ).also {
                    stage++
                }

            36 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "What are you talking about?", STAGE_WHAT_TALKING),
                    Topic(FaceAnim.NEUTRAL, "Why did you leave?", STAGE_WHY_LEAVE),
                    Topic(FaceAnim.NEUTRAL, "Why didn't you bring the Grail with you?", STAGE_GRAIL_WITH_YOU),
                    Topic(FaceAnim.NEUTRAL, "Well, I'd better be going then.", STAGE_GET_GOING),
                )

            STAGE_WHY_LEAVE ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Apparently the time is getting close when the world will need Arthur and his knights of the round table again.",
                ).also {
                    stage++
                }

            41 -> npcl(FaceAnim.NEUTRAL, "And that includes me.").also { stage++ }
            42 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Leaving was tough for me, so I took this small cloth from the table as a keepsake.",
                ).also {
                    stage++
                }

            STAGE_BETTER_GET_ON -> {
                playerl(FaceAnim.NEUTRAL, "I'd better get on with the quest.")
                stage++
            }

            44 -> npcl(FaceAnim.NEUTRAL, "Half a moment, your cup of tea is ready.").also { stage++ }
            45 ->
                sendItemDialogue(player!!, CUP_OF_TEA_712, "Brother Galahad gives you a cup of tea.").also {
                    addItemOrDrop(player!!, CUP_OF_TEA_712)
                    stage = END_DIALOGUE
                }

            STAGE_GRAIL_WITH_YOU ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "...I'm not sure. Because... it seemed to be... NEEDED in the Grail castle? Half a moment, your cup of tea is ready.",
                ).also {
                    stage =
                        STAGE_CUP_TEA
                }

            STAGE_GET_GOING -> npcl(FaceAnim.NEUTRAL, "Half a moment, your cup of tea is ready.").also { stage++ }
            61 ->
                sendItemDialogue(player!!, CUP_OF_TEA_712, "Brother Galahad gives you a cup of tea.").also {
                    addItemOrDrop(player!!, CUP_OF_TEA_712)
                    stage++
                }
            62 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "If you do come across any particularily difficult obstacles on your quest, do not hesitate to ask my advice.",
                ).also {
                    stage++
                }
            63 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I know more about the realm of the Grail than many, and I have a feeling you may need to come back and speak to me anyway...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            70 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Funny you should mention that, but when I left there I took this small cloth from the table as a keepsake.",
                ).also {
                    stage++
                }
            71 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "I don't suppose I could borrow that? It could come in useful on my quest.",
                ).also {
                    stage++
                }
            72 ->
                sendItemDialogue(
                    player!!,
                    Items.HOLY_TABLE_NAPKIN_15,
                    "Galahad reluctantly passes you a small cloth.",
                ).also {
                    addItemOrDrop(player!!, Items.HOLY_TABLE_NAPKIN_15)
                    stage = STAGE_BETTER_GET_ON
                }
            73 -> playerl(FaceAnim.NEUTRAL, "I'd better get on with the quest.").also { stage = STAGE_GET_GOING }
        }
    }
}
