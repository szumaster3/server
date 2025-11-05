package content.region.kandarin.feldip.jiggig.quest.zogre.dialogue

import content.data.GameAttributes
import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import content.region.kandarin.yanille.quest.handsand2.FuneralCutscene
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.*

/**
 * Represents the Zavistic Rarve dialogue file (Zogre flesh eaters).
 */
class ZavisticRarveDialogues : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val p = player!!
        npc = NPC(NPCs.ZAVISTIC_RARVE_2059)
        val questComplete = getVarbit(p, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13 || getQuestStage(p, Quests.ZOGRE_FLESH_EATERS) == 100
        val zogreProgress = isQuestInProgress(p, Quests.ZOGRE_FLESH_EATERS, 1, 99)
        val canStartMiniQuest = getVarbit(p, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13 || getQuestStage(p, Quests.ZOGRE_FLESH_EATERS) == 100 && hasRequirement(p, Quests.THE_HAND_IN_THE_SAND, false)
        val miniquestComplete = getVarbit(p, Vars.VARBIT_MINIQUEST_RETURNING_CLARENCE_PROGRESS_4054) == 1
        val hasMiniQuestDiaryItem = inInventory(p, Items.UNLOCKED_DIARY_11762)
        val hasBlackPrism = inInventory(p, Items.BLACK_PRISM_4808) && !getAttribute(p, ZogreUtils.TALK_ABOUT_BLACK_PRISM, false)
        val hasTornPage = inInventory(p, Items.TORN_PAGE_4809) && !getAttribute(p, ZogreUtils.TALK_ABOUT_TORN_PAGE, false)
        val usedTankard = inInventory(p, Items.DRAGON_INN_TANKARD_4811) && !getAttribute(p, ZogreUtils.TALK_ABOUT_TANKARD, false)
        val hasOrLostStrangePotion = getAttribute(p, ZogreUtils.TALK_WITH_ZAVISTIC_DONE, false)
        val saleBlackPrism = questComplete && inInventory(p, Items.BLACK_PRISM_4808)

        when (stage) {
            START_DIALOGUE -> if(getAttribute(player!!, ZogreUtils.NPC_ACTIVE, false)) {
                if (hasRequirement(player!!, Quests.THE_HAND_IN_THE_SAND, false)) {
                    npcl(FaceAnim.NEUTRAL, "What are you doing... Oh, it's you... sorry... didn't realise... what can I do for you?"). also { stage = 2 }
                } else {
                    npcl(FaceAnim.NEUTRAL, "What are you doing ringing that bell?! Don't you think some of us have work to do?").also { stage = 1 }
                }
            } else {
                npcl(FaceAnim.NEUTRAL, "What are you doing bothering me? Don't you think some of us have work to do?").also { stage = 2 }
            }
            1 -> playerl("I thought you were here to help?").also { stage = 3 }
            2 -> playerl("But I was told to ring the bell if I wanted some attention.").also { stage = 4 }

            3 -> npcl("Well... I am, I suppose, anyway... we're very busy here, hurry up, what do you want?").also { stage = 5 }
            4 -> npcl("Well...anyway...we're very busy here, hurry up what do you want?").also { stage = 5 }
            5 -> when {

                canStartMiniQuest -> showTopics(
                    Topic("I'm here about the sicks...err Zogres", 13, true),
                    if (miniquestComplete) {
                        Topic("I have a rather sandy problem that I'd like to palm off on you.",  18)
                    } else {
                        Topic("I have a rather sandy problem that I'd like to palm off on you.",  16)
                    }
                )

                hasMiniQuestDiaryItem -> {
                    player(FaceAnim.HALF_ASKING, "I have a rather sandy problem that I'd like to palm off on you.")
                    stage = 57
                }

                zogreProgress -> {
                    val opts = if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_SIGN_PORTRAIT, false)) {
                        listOf("What did you say I should do?", "Where is Sithik?", "I have some items that I'd like you to look at.", "I want to ask about the Magic Guild.", "Sorry, I have to go.")
                    } else {
                        listOf("What did you say I should do?", "Where is Sithik?", "I want to ask about the Magic Guild.", "Sorry, I have to go.")
                    }
                    options(*opts.toTypedArray())
                    stage = 70
                }

                usedTankard -> {
                    player(FaceAnim.HALF_GUILTY, "Well, I found this...")
                    stage = 109
                }

                hasBlackPrism && hasTornPage -> {
                    playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at Jiggig, I've found some clues, I wondered if you'd have a look at them.")
                    stage = 112
                }

                hasBlackPrism -> if (getAttribute(player!!, ZogreUtils.TALK_WITH_ZAVISTIC_DONE, false)) {
                    playerl(FaceAnim.FRIENDLY, "I found this black prism at Jiggig where the undead ogre activity was happening?")
                    stage = 131
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at 'Jiggig', and the ogres have asked me to look into it. I think I've found a clue and I wonder if you could take a look at it for me?")
                    stage = 128
                }

                saleBlackPrism -> {
                    sendDialogue(player!!, "You show the black prism to Zavistic.")
                    stage = 134
                }

                hasTornPage -> {
                    playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at Jiggig, I've found a clue that you may be able to help with.")
                    stage = 142
                }

                hasOrLostStrangePotion -> if (!inInventory(player!!, ZogreUtils.STRANGE_POTION)) {
                    playerl(FaceAnim.FRIENDLY, "Well, actually, I've lost it, could I have another one please?")
                    stage = 104
                } else {
                    playerl(FaceAnim.FRIENDLY, "No, not yet, what was I supposed to do again?")
                    stage = 106
                }

                else -> options(
                    "What is there to do in the Wizards' Guild?",
                    "What are the requirements to get in the Wizards' Guild?",
                    "What do you do in the Guild?",
                    "Ok, thanks."
                ).also { stage = 7 }
            }

            // 6 -> options("What is there to do in the Wizards' Guild?", "What are the requirements to get in the Wizards' Guild?", "What do you do in the Guild?", "Ok, thanks.").also { stage++ }

            7 -> when (buttonID) {
                1 -> playerl(FaceAnim.HALF_GUILTY, "What is there to do in the Wizards' Guild?").also { stage++ }
                2 -> playerl(FaceAnim.HALF_GUILTY, "What are the requirements to get in the Wizards' Guild?").also { stage = 8 }
                3 -> playerl(FaceAnim.HALF_GUILTY, "What do you do in the Guild?").also { stage = 9 }
                4 -> playerl(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
            }

            8 -> npcl(FaceAnim.HALF_GUILTY, "This is the finest wizards' establishment in the land. We have magic portals to the other towers of wizardry around Gielinor. We have a particularly wide collection of runes in our rune shop.").also { stage++ }
            9 -> npcl(FaceAnim.HALF_GUILTY, "We sell some of the finest mage robes in the land and we have a training area full of zombies for you to practice your magic on.").also { stage = 6 }
            10 -> npcl(FaceAnim.HALF_GUILTY, "You need a magic level of 66, the high magic energy level is too dangerous for anyone below that level.").also { stage = 6 }
            11 -> npcl(FaceAnim.HALF_GUILTY, "I'm the Grand Secretary for the Wizards' Guild, I have lots of correspondence to keep up with, as well as attending to the discipline of the more problematic guild members.").also { stage = 6 }

            // 12 -> showTopics(
            //     Topic("I'm here about the sicks...err Zogres", 13, true),
            //     Topic("I have a rather sandy problem that I'd like to palm off on you.",
            //         if(miniquestComplete) 18 else 16, true
            //     )
            // )
            13 -> npcl(FaceAnim.FRIENDLY, "Don't you worry about Sithik, he's not likely to be moving from his bed for a long time. When he eventually does get better, he's going to be sent before a disciplinary tribunal, then we'll sort out what's what.").also { stage++ }
            14 -> playerl(FaceAnim.FRIENDLY, "Thank you for your help with all of this.").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "Ooohh, no thanks required. It's I who should be thanking you my friend...your investigative mind has shown how vigilant we really should be for this type of evil use of the magical arts.").also { stage = 5 }
            16 -> if(!inInventory(player!!, Items.HAND_11763)) {
                npc(FaceAnim.FRIENDLY, "Thank you so much for helping to bring Clarence home", "and lock up his murderer! I only wish we could find", "the rest of him to truly put him to rest.").also { stage++ }
            } else if(hasRequirement(player!!, Quests.BACK_TO_MY_ROOTS, false)) {
                npc(FaceAnim.FRIENDLY, "Thank you so much for helping to bring Clarence home", "and lock up his murderer! I only wish we could find", "the rest of him to truly put him to rest.").also { stage = 41 }
            } else if(getAttribute(player!!,GameAttributes.RETURNING_CLARENCE_CHECKPOINT, false)) {
                npc(FaceAnim.FRIENDLY, "Thank you so much for helping to bring Clarence home", "and lock up his murderer! I only wish we could find", "the rest of him to truly put him to rest.").also { stage = 49 }
            } else {
                player("I think...that I might have found something.").also { stage = 26 }
            }
            17 -> player(FaceAnim.HALF_ASKING, "I'll see what I can do, I'm sure I saw a hand", "somewhere...If I find it I'll give it to you.").also { stage = 5 }
            18 -> npcl(FaceAnim.FRIENDLY, "It's so good to have Clarence back in mostly one piece.").also { stage++ }
            19 -> player(FaceAnim.HALF_ASKING, "Back?").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "Yes indeed: he may not be alive, but he is buried in the grounds of the Wizards' Guild here. So he is back with us. All thanks to you.").also { stage++ }
            21 -> player(FaceAnim.HALF_ASKING, "Pleased I could lend a hand.").also { stage++ }
            22 -> npcl(FaceAnim.FRIENDLY, "That was an incredibly bad pun. You know that puns are the lowest form of wheat?").also { stage++ }
            23 -> player(FaceAnim.HALF_ASKING, "Sorry, I don't seem to be able to help myself... I appear to have lost my head.").also { stage++ }
            24 -> npcl(FaceAnim.FRIENDLY, "ARG! Another! Away with you!").also { stage++ }
            25 -> player(FaceAnim.HALF_ASKING, "But...").also { stage = 5 }
            26 -> npcl(FaceAnim.FRIENDLY, "Oh? What's that?").also { stage++ }
            27 -> {
                val items = listOf(
                    Items.TORSO_11765,
                    Items.LEFT_ARM_11766,
                    Items.RIGHT_ARM_11767,
                    Items.LEFT_LEG_11768,
                    Items.RIGHT_LEG_11769,
                    Items.FOOT_11764
                )

                for (item in items) {
                    if (inInventory(player!!, item)) {
                        player!!.inventory.remove(Item(item, 1))
                        val message = when(item) {
                            Items.LEFT_ARM_11766 -> "You give the left arm to Zavistic."
                            Items.RIGHT_ARM_11767 -> "You pass the right arm to Zavistic."
                            Items.LEFT_LEG_11768 -> "You present the left leg to Zavistic."
                            Items.RIGHT_LEG_11769 -> "You thrust the right leg at Zavistic who almost doesn't catch it"
                            Items.TORSO_11765 -> "You bodily heave the torso out of your pack to the wizard's astonishment."
                            Items.FOOT_11764 -> "You wave the foot in an amusing manner at Zavistic who turns a satisfying shade of pale until you foot the bill and hand it over."
                            else -> ""
                        }
                        if (message.isEmpty()) continue
                        sendDialogue(player!!, message)
                    }
                }
                setAttribute(player!!, GameAttributes.RETURNING_CLARENCE_CHECKPOINT, true)
                stage = 34
            }
            28 -> npc(FaceAnim.NEUTRAL, "In the sand?").also { stage++ }
            29 -> player(FaceAnim.NEUTRAL, "No.").also { stage++ }
            30 -> npc(FaceAnim.NEUTRAL, "On your arm?").also { stage++ }
            31 -> player("Err, no, I mean, yes...but...no.").also { stage++ }
            32 -> npc(FaceAnim.NEUTRAL, "Oh my, it really does sound like you've lost your head", "and are a bit shaken up!").also { stage++ }
            33 -> player("Wouldn't you be? It was in a package in the RPDT in", "Ardougne. But I have it here with me.").also { stage++ }
            34 -> sendItemDialogue(player!!, Items.HAND_11763, "You show the hand to Zavistic.").also { stage++ }
            35 -> npc(FaceAnim.EXTREMELY_SHOCKED, "It's...It's Clarence. I shall keep it with the rest of him...if", "we can find enough, we can finally put him to rest.").also { stage++ }
            36 -> player("You mean, you didn't already burn him...or, at least,", "what y ou had of him?").also { stage++ }
            37 -> npc(FaceAnim.EXTREMELY_SHOCKED, "Oh no, that would be terrible! We must find as much", "as we can before we bury him so that he go whole", "to the Wizards' Great Hall.").also { stage++ }
            38 -> player("Right. Are all wizards a little potty?").also { stage++ }
            39 -> player!!.dialogueInterpreter.sendItemMessage(Items.HAND_11763, "You hand over the hand and get a weird sense of déja", "vu.").also { stage++ }
            40 -> if(removeItem(player!!, Items.HAND_11763)) {
                npc("Thank you for helping us, please see if you can find", "any more of him.")
                stage = END_DIALOGUE
            } else end()
            41 -> player("I'll see what I can do, I'm sure I saw some other body", "parts somewhere...If I find any I'll give them to you.").also { stage = 5 }

            42 -> npcl(FaceAnim.FRIENDLY, "Than you so much for returning Clarence to us. Despite the lack of his foot we shall go ahead with the burial soon. We must find some more evidence against that scoundrel Sandy first, though. Take a look around his office, would you? See what you can dig up.").also { stage++ }
            43 -> player(FaceAnim.HALF_ASKING, "You're welcome ... but why on earth do we need more evidence? Surely we solved that when he got arrested?").also { stage++ }
            44 -> npcl(FaceAnim.FRIENDLY, "I would agree with you, but the courts want more evidence to have a trial.").also { stage++ }
            45 -> player(FaceAnim.HALF_ASKING, "Is this the court in Seer's Village?").also { stage++ }
            46 -> npcl(FaceAnim.FRIENDLY, "Yes indeed, we have heard good things of them.").also { stage++ }
            47 -> player(FaceAnim.HALF_ASKING, "Yes, they appeared to be very fair.").also { stage++ }
            48 -> npcl(FaceAnim.FRIENDLY, "A very fair one ... so prove us right, find that evidence! There must be something. We need time to prepare the burial anyway: digging a hole for a coffin is a grave matter. I can teleport you there if you're ready, but only once as I'm very busy.").also { stage++ }
            49 -> options("Yes, I'm ready to go.", "I'm not ready yet. I'll be back.").also { stage++ }
            50 -> when(buttonID) {
                1 -> player(FaceAnim.HALF_ASKING, "Yes, I'm ready to go.").also { stage++ }
                2 -> player("I'm not ready yet. I'll be back.").also { stage = END_DIALOGUE }
            }
            51 -> npcl(FaceAnim.FRIENDLY, "Okay, just click your heels three times and you'll be there.").also { stage++ }
            52 -> player(FaceAnim.SCARED, "Err ... what?").also { stage++ }
            53 -> npcl(FaceAnim.LAUGH, "Only joking, there was a girl last week who believed me, but she disappeared before I could teleport her!").also { stage++ }
            54 -> {
                end()
                val rarve = Repository.findNPC(NPCs.ZAVISTIC_RARVE_2059)
                rarve?.face(player)
                rarve?.animate(Animation(Animations.CAST_SPELL_707))
                teleport(player!!, Location.create(2789, 3175, 0), TeleportManager.TeleportType.RANDOM_EVENT_OLD, 2)
            }

            // 55 -> npcl(FaceAnim.FRIENDLY, "What are you doing... Oh, it's you ... sorry ... didn't realise. What can I do for you?").also { stage++ }
            // 56  -> player(FaceAnim.HALF_ASKING, "I have a rather sandy problem that I'd like to palm off on you.").also { stage++ }
            57  -> npcl(FaceAnim.FRIENDLY, "Do you have anything we can use against that rotten murderer Sandy?").also { stage++ }
            58  -> player(FaceAnim.HALF_ASKING, "I'm sure I could find a sword somewhere ...").also { stage++ }
            59  -> npcl(FaceAnim.FRIENDLY, "No, no. I mean evidence!").also { stage++ }
            60  -> player(FaceAnim.HALF_ASKING, "As a matter of fact, yes. I have his diary!").also { stage++ }
            61  -> npcl(FaceAnim.FRIENDLY, "How does that help us? Surely you wouldn't record that kind of thing in your diary? 'Dear Diary, today I killed a wonderful, talented wizard ... ' that would be silly!").also { stage++ }
            62  -> player(FaceAnim.HALF_ASKING, "Actually, he did! I think he was a few buckets short of a full sandpit.").also { stage++ }
            63  -> npcl(FaceAnim.FRIENDLY, "Well, that's the evidence we need then!").also { stage++ }
            64 -> sendItemDialogue(player!!, Items.UNLOCKED_DIARY_11762, "You hand over the diary and Zavistic hands you some runes as a reward.").also {
                removeItem(player!!, Items.UNLOCKED_DIARY_11762)
                stage++
            }
            65 -> npcl(FaceAnim.FRIENDLY, "This will definitely put Sandy away. Now that we have this we can bury Clarence. For all your hard work you are invited to the ceremony.").also { stage++ }
            66 -> options("Sure. I'd be honoured to attend.", "I'll be back in a bit.").also { stage++ }
            67 -> when(buttonID){
                1 -> player(FaceAnim.FRIENDLY, "Sure. I'd be honoured to attend.").also { stage++ }
                2 -> player("I'll be back in a bit.").also { stage = END_DIALOGUE }
            }

            68 -> player?.let { p ->
                if (!getAttribute(p, GameAttributes.RETURNING_CLARENCE_COMPLETE, false)) {
                    setAttribute(p, GameAttributes.RETURNING_CLARENCE_COMPLETE, true)
                    rewardXP(p, Skills.MAGIC, 10000.0)
                    addItemOrBank(p, Items.BLOOD_RUNE_565, 200)
                    addItemOrBank(p, Items.LAW_RUNE_563, 100)
                }
                FuneralCutscene(p).start(true)
            }

            // 69 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_SIGN_PORTRAIT, false)) {
            //     options("What did you say I should do?", "Where is Sithik?", "I have some items that I'd like you to look at.", "I want to ask about the Magic Guild.", "Sorry, I have to go.").also { stage++ }
            // } else {
            //     options("What did you say I should do?", "Where is Sithik?", "I want to ask about the Magic Guild.", "Sorry, I have to go.").also { stage++ }
            // }

            70 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_SIGN_PORTRAIT, false)) {
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "What did you say I should do?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Where is Sithik?").also { stage = 71 }
                    3 -> player("I have some items that I'd like you to look at.").also { stage = 75 }
                    4 -> playerl(FaceAnim.HALF_GUILTY, "I want to ask about the Magic Guild.").also { stage = 6 }
                    5 -> playerl(FaceAnim.HALF_GUILTY, "Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            } else {
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "What did you say I should do?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Where is Sithik?").also { stage = 71 }
                    3 -> playerl(FaceAnim.HALF_GUILTY, "I want to ask about the Magic Guild.").also { stage = 6 }
                    4 -> playerl(FaceAnim.HALF_GUILTY, "Sorry, I have to go.").also { stage = END_DIALOGUE }
                }
            }

            71 -> npcl(FaceAnim.HALF_GUILTY, "You should go and have a chat with Sithik Ints, he's in that house just to the north.").also { stage++ }
            72 -> npcl(FaceAnim.HALF_GUILTY, "He's a lodger and has a room upstairs. Just tell him that I sent you to see him. He should be fine once you've mentioned my name.").also { stage = END_DIALOGUE }
            73 -> npcl(FaceAnim.HALF_GUILTY, "He's in that house just to the north, less than a few seconds walk away. He's a lodger and has a room upstairs...he's not very well though.").also { stage = END_DIALOGUE }
            74 -> playerl(FaceAnim.HALF_GUILTY, "Sure...I mean, I'll try if I remember.").also { stage = END_DIALOGUE }

            75 -> when {
                inInventory(player!!, Items.NECROMANCY_BOOK_4837) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_1, false) ->
                    sendItemDialogue(player!!, Items.NECROMANCY_BOOK_4837, "You show the Necromancy book to Zavistic.").also { stage++ }

                inInventory(player!!, Items.BOOK_OF_HAM_4829) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_2, false) ->
                    sendItemDialogue(player!!, Items.BOOK_OF_HAM_4829, "You show the HAM book to Zavistic.").also { stage = 80 }

                inInventory(player!!, Items.DRAGON_INN_TANKARD_4811) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_3, false) ->
                    sendDoubleItemDialogue(player!!, -1, Items.DRAGON_INN_TANKARD_4811, "You show the dragon Inn Tankard to Zavistic.").also { stage = 82 }

                inInventory(player!!, ZogreUtils.UNREALIST_PORTRAIT) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_4, false) ->
                    player("Look, I made a portrait of Sithik.").also { stage = 84 }

                inInventory(player!!, ZogreUtils.REALIST_PORTRAIT) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_5, false) ->
                    sendItemDialogue(player!!, ZogreUtils.REALIST_PORTRAIT, "You show the portrait of Sithik to Zavistic.").also { stage = 86 }

                inInventory(player!!, ZogreUtils.SIGNED_PORTRAIT) && !getAttribute(player!!, ZogreUtils.TALK_AGAIN_6, false) ->
                    sendItemDialogue(player!!, ZogreUtils.SIGNED_PORTRAIT, "You show the signed portrait of Sithik to Zavistic.").also { stage = 87 }

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

            76 -> player("I have this necromancy book as evidence that Sithik is", "involved with the undead ogres at Jiggig.").also { stage++ }
            77 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_NECRO_BOOK, false)) {
                npcl(FaceAnim.FRIENDLY, "Yeah, you've shown me this before...if this is all the evidence you have?").also { stage = 92 }
            } else {
                npc("Ok, so he's researching necromancy...it doesn't mean", "anything in itself.").also { stage++ }
            }

            78 -> player("Yes, but if you look, you can see that there is a half", "torn page which matches the page I found at Jiggig.").also { stage++ }
            79 -> npc("Hmm, yes, but someone could have stolen that from him", "and then gone and cast it without his permission or to", "try and deliberately implicate him.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_1, true)
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_NECRO_BOOK, true)
                stage = 75
            }

            80 -> playerl(FaceAnim.FRIENDLY, "Look, this book proves that Sithik hates all monsters and most likely Ogres with a passion.").also { stage++ }
            81 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_NECRO_BOOK, false)) {
                npcl(FaceAnim.FRIENDLY, "Yeah, you've shown me this before...if this is all the evidence you have?").also { stage = 95 }
            } else {
                npcl(FaceAnim.FRIENDLY, "So what, hating monsters isn't a crime in itself...although I suppose that it does give a motive if Sithik was involved. On its own, it's not enough evidence though.").also {
                    setAttribute(player!!, ZogreUtils.TALK_AGAIN_2, true)
                    setAttribute(player!!, ZogreUtils.TALK_AGAIN_ABOUT_HAM_BOOK, true)
                    stage = 75
                }
            }

            82 -> player("This is the tankard I found on the remains of Brentle", "Vahn!").also { stage++ }
            83 -> if (getAttribute(player!!, ZogreUtils.TALK_ABOUT_TANKARD_AGAIN, false)) {
                npcl(FaceAnim.FRIENDLY, "Yeah, you've shown me this before...if this is all the evidence you have?").also { stage = 99 }
            } else {
                npc("That doesn't mean anything in itself, you could have", "gotten that from anywhere. Even from the Dragon Inn", "tavern! There isn't anything to link Brentle Vahn with", "Sithik Ints.").also {
                    setAttribute(player!!, ZogreUtils.TALK_AGAIN_3, true)
                    stage = 75
                }
            }

            84 -> sendItemDialogue(player!!, ZogreUtils.UNREALIST_PORTRAIT, "You show the sketch...").also { stage++ }
            85 -> npcl(FaceAnim.FRIENDLY, "Who the demonikin is that? Is it meant to be a portrait of Sithik, it doesn't look anything like him!").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_4, true)
                stage = 75
            }

            86 -> npcl(FaceAnim.FRIENDLY, "Hmm, great...but I already know what he looks like!").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_5, true)
                stage = 75
            }

            87 -> playerl(FaceAnim.FRIENDLY, "This is a portrait of Sithik, signed by the landlord of the Dragon Inn saying that he saw Sithik and Brentle Vahn together.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_5, true)
                stage++
            }

            88 -> npcl(FaceAnim.FRIENDLY, "Hmmm, well that is interesting.").also { stage++ }
            89 -> npcl(FaceAnim.FRIENDLY, "However, there isn't enough evidence for me to take the issue further at this point. If you find any further evidence bring it to me.").also { stage++ }
            90 -> npcl(FaceAnim.FRIENDLY, "And I'm starting to think that Sithik may be involved. Here, take this potion and give some to Sithik.").also { stage++ }
            91 -> npcl(FaceAnim.FRIENDLY, "It'll bring on a change which should solicit some answers - tell him the effects won't revert until he's told the truth.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_6, true)
                stage = 75
            }

            92 -> playerl(FaceAnim.FRIENDLY, "Please just look at it again...").also { stage++ }
            93 -> npcl(FaceAnim.FRIENDLY, "Ok, let me look then.").also { stage++ }
            94 -> npc(FaceAnim.FRIENDLY, "Ok, so he's researching necromancy...it doesn't mean", "anything in itself.").also { stage = 78 }
            95 -> playerl(FaceAnim.FRIENDLY, "Please just look at it again...").also { stage++ }
            96 -> npcl(FaceAnim.FRIENDLY, "Ok, let me look then.").also { stage++ }
            97 -> sendItemDialogue(player!!, Items.BOOK_OF_HAM_4829, "You show the HAM book to Zavistic, he looks through it again.").also { stage++ }
            98 -> npcl(FaceAnim.FRIENDLY, "So what, hating monsters isn't a crime in itself...although I suppose that it does give a motive if Sithik was involved. On its own, it's not enough evidence though.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_2, true)
                stage = 75
            }

            99 -> playerl(FaceAnim.FRIENDLY, "Please just look at it again...").also { stage++ }
            100 -> npcl(FaceAnim.FRIENDLY, "Ok, let me look then.").also { stage++ }
            101 -> sendItemDialogue(player!!, Items.DRAGON_INN_TANKARD_4811, "You show the tankard to Zavistic, he looks at it again.").also { stage++ }
            102 -> npc("That doesn't mean anything in itself, you could have", "gotten that from anywhere. Even from the Dragon Inn", "tavern! There isn't anything to link Brentle Vahn with", "Sithik Ints.").also {
                setAttribute(player!!, ZogreUtils.TALK_AGAIN_3, true)
                stage = 75
            }

            // 103 -> if (!inInventory(player!!, ZogreUtils.STRANGE_POTION)) {
            //     playerl(FaceAnim.FRIENDLY, "Well, actually, I've lost it, could I have another one please?").also { stage++ }
            // } else {
            //     playerl(FaceAnim.FRIENDLY, "No, not yet, what was I supposed to do again?").also { stage = 106 }
            // }

            104 -> npcl(FaceAnim.HALF_GUILTY, "Sure, but don't lose it this time.").also { stage++ }
            105 -> {
                end()
                if (freeSlots(player!!) < 1) {
                    sendItemDialogue(player!!, ZogreUtils.STRANGE_POTION, "Zavistic hands you a bottle of strange potion, but you don't have enough room to take it.")
                } else {
                    sendItemDialogue(player!!, ZogreUtils.STRANGE_POTION, "Zavistic hands you a bottle of strange potion.")
                    addItem(player!!, ZogreUtils.STRANGE_POTION)
                }
            }

            106 -> npcl(FaceAnim.FRIENDLY, "Try to use the potion on Sithik somehow, he should undergo an interesting transformation, though you'll probably want to leave the house in case there are any side effects. Then go back and question Sithik and tell").also { stage++ }
            107 -> npcl(FaceAnim.FRIENDLY, "him the effects won't wear off until he tells the truth. In fact, that's not exactly true, but I'm sure it'll be an extra incentive to get him to be honest.").also { stage = 5 }

            // 108 -> player(FaceAnim.HALF_GUILTY, "Well, I found this...").also { stage++ }

            109 -> sendDoubleItemDialogue(player!!, -1, Items.DRAGON_INN_TANKARD_4811, "You show the tankard to Zavistic.").also { stage++ }
            110 -> npcl(FaceAnim.THINKING, "Hmmm, no, that's not really associated with this to be honest.").also {
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_TANKARD, true)
                stage = 5
            }

            // 111 -> playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at Jiggig, I've found some clues, I wondered if you'd have a look at them.").also { stage++ }

            112 -> sendDoubleItemDialogue(player!!, Items.BLACK_PRISM_4808, Items.TORN_PAGE_4809, "You show the prism and the necromantic half page to the aged wizard.").also { stage++ }
            113 -> npcl(FaceAnim.HALF_GUILTY, "Hmmm, now this is interesting! Where did you get these from?").also { stage++ }
            114 -> playerl(FaceAnim.HALF_GUILTY, "I got them from a nearby Ogre tomb, it's recently been infested with zombie ogres and I'm trying to work out what happened there.").also { stage++ }
            115 -> npcl(FaceAnim.HALF_GUILTY, "This is very troubling Player, very troubling indeed.").also { stage++ }
            116 -> npcl(FaceAnim.HALF_GUILTY, "While it's permitted for learned members of our order to research the 'dark arts', it's absolutely forbidden to make use of such magic.").also { stage++ }
            117 -> playerl(FaceAnim.HALF_GUILTY, "Do you have any leads on people that I might talk to regarding this?").also { stage++ }
            118 -> npcl(FaceAnim.HALF_GUILTY, "Well a wizard by the name of 'Sithik Ints' was doing some research in this area. He may know something about it.").also { stage++ }
            119 -> npcl(FaceAnim.HALF_GUILTY, "He's lodged at that guest house to the North, though he's ill and isn't able to leave his room.").also { stage++ }
            120 -> npcl(FaceAnim.HALF_GUILTY, "Why not go and talk to him, poke around a bit and see if anything comes up. Let me know how you get on. However,").also { stage++ }
            121 -> npcl(FaceAnim.HALF_GUILTY, "I doubt that 'Sithik' had anything to do with it. There's a severe penalty for using the 'dark arts'. If you find any evidence to the contrary, please bring it to me.").also { stage++ }
            122 -> {
                setAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, true)
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_BLACK_PRISM, true)
                setAttribute(player!!, ZogreUtils.TALK_ABOUT_TORN_PAGE, true)

                if (inInventory(player!!, Items.DRAGON_INN_TANKARD_4811)) {
                    npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?").also { stage = 5 }
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "Not really").also { stage++ }
                }
            }
            123 -> npcl(FaceAnim.HALF_GUILTY, "I don't know what to say then, there isn't enough to go on with the clues you've shown me so far.").also { stage++ }
            124 -> npcl(FaceAnim.THINKING, "I'd suggest going back to search a bit more, but you may just be wasting your time? Hmm, but this prism does seem to have some magical protection.").also { stage++ }
            125 -> npcl(FaceAnim.HALF_GUILTY, "Once you've finished with this item, bring it back to me would you? I may have a reward for you!").also { stage++ }
            126 -> playerl(FaceAnim.HALF_GUILTY, "Sure...I mean, I'll try if I remember.").also { stage = 5 }

            // 127 -> if (getAttribute(player!!, ZogreUtils.TALK_WITH_ZAVISTIC_DONE, false)) {
            //     playerl(FaceAnim.FRIENDLY, "I found this black prism at Jiggig where the undead ogre activity was happening?").also { stage = 131 }
            // } else {
            //     playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at 'Jiggig', and the ogres have asked me to look into it. I think I've found a clue and I wonder if you could take a look at it for me?").also { stage++ }
            // }

            128 -> sendDoubleItemDialogue(player!!, -1, Items.BLACK_PRISM_4808, "You show the black prism to the aged wizard.").also { stage++ }
            129 -> npcl(FaceAnim.FRIENDLY, "Hmmm, well this is an uncommon spell component. On it's own it's useless, but with certain necromantic spells it can be very powerful.").also { stage++ }
            130 -> {
                npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?")
                stage = when {
                    inInventory(player!!, Items.TORN_PAGE_4809) &&
                            !getAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false) -> 0

                    inInventory(player!!, Items.DRAGON_INN_TANKARD_4811) &&
                            !getAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false) -> 0

                    else -> 5
                }
            }
            131 -> sendDoubleItemDialogue(player!!, -1, Items.BLACK_PRISM_4808, "You show the black prism to the aged wizard.").also { stage++ }
            132 -> npcl(FaceAnim.FRIENDLY, "Yes, you've already showed me that, bring it to me when you've resolved the problems at Jiggig and I'll see what I can do.").also { stage = END_DIALOGUE }
            133 -> sendDialogue(player!!, "You show the black prism to Zavistic.").also { stage++ }
            134 -> npcl(FaceAnim.FRIENDLY, "Ah yes, I remember saying something about a reward didn't I? Well, I can offer you 2000 coins for it as it stands,").also { stage++ }
            135 -> npcl(FaceAnim.FRIENDLY,  "but I know that Yanni Salika in Shilo Village would offer you more than twice as much.").also { stage++ }
            136 -> {
                setTitle(player!!, 2)
                sendOptions(player!!, "WHO WOULD YOU LIKE TO SELL THE PRISM TO?", "Sell it to Zavistic for 2000", "Take it to Yanni for a greater reward.")
                stage++
            }
            137 -> when (buttonID) {
                1 -> player("I'll sell it to you for 2000 coins!").also { stage = 139 }
                2 -> player("I think I'm going to take it to Yanni for an even greater reward.").also { stage++ }
            }
            138 -> npc("Fair enough my friend, you deserve it!").also { stage = END_DIALOGUE }
            139 -> npc("Very well my friend.").also { stage++ }
            140 -> {
                end()
                if (removeItem(player!!, Items.BLACK_PRISM_4808)) {
                    sendMessage(player!!, "You sell the black prism for 2000 coins.")
                    addItemOrDrop(player!!, Items.COINS_995, 2000)
                    npc("Thanks!")
                }
            }

            // 141 -> playerl(FaceAnim.HALF_GUILTY, "There's some undead ogre activity over at Jiggig, I've found a clue that you may be able to help with.").also { stage++ }

            142 -> sendDoubleItemDialogue(player!!, -1, Items.TORN_PAGE_4809, "You show the necromantic half page to the aged wizard.").also { stage++ }
            143 -> npcl(FaceAnim.HALF_ASKING, "Hmm, this is a half torn spell page, it requires another spell component to be effective.").also { stage++ }
            144 -> {
                npcl(FaceAnim.HALF_ASKING, "Did you find anything else there?")
                stage = when {
                    inInventory(player!!, Items.BLACK_PRISM_4808) &&
                            !getAttribute(player!!, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false) -> 133
                    else -> 5
                }
            }
        }
    }
}