package content.region.kandarin.seersvillage.quest.fishingcompo.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Austri & Vestri dwarfs dialogue.
 *
 * Relations:
 * - [Fishing Contest][content.region.kandarin.quest.fishingcompo.FishingContest]
 */
@Initializable
class DwarfDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        val questStage = getQuestStage(player, Quests.FISHING_CONTEST)
        val hasFishingPass = inInventory(player, Items.FISHING_PASS_27, 1)
        val hasFishingTrophy = inInventory(player, Items.FISHING_TROPHY_26, 1)
        val hasWonFishingCompetition = getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_WON, false)

        when {
            /*
             * Lost fishing pass.
             */
            questStage in 1..19 && !hasFishingPass -> {
                player("I lost my fishing pass...")
                stage = 1000
            }
            /*
             * Token of Friendship.
             */
            hasFishingTrophy && hasWonFishingCompetition -> {
                npc(FaceAnim.OLD_NORMAL, "Have you won yet?")
                stage = 2000
            }

            questStage >= 10 && !hasWonFishingCompetition -> {
                npc(FaceAnim.OLD_NORMAL, "Have you won yet?")
                stage = 1500
            }

            /*
             * Post-quest dialogue.
             */
            questStage == 100 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Welcome, oh great fishing champion!",
                    "Feel free to pop by and use",
                    "our tunnel any time!"
                )
                stage = 2500
            }

            else -> {
                /*
                 * Secret Tunnel - start quest dialogue.
                 */
                npc(FaceAnim.OLD_NORMAL, "Hmph! What do you want?")
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options(
                    "I was wondering what was down that tunnel?",
                    "I was just stopping to say hello!",
                    "Do you have a brother?"
                )
                stage++
            }

            1 -> {
                when (buttonId) {
                    1 -> player("I was wondering what was down that tunnel?").also { stage += 3 }
                    2 -> player("I was just stopping to say hello!").also { stage++ }
                    3 -> player("Do you have a brother?").also { stage += 2 }
                }
            }

            2 -> npcl(FaceAnim.OLD_DEFAULT, "Hello then.").also { stage = END_DIALOGUE }
            3 -> npc(FaceAnim.OLD_NORMAL, "What if I do! It's no business of yours.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.OLD_ANGRY1, "You can't go down there!").also { stage++ }
            5 -> options("I didn't want to anyway.", "Why not?", "I'm bigger than you. Let me by.").also { stage++ }
            6 -> {
                when (buttonId) {
                    1 -> {
                        player("I didn't want to anyway.")
                        stage = 10
                    }

                    2 -> {
                        player("Why not?")
                        stage = 21
                    }

                    3 -> {
                        player("I'm bigger than you. Let me by.")
                        stage++
                    }
                }
            }

            7 -> {
                npcl(FaceAnim.OLD_ANGRY3, "Go away! You're not going to bully your way in HERE!")
                stage = 100
            }

            10 -> {
                npc(FaceAnim.OLD_NORMAL, "Good. Because you can't.")
                stage++
            }

            11 -> {
                player("Because I don't want to.")
                stage++
            }

            12 -> {
                npc(FaceAnim.OLD_NORMAL, "Because you can't. So that's fine.")
                stage++
            }

            13 -> {
                player("Yes it is.")
                stage++
            }

            14 -> {
                npc(FaceAnim.OLD_NORMAL, "Yes. Fine.")
                stage++
            }

            15 -> {
                player("Absolutely.")
                stage++
            }

            16 -> {
                npc(FaceAnim.OLD_NORMAL, "Well then.")
                stage = 100
            }

            20 -> {
                player("Why not?")
                stage++
            }

            21 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "This is the home of the Mountain Dwarves.",
                    "How would you like it if I",
                    "wanted to take a shortcut through your home?",
                )
                stage++
            }

            22 -> {
                options(
                    "Ooh... is this a short cut to somewhere?",
                    "Oh, sorry, I hadn't realized it was private.",
                    "If you were my friend I wouldn't mind it.",
                )
                stage++
            }

            23 -> when (buttonId) {
                1 -> {
                    player("Ooh... is this a short cut to somewhere?")
                    stage = 30
                }

                2 -> {
                    player("Oh, sorry, I hadn't realized it was private.")
                    stage = 40
                }

                3 -> {
                    player("If you were my friend I wouldn't mind it.")
                    stage = 50
                }
            }

            30 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Well, it is a lot easier to go this way",
                    "to get past White Wolf Mountain than through",
                    "those wolf filled passes.",
                )
                stage = 22
            }

            40 -> {
                npc(FaceAnim.OLD_NORMAL, "Well, it is.")
                stage = 22
            }

            50 -> {
                npc(FaceAnim.OLD_NORMAL, "Yes, but I don't even know you.")
                stage++
            }

            51 -> {
                options("Well, let's be friends!", "You're a grumpy little man aren't you?")
                stage++
            }

            52 -> {
                when (buttonId) {
                    1 -> {
                        player("Well, let's be friends!")
                        stage++
                    }

                    2 -> {
                        player("You're a grumpy little man aren't you?")
                        stage = 70
                    }
                }
            }

            53 -> {
                npc(FaceAnim.OLD_NORMAL, "I don't make friends easily.", "People need to earn my trust first.")
                stage++
            }

            54 -> {
                player("And how am I meant to do that?")
                stage++
            }

            55 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "My, we are the persistent one aren't we?",
                    "Well, there's a certain gold artefact we're after.",
                    "We dwarves are big fans of gold! This artefact",
                    "is the first prize at the Hemenster fishing competition.",
                )
                stage++
            }

            56 -> {
                npc(FaceAnim.OLD_NORMAL, "Fortunately we have acquired a pass to enter", "that competition...")
                stage++
            }
            57 -> {
                npc(FaceAnim.OLD_NORMAL, "Unfortunately Dwarves don't make good fishermen.")
                stage++
            }

            58 -> {
                /*
                 * Requirements check.
                 */
                if(getStatLevel(player, Skills.FISHING) < 10) {
                    npc(FaceAnim.OLD_NORMAL, "Seems to me like you're not much of anything.")
                    stage++
                } else {
                    npc(FaceAnim.OLD_NORMAL, "Okay, I entrust you with our competition pass.")
                    stage = 61
                }
            }

            59 -> {
                player(FaceAnim.FRIENDLY, "I'll get better at fishing and come back!")
                stage++
            }

            60 -> {
                npcl(FaceAnim.OLD_NORMAL, "Aye, it shouldn't take you long. When it's up to 10, come back and find me.")
                stage = 100
            }

            61 -> {
                npc(FaceAnim.OLD_NORMAL, "Don't forget to take some gold", "with you for the entrance fee.")
                stage++
            }

            62 -> {
                sendItemDialogue(player, Items.FISHING_PASS_27, "You got the Fishing Contest Pass!")
                addItemOrDrop(player, Items.FISHING_PASS_27, 1)
                setQuestStage(player, Quests.FISHING_CONTEST, 10)
                stage++
            }

            63 -> {
                npc(FaceAnim.OLD_NORMAL, "Go to Hemenster and do us proud!")
                stage = 100
            }

            70 -> {
                npc(FaceAnim.OLD_NORMAL, "Don't you know it.")
                stage = 51
            }

            100 -> end()

            1000 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Hmm. It's a good job they sent us spares.",
                    "There you go. Try not to lose that one.",
                )
                addItem(player, Items.FISHING_PASS_27, 1)
                stage++
            }

            1001 -> {
                player("No, it takes preparation to win", "fishing competitions.")
                stage++
            }

            1002 -> {
                npc(FaceAnim.OLD_NORMAL, "Maybe that's where we are going wrong", "when we try fishing?")
                stage++
            }

            1003 -> {
                player("Probably.")
                stage++
            }

            1004 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Maybe we should talk to that old Jack",
                    "fella near the competition, everyone",
                    "seems to be ranting about him.",
                )
                stage = 100
            }

            2000 -> {
                player("Yes, I have!")
                stage++
            }

            2001 -> {
                npc(FaceAnim.OLD_NORMAL, "Well done! That's brilliant, do you have", "the trophy?")
                stage++
            }

            2002 -> {
                player("Yep, I have it right here!")
                stage++
            }

            2003 -> {
                npc(FaceAnim.OLD_NORMAL, "Oh, it's even more shiny and gold than", "I thought possible...")
                stage++
            }

            2004 -> {
                if (removeItem(player, Items.FISHING_TROPHY_26)) {
                    end()
                    setAttribute(player, "temp-npc", npc.id)
                    finishQuest(player, Quests.FISHING_CONTEST)
                }
            }

            1500 -> {
                player("No, I haven't.")
                stage++
            }

            1501 -> end()
            2500 -> {
                player("Thanks, I think I will stop by.")
                stage = 100
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.AUSTRI_232, NPCs.VESTRI_3679)
}
