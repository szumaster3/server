package content.region.kandarin.dialogue.seers

import content.region.kandarin.quest.merlin.handlers.MerlinUtils
import core.api.addItemOrDrop
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.removeItem
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LadyOfTheLakeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        var canSeekSword = getQuestStage(player!!, Quests.MERLINS_CRYSTAL) >= 40
        var missingSword = !player!!.hasItem(Item(Items.EXCALIBUR_35, 1))
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Good day to you sir.").also { stage++ }
                1 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Who are you?", 2),
                        Topic(FaceAnim.NEUTRAL, "Good day.", 5),
                        IfTopic(FaceAnim.NEUTRAL, "I seek the sword Excalibur.", 10, canSeekSword),
                    )
                2 -> npcl(FaceAnim.NEUTRAL, "I am the Lady of the Lake.").also { stage = END_DIALOGUE }
                5 ->
                    npcl(FaceAnim.NEUTRAL, "Good day to you ${if (player!!.isMale) "sir" else "madam"}.").also {
                        stage =
                            END_DIALOGUE
                    }
                10 -> npcl(FaceAnim.NEUTRAL, "Aye, I have that artefact in my possession.").also { stage++ }
                11 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "'Tis very valuable, and not an artefact to be given away lightly.",
                    ).also { stage++ }
                12 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I would want to give it away only to one who is worthy and good.",
                    ).also { stage++ }
                13 -> playerl(FaceAnim.NEUTRAL, "And how am I meant to prove that?").also { stage++ }
                14 -> npcl(FaceAnim.NEUTRAL, "I shall set a test for you.").also { stage++ }
                15 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "First I need you to travel to Port Sarim. Then go to the upstairs room of the jeweller's shop there.",
                    ).also {
                        stage++
                    }
                16 ->
                    playerl(FaceAnim.NEUTRAL, "Ok. That seems easy enough.").also {
                        setAttribute(player!!, MerlinUtils.ATTR_STATE_TALK_LADY, true)
                        setAttribute(player!!, MerlinUtils.ATTR_STATE_TALK_BEGGAR, false)
                        stage = END_DIALOGUE
                    }
            }
        } else {
            when (stage) {
                0 -> {
                    if (player!!.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.isComplete(2) &&
                        player!!.equipment.contains(14631, 1) &&
                        player!!.equipment.contains(35, 1)
                    ) {
                        npcl(FaceAnim.HAPPY, "I am the Lady of the Lake.")
                        stage = 110
                    } else {
                        npc("Good day to you, " + (if (player!!.isMale) "sir" else "madam") + ".")
                        stage = 1
                    }
                }

                110 -> player("And I'm-").also { stage++ }
                111 ->
                    npc(
                        "You're " + player!!.username + ". And I see from the sign you",
                        "wear that you have earned the trust of Sir Kay.",
                    ).also {
                        stage++
                    }
                112 -> player("It was nothing.. really...").also { stage++ }
                113 -> npc("You shall be rewarded handsomely!").also { stage++ }
                114 -> {
                    interpreter!!
                        .sendItemMessage(
                            Items.EXCALIBUR_35,
                            "The Lady of the Lake reaches out and touches the",
                            "blade Excalibur which seems to vibrate with new power.",
                        ).also {
                            if (removeItem(player!!, Items.EXCALIBUR_35)) {
                                addItemOrDrop(player!!, Items.ENHANCED_EXCALIBUR_14632)
                            } else if (player!!.equipment.containsAtLeastOneItem(Items.EXCALIBUR_35)) {
                                player!!.equipment.remove(Item(Items.EXCALIBUR_35))
                                player!!.equipment.add(Item(Items.ENHANCED_EXCALIBUR_14632), true, false)
                            }
                            stage++
                        }
                }

                115 -> player("What does this do then?").also { stage++ }
                116 ->
                    npc(
                        "I made the blade more powerful, and also gave it a",
                        "rather healthy effect when you use the special.",
                    ).also {
                        stage++
                    }

                117 -> player("Thanks!").also { stage = END_DIALOGUE }

                1 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Who are you?", 2),
                        Topic(FaceAnim.NEUTRAL, "Good day.", 5),
                        IfTopic(FaceAnim.NEUTRAL, "I seek the sword Excalibur.", 50, missingSword),
                    )

                2 -> npcl(FaceAnim.NEUTRAL, "I am the Lady of the Lake.").also { stage = END_DIALOGUE }
                5 ->
                    npcl(FaceAnim.NEUTRAL, "Good day to you ${if (player!!.isMale) "sir" else "madam"}.").also {
                        stage = END_DIALOGUE
                    }

                50 ->
                    npc(
                        "... But you have already proved thyself to be worthy",
                        "of wielding it once already. I shall return it to you",
                        "if you can prove yourself to still be worthy.",
                    ).also {
                        stage++
                    }

                51 -> player("... And how can I do that?").also { stage++ }
                52 -> npc("Why, by proving yourself to be above material goods.").also { stage++ }
                53 -> npc("500 coins ought to do it.").also { stage++ }
                54 ->
                    if (player!!.inventory.contains(Items.COINS_995, 500)) {
                        player("Ok, here you go.").also { stage = 56 }
                    } else {
                        player("I don't have that kind of money...").also { stage = 55 }
                    }

                55 -> npc("Well, come back when you do.").also { stage = END_DIALOGUE }
                56 ->
                    if (player!!.inventory.freeSlots() == 0) {
                        player("Sorry, I don't seem to have enough inventory space.").also { stage = END_DIALOGUE }
                    } else {
                        player!!.inventory.remove(Item(Items.COINS_995, 500))
                        player!!.inventory.add(Item(Items.EXCALIBUR_35, 1))
                        npc(
                            "You are still worthy to wield Excalibur! And thanks",
                            "for the cash! I felt like getting a new haircut!",
                        )
                        stage++
                    }

                57 ->
                    interpreter!!.sendDialogue("The lady of the Lake hands you Excalibur.").also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LadyOfTheLakeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THE_LADY_OF_THE_LAKE_250)
    }
}
