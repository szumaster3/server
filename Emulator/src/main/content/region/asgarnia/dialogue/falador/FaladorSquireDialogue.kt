package content.region.asgarnia.dialogue.falador

import content.region.asgarnia.dialogue.FaladorSquireDiaryDialogue
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FaladorSquireDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        sendDialogueOptions(player, "What do you want to do?", "Chat", "Talk about the Falador Achievement Diary")
        stage = -1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if ((stage == -1 && buttonId == 2) || Diary.canReplaceReward(player, DiaryType.FALADOR, 2)) {
            openDialogue(player, FaladorSquireDiaryDialogue(), npc)
        } else {
            when (getQuestStage(player, Quests.THE_KNIGHTS_SWORD)) {
                100 ->
                    when (stage) {
                        -1 ->
                            npc(
                                "Hello friend! Many thanks for all of your help! Vyvin",
                                "never even realised it was a different sword, and I still",
                                "have my job!",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                    }

                60 ->
                    when (stage) {
                        -1 -> npc("So how are you doing getting a sword?").also { stage = 0 }
                        0 -> {
                            if (!inInventory(player, Items.BLURITE_SWORD_667)) {
                                player(
                                    "I've found a dwarf who will make the sword, I've just",
                                    "got to find the materials for it now!",
                                ).also {
                                    stage =
                                        END_DIALOGUE
                                }
                            } else {
                                player("I have retrieved your sword for you.").also { stage = 2 }
                            }
                        }
                        2 ->
                            npc(
                                "Thank you, thank you, thank you! I was seriously",
                                "worried I would have to own up to Sir Vyvin!",
                            ).also {
                                stage++
                            }
                        3 -> sendDialogue(player!!, "You give the sword to the squire.").also { stage++ }
                        4 -> {
                            if (removeItem(player, Item(Items.BLURITE_SWORD_667))) {
                                finishQuest(player, Quests.THE_KNIGHTS_SWORD)
                                end()
                            }
                        }
                    }

                50 ->
                    when (stage) {
                        -1 -> npc("So how are you doing getting a sword?").also { stage = 0 }
                        0 -> {
                            if (!inInventory(player, Items.PORTRAIT_666, 1)) {
                                player("I didn't get the picture yet...").also { stage = 1 }
                            } else {
                                player("I have the picture, I'll just take it to the dwarf now!").also { stage = 3 }
                            }
                        }

                        1 ->
                            npc("Please try and get it quickly... I am scared Sir Vyvin", "will find out!").also {
                                stage = END_DIALOGUE
                            }

                        3 -> npc("Please hurry!").also { stage = END_DIALOGUE }
                    }

                40 ->
                    when (stage) {
                        -1 -> npc("So how are you doing getting a sword?").also { stage = 0 }
                        0 ->
                            player(
                                "I have found an Imcando dwarf but he needs a picture",
                                "of the sword before he can make it.",
                            ).also {
                                stage++
                            }
                        1 ->
                            npc(
                                "A picture eh? Hmmm.... The only one I can think of is",
                                "in a small portrait of Sir Vyvin's father... Sir Vyvin",
                                "keeps it in a cupboard in his room I think.",
                            ).also {
                                stage++
                            }
                        2 -> player("Ok, I'll try to get that then.").also { stage++ }
                        3 -> npc("Please don't let him catch you! He MUSTN'T know", "what happened!").also { stage++ }
                        4 -> {
                            setQuestStage(player, Quests.THE_KNIGHTS_SWORD, 50)
                            end()
                        }
                    }

                30 ->
                    when (stage) {
                        -1 -> npc("So how are you doing getting a sword?").also { stage = 0 }
                        0 ->
                            player(
                                "I have found an Imcando Dwarf named Thurgo!",
                                "I have given him Redberry pie, I hope he will",
                                "help me now.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                    }

                20, 10 ->
                    when (stage) {
                        -1 -> npc("So how are you doing getting a sword?").also { stage = 0 }
                        0 -> player("I'm still looking for Imcando dwarves to help me...").also { stage++ }
                        1 ->
                            npc("Please try and find them quickly... I am scared Sir", "Vyvin will find out!").also {
                                stage =
                                    END_DIALOGUE
                            }
                    }

                0 ->
                    when (stage) {
                        -1 -> npc("Hello. I am the squire to Sir Vyvin.").also { stage = 0 }
                        0 ->
                            options(
                                "And how is life as a squire?",
                                "Wouldn't you prefer to be a squire for me?",
                            ).also { stage++ }

                        1 ->
                            when (buttonId) {
                                1 -> player("And how is life as a squire?").also { stage = 10 }
                                2 -> player("Wouldn't you prefer to be a squire for me?").also { stage = 20 }
                            }

                        10 ->
                            npc(
                                "Well, Sir Vyvin is a good guy to work for, however,",
                                "I'm in a spot of trouble today. I've gone and lost Sir",
                                "Vyvin's sword!",
                            ).also {
                                stage++
                            }

                        11 ->
                            options(
                                "Do you know where you lost it?",
                                "I can make a new sword if you like...",
                                "Is he angry?",
                            ).also {
                                stage++
                            }

                        12 ->
                            when (buttonId) {
                                1 -> player("Do you know where you lost it?").also { stage = 100 }
                                2 -> player("I can make a new sword if you like...").also { stage = 120 }
                                3 -> player("Is he angry?").also { stage = 130 }
                            }
                        20 -> npc("No, sorry, I'm loyal to Sir Vyvin.").also { stage = END_DIALOGUE }
                        100 ->
                            npc("Well now, if I knew THAT it wouldn't be lost, now", "would it?").also {
                                stage =
                                    END_DIALOGUE
                            }
                        120 -> npc("Thanks for the offer. I'd be surprised if you could", "though.").also { stage++ }
                        121 ->
                            npc(
                                "The thing is, this sword is a family heirloom. It has been",
                                "passed down through Vyvin's family for five",
                                "generations! It was originally made by the Imcando",
                                "dwarves, who were",
                            ).also {
                                stage++
                            }
                        122 ->
                            npc(
                                "a particularly skilled tribe of dwarven smiths. I doubt",
                                "anyone could make it in the style they do.",
                            ).also {
                                stage++
                            }
                        123 ->
                            options("So would these dwarves make another one?", "Well I hope you find it soon.").also {
                                stage =
                                    134
                            }
                        134 ->
                            when (buttonId) {
                                1 -> player("So would these dwarves make another one?").also { stage = 160 }
                                2 -> player("Well I hope you find it soon.").also { stage = 170 }
                            }
                        160 ->
                            npc(
                                "I'm not a hundred percent sure the Imcando tribe",
                                "exists anymore. I should think Reldo, the palace",
                                "librarian in Varrock, will know; he has done a lot of",
                                "research on the races of " + settings!!.name + ".",
                            ).also { stage++ }
                        161 ->
                            npc(
                                "I don't suppose you could try and track down the",
                                "Imcando dwarves for me? I've got so much work to",
                                "do...",
                            ).also {
                                stage++
                            }
                        162 ->
                            options(
                                "Ok, I'll give it a go.",
                                "No, I've got lots of mining work to do.",
                            ).also { stage++ }
                        163 ->
                            when (buttonId) {
                                1 -> player("Ok, I'll give it a go.").also { stage = 165 }
                                2 -> player("No, I've got lots of mining work to do.").also { stage++ }
                            }
                        164 -> npc("Oh man... I'm in such trouble...").also { stage = END_DIALOGUE }
                        165 -> {
                            npc("Thank you very much! As I say, the best place to start", "should be with Reldo...")
                            setQuestStage(player, Quests.THE_KNIGHTS_SWORD, 10)
                            player.getQuestRepository().syncronizeTab(player)
                            end()
                        }
                        170 ->
                            npc(
                                "Yes, me too. I'm not looking forward to telling Vyvin",
                                "I've lost it. He's going to want it for the parade next",
                                "week as well.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        130 ->
                            npc(
                                "He doesn't know yet. I was hoping I could think of",
                                "something to do before he does find out, But I find",
                                "myself at a loss.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                    }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SQUIRE_606)
    }
}
