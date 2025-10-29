package content.region.kandarin.feldip.jiggig.quest.zogre.dialogue

import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Vars

@Initializable
class BartenderDialogue(player: Player? = null) : Dialogue(player) {

    /**
     * Represents the internal dialogue flow branches for Bartender.
     */
    private enum class Flow {
        NONE,
        MAIN,
        TANKARD,
        TORN_PAGE,
        BLACK_PRISM,
        WRONG_PORTRAIT,
        CORRECT_PORTRAIT
    }

    private var flow = Flow.NONE

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val p = player ?: return false
        npc = NPC(NPCs.BARTENDER_739)

        if (stage == START_DIALOGUE) {
            flow = Flow.MAIN
            stage = 0
        }

        when (flow) {
            Flow.MAIN -> handleMain(interfaceId, buttonId)
            Flow.TANKARD -> handleTankard(interfaceId, buttonId)
            Flow.TORN_PAGE -> handleTornPage(interfaceId, buttonId)
            Flow.BLACK_PRISM -> handleBlackPrism(interfaceId, buttonId)
            Flow.WRONG_PORTRAIT -> handleWrongPortrait(interfaceId, buttonId)
            Flow.CORRECT_PORTRAIT -> handleCorrectPortrait(interfaceId, buttonId)
            else -> end()
        }
        return true
    }

    override fun getIds() = intArrayOf(NPCs.BARTENDER_739)

    /**
     * Main bartender dialogue.
     */
    private fun handleMain(componentID: Int, buttonID: Int) {
        val p = player!!
        val hasTankard = inInventory(p, Items.DRAGON_INN_TANKARD_4811)
        val hasTornPage = inInventory(p, Items.TORN_PAGE_4809)
        val hasBlackPrism = inInventory(p, Items.BLACK_PRISM_4808)
        val hasPortraitWrong = inInventory(p, ZogreUtils.UNREALIST_PORTRAIT)
        val hasPortraitCorrect = inInventory(p, ZogreUtils.REALIST_PORTRAIT)

        flow = when {
            hasTankard -> Flow.TANKARD
            hasTornPage -> Flow.TORN_PAGE
            hasBlackPrism -> Flow.BLACK_PRISM
            hasPortraitWrong -> Flow.WRONG_PORTRAIT
            hasPortraitCorrect -> Flow.CORRECT_PORTRAIT
            else -> Flow.MAIN
        }

        if (flow == Flow.MAIN) {
            when (stage) {
                0 -> npc(FaceAnim.HALF_ASKING, "What can I get you?").also { stage++ }
                1 -> player("What's on the menu?").also { stage++ }
                2 -> npc("Dragon Bitter and Greenman's Ale, oh and some cheap beer.").also { stage++ }
                3 -> options("I'll give it a miss I think.", "I'll try the Dragon Bitter.", "Can I have some Greenman's Ale?", "One cheap beer please!").also { stage++ }
                4 -> when (buttonID) {
                    1 -> player("I'll give it a miss I think.").also { stage++ }
                    2 -> player("I'll try the Dragon Bitter.").also { stage = 6 }
                    3 -> player("Can I have some Greenman's Ale?").also { stage = 8 }
                    4 -> player("One cheap beer please!").also { stage = 10 }
                }
                5 -> npc("Come back when you're a little thirstier.").also { stage = END_DIALOGUE }
                6 -> npc("Ok, that'll be two coins.").also { stage++ }
                7 -> {
                    end()
                    if (amountInInventory(player, Items.COINS_995) >= 2) {
                        player.inventory.remove(Item(Items.COINS_995, 2))
                        sendMessage(player, "You buy a pint of Dragon Bitter.")
                        addItemOrDrop(player, Items.DRAGON_BITTER_1911)
                    } else {
                        sendMessage(player, "You don't have enough coins.")
                    }
                }
                8 -> npc("Ok, that'll be ten coins.").also { stage++ }
                9 -> {
                    end()
                    if (amountInInventory(player, Items.COINS_995) >= 10) {
                        player.inventory.remove(Item(Items.COINS_995, 10))
                        sendMessage(player, "You buy a pint of Greenman's Ale.")
                        addItemOrDrop(player, Items.GREENMANS_ALE_1909)
                    } else {
                        sendMessage(player, "You don't have enough coins.")
                    }
                }
                10 -> npc("That'll be 2 gold coins please!").also { stage++ }
                11 -> {
                    if (amountInInventory(player, Items.COINS_995) >= 2) {
                        player.inventory.remove(Item(Items.COINS_995, 2))
                        sendDialogue(player, "You buy a pint of cheap beer.")
                        addItemOrDrop(player, Items.BEER_1917)
                        stage = 12
                    } else {
                        end()
                        sendMessage(player, "You don't have enough coins.")
                    }
                }

                12 -> npc(FaceAnim.HAPPY, "Have a super day!").also { stage = END_DIALOGUE }
            }
        } else {
            stage = 0
        }
    }

    private fun handleTankard(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendItemDialogue(player!!, Items.DRAGON_INN_TANKARD_4811, "You show the tankard to the Inn Keeper.").also {
                stage++
            }
            1 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_TANKARD_AGAIN, false)) {
                player("Hello again. Can you tell me what you know about", "this tankard again please?").also { stage = 10 }
            } else {
                player("Hello there, I found this tankard in an ogre tomb", "cavern. It has the emblem of this Inn on it and I", "wondered if you knew anything about it?").also { stage++ }
            }
            2 -> npc("Oh yes, this is Brentle's mug...I'm surprised he left it", "just lying around down some cave. He's quite protective", "of it.").also { stage++ }
            3 -> player("Brentle you say? So you knew him then?").also { stage++ }
            4 -> npc("Yeah, this belongs to 'Brentle Vahn', he's quite a", "common customer, though I've not seen him in a while.").also { stage++ }
            5 -> npc("He was talking to some shifty looking wizard the other", "day. I don't know his name, but I'd recognise him if I", "saw him.").also { stage++ }
            6 -> player("Hmm, I'm sorry to tell you this, but Brentle Vahn is", "dead - I believe he was murdered.").also { stage++ }
            7 -> npc(FaceAnim.SCARED, "Noooo! I'm shocked...").also { stage++ }
            8 -> npc("...but not surprised. He was a good customer...but I", "knew he would sell his sword arm and do many a dark", "deed if paid enough.").also { stage++ }
            9 -> npc("If you need help bringing the culprit to justice, you let", "me know.").also { stage = END_DIALOGUE }
            10 -> npc("Oh yes, Brentle's tankard. Yeah, you've shown me this", "already. It belonged to Brentle Vahn, he was quite a", "common customer, though I've not seen him in a while.").also { stage++ }
            11 -> npc("He was talking to some shifty looking wizard the other", "day. I don't know his name, but I'd recognise him if", "I saw him.").also {
                setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 4, true)
                stage = END_DIALOGUE
            }
        }
    }

    private fun handleBlackPrism(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendItemDialogue(player!!, Items.BLACK_PRISM_4808, "You show the bar tender the black prism.").also { stage++ }
            1 -> player("Hello there, I found this black prism,", "I wondered if you knew anything", "about it.").also { stage++ }
            2 -> npc("Hmmm, it's not really familiar to me, sorry", "I don't know what it's for, looks magical to me...maybe someone else in", "Yanille can help you?").also { stage = END_DIALOGUE }
        }
    }

    private fun handleTornPage(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendItemDialogue(player!!, Items.TORN_PAGE_4809, "You show the bar tender the torn page.").also { stage++ }
            1 -> player("Do you have any clue what this might be?").also { stage++ }
            2 -> npc("Oooh, don't show me that sort of stuff, it's probably all magical", "and wizardy, probably turn me into a frog as soon as I look", "at it...").also { stage = END_DIALOGUE }
        }
    }

    private fun handleWrongPortrait(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendItemDialogue(player!!, ZogreUtils.UNREALIST_PORTRAIT, "You show the sketch to the Inn keeper.").also { stage++ }
            1 -> npcl(FaceAnim.HALF_ASKING, "Who's that? I mean, I guess it's a picture of a person isn't it? Sorry...you've got me? And before you ask, you're not putting it up on my wall!").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "It's a portrait of Sithik Ints...don't you recognise him?").also { stage++ }
            3 -> npcl(FaceAnim.HALF_GUILTY, "I'm sorry, I really am, but I just don't see it...can you make a better picture?").also { stage++ }
            4 -> playerl(FaceAnim.NEUTRAL, "I'll try...").also { stage = END_DIALOGUE }
        }
    }

    private fun handleCorrectPortrait(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendItemDialogue(player!!, ZogreUtils.REALIST_PORTRAIT, "You show the portrait to the Inn keeper.").also { stage++ }
            1 -> npc("Yeah, that's the guy who was talking to Brentle Vahn", "the other day! Look at those eyes, never a more shifty", "looking pair will you ever see!").also { stage++ }
            2 -> player("Hmm, you've just identified the man who I think sent", "Brentle Vahn to his death.").also { stage++ }
            3 -> player("I'm trying to bring him to justice with the wizards", "guild grand secretary. Do you think you could sign", "this portrait to say that he was talking to Brentle Vahn.").also { stage++ }
            4 -> npcl(FaceAnim.HAPPY, "I can and I will!").also {
                removeItem(player!!, ZogreUtils.REALIST_PORTRAIT)
                addItem(player!!, ZogreUtils.SIGNED_PORTRAIT)
                stage++
            }
            5 -> sendItemDialogue(player!!, ZogreUtils.SIGNED_PORTRAIT, "The Dragon Inn bartender signs the portrait.").also { stage++ }
            6 -> player("Many thanks for your help, it's really very good of", "you.").also { stage++ }
            7 -> npc("Not at all, just doing my part.").also {
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_SIGN_PORTRAIT, true)
                stage = END_DIALOGUE
            }
        }
    }
}
