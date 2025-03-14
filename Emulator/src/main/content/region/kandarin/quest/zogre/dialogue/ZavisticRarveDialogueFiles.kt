package content.region.kandarin.quest.zogre.dialogue

import content.region.kandarin.quest.zogre.handlers.ZUtils
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

class ZavisticRarveDialogueFiles : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        var questComplete = getVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13
        var hasBlackPrism =
            inInventory(player!!, Items.BLACK_PRISM_4808) &&
                !getAttribute(player!!, ZUtils.TALK_ABOUT_BLACK_PRISM, false)
        var hasTornPage =
            inInventory(player!!, Items.TORN_PAGE_4809) && !getAttribute(player!!, ZUtils.TALK_ABOUT_TORN_PAGE, false)
        var hasTankard =
            inInventory(player!!, Items.DRAGON_INN_TANKARD_4811) &&
                !getAttribute(player!!, ZUtils.TALK_ABOUT_TANKARD, false)
        var hasBlackPrismAndTornPage =
            hasBlackPrism && hasTornPage && !getAttribute(player!!, ZUtils.SITHIK_DIALOGUE_UNLOCK, false)
        var hasOrLostStrangePotion = getAttribute(player!!, ZUtils.TALK_WITH_ZAVISTIC_DONE, false)
        var hasTalkWithSithik = getAttribute(player!!, ZUtils.TALK_WITH_SITHIK_OGRE_DONE, false)
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            START_DIALOGUE ->
                npcl(
                    "What are you doing...Oh, it's you...sorry...didn't realise... what can I do for you?",
                ).also {
                    stage++
                }
            1 ->
                if (hasTalkWithSithik) {
                    end().also { openDialogue(player!!, ZavisticRarveLastDialogue()) }
                } else {
                    playerl("But I was told to ring the bell if I wanted some attention.").also { stage++ }
                }

            2 -> npcl("Well...anyway...we're very busy here, hurry up what do you want?").also { stage++ }
            3 ->
                when {
                    hasBlackPrismAndTornPage ->
                        end().also {
                            openDialogue(
                                player!!,
                                ZavisticRarveHasBothItemsDialogue(),
                            )
                        }
                    hasBlackPrism -> end().also { openDialogue(player!!, ZavisticRarveBlackPrismDialogue()) }
                    hasTornPage -> end().also { openDialogue(player!!, ZavisticRarveTornPageDialogue()) }
                    hasTankard -> end().also { openDialogue(player!!, ZavisticRarveTankardDialogue()) }
                    questComplete -> end().also { openDialogue(player!!, ZavisticRarveDefaultDialogue()) }
                    hasOrLostStrangePotion -> end().also { openDialogue(player!!, ZavisticRarvePotionDialogue()) }
                    else -> end().also { openDialogue(player!!, ZavisticRarveDefaultQuestDialogue()) }
                }
        }
    }
}

class ZavisticRarveDefaultDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 ->
                options(
                    "What is there to do in the Wizards' Guild?",
                    "What are the requirements to get in the Wizards' Guild?",
                    "What do you do in the Guild?",
                    "Ok, thanks.",
                ).also { stage++ }

            1 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "What is there to do in the Wizards' Guild?").also { stage++ }
                    2 ->
                        playerl(FaceAnim.HALF_GUILTY, "What are the requirements to get in the Wizards' Guild?").also {
                            stage =
                                4
                        }
                    3 -> playerl(FaceAnim.HALF_GUILTY, "What do you do in the Guild?").also { stage = 5 }
                    4 -> playerl(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This is the finest wizards' establishment in the land. We have magic portals to the other towers of wizardry around Gielinor. We have a particularly wide collection of runes in our rune shop.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "We sell some of the finest mage robes in the land and we have a training area full of zombies for you to practice your magic on.",
                ).also {
                    stage =
                        0
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "You need a magic level of 66, the high magic energy level is too dangerous for anyone below that level.",
                ).also {
                    stage =
                        0
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I'm the Grand Secretary for the Wizards' Guild, I have lots of correspondence to keep up with, as well as attending to the discipline of the more problematic guild members.",
                ).also {
                    stage =
                        0
                }
        }
    }
}

class ZavisticRarveDefaultQuestDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 ->
                if (getAttribute(player!!, ZUtils.TALK_ABOUT_SIGN_PORTRAIT, false)) {
                    options(
                        "What did you say I should do?",
                        "Where is Sithik?",
                        "I have some items that I'd like you to look at.",
                        "I want to ask about the Magic Guild.",
                        "Sorry, I have to go.",
                    ).also { stage++ }
                } else {
                    options(
                        "What did you say I should do?",
                        "Where is Sithik?",
                        "I want to ask about the Magic Guild.",
                        "Sorry, I have to go.",
                    ).also { stage++ }
                }

            1 ->
                if (getAttribute(player!!, ZUtils.TALK_ABOUT_SIGN_PORTRAIT, false)) {
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

            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "You should go and have a chat with Sithik Ints, he's in that house just to the north.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "He's a lodger and has a room upstairs. Just tell him that I sent you to see him. He should be fine once you've mentioned my name.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "He's in that house just to the north, less than a few seconds walk away. He's a lodger and has a room upstairs...he's not very well though.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 -> playerl(FaceAnim.HALF_GUILTY, "Sure...I mean, I'll try if I remember.").also { stage = END_DIALOGUE }
            6 -> end().also { openDialogue(player!!, ZavisticRarveDefaultDialogue()) }
            7 ->
                when {
                    inInventory(
                        player!!,
                        Items.NECROMANCY_BOOK_4837,
                    ) &&
                        !getAttribute(
                            player!!,
                            ZUtils.TALK_AGAIN_1,
                            false,
                        ) ->
                        sendItemDialogue(
                            player!!,
                            Items.NECROMANCY_BOOK_4837,
                            "You show the Necromancy book to Zavistic.",
                        ).also {
                            stage++
                        }
                    inInventory(
                        player!!,
                        Items.BOOK_OF_HAM_4829,
                    ) &&
                        !getAttribute(player!!, ZUtils.TALK_AGAIN_2, false) ->
                        sendItemDialogue(player!!, Items.BOOK_OF_HAM_4829, "You show the HAM book to Zavistic.").also {
                            stage =
                                12
                        }
                    inInventory(player!!, Items.DRAGON_INN_TANKARD_4811) &&
                        !getAttribute(player!!, ZUtils.TALK_AGAIN_3, false) ->
                        sendItemDialogue(
                            player!!,
                            Items.DRAGON_INN_TANKARD_4811,
                            "You show the dragon Inn Tankard to Zavistic.",
                        ).also {
                            stage =
                                14
                        }
                    inInventory(
                        player!!,
                        ZUtils.UNREALIST_PORTRAIT,
                    ) &&
                        !getAttribute(player!!, ZUtils.TALK_AGAIN_4, false) ->
                        player("Look, I made a portrait of Sithik.").also {
                            stage =
                                16
                        }
                    inInventory(player!!, ZUtils.REALIST_PORTRAIT) &&
                        !getAttribute(player!!, ZUtils.TALK_AGAIN_5, false) ->
                        sendItemDialogue(
                            player!!,
                            ZUtils.REALIST_PORTRAIT,
                            "You show the portrait of Sithik to Zavistic.",
                        ).also {
                            stage =
                                18
                        }
                    inInventory(
                        player!!,
                        ZUtils.SIGNED_PORTRAIT,
                    ) &&
                        !getAttribute(player!!, ZUtils.TALK_AGAIN_6, false) ->
                        sendItemDialogue(
                            player!!,
                            ZUtils.SIGNED_PORTRAIT,
                            "You show the signed portrait of Sithik to Zavistic.",
                        ).also {
                            stage =
                                19
                        }

                    else -> {
                        end()
                        player!!.inventory.remove(
                            Item(Items.NECROMANCY_BOOK_4837),
                            Item(Items.BOOK_OF_HAM_4829),
                            Item(Items.DRAGON_INN_TANKARD_4811),
                            Item(
                                ZUtils.SIGNED_PORTRAIT,
                            ),
                        )
                        removeAttributes(
                            player!!,
                            ZUtils.TALK_AGAIN_1,
                            ZUtils.TALK_AGAIN_2,
                            ZUtils.TALK_AGAIN_3,
                            ZUtils.TALK_AGAIN_4,
                            ZUtils.TALK_AGAIN_5,
                            ZUtils.TALK_AGAIN_6,
                        )
                        sendItemDialogue(
                            player!!,
                            ZUtils.STRANGE_POTION,
                            "Zavistic hands you a strange looking potion bottle and" +
                                "takes all the evidence you've accumulated so far.",
                        )
                        setAttribute(player!!, "/save:${ZUtils.TALK_WITH_ZAVISTIC_DONE}", true)
                        setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 6, true)
                        addItem(player!!, ZUtils.STRANGE_POTION)
                    }
                }

            8 ->
                player(
                    "I have this necromancy book as evidence that Sithik is",
                    "involved with the undead ogres at Jiggig.",
                ).also {
                    stage++
                }
            9 ->
                if (getAttribute(player!!, ZUtils.TALK_ABOUT_NECRO_BOOK, false)) {
                    npcl("Yeah, you've shown me this before...if this is all the evidence you have?").also {
                        stage = 24
                    }
                } else {
                    npc("Ok, so he's researching necromancy...it doesn't mean", "anything in itself.").also { stage++ }
                }

            10 ->
                player(
                    "Yes, but if you look, you can see that there is a half",
                    "torn page which matches the page I found at Jiggig.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    "Hmm, yes, but someone could have stolen that from him",
                    "and then gone and cast it without his permission or to",
                    "try and deliberately implicate him.",
                ).also {
                    setAttribute(player!!, ZUtils.TALK_AGAIN_1, true)
                    setAttribute(
                        player!!,
                        ZUtils.TALK_ABOUT_NECRO_BOOK,
                        true,
                    )
                    stage = 7
                }

            12 ->
                playerl(
                    "Look, this book proves that Sithik hates all monsters and most likely Ogres with a passion.",
                ).also {
                    stage++
                }
            13 ->
                if (getAttribute(player!!, ZUtils.TALK_ABOUT_NECRO_BOOK, false)) {
                    npcl("Yeah, you've shown me this before...if this is all the evidence you have?").also {
                        stage = 27
                    }
                } else {
                    npcl(
                        "So what, hating monsters isn't a crime in itself...although I suppose that it does give a motive if Sithik was involved. On its own, it's not enough evidence though.",
                    ).also {
                        setAttribute(player!!, ZUtils.TALK_AGAIN_2, true)
                        setAttribute(
                            player!!,
                            ZUtils.TALK_AGAIN_ABOUT_HAM_BOOK,
                            true,
                        )
                        stage = 7
                    }
                }

            14 -> player("This is the tankard I found on the remains of Brentle", "Vahn!").also { stage++ }
            15 ->
                if (getAttribute(player!!, ZUtils.TALK_ABOUT_TANKARD_AGAIN, false)) {
                    npcl("Yeah, you've shown me this before...if this is all the evidence you have?").also {
                        stage = 31
                    }
                } else {
                    npc(
                        "That doesn't mean anything in itself, you could have",
                        "gotten that from anywhere. Even from the Dragon Inn",
                        "tavern! There isn't anything to link Brentle Vahn with",
                        "Sithik Ints.",
                    ).also {
                        setAttribute(player!!, ZUtils.TALK_AGAIN_3, true)
                        stage =
                            7
                    }
                }

            16 -> sendItemDialogue(player!!, ZUtils.UNREALIST_PORTRAIT, "You show the sketch...").also { stage++ }
            17 ->
                npcl(
                    "Who the demonikin is that? Is it meant to be a portrait of Sithik, it doesn't look anything like him!",
                ).also {
                    setAttribute(
                        player!!,
                        ZUtils.TALK_AGAIN_4,
                        true,
                    )
                    stage = 7
                }

            18 ->
                npcl("Hmm, great...but I already know what he looks like!").also {
                    setAttribute(
                        player!!,
                        ZUtils.TALK_AGAIN_5,
                        true,
                    )
                    stage = 7
                }

            19 ->
                playerl(
                    "This is a portrait of Sithik, signed by the landlord of the Dragon Inn saying that he saw Sithik and Brentle Vahn together.",
                ).also {
                    setAttribute(
                        player!!,
                        ZUtils.TALK_AGAIN_5,
                        true,
                    )
                    stage++
                }

            20 -> npcl("Hmmm, well that is interesting.").also { stage++ }
            21 ->
                npcl(
                    "However, there isn't enough evidence for me to take the issue further at this point. If you find any further evidence bring it to me.",
                ).also {
                    stage++
                }
            22 ->
                npcl(
                    "And I'm starting to think that Sithik may be involved. Here, take this potion and give some to Sithik.",
                ).also {
                    stage++
                }
            23 ->
                npcl(
                    "It'll bring on a change which should solicit some answers - tell him the effects won't revert until he's told the truth.",
                ).also {
                    setAttribute(
                        player!!,
                        ZUtils.TALK_AGAIN_6,
                        true,
                    )
                    stage = 7
                }

            24 -> playerl("Please just look at it again...").also { stage++ }
            25 -> npcl("Ok, let me look then.").also { stage++ }
            26 -> npc("Ok, so he's researching necromancy...it doesn't mean", "anything in itself.").also { stage = 10 }
            27 -> playerl("Please just look at it again...").also { stage++ }
            28 -> npcl("Ok, let me look then.").also { stage++ }
            29 ->
                sendItemDialogue(
                    player!!,
                    Items.BOOK_OF_HAM_4829,
                    "You show the HAM book to Zavistic, he looks through it again.",
                ).also {
                    stage++
                }

            30 ->
                npcl(
                    "So what, hating monsters isn't a crime in itself...although I suppose that it does give a motive if Sithik was involved. On its own, it's not enough evidence though.",
                ).also {
                    setAttribute(
                        player!!,
                        ZUtils.TALK_AGAIN_2,
                        true,
                    )
                    stage = 7
                }
            31 -> playerl("Please just look at it again...").also { stage++ }
            32 -> npcl("Ok, let me look then.").also { stage++ }
            33 ->
                sendItemDialogue(
                    player!!,
                    Items.DRAGON_INN_TANKARD_4811,
                    "You show the tankard to Zavistic, he looks at it again.",
                ).also {
                    stage++
                }
            34 ->
                npc(
                    "That doesn't mean anything in itself, you could have",
                    "gotten that from anywhere. Even from the Dragon Inn",
                    "tavern! There isn't anything to link Brentle Vahn with",
                    "Sithik Ints.",
                ).also {
                    setAttribute(player!!, ZUtils.TALK_AGAIN_3, true)
                    stage =
                        7
                }
        }
    }
}

class ZavisticRarveHasBothItemsDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "There's some undead ogre activity over at Jiggig, I've found some clues, I wondered if you'd have a look at them.",
                ).also {
                    stage++
                }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    Items.BLACK_PRISM_4808,
                    Items.TORN_PAGE_4809,
                    "You show the prism and the necromantic half page to the aged wizard.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Hmmm, now this is interesting! Where did you get these from?",
                ).also { stage++ }
            3 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "I got them from a nearby Ogre tomb, it's recently been infested with zombie ogres and I'm trying to work out what happened there.",
                ).also {
                    stage++
                }
            4 -> npcl(FaceAnim.HALF_GUILTY, "This is very troubling Player, very troubling indeed.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "While it's permitted for learned members of our order to research the 'dark arts', it's absolutely forbidden to make use of such magic.",
                ).also {
                    stage++
                }
            6 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Do you have any leads on people that I might talk to regarding this?",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well a wizard by the name of 'Sithik Ints' was doing some research in this area. He may know something about it.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "He's lodged at that guest house to the North, though he's ill and isn't able to leave his room.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Why not go and talk to him, poke around a bit and see if anything comes up. Let me know how you get on. However,",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I doubt that 'Sithik' had anything to do with it. There's a severe penalty for using the 'dark arts'. If you find any evidence to the contrary, please bring it to me.",
                ).also {
                    stage++
                }
            11 ->
                npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?").also {
                    setAttribute(player!!, "/save:${ZUtils.SITHIK_DIALOGUE_UNLOCK}", true)
                    setAttribute(player!!, ZUtils.TALK_ABOUT_BLACK_PRISM, true)
                    setAttribute(
                        player!!,
                        ZUtils.TALK_ABOUT_TORN_PAGE,
                        true,
                    )
                    stage++
                }

            12 ->
                if (inInventory(player!!, Items.DRAGON_INN_TANKARD_4811)) {
                    end().also { openDialogue(player!!, ZavisticRarveTankardDialogue()) }
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "Not really").also { stage++ }
                }
            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I don't know what to say then, there isn't enough to go on with the clues you've shown me so far.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.THINKING,
                    "I'd suggest going back to search a bit more, but you may just be wasting your time? Hmm, but this prism does seem to have some magical protection.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Once you've finished with this item, bring it back to me would you? I may have a reward for you!",
                ).also {
                    stage++
                }
            16 -> playerl(FaceAnim.HALF_GUILTY, "Sure...I mean, I'll try if I remember.").also { stage++ }
            17 -> end().also { openDialogue(player!!, ZavisticRarveDefaultQuestDialogue()) }
        }
    }
}

class ZavisticRarveTankardDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Well, I found this...").also { stage++ }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.DRAGON_INN_TANKARD_4811,
                    "You show the tankard to Zavistic.",
                ).also {
                    stage++
                }
            2 ->
                npcl(FaceAnim.THINKING, "Hmmm, no, that's not really associated with this to be honest.").also {
                    setAttribute(player!!, ZUtils.TALK_ABOUT_TANKARD, true)
                    stage++
                }
            3 -> end().also { openDialogue(player!!, ZavisticRarveDefaultQuestDialogue()) }
        }
    }
}

class ZavisticRarveTornPageDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "There's some undead ogre activity over at Jiggig, I've found a clue that you may be able to help with.",
                ).also {
                    stage++
                }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.TORN_PAGE_4809,
                    "You show the necromantic half page to the aged wizard.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "Hmm, this is a half torn spell page, it requires another spell component to be effective.",
                ).also {
                    stage++
                }
            3 -> npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?").also { stage++ }
            4 ->
                if (inInventory(player!!, Items.BLACK_PRISM_4808) &&
                    !getAttribute(player!!, ZUtils.SITHIK_DIALOGUE_UNLOCK, false)
                ) {
                    openDialogue(player!!, ZavisticRarveBlackPrismDialogue())
                } else {
                    end().also { openDialogue(player!!, ZavisticRarveDefaultQuestDialogue()) }
                }
        }
    }
}

class ZavisticRarveBlackPrismDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 ->
                if (getAttribute(player!!, ZUtils.TALK_WITH_ZAVISTIC_DONE, false)) {
                    playerl("I found this black prism at Jiggig where the undead ogre activity was happening?").also {
                        stage =
                            5
                    }
                } else {
                    playerl(
                        FaceAnim.HALF_GUILTY,
                        "There's some undead ogre activity over at 'Jiggig', and the ogres have asked me to look into it. I think I've found a clue and I wonder if you could take a look at it for me?",
                    ).also {
                        stage++
                    }
                }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.BLACK_PRISM_4808,
                    "You show the black prism to the aged wizard.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    "Hmmm, well this is an uncommon spell component. On it's own it's useless, but with certain necromantic spells it can be very powerful.",
                ).also {
                    stage++
                }
            3 -> npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?").also { stage++ }
            4 ->
                if (inInventory(player!!, Items.TORN_PAGE_4809) &&
                    !getAttribute(player!!, ZUtils.SITHIK_DIALOGUE_UNLOCK, false)
                ) {
                    end().also { openDialogue(player!!, ZavisticRarveTornPageDialogue()) }
                } else if (inInventory(player!!, Items.DRAGON_INN_TANKARD_4811) &&
                    !getAttribute(player!!, ZUtils.SITHIK_DIALOGUE_UNLOCK, false)
                ) {
                    end().also { openDialogue(player!!, ZavisticRarveTankardDialogue()) }
                } else {
                    end().also { openDialogue(player!!, ZavisticRarveDefaultQuestDialogue()) }
                }

            5 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.BLACK_PRISM_4808,
                    "You show the black prism to the aged wizard.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    "Yes, you've already showed me that, bring it to me when you've resolved the problems at Jiggig and I'll see what I can do.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class ZavisticRarvePotionDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 ->
                if (!inInventory(player!!, ZUtils.STRANGE_POTION)) {
                    playerl("Well, actually, I've lost it, could I have another one please?").also { stage++ }
                } else {
                    playerl("No, not yet, what was I supposed to do again?").also { stage = 3 }
                }

            1 -> npcl(FaceAnim.HALF_GUILTY, "Sure, but don't lose it this time.").also { stage++ }
            2 -> {
                end()
                if (freeSlots(player!!) < 1) {
                    sendItemDialogue(
                        player!!,
                        ZUtils.STRANGE_POTION,
                        "Zavistic hands you a bottle of strange potion, but you don't have enough room to take it.",
                    )
                } else {
                    sendItemDialogue(player!!, ZUtils.STRANGE_POTION, "Zavistic hands you a bottle of strange potion.")
                    addItem(player!!, ZUtils.STRANGE_POTION)
                }
            }

            3 ->
                npcl(
                    "Try to use the potion on Sithik somehow, he should undergo an interesting transformation, though you'll probably want to leave the house in case there are any side effects. Then go back and question Sithik and tell",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    "him the effects won't wear off until he tells the truth. In fact, that's not exactly true, but I'm sure it'll be an extra incentive to get him to be honest.",
                ).also {
                    stage++
                }
            5 -> end().also { openDialogue(player!!, ZavisticRarveDefaultDialogue()) }
        }
    }
}

class ZavisticRarveLastDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        when (stage) {
            0 ->
                npcl(
                    "Don't you worry about Sithik, he's not likely to be moving from his bed for a long time.",
                ).also { stage++ }
            1 ->
                npcl(
                    "When he eventually does get better, he's going to be sent before a disciplinary tribunal, then we'll sort out what's what.",
                ).also {
                    stage++
                }
            2 -> player("Thanks for your help with all of this.").also { stage++ }
            3 ->
                npcl(
                    "Ooohh, no thanks required. It's I who should be thanking you my friend...your investigative mind has shown how vigilant we really should be for this type of evil use of the magical arts.",
                ).also {
                    stage++
                }
            4 -> end().also { openDialogue(player!!, ZavisticRarveDefaultDialogue()) }
        }
    }
}

class ZavisticRarveSellBlackPrismDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.YANNI_SALIKA_515)
        when (stage) {
            0 -> sendDialogue(player!!, "You show the black prism to Zavistic.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah yes, I remember saying something about a reward didn't I? Well, I can offer you 2000 coins for it as it stands,",
                ).also {
                    stage++
                }

            2 ->
                npcl(
                    "but I know that Yanni Salika in Shilo Village would offer you more than twice as much.",
                ).also { stage++ }
            3 -> {
                setTitle(player!!, 2)
                sendDialogueOptions(
                    player!!,
                    "WHO WOULD YOU LIKE TO SELL THE PRISM TO?",
                    "Sell it to Zavistic for 2000",
                    "Take it to Yanni for a greater reward.",
                )
                stage++
            }

            4 ->
                when (buttonID) {
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
        }
    }
}
