package content.region.kandarin.feldip.jiggig.quest.zogre.dialogue

import content.data.GameAttributes
import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class ZavisticRarveDialogue(player: Player? = null) : Dialogue(player) {

    /**
     * Represents the internal dialogue flow branches for Zavistic Rarve.
     */
    private enum class Flow {
        NONE,
        MAIN,
        DEFAULT_AFTER_QUEST,
        QUEST,
        HAS_BOTH_ITEMS,
        TANKARD,
        TORN_PAGE,
        BLACK_PRISM,
        POTION,
        POST_QUEST,
        SELL_BLACK_PRISM,
        RETURNING_CLARENCE
    }

    private var flow = Flow.NONE

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val p = player ?: return false
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)

        if (stage == START_DIALOGUE) {
            flow = if (isQuestComplete(p, Quests.ZOGRE_FLESH_EATERS)) Flow.DEFAULT_AFTER_QUEST else Flow.MAIN
            stage = 0
        }

        when (flow) {
            Flow.MAIN -> handleMainFiles(interfaceId, buttonId)
            Flow.DEFAULT_AFTER_QUEST -> handleDefaultAfterQuest(interfaceId, buttonId)
            Flow.QUEST -> handleDefaultQuest(interfaceId, buttonId)
            Flow.HAS_BOTH_ITEMS -> handleHasBoth(interfaceId, buttonId)
            Flow.TANKARD -> handleTankard(interfaceId, buttonId)
            Flow.TORN_PAGE -> handleTornPage(interfaceId, buttonId)
            Flow.BLACK_PRISM -> handleBlackPrism(interfaceId, buttonId)
            Flow.POTION -> handlePotion(interfaceId, buttonId)
            Flow.POST_QUEST -> handleLast(interfaceId, buttonId)
            Flow.SELL_BLACK_PRISM -> handleSellBlackPrism(interfaceId, buttonId)
            Flow.RETURNING_CLARENCE -> handleMiniquestReturningClarence(interfaceId, buttonId)
            else -> end()
        }
        return true
    }

    override fun getIds() = intArrayOf(NPCs.ZAVISTIC_RARVE_2059)

    /**
     * Main entry dialogue.
     */
    private fun handleMainFiles(componentID: Int, buttonID: Int) {
        val p = player!!
        val questComplete = getVarbit(p, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13 || getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) == 100
        val hasBlackPrism = inInventory(p, Items.BLACK_PRISM_4808) && !getAttribute(p, ZogreUtils.TALK_ABOUT_BLACK_PRISM, false)
        val hasTornPage = inInventory(p, Items.TORN_PAGE_4809) && !getAttribute(p, ZogreUtils.TALK_ABOUT_TORN_PAGE, false)
        val hasTankard = inInventory(p, Items.DRAGON_INN_TANKARD_4811) && !getAttribute(p, ZogreUtils.TALK_ABOUT_TANKARD, false)
        val hasBlackPrismAndTornPage = hasBlackPrism && hasTornPage && !getAttribute(p, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false)
        val hasOrLostStrangePotion = getAttribute(p, ZogreUtils.TALK_WITH_ZAVISTIC_DONE, false)
        val hasTalkWithSithik = getAttribute(p, ZogreUtils.TALK_WITH_SITHIK_OGRE_DONE, false)
        val hasReqForMiniquest = getVarbit(p, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13 || getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) == 100 && hasRequirement(player, Quests.THE_HAND_IN_THE_SAND, false)

        when (stage) {
            0 -> {
                if (questComplete && inInventory(p, Items.BLACK_PRISM_4808)) {
                    flow = Flow.SELL_BLACK_PRISM
                    stage = 0
                } else {
                    npcl(FaceAnim.FRIENDLY, "What are you doing...Oh, it's you...sorry...didn't realise... what can I do for you?")
                    stage++
                }
            }

            1 -> {
                if (hasTalkWithSithik) {
                    flow = Flow.POST_QUEST
                    stage = 0
                } else if (hasReqForMiniquest) {
                    flow = Flow.RETURNING_CLARENCE
                    stage = 0
                } else {
                    playerl(FaceAnim.FRIENDLY, "But I was told to ring the bell if I wanted some attention.")
                    stage++
                }

            }

            2 -> {
                npcl(FaceAnim.FRIENDLY, "Well...anyway...we're very busy here, hurry up what do you want?")
                stage++
            }

            3 -> {
                val nextFlow = when {
                    hasBlackPrismAndTornPage -> Flow.HAS_BOTH_ITEMS
                    hasBlackPrism -> Flow.BLACK_PRISM
                    hasTornPage -> Flow.TORN_PAGE
                    hasTankard -> Flow.TANKARD
                    questComplete -> Flow.DEFAULT_AFTER_QUEST
                    hasOrLostStrangePotion -> Flow.POTION
                    else -> Flow.QUEST
                }
                flow = nextFlow
                stage = 0
            }
        }
    }

    /**
     * Handles default dialogue after completing the quest.
     */
    private fun handleDefaultAfterQuest(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> options(
                "What is there to do in the Wizards' Guild?",
                "What are the requirements to get in the Wizards' Guild?",
                "What do you do in the Guild?",
                "Ok, thanks."
            ).also { stage++ }

            1 -> when (buttonID) {
                1 -> playerl(FaceAnim.HALF_GUILTY, "What is there to do in the Wizards' Guild?").also { stage++ }
                2 -> playerl(FaceAnim.HALF_GUILTY, "What are the requirements to get in the Wizards' Guild?").also { stage = 4 }
                3 -> playerl(FaceAnim.HALF_GUILTY, "What do you do in the Guild?").also { stage = 5 }
                4 -> playerl(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
            }

            2 -> npcl(FaceAnim.HALF_GUILTY, "This is the finest wizards' establishment in the land. We have magic portals to the other towers of wizardry around Gielinor. We have a particularly wide collection of runes in our rune shop.").also { stage++ }
            3 -> npcl(FaceAnim.HALF_GUILTY, "We sell some of the finest mage robes in the land and we have a training area full of zombies for you to practice your magic on.").also { stage = 0 }
            4 -> npcl(FaceAnim.HALF_GUILTY, "You need a magic level of 66, the high magic energy level is too dangerous for anyone below that level.").also { stage = 0 }
            5 -> npcl(FaceAnim.HALF_GUILTY, "I'm the Grand Secretary for the Wizards' Guild, I have lots of correspondence to keep up with, as well as attending to the discipline of the more problematic guild members.").also { stage = 0 }
            else -> end()
        }
    }

    /**
     * Handles the main quest-related dialogue before completion.
     */
    private fun handleDefaultQuest(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_SIGN_PORTRAIT, false)) {
                options("What did you say I should do?", "Where is Sithik?", "I have some items that I'd like you to look at.", "I want to ask about the Magic Guild.", "Sorry, I have to go.").also { stage++ }
            } else {
                options("What did you say I should do?", "Where is Sithik?", "I want to ask about the Magic Guild.", "Sorry, I have to go.").also { stage++ }
            }

            1 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_SIGN_PORTRAIT, false)) {
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "What did you say I should do?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Where is Sithik?").also { stage = 2 }
                    3 -> player("I have some items that I'd like you to look at.").also { stage = 7 }
                    4 -> playerl(FaceAnim.HALF_GUILTY, "I want to ask about the Magic Guild.").also { stage = 6 }
                    5 -> playerl(FaceAnim.HALF_GUILTY, "Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            } else {
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "What did you say I should do?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Where is Sithik?").also { stage = 2 }
                    3 -> playerl(FaceAnim.HALF_GUILTY, "I want to ask about the Magic Guild.").also { stage = 6 }
                    4 -> playerl(FaceAnim.HALF_GUILTY, "Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            }

            2 -> npcl(FaceAnim.HALF_GUILTY, "You should go and have a chat with Sithik Ints, he's in that house just to the north.").also { stage++ }
            3 -> npcl(FaceAnim.HALF_GUILTY, "He's a lodger and has a room upstairs. Just tell him that I sent you to see him. He should be fine once you've mentioned my name.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.HALF_GUILTY, "He's in that house just to the north, less than a few seconds walk away. He's a lodger and has a room upstairs...he's not very well though.").also { stage = END_DIALOGUE }
            5 -> playerl(FaceAnim.HALF_GUILTY, "Sure...I mean, I'll try if I remember.").also { stage = END_DIALOGUE }
            6 -> {
                Flow.DEFAULT_AFTER_QUEST
                stage = 0
            }

            7 -> when {
                inInventory(player!!, Items.NECROMANCY_BOOK_4837) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_1, false) ->
                    sendItemDialogue(player!!, Items.NECROMANCY_BOOK_4837, "You show the Necromancy book to Zavistic.").also { stage++ }

                inInventory(player!!, Items.BOOK_OF_HAM_4829) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_2, false) ->
                    sendItemDialogue(player!!, Items.BOOK_OF_HAM_4829, "You show the HAM book to Zavistic.").also { stage = 12 }

                inInventory(player!!, Items.DRAGON_INN_TANKARD_4811) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_3, false) ->
                    sendDoubleItemDialogue(player!!, -1, Items.DRAGON_INN_TANKARD_4811, "You show the dragon Inn Tankard to Zavistic.").also { stage = 14 }

                inInventory(player!!, ZogreUtils.UNREALIST_PORTRAIT) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_4, false) ->
                    player("Look, I made a portrait of Sithik.").also { stage = 16 }

                inInventory(player!!, ZogreUtils.REALIST_PORTRAIT) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_5, false) ->
                    sendItemDialogue(player!!, ZogreUtils.REALIST_PORTRAIT, "You show the portrait of Sithik to Zavistic.").also { stage = 18 }

                inInventory(player!!, ZogreUtils.SIGNED_PORTRAIT) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_6, false) ->
                    sendItemDialogue(player!!, ZogreUtils.SIGNED_PORTRAIT, "You show the signed portrait of Sithik to Zavistic.").also { stage = 19 }

                else -> {
                    end()
                    player!!.inventory.remove(
                        Item(Items.NECROMANCY_BOOK_4837),
                        Item(Items.BOOK_OF_HAM_4829),
                        Item(Items.DRAGON_INN_TANKARD_4811),
                        Item(ZogreUtils.SIGNED_PORTRAIT)
                    )
                    removeAttributes(
                        player!!,
                        ZogreUtils.TALK_AGAIN_1,
                        ZogreUtils.TALK_AGAIN_2,
                        ZogreUtils.TALK_AGAIN_3,
                        ZogreUtils.TALK_AGAIN_4,
                        ZogreUtils.TALK_AGAIN_5,
                        ZogreUtils.TALK_AGAIN_6
                    )
                    sendItemDialogue(
                        player!!,
                        ZogreUtils.STRANGE_POTION,
                        "Zavistic hands you a strange looking potion bottle and takes all the evidence you've accumulated so far."
                    )
                    setAttribute(player!!, ZogreUtils.TALK_WITH_ZAVISTIC_DONE, true)
                    setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 6, true)
                    addItem(player!!, ZogreUtils.STRANGE_POTION)
                }
            }

            8 -> player("I have this necromancy book as evidence that Sithik is", "involved with the undead ogres at Jiggig.").also { stage++ }
            9 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_NECRO_BOOK, false)) {
                npcl(FaceAnim.FRIENDLY, "Yeah, you've shown me this before...if this is all the evidence you have?").also { stage = 24 }
            } else {
                npc("Ok, so he's researching necromancy...it doesn't mean", "anything in itself.").also { stage++ }
            }

            10 -> player("Yes, but if you look, you can see that there is a half", "torn page which matches the page I found at Jiggig.").also { stage++ }
            11 -> npc("Hmm, yes, but someone could have stolen that from him", "and then gone and cast it without his permission or to", "try and deliberately implicate him.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_1, true)
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_NECRO_BOOK, true)
                stage = 7
            }

            12 -> playerl(FaceAnim.FRIENDLY, "Look, this book proves that Sithik hates all monsters and most likely Ogres with a passion.").also { stage++ }
            13 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_NECRO_BOOK, false)) {
                npcl(FaceAnim.FRIENDLY, "Yeah, you've shown me this before...if this is all the evidence you have?").also { stage = 27 }
            } else {
                npcl(FaceAnim.FRIENDLY, "So what, hating monsters isn't a crime in itself...although I suppose that it does give a motive if Sithik was involved. On its own, it's not enough evidence though.").also {
                    setAttribute(player!!, ZogreUtils.TALK_AGAIN_2, true)
                    setAttribute(player!!, ZogreUtils.TALK_AGAIN_ABOUT_HAM_BOOK, true)
                    stage = 7
                }
            }

            14 -> player("This is the tankard I found on the remains of Brentle", "Vahn!").also { stage++ }
            15 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_TANKARD_AGAIN, false)) {
                npcl(FaceAnim.FRIENDLY, "Yeah, you've shown me this before...if this is all the evidence you have?").also { stage = 31 }
            } else {
                npc("That doesn't mean anything in itself, you could have", "gotten that from anywhere. Even from the Dragon Inn", "tavern! There isn't anything to link Brentle Vahn with", "Sithik Ints.").also {
                    setAttribute(player!!, ZogreUtils.TALK_AGAIN_3, true)
                    stage = 7
                }
            }

            16 -> sendItemDialogue(player!!, ZogreUtils.UNREALIST_PORTRAIT, "You show the sketch...").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "Who the demonikin is that? Is it meant to be a portrait of Sithik, it doesn't look anything like him!").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_4, true)
                stage = 7
            }

            18 -> npcl(FaceAnim.FRIENDLY, "Hmm, great...but I already know what he looks like!").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_5, true)
                stage = 7
            }

            19 -> playerl(FaceAnim.FRIENDLY, "This is a portrait of Sithik, signed by the landlord of the Dragon Inn saying that he saw Sithik and Brentle Vahn together.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_5, true)
                stage++
            }

            20 -> npcl(FaceAnim.FRIENDLY, "Hmmm, well that is interesting.").also { stage++ }
            21 -> npcl(FaceAnim.FRIENDLY, "However, there isn't enough evidence for me to take the issue further at this point. If you find any further evidence bring it to me.").also { stage++ }
            22 -> npcl(FaceAnim.FRIENDLY, "And I'm starting to think that Sithik may be involved. Here, take this potion and give some to Sithik.").also { stage++ }
            23 -> npcl(FaceAnim.FRIENDLY, "It'll bring on a change which should solicit some answers - tell him the effects won't revert until he's told the truth.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_6, true)
                stage = 7
            }

            24 -> playerl(FaceAnim.FRIENDLY, "Please just look at it again...").also { stage++ }
            25 -> npcl(FaceAnim.FRIENDLY, "Ok, let me look then.").also { stage++ }
            26 -> npc(FaceAnim.FRIENDLY, "Ok, so he's researching necromancy...it doesn't mean", "anything in itself.").also { stage = 10 }
            27 -> playerl(FaceAnim.FRIENDLY, "Please just look at it again...").also { stage++ }
            28 -> npcl(FaceAnim.FRIENDLY, "Ok, let me look then.").also { stage++ }
            29 -> sendItemDialogue(player!!, Items.BOOK_OF_HAM_4829, "You show the HAM book to Zavistic, he looks through it again.").also { stage++ }
            30 -> npcl(FaceAnim.FRIENDLY, "So what, hating monsters isn't a crime in itself...although I suppose that it does give a motive if Sithik was involved. On its own, it's not enough evidence though.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_2, true)
                stage = 7
            }

            31 -> playerl(FaceAnim.FRIENDLY, "Please just look at it again...").also { stage++ }
            32 -> npcl(FaceAnim.FRIENDLY, "Ok, let me look then.").also { stage++ }
            33 -> sendItemDialogue(player!!, Items.DRAGON_INN_TANKARD_4811, "You show the tankard to Zavistic, he looks at it again.").also { stage++ }

            34 -> npc("That doesn't mean anything in itself, you could have", "gotten that from anywhere. Even from the Dragon Inn", "tavern! There isn't anything to link Brentle Vahn with", "Sithik Ints.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_3, true)
                stage = 7
            }

            else -> end()
        }
    }

    /**
     * Handles the dialogue path when the player shows both key quest items.
     */
    private fun handleHasBoth(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at Jiggig, I've found some clues, I wondered if you'd have a look at them.").also { stage++ }
            1 -> sendDoubleItemDialogue(player!!, Items.BLACK_PRISM_4808, Items.TORN_PAGE_4809, "You show the prism and the necromantic half page to the aged wizard.").also { stage++ }
            2 -> npcl(FaceAnim.HALF_GUILTY, "Hmmm, now this is interesting! Where did you get these from?").also { stage++ }
            3 -> playerl(FaceAnim.HALF_GUILTY, "I got them from a nearby Ogre tomb, it's recently been infested with zombie ogres and I'm trying to work out what happened there.").also { stage++ }
            4 -> npcl(FaceAnim.HALF_GUILTY, "This is very troubling Player, very troubling indeed.").also { stage++ }
            5 -> npcl(FaceAnim.HALF_GUILTY, "While it's permitted for learned members of our order to research the 'dark arts', it's absolutely forbidden to make use of such magic.").also { stage++ }
            6 -> playerl(FaceAnim.HALF_GUILTY, "Do you have any leads on people that I might talk to regarding this?").also { stage++ }
            7 -> npcl(FaceAnim.HALF_GUILTY, "Well a wizard by the name of 'Sithik Ints' was doing some research in this area. He may know something about it.").also { stage++ }
            8 -> npcl(FaceAnim.HALF_GUILTY, "He's lodged at that guest house to the North, though he's ill and isn't able to leave his room.").also { stage++ }
            9 -> npcl(FaceAnim.HALF_GUILTY, "Why not go and talk to him, poke around a bit and see if anything comes up. Let me know how you get on. However,").also { stage++ }
            10 -> npcl(FaceAnim.HALF_GUILTY, "I doubt that 'Sithik' had anything to do with it. There's a severe penalty for using the 'dark arts'. If you find any evidence to the contrary, please bring it to me.").also { stage++ }
            11 -> npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?").also {
                setAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, true)
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_BLACK_PRISM, true)
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_TORN_PAGE, true)
                stage++
            }

            12 -> if (inInventory(player!!, Items.DRAGON_INN_TANKARD_4811)) {
                flow = Flow.TANKARD
                stage = 0
            } else {
                playerl(FaceAnim.HALF_GUILTY, "Not really").also { stage++ }
            }

            13 -> npcl(FaceAnim.HALF_GUILTY, "I don't know what to say then, there isn't enough to go on with the clues you've shown me so far.").also { stage++ }
            14 -> npcl(FaceAnim.THINKING, "I'd suggest going back to search a bit more, but you may just be wasting your time? Hmm, but this prism does seem to have some magical protection.").also { stage++ }
            15 -> npcl(FaceAnim.HALF_GUILTY, "Once you've finished with this item, bring it back to me would you? I may have a reward for you!").also { stage++ }
            16 -> playerl(FaceAnim.HALF_GUILTY, "Sure...I mean, I'll try if I remember.").also { stage++ }
            17 -> {
                flow = Flow.QUEST
                stage = 0
            }
            else -> end()
        }
    }

    /**
     * Handles dialogue when the player shows the tankard item to Zavistic.
     */
    private fun handleTankard(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Well, I found this...").also { stage++ }
            1 -> sendDoubleItemDialogue(player!!, -1, Items.DRAGON_INN_TANKARD_4811, "You show the tankard to Zavistic.").also { stage++ }
            2 -> npcl(FaceAnim.THINKING, "Hmmm, no, that's not really associated with this to be honest.").also {
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_TANKARD, true)
                stage++
            }
            3 -> {
                flow = Flow.QUEST
                stage = 0
            }
            else -> end()
        }
    }

    /**
     * Handles dialogue when the player presents the Torn page to Zavistic.
     */
    private fun handleTornPage(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at Jiggig, I've found a clue that you may be able to help with.").also { stage++ }
            1 -> sendDoubleItemDialogue(player!!, -1, Items.TORN_PAGE_4809, "You show the necromantic half page to the aged wizard.").also { stage++ }
            2 -> npcl(FaceAnim.HALF_ASKING, "Hmm, this is a half torn spell page, it requires another spell component to be effective.").also { stage++ }
            3 -> npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?").also { stage++ }
            4 -> if (inInventory(player!!, Items.BLACK_PRISM_4808) && !getAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false)) {
                Flow.BLACK_PRISM
                stage = 0
            } else {
                Flow.QUEST
                stage = 0
            }
            else -> end()
        }
    }

    /**
     * Handles dialogue when the player shows the black prism to Zavistic.
     */
    private fun handleBlackPrism(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> if (getAttribute(player!!, ZogreUtils.TALK_WITH_ZAVISTIC_DONE, false)) {
                playerl(FaceAnim.FRIENDLY, "I found this black prism at Jiggig where the undead ogre activity was happening?").also { stage = 5 }
            } else {
                playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at 'Jiggig', and the ogres have asked me to look into it. I think I've found a clue and I wonder if you could take a look at it for me?").also { stage++ }
            }

            1 -> sendDoubleItemDialogue(player!!, -1, Items.BLACK_PRISM_4808, "You show the black prism to the aged wizard.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Hmmm, well this is an uncommon spell component. On it's own it's useless, but with certain necromantic spells it can be very powerful.").also { stage++ }
            3 -> npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?").also { stage++ }
            4 -> if (inInventory(player!!, Items.TORN_PAGE_4809) && !getAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false)) {
                Flow.TORN_PAGE
                stage = 0
            } else if (inInventory(player!!, Items.DRAGON_INN_TANKARD_4811) && !getAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false)) {
                Flow.TANKARD
                stage = 0
            } else {
                Flow.QUEST
                stage = 0
            }

            5 -> sendDoubleItemDialogue(player!!, -1, Items.BLACK_PRISM_4808, "You show the black prism to the aged wizard.").also { stage++ }
            6 -> npcl(FaceAnim.FRIENDLY, "Yes, you've already showed me that, bring it to me when you've resolved the problems at Jiggig and I'll see what I can do.").also { stage = END_DIALOGUE }
            else -> end()
        }
    }

    /**
     * Handles the conversation about the strange potion given during the quest.
     */
    private fun handlePotion(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> if (!inInventory(player!!, ZogreUtils.STRANGE_POTION)) {
                playerl(FaceAnim.FRIENDLY, "Well, actually, I've lost it, could I have another one please?").also { stage++ }
            } else {
                playerl(FaceAnim.FRIENDLY, "No, not yet, what was I supposed to do again?").also { stage = 3 }
            }

            1 -> npcl(FaceAnim.HALF_GUILTY, "Sure, but don't lose it this time.").also { stage++ }
            2 -> {
                end()
                if (freeSlots(player!!) < 1) {
                    sendItemDialogue(player!!, ZogreUtils.STRANGE_POTION, "Zavistic hands you a bottle of strange potion, but you don't have enough room to take it.")
                } else {
                    sendItemDialogue(player!!, ZogreUtils.STRANGE_POTION, "Zavistic hands you a bottle of strange potion.")
                    addItem(player!!, ZogreUtils.STRANGE_POTION)
                }
            }

            3 -> npcl(FaceAnim.FRIENDLY, "Try to use the potion on Sithik somehow, he should undergo an interesting transformation, though you'll probably want to leave the house in case there are any side effects. Then go back and question Sithik and tell").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "him the effects won't wear off until he tells the truth. In fact, that's not exactly true, but I'm sure it'll be an extra incentive to get him to be honest.").also { stage++ }
            5 -> {
                Flow.DEFAULT_AFTER_QUEST
                stage = 0
            }
            else -> end()
        }
    }

    /**
     * Handles post-quest dialogue where Zavistic comments on Sithik punishment.
     */
    private fun handleLast(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Don't you worry about Sithik, he's not likely to be moving from his bed for a long time.").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "When he eventually does get better, he's going to be sent before a disciplinary tribunal, then we'll sort out what's what.").also { stage++ }
            2 -> player(FaceAnim.FRIENDLY, "Thanks for your help with all of this.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Ooohh, no thanks required. It's I who should be thanking you my friend...your investigative mind has shown how vigilant we really should be for this type of evil use of the magical arts.").also { stage++ }
            4 -> {
                Flow.DEFAULT_AFTER_QUEST
                stage = 0
            }
            else -> end()
        }
    }

    /**
     * Handles the optional dialogue where the player decides who to sell the Black Prism.
     */
    private fun handleSellBlackPrism(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.YANNI_SALIKA_515)
        when (stage) {
            0 -> sendDialogue(player!!, "You show the black prism to Zavistic.").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Ah yes, I remember saying something about a reward didn't I? Well, I can offer you 2000 coins for it as it stands,").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY,  "but I know that Yanni Salika in Shilo Village would offer you more than twice as much.").also { stage++ }
            3 -> {
                setTitle(player!!, 2)
                sendOptions(player!!, "WHO WOULD YOU LIKE TO SELL THE PRISM TO?", "Sell it to Zavistic for 2000", "Take it to Yanni for a greater reward.")
                stage++
            }
            4 -> when (buttonID) {
                1 -> player("I'll sell it to you for 2000 coins!").also { stage = 6 }
                2 -> player("I think I'm going to take it to Yanni for an even greater reward.").also { stage++ }
            }
            5 -> npc("Fair enough my friend, you deserve it!").also { stage = END_DIALOGUE }
            6 -> npc("Very well my friend.").also { stage++ }
            7 -> {
                end()
                if (removeItem(player!!, Items.BLACK_PRISM_4808)) {
                    sendMessage(player!!, "You sell the black prism for 2000 coins.")
                    addItemOrDrop(player!!, Items.COINS_995, 2000)
                    npc("Thanks!")
                }
            }
            else -> end()
        }
    }

    /**
     * Handles the dialogue after hand of the sand - starts returning clarence miniquest.
     */
    private fun handleMiniquestReturningClarence(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        val miniquestComplete = getAttribute(player, GameAttributes.RETURNING_CLARENCE_COMPLETE, false)
        when (stage) {
            0 -> showTopics(
                Topic("I'm here about the sicks...err Zogres", 1, true),
                Topic("I have a rather sandy problem that I'd like to palm off on you.",
                    if(miniquestComplete) 8 else 5, true
                )
            )
            1 -> npcl(FaceAnim.FRIENDLY, "Don't you worry about Sithik, he's not likely to be moving from his bed for a long time. When he eventually does get better, he's going to be sent before a disciplinary tribunal, then we'll sort out what's what.").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "Thank you for your help with all of this.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Ooohh, no thanks required. It's I who should be thanking you my friend...your investigative mind has shown how vigilant we really should be for this type of evil use of the magical arts.").also { stage++ }
            4 -> {
                Flow.DEFAULT_AFTER_QUEST
                stage = 0
            }
            5 -> if(!inInventory(player, Items.HAND_11763)) {
                npc(FaceAnim.FRIENDLY, "Thank you so much for helping to bring Clarence home", "and lock up his murderer! I only wish we could find", "the rest of him to truly put him to rest.").also { stage++ }
            } else if(getVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055) == 35){
                npc(FaceAnim.FRIENDLY, "Thank you so much for helping to bring Clarence home", "and lock up his murderer! I only wish we could find", "the rest of him to truly put him to rest.").also { stage = 32 }
            } else {
                player("I think...that I might have found something.").also { stage = 17 }
            }
            6 -> player(FaceAnim.HALF_ASKING, "I'll see what I can do, I'm sure I saw a hand", "somewhere...If I find it I'll give it to you.").also { stage++ }
            7 -> {
                Flow.DEFAULT_AFTER_QUEST
                stage = 0
            }
            8 -> npcl(FaceAnim.FRIENDLY, "It's so good to have Clarence back in mostly one piece.")
            9 -> player(FaceAnim.HALF_ASKING, "Back?").also { stage++ }
            10 -> npcl(FaceAnim.FRIENDLY, "Yes indeed: he may not be alive, but he is buried in the grounds of the Wizards' Guild here. So he is back with us. All thanks to you.").also { stage++ }
            11 -> player(FaceAnim.HALF_ASKING, "Pleased I could lend a hand.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "That was an incredibly bad pun. You know that puns are the lowest form of wheat?").also { stage++ }
            13 -> player(FaceAnim.HALF_ASKING, "Sorry, I don't seem to be able to help myself... I appear to have lost my head.").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "ARG! Another! Away with you!").also { stage++ }
            15 -> player(FaceAnim.HALF_ASKING, "But...").also { stage++ }
            16 -> {
                Flow.DEFAULT_AFTER_QUEST
                stage = 0
            }
            17 -> npcl(FaceAnim.FRIENDLY, "Oh? What's that?").also { stage++ }
            18 -> player(FaceAnim.NEUTRAL, "Another hand.").also { stage++ }
            19 -> npc(FaceAnim.NEUTRAL, "In the sand?").also { stage++ }
            20 -> player(FaceAnim.NEUTRAL, "No.").also { stage++ }
            21 -> npc(FaceAnim.NEUTRAL, "On your arm?").also { stage++ }
            22 -> player("Err, no, I mean, yes...but...no.").also { stage++ }
            23 -> npc(FaceAnim.NEUTRAL, "Oh my, it really does sound like you've lost your head", "and are a bit shaken up!").also { stage++ }
            24 -> player("Wouldn't you be? It was in a package in the RPDT in", "Ardougne. But I have it here with me.").also { stage++ }
            25 -> sendItemDialogue(player, Items.HAND_11763, "You show the hand to Zavistic.").also { stage++ }
            26 -> npc(FaceAnim.EXTREMELY_SHOCKED, "It's...It's Clarence. I shall keep it with the rest of him...if", "we can find enough, we can finally put him to rest.").also { stage++ }
            27 -> player("You mean, you didn't already burn him...or, at least,", "what y ou had of him?").also { stage++ }
            28 -> npc(FaceAnim.EXTREMELY_SHOCKED, "Oh no, that would be terrible! We must find as much", "as we can before we bury him so that he go whole", "to the Wizards' Great Hall.").also { stage++ }
            29 -> player("Right. Are all wizards a little potty?").also { stage++ }
            30 -> player.dialogueInterpreter.sendItemMessage(Items.HAND_11763, "You hand over the hand and get a weird sense of déja", "vu.").also { stage++ }
            31 -> if(removeItem(player, Items.HAND_11763)) {
                npc("Thank you for helping us, please see if you can find", "any more of him.")
                setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 35)
                stage = END_DIALOGUE
            } else end()
            32 -> player("I'll see what I can do, I'm sure I saw some other body", "parts somewhere...If I find any I'll give them to you.").also { stage++ }
            33 -> {
                Flow.DEFAULT_AFTER_QUEST
                stage = 0
            }
        }
    }
}
