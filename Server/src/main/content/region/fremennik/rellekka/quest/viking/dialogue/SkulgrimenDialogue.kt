package content.region.fremennik.rellekka.quest.viking.dialogue

import content.data.GameAttributes
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Skulgrimen dialogue.
 *
 * # Relations
 * - [FremennikTrials]
 */
@Initializable
class SkulgrimenDialogue(player: Player? = null) : Dialogue(player) {

    private var selectedArmour: RockShellArmour? = null

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when {
            inInventory(player, Items.UNUSUAL_FISH_3703, 1) -> {
                playerl(FaceAnim.HAPPY, "Hi there. I got your fish, so can I have that bowstring for Sigli now?")
                stage = 20
            }

            inInventory(player, Items.CUSTOM_BOW_STRING_3702, 1) -> {
                playerl(FaceAnim.ASKING, "So about this bowstring... was it hard to make or something?")
                stage = 25
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_RETURN, false) -> {
                playerl(FaceAnim.ASKING, "Is this trade item for you?")
                stage = 26
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 7 -> {
                playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find an exotic and extremely odd fish, do you?")
                stage = 15
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 6 -> {
                playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find a finely balanced custom bowstring, do you?")
                stage = 1
            }

            isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) -> {
                npcl(FaceAnim.HAPPY, "Hello again, ${FremennikTrials.getFremennikName(player)}. Come to see what's for sale?")
                stage = 101
            }

            else -> {
                playerl(FaceAnim.HAPPY, "Hello!")
                stage = 100
            }
        }
        // showTopics(
        //     IfTopic("Hi there. I got your fish, so can I have that bowstring for Sigli now?", 20, inInventory(player, Items.UNUSUAL_FISH_3703, 1)),
        //     IfTopic("So about this bowstring... was it hard to make or something?", 25, inInventory(player, Items.CUSTOM_BOW_STRING_3702, 1)),
        //     IfTopic("Is this trade item for you?", 26, getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_RETURN, false)),
        //     IfTopic("I don't suppose you have any idea where I could find an exotic and extremely odd fish, do you?", 15, getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 7),
        //     IfTopic("I don't suppose you have any idea where I could find a finely balanced custom bowstring, do you?", 1, getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 6),
        //     npcl(FaceAnim.HAPPY, "Hello again, ${FremennikTrials.getFremennikName(player)}. Come to see what's for sale?")
        //)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            1 -> npcl(FaceAnim.THINKING, "Aye, I have a few in stock. What would an outerlander be wanting with equipment like that?").also { stage++ }
            2 -> playerl(FaceAnim.HAPPY, "It's for Sigli. It needs to be weighted precisely to suit his hunting bow.").also { stage++ }
            3 -> npcl(FaceAnim.HAPPY, "For Sigli eh? Well, I made his bow in the first place, so I'll be able to select the right string for you... just one small problem.").also { stage++ }
            4 -> playerl(FaceAnim.THINKING, "What's that?").also { stage++ }
            5 -> npcl(FaceAnim.THINKING, "This string you'll be wanting... Very special it is. Take a lot of time to recreate. Not sure you have the cash for it.").also { stage++ }
            6 -> playerl(FaceAnim.THINKING, "Then maybe you'll accept something else...?").also { stage++ }
            7 -> npcl(FaceAnim.HAPPY, "Heh. Good thinking outerlander. Well, it's true, there is more to life than just making money. Making weapons is good money, but it's not why I do it. I'll tell you what.").also { stage++ }
            8 -> npcl(FaceAnim.HAPPY, "I heard a rumour that one of the fishermen down by the docks caught some weird looking fish as they were fishing the other day. From what I hear this fish is unique.").also { stage++ }
            9 -> npcl(FaceAnim.HAPPY, "From what I hear this fish is unique. Nobody's ever seen its like before. This intrigues me. I'd like to have it for myself.").also { stage++ }

            10 -> npcl(FaceAnim.HAPPY, "Make a good trophy. You get me that fish, I give you the bowstring. What do you say? We got a deal?").also { stage++ }
            11 -> playerl(FaceAnim.HAPPY, "Sounds good to me.").also {
                player.incrementAttribute(GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 1)
                stage = END_DIALOGUE
            }
            15 -> npcl(FaceAnim.EXTREMELY_SHOCKED, "What? There's another one?").also { stage++ }
            16 -> playerl(FaceAnim.ANNOYED, "Er... no, it's the one for you that I'm looking for...").also { stage++ }
            17 -> npcl(FaceAnim.ANNOYED, "Ah. I see. I already told you. Some guy down by the docks was bragging. Best ask there, I reckon.").also { stage = END_DIALOGUE }
            20 -> npcl(FaceAnim.HAPPY, "Ohh... That's a nice fish. Very pleased. Here. Take the bowstring. You fulfilled agreement. Only fair I do same. Good work outerlander.").also {
                removeItem(player, Items.UNUSUAL_FISH_3703)
                addItemOrDrop(player, Items.CUSTOM_BOW_STRING_3702, 1)
                stage++
            }

            21 -> playerl(FaceAnim.HAPPY, "Thanks!").also { stage = END_DIALOGUE }
            25 -> npcl(FaceAnim.HAPPY, "Not hard. Just a trick to it. Takes skill to learn, but when learnt, easy. Sigli will be happy. Finest bowstring on continent. Will suit his needs perfectly.").also { stage = END_DIALOGUE }
            26 -> npcl(FaceAnim.ANNOYED, "Not for me, I'm afraid.").also { stage = END_DIALOGUE }

            100 -> npcl(FaceAnim.HAPPY, "Sorry. I can't sell weapons to outlanders. Wouldn't be right. Against our beliefs.").also { stage = END_DIALOGUE }

            101 -> {
                if (anyInInventory(player, Items.DAGANNOTH_HIDE_6155, Items.ROCK_SHELL_CHUNK_6157, Items.ROCK_SHELL_SHARD_6159, Items.ROCK_SHELL_SPLINTER_6161)) {
                    setTitle(player, 2)
                    sendOptions(player, "What would you like to ask about?", "Rock Crab Armour", "Show me what you've got.").also { stage++ }
                } else {
                    end()
                    openNpcShop(player, NPCs.SKULGRIMEN_1303)
                }
            }

            102 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "Hello there. I have these shards of rock crab shell, and believe you might be able to make them into armour for me...?").also { stage = 103 }
                2 -> { end(); openNpcShop(player, NPCs.SKULGRIMEN_1303) }
            }

            103 -> npcl(FaceAnim.FRIENDLY, "No problem, ${FremennikTrials.getFremennikName(player)}. What armour do you want?").also { stage = 104 }

            104 -> options("A fine helm", "Sturdy bodyarmour", "Powerful leg armour", "Nothing").also { stage = 105 }

            105 -> {
                selectedArmour = when (buttonId) {
                    1 -> RockShellArmour.HELM
                    2 -> RockShellArmour.TOP
                    3 -> RockShellArmour.LEGS
                    4 -> null
                    else -> null
                }

                if (selectedArmour != null) {
                    npcl(FaceAnim.HALF_ASKING, "It'll cost ${selectedArmour!!.coinAmount} coins. That ok?").also { stage = 106 }
                } else {
                    npcl(FaceAnim.FRIENDLY, "As you wish.").also { stage = END_DIALOGUE }
                }
            }

            106 -> options("YES", "NO").also { stage = 107 }
            107 -> when (buttonId) {
                1 -> {
                    val armour = selectedArmour!!
                    val hasMaterials = player!!.inventory.containsItems(
                        Item(Items.COINS_995, armour.coinAmount),
                        Item(Items.DAGANNOTH_HIDE_6155, armour.hideAmount),
                        Item(armour.shellItem, 1)
                    )

                    if (!hasMaterials) {
                        npcl(FaceAnim.HALF_GUILTY, "Sorry. You need ${armour.hideAmount} daggermouth hides, 1 ${getItemName(armour.shellItem).lowercase()}, and ${armour.coinAmount} coins. Come back when you got it.").also { stage = END_DIALOGUE }
                    } else {
                        removeItem(player!!, Item(Items.COINS_995, armour.coinAmount))
                        removeItem(player!!, Item(Items.DAGANNOTH_HIDE_6155, armour.hideAmount))
                        removeItem(player!!, Item(armour.shellItem, 1))
                        addItemOrDrop(player!!, armour.product)
                        npcl(FaceAnim.FRIENDLY, "There you go. ${armour.name.lowercase().replace('_',' ')}. You want another?").also { stage = 104 }
                    }
                }
                2 -> {
                    npcl(FaceAnim.FRIENDLY, "As you wish. What kind of armour do you want?").also { stage = 104 }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SKULGRIMEN_1303)
}

private enum class RockShellArmour(val product: Int, val hideAmount: Int, val shellItem: Int, val coinAmount: Int) {
    HELM(Items.ROCK_SHELL_HELM_6128, 1, Items.ROCK_SHELL_CHUNK_6157, 5000),
    TOP(Items.ROCK_SHELL_PLATE_6129, 3, Items.ROCK_SHELL_SHARD_6159, 10000),
    LEGS(Items.ROCK_SHELL_LEGS_6130, 2, Items.ROCK_SHELL_SPLINTER_6161, 7500)
}
