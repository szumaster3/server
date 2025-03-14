package content.region.kandarin.quest.zogre.dialogue

import content.region.kandarin.quest.zogre.handlers.ZUtils
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class SithikDialogueFiles : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> npc("What do you want now?").also { stage++ }
            1 -> player("Hey there's no need to be rude!").also { stage++ }
            2 ->
                npc(
                    "What do you expect when you just go snooping around a",
                    "person's place against their express permission.",
                ).also {
                    stage++
                }

            3 ->
                options(
                    "What do you do?",
                    "Why do you spend most of your time in bed?",
                    "Ok, thanks.",
                ).also { stage++ }
            4 ->
                when (buttonID) {
                    1 -> playerl("What do you do?").also { stage++ }
                    2 -> playerl("Why do you spend most of your time in bed?").also { stage = 11 }
                    3 -> playerl("Ok, thanks.").also { stage = END_DIALOGUE }
                }
            5 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm a scholarly student of the magical arts. When I was",
                    "younger I used to be an adventurer, probably just like",
                    "yourself. But I lost interest in the constant fighting,",
                    "looting and gaining abilities.",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    FaceAnim.CALM,
                    "Instead I decided to focus my attention and time to",
                    "study the purer form of the lost arts.",
                ).also {
                    stage++
                }
            7 -> player("The lost arts? What are they?").also { stage++ }
            8 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Ignorant people call them the 'dark arts'. I'm talking",
                    "about Necromancy, the power to bring the dead back to",
                    "life - the power of the gods! Surely the most awesome",
                    "power known to man.",
                ).also {
                    stage++
                }
            9 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Hmm, well I guess I must be an ignorant person then,",
                    "because bringing the dead back to life sounds very",
                    "unnatural.",
                ).also {
                    stage =
                        3
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm actually quite old and not so very well and I'd like",
                    "to get over this illness I have, then I'll return to my",
                    "very serious and important studies.",
                ).also {
                    stage =
                        3
                }
        }
    }
}

class SithikPermissionDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> npc(FaceAnim.NEUTRAL, "Hey...who gave you permission to come in here!").also { stage++ }
            1 -> npc(FaceAnim.ANNOYED, "Get out, get out I say.").also { stage++ }
            2 -> playerl("Alright, alright...keep your night cap on.").also { stage = END_DIALOGUE }
        }
    }
}

class SithikQuestDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> npc(FaceAnim.CALM, "Hey...who gave you permission to come in here!").also { stage++ }
            1 ->
                player(
                    "Zavistic Rarve said that I could come and talk to you",
                    "and ask you a few questions.",
                ).also { stage++ }

            2 -> npc(FaceAnim.HALF_THINKING, "Oh, Zavistic...why...why would he send you to me?").also { stage++ }
            3 ->
                if (getAttribute(player!!, ZUtils.ASK_SITHIK_ABOUT_OGRES, false)) {
                    options(
                        "Do you know anything about the undead ogres at Jiggig?",
                        "What do you do?",
                        "Do you mind if I look around?",
                        "Ok, thanks.",
                    ).also { stage++ }
                } else if (getAttribute(player!!, ZUtils.ASK_SITHIK_AGAIN, false)) {
                    options(
                        "What do you do?",
                        "Why do you spend most of your time in bed?",
                        "Ok, thanks.",
                    ).also { stage++ }
                }

            4 ->
                if (!getAttribute(player!!, ZUtils.ASK_SITHIK_AGAIN, false)) {
                    when (buttonID) {
                        1 ->
                            player(
                                FaceAnim.HALF_GUILTY,
                                "Do you know anything about the undead ogres at",
                                "Jiggig?",
                            ).also { stage++ }
                        2 -> playerl(FaceAnim.HALF_GUILTY, "What do you do?").also { stage = 14 }
                        3 ->
                            if (getAttribute(player!!, ZUtils.ASK_SITHIK_ABOUT_OGRES, false)) {
                                playerl(FaceAnim.HALF_GUILTY, "Do you mind if I look around?").also { stage = 19 }
                            } else {
                                playerl(FaceAnim.HALF_GUILTY, "Why do you spend most of your time in bed?").also {
                                    stage =
                                        23
                                }
                            }

                        4 -> playerl(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
                    }
                } else {
                    when (buttonID) {
                        1 -> playerl(FaceAnim.HALF_GUILTY, "What do you do?").also { stage = 14 }
                        2 ->
                            if (getAttribute(player!!, ZUtils.ASK_SITHIK_ABOUT_OGRES, false)) {
                                playerl(FaceAnim.HALF_GUILTY, "Do you mind if I look around?").also { stage = 19 }
                            } else {
                                playerl(FaceAnim.HALF_GUILTY, "Why do you spend most of your time in bed?").also {
                                    stage =
                                        23
                                }
                            }

                        3 -> playerl(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
                    }
                }

            5 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Er...undead ogres...no, sorry, no idea what you're",
                    "talking about there.",
                ).also {
                    stage++
                }
            6 -> playerl(FaceAnim.HALF_GUILTY, "Hmm, is that right...").also { stage++ }
            7 -> npcl(FaceAnim.HALF_GUILTY, "Well, yes, yes it is. If I knew something, I'd tell you.").also { stage++ }
            8 ->
                npc(
                    FaceAnim.HAPPY,
                    "Anyways, dead ogres you say? How strange? That must",
                    "be a strange sight?",
                ).also { stage++ }
            9 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Very well, if you don't know anything about it, you",
                    "won't mind if I look around then?",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    FaceAnim.ANNOYED,
                    "Well, err....well, actually yes I do mind...it's my place",
                    "and I don't want strangers going through my things.",
                ).also {
                    stage++
                }
            11 ->
                player(
                    "Well, I'm going to have a look around anyway, if",
                    "you're not involved in this whole thing, you won't have",
                    "anything to hide.",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.ANNOYED,
                    "Why, if I was a few years younger I'd give you a",
                    "good hiding!",
                ).also { stage++ }
            13 ->
                player("I'm sure!").also {
                    setAttribute(player!!, "/save:${ZUtils.ASK_SITHIK_ABOUT_OGRES}", true)
                    stage = 3
                }
            14 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm a scholarly student of the magical arts. When I was",
                    "younger I used to be an adventurer, probably just like",
                    "yourself. But I lost interest in the constant fighting,",
                    "looting and gaining abilities.",
                ).also {
                    stage++
                }
            15 ->
                npc(
                    FaceAnim.CALM,
                    "Instead I decided to focus my attention and time to",
                    "study the purer form of the lost arts.",
                ).also {
                    stage++
                }
            16 -> player("The lost arts? What are they?").also { stage++ }
            17 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Ignorant people call them the 'dark arts'. I'm talking",
                    "about Necromancy, the power to bring the dead back to",
                    "life - the power of the gods! Surely the most awesome",
                    "power known to man.",
                ).also {
                    stage++
                }
            18 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Hmm, well I guess I must be an ignorant person then,",
                    "because bringing the dead back to life sounds very",
                    "unnatural.",
                ).also {
                    stage =
                        3
                }
            19 ->
                npc(
                    FaceAnim.CALM,
                    "Well, err....well, actually yes I do mind...it's my place",
                    "and I don't want strangers going through my things.",
                ).also {
                    stage++
                }
            20 ->
                player(
                    "Well, I'm going to have a look around anyway, if",
                    "you're not involved in this whole thing, you won't have",
                    "anything to hide.",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    FaceAnim.ANNOYED,
                    "Why, if I was a few years younger I'd give you a",
                    "good hiding!",
                ).also { stage++ }
            22 ->
                playerl("I'm sure!").also {
                    setAttribute(player!!, "/save:${ZUtils.ASK_SITHIK_AGAIN}", true)
                    stage = 3
                }
            23 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm actually quite old and not so very well and I'd like",
                    "to get over this illness I have, then I'll return to my",
                    "very serious and important studies.",
                ).also {
                    stage =
                        3
                }
        }
    }
}

class SithikIntsPortraitureBookDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl("Oh, so explain this then?").also { stage++ }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.BOOK_OF_PORTRAITURE_4817,
                    "You show the book on portraiture to Sithik.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    "It's my hobby...I'm interested in portraiture, but all art in general. It's fun, you should try it.",
                ).also {
                    stage++
                }
            3 -> playerl("How do I do it...").also { stage++ }
            4 -> npcl("Well...you could start by reading the book!").also { stage = END_DIALOGUE }
        }
    }
}

class SithikIntsHamBookDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl("What's this then?").also { stage++ }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.BOOK_OF_HAM_4829,
                    "You show the HAM book to Sithik.",
                ).also { stage++ }
            2 ->
                npcl(
                    "What do you mean? It's a book by the respected HAM leader Johanhus Ulsbrecht, that man speaks for a lot of people who are unhappy with the current state of affairs.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    "Can you honestly tell me that you've not had to fight for your life against the odd monster or two?",
                ).also {
                    stage++
                }
            4 ->
                playerl(
                    "Hmm, that may be true, but I don't universally hate all monsters, whereas I have a sneaking suspicion that you do...and ogres in particular!",
                ).also {
                    stage++
                }
            5 ->
                npcl("Hmm, that's an interesting theory, care to back it up with any facts?").also {
                    stage = END_DIALOGUE
                }
        }
    }
}

class SithikIntsNecromancyBookDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl("Aha! A necromantic book! What's this doing here then?").also { stage++ }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.NECROMANCY_BOOK_4837,
                    "You show the Necromantic book to Sithik.",
                ).also {
                    stage++
                }
            2 -> npcl("Oh..I'm not quite sure actually...where did you find that then?").also { stage++ }
            3 -> playerl("I found it in this cupboard! What do you have to say for yourself?").also { stage++ }
            4 ->
                npcl(
                    "Oh yes, that's right...I remember now. It's for my research, there's nothing really dangerous about it, unless it falls into the wrong hands. I'm sure it's pretty safe with me.",
                ).also {
                    stage++
                }
            5 -> playerl("Hmmm, likely story!").also { stage = END_DIALOGUE }
        }
    }
}

class SithikIntsTornPageDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl("Have you ever seen anything like this before?").also { stage++ }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.TORN_PAGE_4809,
                    "You show the torn page to Sithik.",
                ).also { stage++ }
            2 ->
                npcl(
                    "It's probably a piece of rubbish someone threw away...what does it say, I can't read it?",
                ).also { stage++ }
            3 ->
                playerl(
                    "You should be able to read it, it's been torn from a book on necromancy and you're meant to be a specialist in the subject.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    "Oh, no..., not really a specialist, just a hobby of mine really. Hardly know anything about it, but it does seem interesting...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class SithikIntsBlackPrismDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl("Hey, what's this then, can you explain it?!").also { stage++ }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.BLACK_PRISM_4808,
                    "You show the black prism to Sithik.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    "Err..it looks sort of familiar, did you steal it from me? Come to think of it, you have the appearance of a common thief!",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    "I found it in a place called Jiggig where some undead ogres happen to be wandering around.",
                ).also {
                    stage++
                }
            4 -> npcl("Oh, nothing to do with me then, never seen it in my life before!").also { stage = END_DIALOGUE }
        }
    }
}

class SithikIntsDragonTankardDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl("What about this then? Guess where I found this?").also { stage++ }
            1 ->
                sendDoubleItemDialogue(
                    player!!,
                    -1,
                    Items.DRAGON_INN_TANKARD_4811,
                    "You show the tankard to Sithik.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    "You probably found it at the local brewhouse! It doesn't take a genius to figure that one out.",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    "Aha! But I found this in an old ogre tomb! I suspect it's a clue which will lead me to the suspect.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    "Hmmm, well that eliminates all the local people who don't actually drink at the 'Dragon Inn'. ",
                ).also {
                    stage++
                }
            5 ->
                npcl("When do you think you'll start questioning the remaining population of Yanille?").also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class SithikIntsPortraitDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val papyrus = Item(Items.PAPYRUS_970)
        val correctPortrait = ZUtils.REALIST_PORTRAIT
        val incorrectPortrait = ZUtils.UNREALIST_PORTRAIT
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> npcl("Oh lovely! You're making my portrait! Let me see it afterwards!").also { stage++ }
            1 -> sendDialogue(player!!, "You begin sketching the irritable Sithik.").also { stage++ }
            2 -> {
                submitIndividualPulse(
                    player!!,
                    object : Pulse() {
                        override fun pulse(): Boolean {
                            if (removeItem(player!!, papyrus)) {
                                animate(player!!, 909)
                                when ((0..1).random()) {
                                    0 -> {
                                        addItem(player!!, correctPortrait)
                                        sendItemDialogue(
                                            player!!,
                                            ZUtils.REALIST_PORTRAIT,
                                            "You get a portrait of Sithik.",
                                        )
                                    }

                                    1 -> {
                                        addItem(player!!, incorrectPortrait)
                                        sendItemDialogue(
                                            player!!,
                                            ZUtils.UNREALIST_PORTRAIT,
                                            "You get a portrait of Sithik.",
                                        )
                                    }
                                }
                            }
                            player!!.interfaceManager.closeChatbox()
                            sendMessage(player!!, "Nothing interesting happens.")
                            return true
                        }
                    },
                )
            }
        }
    }
}

class SithikIntsUsedPortraitDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl(FaceAnim.HAPPY, "Here you go, what do you think?").also { stage++ }
            1 ->
                if (inInventory(player!!, ZUtils.REALIST_PORTRAIT)) {
                    sendItemDialogue(
                        player!!,
                        ZUtils.REALIST_PORTRAIT,
                        "You get a portrait of Sithik.",
                    ).also { stage++ }
                } else {
                    sendItemDialogue(player!!, ZUtils.UNREALIST_PORTRAIT, "You show the sketch...").also { stage = 3 }
                }
            2 ->
                npcl(
                    FaceAnim.THINKING,
                    "Hmmm, well it's not the most flattering of portraits, but I like the 'honesty' of the work...well done.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            3 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Hmmm, well it's an interesting interpretation, but not really classic realist representation is it? It's not my favourite, but I like the 'truth' of the work...well done.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class SithikIntsSignedPortraitDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 ->
                playerl(
                    "Hey, what do you think of this? I'm going to show it to Zavistic and you're going to be in trouble!",
                ).also {
                    stage++
                }
            1 -> sendItemDialogue(player!!, ZUtils.SIGNED_PORTRAIT, "You show the portrait to Sithik.").also { stage++ }
            2 ->
                npcl(
                    "Hmmm, well, I've got quite a common looking face, I'm often mistaken for other wizards, you know, when I'm wearing my wizard's hat, robes and staff. There's a lot of us around here you know.",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    "I don't think so! This is a signed picture of you, someone recognised you, you're in deep trouble!",
                ).also {
                    stage++
                }
            4 -> npcl("Ok, I'll pay you to keep this secret - how much do you want for the picture?").also { stage++ }
            5 -> playerl("You can't buy me Sithik!").also { stage++ }
            6 ->
                playerl(
                    FaceAnim.HALF_THINKING,
                    "Ok, let's say two million...two million to keep quiet and give me the picture.",
                ).also {
                    stage++
                }
            7 ->
                sendDoubleItemDialogue(
                    player!!,
                    Items.COINS_8897,
                    Items.COINS_8897,
                    "Sithik shows you a chest brimming over with coins...",
                ).also {
                    stage++
                }
            8 ->
                playerl(
                    FaceAnim.HALF_WORRIED,
                    "Oh...erm...well, that is a lot of money actually...er....",
                ).also { stage++ }
            9 -> npcl("Yes, and you deserve it, you're very clever! Now, take the money...").also { stage++ }
            10 -> {
                setTitle(player!!, 2)
                options(
                    "No, I won't take the money, I'm going to bring you to justice!",
                    "Ok, I'll shut up for two million!",
                    title = "Be bribed by Sithik for 2 million?",
                )
                stage++
            }
            11 ->
                when (buttonID) {
                    1 ->
                        player(
                            FaceAnim.GUILTY,
                            "No, I won't take the money, I'm going to bring you to justice!",
                        ).also { stage++ }
                    2 -> playerl("Ok, I'll shut up for two million!").also { stage = 13 }
                }
            12 ->
                npcl(
                    "Oh well, suit yourself! I wasn't going to give you the money anyway! No one will believe some crazy adventurer and an Inn keep.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            13 ->
                npcl(
                    FaceAnim.LAUGH,
                    "Ha! Ha! You believed me! I'm not going to give you all my money! No one will believe a crazy adventurer and a local Inn keep!",
                ).also {
                    stage++
                }
            14 ->
                player(FaceAnim.SAD, "You're a mean and cruel man Sithik, a mean and cruel man!").also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class SithikIntsStrangePotionDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2061)
        when (stage) {
            0 -> playerl(FaceAnim.HAPPY, "Here, try some of this potion, it'll make you feel better!").also { stage++ }
            1 ->
                npc(
                    FaceAnim.SCARED,
                    "Err, yuck....no way am I taking any potions or",
                    "medication off you...I don't trust you!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class SithikIntsAfterTransformDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2062)
        when (stage) {
            0 -> npcl(FaceAnim.OLD_ANGRY1, "Arghhhh..what's happened to me...you beast!").also { stage++ }
            1 ->
                player(
                    "It's your own fault, you shouldn't have lied about your",
                    "involvement with the undead Ogres at Jiggig. The",
                    "potion will wear off once you've told the truth!",
                ).also {
                    setAttribute(player!!, "/save:${ZUtils.TALK_WITH_SITHIK_OGRE_DONE}", true)
                    stage++
                }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Ok, ok, I admit it, I got Brentle Vahn to cast the spell",
                    "to put an end to those awful Ogres...they're just",
                    "disgusting creatures...",
                ).also {
                    stage++
                }
            3 -> player("Ok, that's a start...now I want some answers.").also { stage++ }
            4 ->
                options(
                    "How do I remove the effects of the spell from the area?",
                    "How do I get rid of the undead ogres?",
                    "How do I get rid of the disease?",
                    "Sorry, I have to go.",
                ).also { stage++ }

            5 ->
                when (buttonID) {
                    1 ->
                        player(
                            "How do I remove the effects of the spell from the area?",
                            "The ogres want to get their ceremonial dance area back",
                            "and can't do that with undead walking all over it.",
                        ).also {
                            stage++
                        }

                    2 -> player("How do I get rid of the undead ogres?").also { stage = 9 }
                    3 -> player("How do I get rid of the disease?").also { stage = 15 }
                    4 -> player("Sorry, I have to go.").also { stage = 18 }
                }
            6 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Unfortunately you can't. The spell is permanent, it will",
                    "last forever, the only option you have is to move the",
                    "ceremonial area.",
                ).also {
                    stage++
                }
            7 ->
                player(
                    "You're an evil man and I'm going to make you pay for",
                    "this...you can stay like that forever as far as I'm",
                    "concerned.",
                ).also {
                    stage++
                }
            8 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "No...no, let me try to make amends...please I can help",
                    "you. Just don't leave me like this.",
                ).also {
                    stage =
                        4
                }
            9 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Ok, similar spells have been cast before and the only",
                    "way to deal with the resulting creatures is to cordon off",
                    "the area and not go in there again.",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "The undead creatures usually manifest some sort of",
                    "disease so it's best to attack them from a distance with a",
                    "ranged weapon.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Normal missiles like arrows and darts do very little",
                    "damage to them because they're designed to destroy",
                    "internal organs. This is a waste of time with undead",
                    "creatures like undead ogres.",
                ).also {
                    stage++
                }
            12 -> player("Yeah, clearly so what should we use?").also { stage++ }
            13 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "From my research it looks like a flat ended arrow was",
                    "designed called a 'Brutal arrow'. This does large",
                    "amounts of crushing damage to the creature. You can",
                    "make them by using larger arrows.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "I think some Ogre hunters make them. But instead of",
                    "adding an arrowhead, you hammer a large nail into the",
                    "end of the shaft.",
                ).also {
                    stage =
                        4
                }
            15 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "My research shows that two jungle based herbs can be",
                    "used, one is found near river tributaries and looks like",
                    "a vine, the other is found in caves and grows on the",
                    "wall.",
                ).also {
                    stage++
                }
            16 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "It's quite well camouflaged so it's unlikely that you'll",
                    "find it.",
                ).also { stage++ }
            17 -> player("We'll see about that!").also { stage = 4 }
            18 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "But...you can't just leave me here like this!",
                ).also { stage = END_DIALOGUE }
        }
    }
}

class SithikTalkAgainAfterTransformDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SITHIK_INTS_2062)
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Arghhhh..what do you want now...you've turned me into",
                    "a beast!",
                ).also { stage++ }
            1 ->
                player(
                    "I've got some questions for you...and you'd",
                    "better answer them well or else!",
                ).also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Ok, ok, I'll tell you anything, just turn me back into a",
                    "human again!",
                ).also {
                    stage++
                }
            3 -> end().also { openDialogue(player!!, SithikIntsAfterTransformDialogueFile()) }
        }
    }
}
