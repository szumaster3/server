package content.region.kandarin.dialogue.seers

import content.region.kandarin.quest.grail.HolyGrail
import core.api.addItemOrDrop
import core.api.quest.*
import core.api.setVarp
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KingArthurDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val STAGE_MERLIN_FINISH = 20

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> {
                    if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 60) {
                        playerl(FaceAnim.NEUTRAL, "I have freed Merlin from his crystal!").also { stage++ }
                        stage = STAGE_MERLIN_FINISH
                    } else {
                        npcl(FaceAnim.NEUTRAL, "Welcome to my court. I am King Arthur.")
                        stage++
                    }
                }
                1 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "I want to become a Knight of the Round Table!", 2),
                        Topic(FaceAnim.NEUTRAL, "So what are you doing in Gielinor?", 10),
                        Topic(FaceAnim.NEUTRAL, "Thank you very much.", END_DIALOGUE),
                    )
                2 -> {
                    if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) > 0) {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well then you must complete your quest to rescue Merlin. Talk to my knights if you need any help.",
                        ).also { stage = END_DIALOGUE }
                    } else {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well, in that case I think you need to go on a quest to prove yourself worthy.",
                        ).also {
                            stage++
                        }
                    }
                }
                3 -> npcl(FaceAnim.NEUTRAL, "My knights all appreciate a good quest.").also { stage++ }
                4 -> npcl(FaceAnim.DISGUSTED, "Unfortunately, our current quest is to rescue Merlin.").also { stage++ }
                5 ->
                    npcl(
                        FaceAnim.THINKING,
                        "Back in England, he got himself trapped in some sort of magical Crystal. We've moved him from the cave we found him in and now he's upstairs in his tower.",
                    ).also {
                        stage++
                    }
                6 -> {
                    playerl(FaceAnim.NEUTRAL, "I will see what I can do then.")
                    setQuestStage(player, Quests.MERLINS_CRYSTAL, 10)
                    stage++
                }
                7 -> npcl(FaceAnim.NEUTRAL, "Talk to my knights if you need any help.").also { stage = END_DIALOGUE }
                10 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Well legend says we will return to Britain in its time of greatest need. But that's not for quite a while yet.",
                    ).also {
                        stage++
                    }
                11 -> npcl(FaceAnim.NEUTRAL, "So we've moved the whole outfit here for now.").also { stage++ }
                12 -> npcl(FaceAnim.NEUTRAL, "We're passing the time in Gielinor!").also { stage = END_DIALOGUE }
                20 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Ah, A good job, well done. I dub thee a Knight Of The Round Table. You are now an honorary knight.",
                    ).also {
                        stage++
                    }
                21 -> {
                    end()
                    finishQuest(player!!, Quests.MERLINS_CRYSTAL)
                }
            }
        } else {
            when (stage) {
                0 -> {
                    if (isQuestComplete(player!!, Quests.HOLY_GRAIL)) {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Thank you for retrieving the Grail! You shall be long remembered as one of the greatest heroes amongst the Knights of the Round Table!",
                        )
                        stage = END_DIALOGUE
                    } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0) {
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Now I am a knight of the round table, do you have any more quests for me?",
                        )
                        stage++
                    } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 40) {
                        playerl(FaceAnim.NEUTRAL, "Hello, do you have a knight named Sir Percival?")
                        stage = 40
                    } else {
                        npcl(FaceAnim.NEUTRAL, "How goes thy quest?")

                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 50 &&
                            player!!.hasItem(Item(Items.HOLY_GRAIL_19, 1))
                        ) {
                            stage = 60
                        } else {
                            stage = 30
                        }
                    }
                }
                1 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Aha! I'm glad you are here! I am sending out various knights on an important quest. I was wondering if you too would like to take up this quest?",
                    ).also {
                        stage++
                    }
                2 ->
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Tell me of this quest.", 13),
                        Topic(FaceAnim.NEUTRAL, "I am weary of questing for the time being...", 23),
                    )
                23 -> npcl(FaceAnim.NEUTRAL, "Maybe later then?").also { stage++ }
                24 -> playerl(FaceAnim.NEUTRAL, "Maybe so.").also { stage = END_DIALOGUE }
                13 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Well, we recently found out that the Holy Grail has passed into Gielinor.",
                    ).also {
                        stage++
                    }
                14 -> npcl(FaceAnim.NEUTRAL, "This is most fortuitous!").also { stage++ }
                15 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "None of my knights ever did return with it last time. Now we have the opportunity to give it another go, maybe this time we will have more luck!",
                    ).also {
                        stage++
                    }
                16 ->
                    showTopics(
                        Topic("I'd enjoy trying that.", 17),
                        Topic("I may come back and try that later.", 20),
                    )
                17 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Go speak to Merlin. He may be able to give a better clue as to where it is now you have freed him from that crystal.",
                    ).also {
                        setQuestStage(player!!, Quests.HOLY_GRAIL, 10)
                        setVarp(player!!, HolyGrail.VARP_INDEX, HolyGrail.VARP_SHOW_MERLIN_VALUE, true)
                        stage++
                    }
                18 ->
                    npcl(FaceAnim.NEUTRAL, "He has set up his workshop in the room next to the library.").also {
                        stage =
                            END_DIALOGUE
                    }
                20 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Be sure that you come speak to me soon then.",
                    ).also { stage = END_DIALOGUE }
                30 ->
                    playerl(
                        FaceAnim.NEUTRAL,
                        "I am making progress, but I have not recovered the Grail yet.",
                    ).also { stage++ }
                31 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Well, the Grail IS very elusive, it may take some perseverance.",
                    ).also { stage++ }
                32 ->
                    npcl(FaceAnim.NEUTRAL, "As I said before, speak to Merlin in the workshop by the library.").also {
                        stage =
                            END_DIALOGUE
                    }
                40 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Ah yes. I remember young Percival. He rode off on a quest a couple of months ago. We are getting a bit worried, he's not back yet...",
                    ).also {
                        stage++
                    }
                41 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "He was going to try and recover the golden boots of Arkaneeses.",
                    ).also { stage++ }
                42 -> playerl(FaceAnim.NEUTRAL, "Any idea which way that would be?").also { stage++ }
                43 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Not exactly. We discovered some magic golden feathers that are said to point the way to the boots...",
                    ).also {
                        stage++
                    }
                44 -> npcl(FaceAnim.NEUTRAL, "They certainly point somewhere.").also { stage++ }
                45 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Just blowing gently on them will supposedly show the way to go.",
                    ).also { stage++ }

                46 -> {
                    if (player!!.hasItem(Item(Items.MAGIC_GOLD_FEATHER_18, 1))) {
                        npcl(FaceAnim.NEUTRAL, "You've got one of the feathers somewhere - off you go.")
                    } else {
                        addItemOrDrop(player!!, Items.MAGIC_GOLD_FEATHER_18, 1)
                        core.api.sendDialogue(player!!, "King Arthur gives you a feather.")
                    }
                    stage = END_DIALOGUE
                }

                60 -> playerl(FaceAnim.NEUTRAL, "I have retrieved the Grail!").also { stage++ }
                61 -> npcl(FaceAnim.NEUTRAL, "Wow! Incredible! You truly are a splendid knight!").also { stage++ }
                62 -> {
                    end()
                    stage = END_DIALOGUE
                    getQuest(player!!, Quests.HOLY_GRAIL).finish(player!!)
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KingArthurDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KING_ARTHUR_251)
    }
}
