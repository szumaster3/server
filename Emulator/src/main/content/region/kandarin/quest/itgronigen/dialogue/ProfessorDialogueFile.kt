package content.region.kandarin.quest.itgronigen.dialogue

import content.region.kandarin.quest.itgronigen.handlers.ObservatoryListener
import content.region.kandarin.quest.itgronigen.handlers.ObservatoryListener.Companion.FAIL_ATTRIBUTE
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ProfessorDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_PROFESSOR_488)
        when (stage) {
            0 ->
                when {
                    getQuestStage(player!!, Quests.OBSERVATORY_QUEST) in 2..3 -> player("Hi again!").also { stage = 40 }
                    getQuestStage(
                        player!!,
                        Quests.OBSERVATORY_QUEST,
                    ) in 4..5 -> npcl("The traveller returns!").also { stage = 48 }

                    getQuestStage(
                        player!!,
                        Quests.OBSERVATORY_QUEST,
                    ) in 7..8 -> npcl("How are you getting on finding me some molten glass?").also { stage = 61 }

                    getQuestStage(
                        player!!,
                        Quests.OBSERVATORY_QUEST,
                    ) in 9..10 -> npcl("Did you bring me the mould?").also { stage = 72 }

                    getQuestStage(player!!, Quests.OBSERVATORY_QUEST) == 11 ->
                        npc("Is the lens finished?").also {
                            stage = 82
                        }

                    getQuestStage(player!!, Quests.OBSERVATORY_QUEST) == 13 -> npc("Hello, friend.").also { stage = 92 }
                    getQuestStage(player!!, Quests.OBSERVATORY_QUEST) == 14 -> npc("Hello, friend.").also { stage = 94 }
                    else -> player("Hi, I was...").also { stage++ }
                }

            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Welcome to the magnificent wonder of the Observatory, where wonder is all around you, where the stars can be clutched from the heavens!",
                ).also { stage++ }

            2 -> player("Wow, nice intro.").also { stage++ }
            3 -> npcl(FaceAnim.HAPPY, "Why, thanks! How might I help you?").also { stage++ }
            4 -> options("I'm totally lost.", "An Observatory?", "I'm just passing through.").also { stage++ }
            5 ->
                when (buttonID) {
                    1 -> player("I'm totally lost.").also { stage = 8 }
                    2 -> player("An Observatory?").also { stage = 11 }
                    3 -> player("I'm just passing through.").also { stage++ }
                }

            6 -> npc("Fair enough. Not everyone is interested in", "this place, I suppose.").also { stage++ }
            7 -> sendDialogue(player!!, "The professor carries on with his studies.").also { stage = END_DIALOGUE }
            8 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "Lost? It must have been those pesky goblins that led you astray. Head north-east to find the city of Ardougne.",
                ).also { stage++ }

            9 -> player("I'm sure I'll find the way. Thanks for all", "your help.").also { stage++ }
            10 -> npcl(FaceAnim.HAPPY, "No problem at all. Come and visit again!").also { stage = 7 }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Of course. We have a superb telescope up in the Observatory, on the hill.",
                ).also { stage++ }

            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "A truly marvellous invention, the likes of which you'll never behold again.",
                ).also { stage++ }

            13 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "Well, it would be if it worked.",
                ).also { stage++ }

            14 -> npcl(FaceAnim.HALF_GUILTY, "Don't interrupt!").also { stage++ }
            15 -> playerl(FaceAnim.HALF_GUILTY, "What? It doesn't work?").also { stage++ }
            16 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, no, no, no. Don't listen to him, he's joking. Aren't you, my FAITHFUL assistant?",
                ).also { stage++ }

            17 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "Nope, dead serious. Hasn't been working for a long time.",
                ).also { stage++ }

            18 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Arghhh! Get back to work and stop sticking your nose in!",
                ).also { stage++ }

            19 -> playerl(FaceAnim.HALF_GUILTY, "So, it's broken. How come?").also { stage++ }
            20 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, I suppose there's no use keeping it secret. Did you see those houses outside?",
                ).also { stage++ }

            21 -> playerl(FaceAnim.HALF_GUILTY, "Up on the hill? Yes, I've seen them.").also { stage++ }
            22 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "It's a horde of goblins. Since they moved here they have caused nothing but trouble. Last week, my telescope was tampered with.",
                ).also { stage++ }

            23 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Now, parts need replacing before it can be used again. They've even been messing around in the dungeons under this area. Something needs to be done.",
                ).also { stage++ }

            24 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "Strikes me that this visitor could help us.",
                ).also { stage++ }

            25 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Stop being so rude. ... Although, he has a point. What do you say?",
                ).also { stage++ }

            26 -> playerl(FaceAnim.HALF_GUILTY, "What, me?").also { stage++ }
            27 -> options("Not right now", "Sounds interesting").also { stage++ }
            28 ->
                when (buttonID) {
                    1 -> player("Oh, sorry, I don't have time for that.").also { stage++ }
                    2 -> player("Sounds interesting, what can I do for you?").also { stage = 30 }
                }

            29 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh dear. I really do need some help. If you see anyone who can help then please send them my way.",
                ).also { stage = END_DIALOGUE }

            30 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, thanks so much. I shall need some materials for the telescope, so it can be used again. Let's start with three planks of wood for the telescope base.",
                ).also { stage++ }

            31 -> npcl(FaceAnim.HALF_GUILTY, "My assistant will help with obtaining these, won't you?").also { stage++ }
            32 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "As if I don't have enough to do. Seems I don't have a choice.",
                ).also { stage++ }

            33 -> npcl(FaceAnim.HALF_GUILTY, "Go talk to him if you need some advice.").also { stage++ }
            34 -> playerl(FaceAnim.HALF_GUILTY, "Okay, I'll be right back.").also { stage++ }
            35 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 2)
            }

            40 -> npcl(FaceAnim.HAPPY, "It's my helping hand, back again.").also { stage++ }
            41 -> npcl(FaceAnim.ASKING, "Do you have the planks yet?").also { stage++ }
            42 ->
                if (amountInInventory(player!!, Items.PLANK_960) < 3) {
                    player(FaceAnim.HALF_GUILTY, "Sorry, not yet. Three planks was it?").also { stage++ }
                } else {
                    player(FaceAnim.NOD_YES, "Yes, I've got them. Here they are.").also {
                        removeItem(player!!, Item(Items.PLANK_960, 3))
                        runTask(player!!, 0) {
                            animate(player!!, 4540)
                            findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                        }
                        stage += 2
                    }
                }

            43 -> npc(FaceAnim.FRIENDLY, "It was indeed.").also { stage = END_DIALOGUE }
            44 -> npcl(FaceAnim.FRIENDLY, "Well done. This will make a big difference.").also { stage++ }
            45 -> npcl("Now, the bronze for the tube. Oh, assistant!").also { stage++ }
            46 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Okay, okay, ask me if you need any help, ${player!!.username}.",
                ).also { stage++ }

            47 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 4)
            }

            48 -> player("Still working hard?").also { stage++ }
            49 -> sendNPCDialogue(player!!, NPCs.OBSERVATORY_ASSISTANT_6118, "Some of us are.").also { stage++ }
            50 -> npcl(FaceAnim.ANNOYED, "What did I tell you about speaking when spoken to?").also { stage++ }
            51 -> npcl(FaceAnim.HAPPY, "So, ${player!!.username}, you have the bronze bar?").also { stage++ }
            52 ->
                if (!inInventory(player!!, Items.BRONZE_BAR_2349)) {
                    player("Not yet.").also { stage++ }
                } else {
                    player("I certainly do. Here you go.").also {
                        removeItem(player!!, Item(Items.BRONZE_BAR_2349, 1))
                        runTask(player!!, 0) {
                            animate(player!!, 4540)
                            findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                        }
                        stage += 2
                    }
                }

            53 -> npcl("Please bring me one, then.").also { stage = END_DIALOGUE }
            54 -> npcl(FaceAnim.HAPPY, "Great. Now all I need is the lens made.").also { stage++ }
            55 -> npcl(FaceAnim.HAPPY, "Please get me some molten glass.").also { stage++ }
            56 -> npcl(FaceAnim.NEUTRAL, "Oi! Lazy bones!").also { stage++ }
            57 -> player(FaceAnim.SCARED, "What? I'm not lazy.").also { stage++ }
            58 -> npcl(FaceAnim.NEUTRAL, "Not you! I'm talking to my assistant.").also { stage++ }
            59 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "Calm down old man, I heard. ${player!!.username}, I'm here if you need any help.",
                ).also { stage++ }

            60 ->
                npcl(FaceAnim.NEUTRAL, "Thank you. Wait a minute, who are you calling 'old'?").also {
                    end()
                    setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 6)
                }

            61 ->
                if (!inInventory(player!!, Items.MOLTEN_GLASS_1775)) {
                    player("Still working on it.").also { stage++ }
                } else {
                    player("Here it is.").also {
                        removeItem(player!!, Item(Items.MOLTEN_GLASS_1775, 1))
                        runTask(player!!, 0) {
                            animate(player!!, 4540)
                            findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                        }
                        stage += 2
                    }
                }

            62 -> npcl("I really need it. Please hurry.").also { stage = END_DIALOGUE }
            63 -> npcl("Excellent work, let's make the lens.").also { stage++ }
            64 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "It'll need to be made to an exact shape and size.",
                ).also { stage++ }

            65 -> npcl("Well, obviously, hence why we have a lens mould.").also { stage++ }
            66 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "Not any more. One of those goblins took it.",
                ).also { stage++ }

            67 ->
                npcl(
                    FaceAnim.SAD,
                    "Great, just what I need. ${player!!.username}, I don't suppose you could find it?",
                ).also { stage++ }

            68 -> playerl("I'll have a look - where should I start?").also { stage++ }
            69 -> npcl(FaceAnim.HAPPY, "No idea. You could ask my USELESS assistant if you want.").also { stage++ }
            70 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "What have I done to deserve this?",
                ).also { stage++ }

            71 -> {
                end()
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 8)
            }

            72 ->
                if (!inInventory(player!!, Items.LENS_MOULD_602)) {
                    player("Still looking for it.").also { stage++ }
                } else {
                    player("I certainly have. You'll never guess what they were", "doing with it.").also { stage += 2 }
                }

            73 -> npcl("Please try and find it; my assistant may be able to help.").also { stage = END_DIALOGUE }
            74 ->
                npc(
                    FaceAnim.HALF_THINKING,
                    "Well, from the smell I'd guess cooking some vile",
                    "concoction.",
                ).also { stage++ }

            75 -> player("Wow, good guess. Well, here you go.").also { stage++ }
            76 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.OBSERVATORY_ASSISTANT_6118,
                    "Please don't give that to him. Last time he tried any Crafting, I had to spend a week cleaning up after the explosion.",
                ).also { stage++ }

            77 -> player(FaceAnim.SCARED, "Explosion?").also { stage++ }
            78 -> npc("Erm, yes. I think in this instance you had probably", "better do it.").also { stage++ }
            79 -> player("I suppose it's better I don't ask.").also { stage++ }
            80 -> npc("You can use the mould with molten glass to make a", "new lens.").also { stage++ }
            81 -> {
                end()
                runTask(player!!, 0) {
                    animate(player!!, 4540)
                    findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                }.also {
                    sendItemDialogue(
                        player!!,
                        Items.MOLTEN_GLASS_1775,
                        "The professor gives you back the molten glass.",
                    )
                    addItemOrDrop(player!!, Items.MOLTEN_GLASS_1775)
                    setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 11)
                }
            }

            82 ->
                if (!inInventory(player!!, Items.OBSERVATORY_LENS_603)) {
                    player("How do I make it again?").also { stage++ }
                } else {
                    player("I certainly have. You'll never guess what they were", "doing with it.").also { stage += 3 }
                }

            83 -> npc("Use the molten glass with the mould.").also { stage++ }
            84 -> player("Huh. Simple.").also { stage = END_DIALOGUE }
            85 -> playerl("Yes, here it is. You may as well take this mould too.").also { stage++ }
            86 -> {
                runTask(player!!, 0) {
                    animate(player!!, 4540)
                    findLocalNPC(player!!, NPCs.OBSERVATORY_PROFESSOR_488)?.let { animate(it, 4540) }
                }
                npcl("Wonderful, at last I can fix the telescope.").also { stage++ }
            }

            87 ->
                npc(
                    "Would you accompany me to the Observatory? You",
                    "simply must see the telescope in operation.",
                ).also { stage++ }

            88 -> player("Sounds interesting. Count me in.").also { stage++ }
            89 ->
                npc(
                    "Superb. You'll have to go via the dungeon under the",
                    "goblin settlement, seeing as the bridge is broken. You'll",
                    "find stairs up to the Observatory from there.",
                ).also { stage++ }

            90 -> player("Okay. See you there.").also { stage++ }
            91 -> {
                end()
                setAttribute(player!!, "/save:$FAIL_ATTRIBUTE", 0)
                setQuestStage(player!!, Quests.OBSERVATORY_QUEST, 12)
            }

            92 -> player("Hi, this really is impressive.").also { stage++ }
            93 ->
                npc(
                    "Certainly is. Please, take a look through the telescope",
                    "and tell me what you see.",
                ).also { stage = END_DIALOGUE }

            94 -> player("I've had a look through the telescope.").also { stage++ }
            95 ->
                npc(
                    "What did you see? If you're not sure, you can find",
                    "out by looking at the star charts dotted around the",
                    "walls downstairs.",
                ).also { stage++ }

            96 -> player("it was...").also { stage++ }
            97 -> {
                end()
                openDialogue(player!!, ProfessorConstellationsDialogue())
            }
        }
    }
}

class ProfessorConstellationsDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.OBSERVATORY_PROFESSOR_488)
        when (stage) {
            0 ->
                showTopics(
                    Topic("Aquarius", 1, true),
                    Topic("Capricorn", 2, true),
                    Topic("Sagittarius", 3, true),
                    Topic("Scorpio", 4, true),
                    Topic("~ next ~", 5, true),
                )

            1 ->
                player("Aquarius!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        1,
                    )
                    stage = 16
                }

            2 ->
                player("Capricorn!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        2,
                    )
                    stage = 16
                }

            3 ->
                player("Sagittarius!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        3,
                    )
                    stage = 16
                }

            4 ->
                player("Scorpio!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        4,
                    )
                    stage = 16
                }

            5 ->
                showTopics(
                    Topic("~ previous ~", 0, true),
                    Topic("Libra", 6, true),
                    Topic("Virgo", 7, true),
                    Topic("Leo", 8, true),
                    Topic("~ next ~", 9, true),
                )

            6 ->
                player("Libra!").also {
                    setAttribute(player!!, ObservatoryListener.CONSTELLATION_ATTRIBUTE, 6)
                    stage = 16
                }

            7 ->
                player("Virgo!").also {
                    setAttribute(player!!, ObservatoryListener.CONSTELLATION_ATTRIBUTE, 7)
                    stage = 16
                }

            8 ->
                player("Leo!").also {
                    setAttribute(player!!, ObservatoryListener.CONSTELLATION_ATTRIBUTE, 8)
                    stage = 16
                }

            9 ->
                showTopics(
                    Topic("~ previous ~", 5, true),
                    Topic("Cancer", 10, true),
                    Topic("Gemini", 11, true),
                    Topic("Taurus", 12, true),
                    Topic("~ next ~", 13, true),
                )

            10 ->
                player("Cancer!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        10,
                    )
                    stage = 16
                }

            11 ->
                player("Gemini!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        11,
                    )
                    stage = 16
                }

            12 ->
                player("Taurus!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        12,
                    )
                    stage = 16
                }

            13 ->
                showTopics(
                    Topic("~ previous ~", 9, true),
                    Topic("Aries", 14, true),
                    Topic("Pisces", 15, true),
                )

            14 ->
                player("Aries!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        14,
                    )
                    stage = 16
                }

            15 ->
                player("Pisces!").also {
                    setAttribute(
                        player!!,
                        ObservatoryListener.CONSTELLATION_ATTRIBUTE,
                        15,
                    )
                    stage = 16
                }

            16 -> npc("That's exactly it!").also { stage++ }
            17 -> {
                animate(player!!, Animations.HUMAN_CHEER_862)
                player("Yes! Woo hoo!").also { stage++ }
            }

            18 -> {
                when (getAttribute(player!!, ObservatoryListener.CONSTELLATION_ATTRIBUTE, -1)) {
                    1 -> npcl(FaceAnim.HALF_GUILTY, "That's Aquarius, the water-bearer.").also { stage = 19 }
                    2 -> npcl(FaceAnim.HALF_GUILTY, "That's Capricorn, the goat.").also { stage = 20 }
                    3 -> npcl(FaceAnim.HALF_GUILTY, "That's Sagittarius, the centaur.").also { stage = 21 }
                    4 -> npcl(FaceAnim.HALF_GUILTY, "That's Scorpio, the scorpion.").also { stage = 22 }
                    6 -> npcl(FaceAnim.HALF_GUILTY, "That's Libra, the scales.").also { stage = 23 }
                    7 -> npcl(FaceAnim.HALF_GUILTY, "That's Virgo, the virtuous.").also { stage = 24 }
                    8 -> npcl(FaceAnim.HALF_GUILTY, "That's Leo, the lion.").also { stage = 25 }
                    10 -> npcl(FaceAnim.HALF_GUILTY, "That's Cancer, the crab.").also { stage = 26 }
                    11 -> npcl(FaceAnim.HALF_GUILTY, "That's Gemini, the twins.").also { stage = 27 }
                    12 -> npcl(FaceAnim.HALF_GUILTY, "That's Taurus, the bull.").also { stage = 28 }
                    14 -> npcl(FaceAnim.HALF_GUILTY, "That's Aries, the ram.").also { stage = 29 }
                    15 -> npcl(FaceAnim.HALF_GUILTY, "That's Pisces, the fish.").also { stage = 30 }
                }
            }

            19 -> npcl("It seems suitable, then, to award you with water runes!").also { stage = 100 }
            20 ->
                npcl("Capricorn will surely reward your insight with an increase to your Strength.").also {
                    stage = 100
                }

            21 -> npcl("As you've spotted the archer, I shall reward you with a maple longbow.").also { stage = 100 }
            22 -> npcl("I think weapon poison would make a suitable reward.").also { stage = 100 }
            23 -> npcl("Hmmm, balance, law, order - I shall award you with law runes!").also { stage = 100 }
            24 -> npcl("Virgo will surely provide you with an increase to Defense.").also { stage = 100 }
            25 -> npcl("I think the majestic power of the lion will improve your Hitpoints.").also { stage = 100 }
            26 ->
                npcl("An armoured creature - I think I shall reward you with an amulet of protection.").also {
                    stage = 100
                }

            27 ->
                npcl(
                    "With the double nature of Gemini, I can't offer you anything more suitable than a two-handed weapon.",
                ).also {
                    stage = 100
                }

            28 -> npcl("This Strength potion should be a suitable reward.").also { stage = 100 }
            29 ->
                npcl(
                    "A fierce fighter. I'm sure he'll look down on you and improve your Attack for such insight.",
                ).also {
                    stage = 100
                }

            30 -> npcl("What's more suitable as a reward than some tuna?").also { stage = 100 }
            100 -> {
                end()
                removeAttribute(player!!, ObservatoryListener.CONSTELLATION_ATTRIBUTE)
                finishQuest(player!!, Quests.OBSERVATORY_QUEST)
            }
        }
    }
}
