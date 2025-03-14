package content.region.kandarin.quest.hazeelcult.dialogue

import content.region.kandarin.quest.hazeelcult.handlers.HazeelCultListener.Companion.CARNILLEAN
import content.region.kandarin.quest.hazeelcult.handlers.HazeelCultListener.Companion.MAHJARRAT
import content.region.kandarin.quest.hazeelcult.handlers.HazeelCultListener.Companion.SEWER_LEFT
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ClivetDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questName = "Hazeel Cult"
        val questStage = getQuestStage(player!!, questName)

        when {
            (questStage == 0) ->
                when (stage) {
                    0 -> npcl(FaceAnim.ANNOYED, "What do you want traveller?").also { stage++ }
                    1 -> playerl(FaceAnim.ANNOYED, "Just passing by.").also { stage++ }
                    2 -> npcl(FaceAnim.ANNOYED, "You have no business here.").also { stage++ }
                    3 -> npcl(FaceAnim.ANGRY, "Leave...now").also { stage = END_DIALOGUE }
                }

            (questStage == 1) ->
                when (stage) {
                    0 -> playerl(FaceAnim.ANNOYED, "Do you know the Carnilleans?").also { stage++ }
                    1 -> npcl(FaceAnim.ANNOYED, "I'll mind my business you mind yours.").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.ANNOYED,
                            "Look I KNOW you're hiding something. I've heard there's a cult hideout down here.",
                        ).also {
                            stage++
                        }
                    3 -> npcl(FaceAnim.ANGRY, "If you want to stay healthy you'll leave now.").also { stage++ }
                    4 -> playerl(FaceAnim.ANNOYED, "I have my orders.").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.HALF_ASKING,
                            "So... that two faced cold hearted snob has made you fall for his propaganda, eh?",
                        ).also {
                            stage++
                        }
                    6 -> playerl(FaceAnim.FRIENDLY, "Sir Ceril Carnillean is a man of honour!").also { stage++ }
                    7 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Is he now? Is he REALLY? there's a lot more to the Carnilleans than meets the eye you fool...",
                        ).also {
                            stage++
                        }
                    8 -> npcl(FaceAnim.ANNOYED, "and none of it is honourable.").also { stage++ }
                    9 -> options("What do you mean?", "I've heard enough of your rubbish.").also { stage++ }
                    10 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.HALF_ASKING, "What do you mean?").also { stage++ }
                            2 -> playerl(FaceAnim.ANNOYED, "I've heard enough of your rubbish.").also { stage = 27 }
                        }
                    11 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "The Carnillean home does not belong to them. The builder of the house was Lord Hazeel one of the Mahjarrat followers of Zamorak. Many years ago there was a civil war",
                        ).also {
                            stage++
                        }
                    12 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "in this land, and the hateful Saradominists declared war upon, all Zamorakians who lived here. Lord Hazeel nobly would not response his beliefs and the Carnilleans harrased",
                        ).also {
                            stage++
                        }
                    13 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Hazeel and his followers for many decades. One fateful night, vader cover of darkness, they stormed his home in an angry mob torturing and butchering all loyal",
                        ).also {
                            stage++
                        }
                    14 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Zamorakians they encountered inside. The following morning, the Carnillean forefathers moved into the empty household and claimed it as their own.",
                        ).also {
                            stage++
                        }
                    15 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "They have grown fat on the hard work of Lord Hazeel ever since, Unluckily for them Lord Hazeel as a Mahjarrat had access to powers and enchantments they knew nothing",
                        ).also {
                            stage++
                        }
                    16 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "about, and made preparations for his return when first they began to storm his home. Soon the day will come when he will return to wreak his vengeance upon the thieves!",
                        ).also {
                            stage++
                        }
                    17 ->
                        playerl(
                            FaceAnim.ANNOYED,
                            "The politics and histories of Ardougne do not concerns me. I have been given a job and intent to see it through to the end.",
                        ).also {
                            stage++
                        }
                    18 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Well then friend, perhaps I can offer you a different job then? Sooner as later Our mmaster WILL return to this land, those faithful to him will be well rewarded.",
                        ).also {
                            stage++
                        }
                    19 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Join our cult and assist us in his restoration, and you will be well rewarded, try and preserve his return and suffer the wrath of Zamorak and the Mahjarrat.",
                        ).also {
                            stage++
                        }
                    20 -> options("You're crazy, i'd never help you.", "So what would i have to do?").also { stage++ }
                    21 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.DISGUSTED_HEAD_SHAKE,
                                    "You're crazy, i'd never help you.",
                                ).also { stage++ }
                            2 -> playerl(FaceAnim.HALF_ASKING, "So what would i have to do?").also { stage = 24 }
                        }
                    22 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Then you are a fool. Go back to your small minded moundane little life, you will never know the glories you could have tasted as one of us!",
                        ).also {
                            stage++
                        }
                    23 -> {
                        end()
                        setAttribute(player, CARNILLEAN, true)
                        setAttribute(player, SEWER_LEFT, 0)
                        setQuestStage(player, Quests.HAZEEL_CULT, 2)
                        runTask(player, 1) {
                            teleport(npc, location(2568, 9683, 1))
                            sendDialogue(
                                player,
                                "The man jumps onto the raft and pushes off down into the sewer system.",
                            )
                            sendMessage(player, "Clivet: You'll never find us...")
                            stage = END_DIALOGUE
                            runTask(player, 10) {
                                teleport(npc, location(2568, 9683, 0))
                            }
                        }
                    }
                    24 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "I can already tell I was right about you. I can see you are of exactly the right character to join the followers of Hazeel.",
                        ).also {
                            stage++
                        }
                    25 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "Here, take this poison and pour it into Ceril Carnillean's food. Once the deed is done, return here and speak to me once more.",
                        ).also {
                            stage++
                        }
                    26 -> {
                        setAttribute(player, MAHJARRAT, true)
                        setQuestStage(player, Quests.HAZEEL_CULT, 2)
                        if (freeSlots(player) < 1) {
                            end()
                            sendItemDialogue(
                                player,
                                Items.POISON_273,
                                "Clivet tries to give you some poison, but you don't have enough room to take it.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            end()
                            sendItemDialogue(player, Items.POISON_273, "Clivet gives you some poison.").also {
                                stage = END_DIALOGUE
                            }
                            addItemOrDrop(player, Items.POISON_273)
                        }
                    }

                    27 -> npcl(FaceAnim.ANNOYED, "Then leave, fool...").also { stage = END_DIALOGUE }
                }

            (questStage == 2) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player!!, MAHJARRAT, true) &&
                            !inInventory(player, Items.POISON_273) &&
                            !getAttribute(player, CARNILLEAN, true)
                        ) {
                            playerl(FaceAnim.FRIENDLY, "I need some more poison.").also { stage++ }
                        } else {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "You have a mission for us, adventurer. Go to the Carnilliean household and poison Ceril Carnillean's meal to prove your loyalty.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        }
                    }

                    1 -> npcl(FaceAnim.ANNOYED, "Fool! Be more careful with it this time.").also { stage++ }
                    2 -> {
                        if (freeSlots(player) < 1) {
                            end()
                            sendItemDialogue(
                                player,
                                Items.POISON_273,
                                "Clivet tries to give you some poison, but you don't have enough room to take it.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            end()
                            addItemOrDrop(player, Items.POISON_273)
                            sendItemDialogue(player, Items.POISON_273, "Clivet gives you some poison.").also {
                                stage = END_DIALOGUE
                            }
                        }
                    }

                    3 -> playerl(FaceAnim.HALF_ASKING, "WHERE is the cult hideout?").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "You're more of a fool than you look if you think you will ever find it. When Lord Hazeel is revived you will be the first to grovel for his mercy!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            (questStage == 3) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, MAHJARRAT, true) && !getAttribute(player, CARNILLEAN, true)) {
                            playerl(
                                FaceAnim.FRIENDLY,
                                "I poisoned the food as requested, but it didn't quite go to plan.",
                            ).also { stage++ }
                        } else {
                            sendMessage(
                                player,
                                "They aren't interested in talking to you.",
                            ).also { stage = END_DIALOGUE }
                        }
                    }
                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Yes, we heard all about it from one of our sources. It's a shame that Ceril Carnillean survived, but that is no fault of yours. You have proven your loyalty to Hazeel.",
                        ).also {
                            stage++
                        }
                    2 -> playerl(FaceAnim.HALF_ASKING, "So what now?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "I would like you to meet with our leader, Alomone. You'll find him in our hideout, deeper in the sewers. You can use this raft to get there.",
                        ).also {
                            stage++
                        }
                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "However, be warned that reaching the hideout is impossible unless the flow of water down here is set correctly. You'll need to use the sewer valves above to do that.",
                        ).also {
                            stage++
                        }
                    5 -> playerl(FaceAnim.HALF_ASKING, "But how will I know how to set the valves?").also { stage++ }
                    6 -> npcl(FaceAnim.NEUTRAL, "For that, you'll need this.").also { stage++ }
                    7 -> {
                        if (freeSlots(player) < 1) {
                            end()
                            sendItemDialogue(
                                player,
                                Items.HAZEELS_MARK_2406,
                                "Clivet tries to give you an amulet, but you don't have enough room to take it.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            addItemOrDrop(player, Items.HAZEELS_MARK_2406)
                            sendItemDialogue(
                                player,
                                Items.HAZEELS_MARK_2406,
                                "Clivet gives you an amulet.",
                            ).also { stage++ }
                        }
                    }
                    8 -> {
                        end()
                        setQuestStage(player, Quests.HAZEEL_CULT, 10)
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Going from left to right, turn each of the five sewer valves above so that they follow the design of the amulet. Once you've done that, you'll be able to use the raft here to enter our hideout.",
                        )
                        stage = END_DIALOGUE
                    }
                }

            (questStage == 100) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, MAHJARRAT, true) && !getAttribute(player, CARNILLEAN, true)) {
                            playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 1 }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 4 }
                        }
                    }
                    1 ->
                        npcl(FaceAnim.FRIENDLY, "It is good to see you once more, adventurer. Glory to Hazeel!").also {
                            stage =
                                END_DIALOGUE
                        }
                    4 ->
                        npcl(
                            FaceAnim.ANNOYED,
                            "You may have won this battle meddler, but the war rages on. Go bother some goblins or something for when Hazeel returns your destruction is assured.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CLIVET_893)
    }
}
