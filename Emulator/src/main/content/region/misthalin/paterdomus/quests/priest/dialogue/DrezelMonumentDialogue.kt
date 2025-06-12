package content.region.misthalin.paterdomus.quests.priest.dialogue

import content.data.GameAttributes
import content.region.morytania.quest.druidspirit.DrezelDialogueFile
import core.api.*
import core.api.quest.finishQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DrezelMonumentDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        var quest = player.getQuestRepository().getQuest(Quests.PRIEST_IN_PERIL)
        if (quest.getStage(player) == 17) {
            npc(
                FaceAnim.HALF_GUILTY,
                "Ah, " + player.username + ". I see you finally made it down here.",
                "Things are worse than I feared. I'm not sure if I will",
                "be able to repair the damage.",
            )
            stage = 900
            return true
        }
        if (quest.getStage(player) == 18) {
            stage =
                if (anyInInventory(player, Items.RUNE_ESSENCE_1436, Items.PURE_ESSENCE_7936)) {
                    player(FaceAnim.HALF_GUILTY, "I brought you some Rune Essence.")
                    100
                } else {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "How many more essence do I need to bring you?",
                    )
                    120
                }
            return true
        }
        if (quest.getStage(player) == 100 && !player.getSavedData().questData.isTalkedDrezel) {
            player(FaceAnim.HALF_GUILTY, "So can I pass through that barrier now?")
            stage = 400
            return true
        }
        quest = player.getQuestRepository().getQuest(Quests.NATURE_SPIRIT)
        if (quest.getStage(player) <= 5) {
            npc(
                FaceAnim.HALF_GUILTY,
                "Greetings again adventurer, How go your travels in",
                "Morytania? Is it as evil as I have heard?",
            )
            stage = 420
        } else if (quest.getStage(player) < 100 ||
            hasAnItem(
                player,
                Items.WOLFBANE_2952,
            ).container == null &&
            quest.getStage(player) == 100
        ) {
            openDialogue(player, DrezelDialogueFile(), npc)
        } else {
            npcl(FaceAnim.NEUTRAL, "I heard you finished your quest with Filliman! Great work!")
            stage = END_DIALOGUE
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.PRIEST_IN_PERIL)
        when (stage) {
            400 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Ah, " + player.username + ". For all the assistance you have given",
                    "both myself and Misthalin in your actions, I cannot let",
                    "you pass without warning you.",
                )
                stage = 401
            }

            401 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Morytania is an evil land, filled with creatures and",
                    "monsters more terrifying than any you have yet",
                    "encountered. Although I will pray for you",
                )
                stage = 402
            }

            402 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "you should take some basic precautions before heading",
                    "over the Salve into it. The first place you will come",
                    "across is the Werewolf trading post.",
                )
                stage = 403
            }

            403 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "In many ways Werewolves are like you and me, except",
                    "never forget that they are evil vicious beasts at heart.",
                    "The dagger I have given you is named 'Wolfbane'",
                )
                stage = 404
            }

            404 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "and it is a holy relic that prevents the werewolf people from",
                    "changing form, I suggest if you battle with them",
                    "that you keep it always equipped, for their",
                )
                stage = 405
            }

            405 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "wolf form is incredibly powerful, and would savage you",
                    "very quickly. Please adventurer, promise me this: I",
                    "should hate for you to die foolishly.",
                )
                stage = 406
            }

            406 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Okay, I will keep it equipped whenever I fight",
                    "werewolves.",
                )
                stage = 407
            }

            407 -> {
                player.getSavedData().questData.isTalkedDrezel = true
                end()
            }

            420 -> {
                options(
                    "Well, I'm going to look around a bit more.",
                    "Is there anything else interesting to do around here?",
                )
                stage = 421
            }

            421 ->
                when (buttonId) {
                    1 -> {
                        playerl(FaceAnim.FRIENDLY, "Well, I'm going to look around a bit more.")
                        stage++
                    }

                    2 -> {
                        playerl(FaceAnim.HALF_THINKING, "Is there anything else interesting to do around here?")
                        stage = 425
                    }
                }

            422 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, that sounds like a good idea. Don't get into any",
                    "trouble though!",
                )
                stage = 423
            }

            423 -> end()
            425 -> {
                npcl(
                    FaceAnim.HALF_THINKING,
                    "Well, not a great deal... but there is something you can do for me if you're interested. Though it is quite dangerous.",
                )
                stage++
            }

            426 -> {
                end()
                player.dialogueInterpreter.open(DrezelDialogueFile(), npc)
                player.dialogueInterpreter.handle(0, 0)
            }

            120 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I need " + getAttribute(player, "${GameAttributes.QUEST_PRIEST_IN_PERIL}:rune", 50) + " more.",
                )
                stage = 121
            }

            121 -> end()
            100 -> {
                npc(FaceAnim.HALF_GUILTY, "Quickly, give it to me!")
                stage = 101
            }

            101 -> {
                val requiredEssences = 50
                val amountBrought = getAttribute(player, "${GameAttributes.QUEST_PRIEST_IN_PERIL}:rune", 0)
                val remaining = (requiredEssences - amountBrought).coerceAtLeast(0)

                val runeEssence = player.inventory.getAmount(Items.RUNE_ESSENCE_1436)
                val pureEssence = player.inventory.getAmount(Items.PURE_ESSENCE_7936)
                val amount = runeEssence + pureEssence

                if (amount == 0 || remaining == 0) {
                    return true
                }

                val left = minOf(remaining, amount)
                var remove = left

                val removeRune = minOf(runeEssence, remove)
                val removePure = minOf(pureEssence, remove - removeRune)

                if (removeRune > 0) {
                    player.inventory.remove(Item(Items.RUNE_ESSENCE_1436, removeRune))
                }
                if (removePure > 0) {
                    player.inventory.remove(Item(Items.PURE_ESSENCE_7936, removePure))
                }

                val total = amountBrought + left
                setAttribute(player, "${GameAttributes.QUEST_PRIEST_IN_PERIL}:rune", total)

                sendMessage(player, "You give the priest your blank runes.")

                if (total >= requiredEssences) {
                    npc(FaceAnim.HAPPY, "Excellent! That should do it! I will bless these stones", "and place them within the well, and Misthalin should be", "protected once more!")
                    stage = 152
                } else {
                    end()
                }
            }

            152 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Please take this dagger; it has been handed down within",
                    "my family for generations and is filled with the power of",
                    "Saradomin. You will find that",
                )
                stage = 153
            }

            153 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "it has the power to prevent werewolves from adopting",
                    "their wolf form in combat as long as you have it",
                    "equipped.",
                )
                stage = 154
            }

            154 -> {
                finishQuest(player, Quests.PRIEST_IN_PERIL)
                end()
            }

            900 -> {
                player(FaceAnim.HALF_GUILTY, "Why, what happened?")
                stage = 901
            }

            901 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "From what I can tell, after you killed the guard dog",
                    "who protected the entrance to the monuments, those",
                    "Zamorakians forced the door into the main chamber",
                )
                stage = 902
            }

            902 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "and have used some kind of evil potion upon the well",
                    "which leads to the source of the river Salve. As they",
                    "have done this at the very mouth of the river",
                )
                stage = 903
            }

            903 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "it will spread along the entire river, disrupting the",
                    "blessing placed upon it and allowing the evil creatures of",
                    "Morytania to invade at their leisure.",
                )
                stage = 904
            }

            904 -> {
                player(FaceAnim.HALF_GUILTY, "What can we do to prevent that?")
                stage = 905
            }

            905 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, as you can see, I have placed a holy barrier on",
                    "the entrance to this room from the South, but it is not",
                    "very powerful and required me to remain",
                )
                stage = 906
            }

            906 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "here focussing upon it to keep it intact. Should an",
                    "attack come, they would be able to breach this defence",
                    "very quickly indeed. What we need to do is",
                )
                stage = 907
            }

            907 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "find some kind of way of removing or counteracting the",
                    "evil magic that has been put into the river source at the",
                    "well, so that the river will flow pure once again.",
                )
                stage = 908
            }

            908 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Couldn't you bless the river to purify it? Like you did",
                    "with the water I took from the well?",
                )
                stage = 909
            }

            909 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "No, that would not work, the power I have from",
                    "Saradomin is not great enough to cleanse an entire",
                    "river of this foul Zamorakian pollutant.",
                )
                stage = 910
            }

            910 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I have only one idea how we could possibly cleanse the",
                    "river.",
                )
                stage = 911
            }

            911 -> {
                player(FaceAnim.HALF_GUILTY, "What's that?")
                stage = 912
            }

            912 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I have heard rumours recently that Mages have found",
                    "some secret ore that absorbs magic into it and allows",
                    "them to create runes.",
                )
                stage = 913
            }

            913 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Should you be able to collect enough of this ore, it is",
                    "possible it will soak up the evil potion that has been",
                    "poured into the river, and purify it.",
                )
                stage = 914
            }

            914 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Kind of like a filter? Okay, I guess it's worth a try.",
                    "How many should I get?",
                )
                stage = 915
            }

            915 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, I have no knowledge of these ores other than",
                    "speculation and gossip, but if the things I hear are true",
                    "around fifty should be sufficient for the task.",
                )
                stage = 916
            }

            916 -> {
                quest.setStage(player, 18)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DREZEL_7707)
}
