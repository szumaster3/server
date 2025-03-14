package content.region.misthalin.quest.phoenixgang.dialogue

import content.region.desert.quest.golem.CuratorHaigHalenGolemDialogue
import core.api.*
import core.api.quest.getQuestPoints
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CuratorHaigHalenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                npcl(FaceAnim.NEUTRAL, "Welcome to Varrock Museum!").also {
                    if (getQuestPoints(player) >= 50 && !hasDiaryTaskComplete(player, DiaryType.VARROCK, 0, 12)) {
                        finishDiaryTask(player, DiaryType.VARROCK, 0, 12)
                    }
                    if (getQuestStage(player, Quests.THE_DIG_SITE) == 1 &&
                        inInventory(player, Items.UNSTAMPED_LETTER_682)
                    ) {
                        stage = 11
                    }
                    stage++
                }
            1 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Have you any interesting news?", 2),
                    Topic(FaceAnim.FRIENDLY, "Do you know where I could find any treasure?", 8),
                    IfTopic<Any?>(
                        FaceAnim.FRIENDLY,
                        "I've lost the letter of recommendation.",
                        18,
                        getQuestStage(
                            player,
                            Quests.THE_DIG_SITE,
                        ) == 2 &&
                            !inInventory(player, Items.SEALED_LETTER_683),
                    ),
                    IfTopic<Any?>(
                        "I have the ${Quests.SHIELD_OF_ARRAV}",
                        CuratorHaigHalenDialogueFile(),
                        getQuestStage(player, Quests.SHIELD_OF_ARRAV) == 70,
                        false,
                    ),
                    IfTopic<Any?>(
                        "I'm looking for a statuette recovered from the city of Uzer.",
                        CuratorHaigHalenGolemDialogue(),
                        getQuestStage(player, Quests.THE_GOLEM) == 3,
                        false,
                    ),
                )
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, we found a rather interesting island to the north of Morytania. We believe that it may be of archaeological significance.",
                ).also {
                    stage++
                }
            3 -> playerl(FaceAnim.FRIENDLY, "Oh? That sounds interesting.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Indeed. I suspect we'll be looking for qualified archaeologists once we have constructed our canal and barge.",
                ).also {
                    stage++
                }
            5 -> playerl(FaceAnim.FRIENDLY, "Would I qualify then?").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You've certainly done a lot to help out Varrock Museum, so we'd be silly not to ask for your expertise.",
                ).also {
                    stage++
                }
            7 -> playerl(FaceAnim.FRIENDLY, "Thank you. I'll look forward to it!").also { stage = END_DIALOGUE }
            8 -> npcl(FaceAnim.FRIENDLY, "Look around you! This museum is full of treasures!").also { stage++ }
            9 -> playerl(FaceAnim.FRIENDLY, "No, I meant treasures for ME.").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Any treasures this museum knows about it goes to great lengths to acquire.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            11 ->
                player(
                    FaceAnim.FRIENDLY,
                    "I've been given this letter by an examiner at the Dig",
                    "Site. Can you stamp this for me?",
                ).also {
                    stage++
                }
            12 -> npc(FaceAnim.FRIENDLY, "What have we here? A letter of recommendation", "indeed...").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The letter here says you name is ${player.username}. Well ${player.username}, I wouldn't normally do this for just anyone, but as you did us such a great service with the Shield of Arrav I don't see why not.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Run this letter back to the Examiner to begin your adventure into the world of Earth Sciences. Enjoy your studies, Student!",
                ).also {
                    stage++
                }
            15 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "There you go, good luck student... Be sure to come",
                    "back and show me your certificates. I would like to see",
                    "how you get on.",
                ).also {
                    if (removeItem(player, Items.UNSTAMPED_LETTER_682)) {
                        addItemOrDrop(player, Items.SEALED_LETTER_683)
                    }
                    stage++
                }

            16 ->
                playerl(FaceAnim.FRIENDLY, "Ok, I will. Thanks, see you later.").also {
                    if (getQuestStage(player, Quests.THE_DIG_SITE) == 1) {
                        setQuestStage(player, Quests.THE_DIG_SITE, 2)
                    }
                    stage = 1
                }

            18 ->
                npc(FaceAnim.FRIENDLY, "Yes, I saw you drop it as you walked off last time. Here it is.").also {
                    addItemOrDrop(player, Items.SEALED_LETTER_683)
                    stage = 1
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return CuratorHaigHalenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CURATOR_HAIG_HALEN_646)
    }
}
