package content.region.kandarin.quest.fishingcompo.dialogue

import content.region.kandarin.quest.fishingcompo.FishingContest
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.quest.updateQuestTab
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DwarfDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.FISHING_CONTEST)
        if (questStage in 1..19 && !inInventory(player, FishingContest.FISHING_PASS.id)) {
            player("I lost my fishing pass...")
            stage = 1000
            return true
        }
        if (inInventory(player, FishingContest.FISHING_TROPHY.id) &&
            player.getAttribute("fishing_contest:won", false)
        ) {
            npc(FaceAnim.OLD_NORMAL, "Have you won yet?")
            stage = 2000
            return true
        }
        if (getQuestStage(player, Quests.FISHING_CONTEST) >= 10 &&
            !player.getAttribute(
                "fishing_contest:won",
                false,
            )
        ) {
            npc(FaceAnim.OLD_NORMAL, "Have you won yet?")
            stage = 1500
            return true
        }
        if (getQuestStage(player, Quests.FISHING_CONTEST) == 100) {
            npc(
                FaceAnim.OLD_NORMAL,
                "Welcome, oh great fishing champion!",
                "Feel free to pop by and use",
                "our tunnel any time!",
            )
            stage = 2500
            return true
        }
        npc(FaceAnim.OLD_NORMAL, "Hmph! What do you want?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                player("I was wondering what was down that tunnel?")
                stage++
            }

            1 -> {
                npc(FaceAnim.OLD_NORMAL, "You can't go down there!")
                stage++
            }

            2 -> {
                options("I didn't want to anyway.", "Why not?")
                stage++
            }

            3 ->
                when (buttonId) {
                    1 -> {
                        player("I didn't want to anyway.")
                        stage = 10
                    }

                    2 -> {
                        player("Why not?")
                        stage = 21
                    }
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

            23 ->
                when (buttonId) {
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
                player("Well, let's be friends!")
                stage++
            }

            52 -> {
                npc(FaceAnim.OLD_NORMAL, "I don't make friends easily.", "People need to earn my trust first.")
                stage++
            }

            53 -> {
                player("And how am I to do that?")
                stage++
            }

            54 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "My, we are the persistent one aren't we?",
                    "Well, there's a certain gold artefact we're after.",
                    "We dwarves are big fans of gold! This artefact",
                    "is the first prize at the Hemenster fishing competition.",
                )
                stage++
            }

            55 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Fortunately we have acquired a pass to enter",
                    "that competition... Unfortunately Dwarves don't",
                    "make good fisherman. Okay, I entrust you with our",
                    "competition pass.",
                )
                stage++
            }

            56 -> {
                npc(FaceAnim.OLD_NORMAL, "Don't forget to take some gold", "with you for the entrance fee.")
                stage++
            }

            57 -> {
                sendItemDialogue(player, FishingContest.FISHING_PASS, "You got the Fishing Contest Pass!")
                addItemOrDrop(player, FishingContest.FISHING_PASS.id, 1)
                setQuestStage(player, Quests.FISHING_CONTEST, 10)
                stage++
            }

            58 -> {
                npc(FaceAnim.OLD_NORMAL, "Go to Hemenster and do us proud!")
                stage = 100
            }

            100 -> end()
            1000 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Hmm. It's a good job they sent us spares.",
                    "There you go. Try not to lose that one.",
                )
                addItem(player, FishingContest.FISHING_PASS.id, 1)
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
                end()
                if (removeItem(player, FishingContest.FISHING_TROPHY)) {
                    finishQuest(player, Quests.FISHING_CONTEST)
                    updateQuestTab(player)
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

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AUSTRI_232, NPCs.VESTRI_3679)
    }
}
