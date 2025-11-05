package content.region.kandarin.ardougne.east.dialogue.market

import content.global.activity.enchkey.EnchKeyUtils
import content.region.kandarin.gnome.quest.makinghistory.MHUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class SilverMerchantDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Silver! Silver! Best prices for buying and selling in all", "Kandarin!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val hasKey = hasAnItem(player, Items.ENCHANTED_KEY_6754).container != null
        val hasJournal = hasAnItem(player, Items.JOURNAL_6755).container == player.inventory
        val questStage = getQuestStage(player, Quests.MAKING_HISTORY)
        val erinProgress = getVarbit(player, MHUtils.ERIN_PROGRESS)

        when (stage) {
            0 -> {
                if (questStage >= 1) {
                    options("Yes please.", "No, thank you.", "Ask about the outpost.").also { stage = 2 }
                } else if (isQuestComplete(player, Quests.MAKING_HISTORY)) {
                    npc("Hello, I hope Jorral was pleased with that Journal.").also { stage = 4 }
                } else {
                    options("Yes please.", "No, thank you.").also { stage++ }
                }
            }

            1 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.SILVER_MERCHANT_569)
                }

                2 -> player(FaceAnim.NEUTRAL, "No, thank you.").also { stage = END_DIALOGUE }
            }

            2 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.SILVER_MERCHANT_569)
                }
                2 -> player(FaceAnim.NEUTRAL, "No, thank you.").also { stage = END_DIALOGUE }
                3 -> {
                    end()
                    when {
                        questStage < 2 -> openDialogue(player, SilverMerchantDialogueExtension(0))
                        erinProgress == 1 || !hasKey -> openDialogue(player, SilverMerchantDialogueExtension(13))
                        inInventory(player, Items.CHEST_6759) -> openDialogue(player, SilverMerchantDialogueExtension(19))
                        erinProgress in 1..3 && !hasJournal -> player("I found a chest, but I lost it and any contents", "it had.").also { stage++ }
                        inInventory(player, Items.JOURNAL_6755) -> openDialogue(player, SilverMerchantDialogueExtension(21))
                        else -> npc("Hello, I hope Jorral was pleased with that Journal.").also { stage = 4 }
                    }
                }
            }
            3 -> npc("Well I suggest you go back to where you found it.").also { stage = END_DIALOGUE }
            4 -> npc("I'm sure it's been a valuable find.").also { stage = END_DIALOGUE }
            5 -> npc("I'm sure it's been a valuable find.").also { stage++ }
            6 -> {
                if (hasAnItem(player!!, Items.ENCHANTED_KEY_6754).exists()) {
                    end()
                    npc(
                        "You know you can use that enchanted key you have on",
                        "your keyring all over Gielinor. Who knows what you might find?",
                    )
                } else if (getAttribute(player!!, EnchKeyUtils.ENCHANTED_KEY_ATTR, 0) == 11) {
                    player("Oh, You know that key you gave me?").also { stage = 11 }
                } else {
                    player("What I came to ask was: I lost that key you gave me.").also { stage++ }
                }
            }

            7 -> npc("Oh dear, luckily I found it, but it'll cost you 500gp", "as I know it's valuable.").also { stage++ }
            8 -> options("Yes please.", "No thanks.").also { stage++ }
            9 -> when (buttonId) {
                1 -> player("Yes please.").also { stage++ }
                2 -> player("No thanks.").also { stage = END_DIALOGUE }
            }

            10 -> {
                end()
                if (freeSlots(player!!) < 0) {
                    npc("You don't have the space to carry it! Empty some space", "in your inventory.")
                    return true
                }
                if (!removeItem(player!!, Item(Items.COINS_995, 500))) {
                    npc("You don't have enough money, sorry.")
                } else {
                    npc("Thank you, enjoy!")
                    setAttribute(player!!, EnchKeyUtils.ENCHANTED_KEY_ATTR, 0)
                    addItemOrDrop(player!!, Items.ENCHANTED_KEY_6754, 1)
                }
            }

            11 -> npc("Yes?").also { stage++ }
            12 -> player("It dissolved!").also { stage++ }
            13 -> npc("Really? I suppose it served its purpose.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SilverMerchantDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SILVER_MERCHANT_569)
}


private class SilverMerchantDialogueExtension(override var stage: Int) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SILVER_MERCHANT_569)
        when (stage) {
            0 -> playerl("Are you Erin?").also { stage++ }
            1 -> npcl("That I am. What do you want? You realise I'm working here.").also { stage++ }
            2 -> playerl("This shouldn't take too long. I just wanted to ask you a bit about your great Grandfather, the one who lived in the outpost?").also { stage++ }
            3 -> npcl("What's it to you?").also { stage++ }
            4 -> playerl("I'm doing some research for a man called Jorral. Apparently the King is going to make the outpost into his very own alchemists' lab.").also { stage++ }
            5 -> npcl("That can only cause chaos! Well, my great-grandfather lived and died there according to my mother, but even she knows very little about him.").also { stage++ }
            6 -> playerl("I see.").also { stage++ }
            7 -> npcl("The only thing I have of his is a key. It's a strange key, it changes temperature by itself as you walk around. I'm afraid I don't know what it's for, though.").also { stage++ }
            8 -> playerl("No idea at all?").also { stage++ }
            9 -> npcl("Well, I imagine it's to some sort of chest of his belongings as it's too small for a door. Perhaps if you find some of his belongings you will discover some clues amongst them.").also { stage++ }
            10 -> playerl("It's better than nothing. Will you lend it to me then?").also { stage++ }
            11 -> npcl("Why not, I can't use it anyway. Try feeling its change in temperature as you walk around.").also { stage++ }
            12 -> {
                end()
                addItemOrDrop(player!!, Items.ENCHANTED_KEY_6754, 1)
                setVarbit(player!!, MHUtils.ERIN_PROGRESS, 1, true)
                setQuestStage(player!!, Quests.MAKING_HISTORY, 2)
            }

            13 -> playerl("I haven't found anything yet.").also { stage++ }
            14 -> npcl("Keep trying. Have you noticed that the key changes temperature as you walk around?").also { stage++ }
            15 -> if (inInventory(player!!, Items.ENCHANTED_KEY_6754)) {
                player("I was just about to. Do you want the key back?").also { stage = 18 }
            } else {
                playerl("That's kind of hard because I lost the key.").also { stage++ }
            }

            16 -> npcl("I was waiting for you to say that...because I just found it! Take it and don't lose it!").also { stage++ }
            17 -> if (freeSlots(player!!) == 0) {
                end()
                npcl("Wait, look how much you're carrying! You can't carry this too.")
            } else {
                end()
                addItemOrDrop(player!!, Items.ENCHANTED_KEY_6754, 1)
            }

            18 -> npc("Keep it, it may come in handy.").also { stage = END_DIALOGUE }
            19 -> playerl("I found a chest!").also { stage++ }
            20 -> npcl("I wonder what is inside.").also { stage = END_DIALOGUE }
            21 -> playerl("Would you like to see the journal I have found?").also { stage++ }
            22 -> npcl("Let's have a look.").also { stage++ }
            23 -> sendDoubleItemDialogue(player!!, -1, Items.JOURNAL_6755, "Erin reads the journal.").also { stage++ }
            24 -> npcl("Very interesting. Best you show it to Jorral at the outpost.").also { stage = END_DIALOGUE }
        }
    }
}
