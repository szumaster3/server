package content.region.karamja.quest.totem.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Horacio dialogue.
 */
@Initializable
class HoracioDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if(getQuestStage(player, Quests.BACK_TO_MY_ROOTS) >= 1) {
            npc("How goes the hunt, brave gardener?").also { stage = 27 }
        } else {
            npcl(FaceAnim.HAPPY, "It's a fine day to be out in a garden, isn't it?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val afterTribalTotemQuest = isQuestComplete(player, Quests.TRIBAL_TOTEM)
        val quest = player.getQuestRepository().getQuest(Quests.BACK_TO_MY_ROOTS)
        when (stage) {
            0 -> showTopics(
                Topic("Yes it's very nice.", 1, false),
                Topic("So... who are you?",2, false)
            )
            1 -> npcl(FaceAnim.HAPPY, "Days like these make me glad to be alive!").also { stage = END_DIALOGUE }
            2 -> {
                npc(FaceAnim.FRIENDLY, "My name is Horacio Dobson. I'm the gardener to Lord", "Handlemort. Take a look around this beautiful garden,", "all of this is my handiwork.").also{
                    stage = if(afterTribalTotemQuest) 3 else END_DIALOGUE
                }
            }
            3 -> {
                showTopics(
                    Topic("So... do you garden round the back too?", 4, false),
                    Topic("Do you need any help?", 10, false),
                )
            }
            4 -> npcl(FaceAnim.HAPPY, "That I do!").also { stage++ }
            5 -> playerl(FaceAnim.ASKING, "Doesn't all of the security around the house get in your way then?").also { stage++ }
            6 -> npcl(FaceAnim.HAPPY, "Ah. I'm used to all that. I have my keys, the guard dogs know me, and I know the combination to the door lock.").also { stage++ }
            7 -> npcl(FaceAnim.HAPPY, "It's rather easy, it's his middle name.").also { stage++ }
            8 -> playerl(FaceAnim.ASKING, "Whose middle name?").also { stage++ }
            9 -> npcl(FaceAnim.ANNOYED, "Hum. I probably shouldn't have said that. Forget I mentioned it.").also { stage = END_DIALOGUE }
            10 -> if(!quest.hasRequirements(player)) {
                npc(FaceAnim.HAPPY, "Actually, now you mention it, yes... but you're not", "experienced enough to help me just yet.").also { stage = END_DIALOGUE }
                sendMessage(player, "Check your quest journal for the requirements to start the Back to my Roots quest.")
            } else if (isQuestComplete(player, Quests.BACK_TO_MY_ROOTS)) {
                npc(FaceAnim.HAPPY, "You've done more than enough to help.", "Hope you're enjoying your vine patch!").also { stage = END_DIALOGUE }
            } else {
                npc(FaceAnim.HAPPY, "Actually, now you mention it, yes... I'm going to", "improve the garden around the house. Would you be", "willing to help me?").also { stage++ }
            }
            11 -> showTopics(
                Topic("Sure, I enjoy a bit of gardening.", 12, false),
                Topic("No thanks, I don't like getting my hands dirty.", END_DIALOGUE, false),
            )
            12 -> npc("Well, let's see now. I'm reworking the beds and have", "marked out two special patches as you can see, they're", "a bit weedy at the moment, though. I'm sure Lord", "Handlemort will appreciate the beauty of what I have").also { stage++ }
            13 -> npc("planned there. It may even cheer him up a little.").also { stage++ }
            14 -> player(FaceAnim.HALF_ASKING, "What's wrong with him?").also { stage++ }
            15 -> npc(FaceAnim.SAD, "One of his treasures was stolen...").also { stage++ }
            16 -> player(FaceAnim.WORRIED, "Oh...err...I see. There are some...nasty people around", "these days.").also { stage++ }
            17 -> npc(FaceAnim.FRIENDLY, "Indeed there are. Still, life isn't always a bed of roses is", "it? Back to the root of the problem: I need a very rare", "plant... and I think you can get it for me.").also { stage++ }
            18 -> player(FaceAnim.HALF_ASKING, "Oh? What plant would that be? A magic tree?").also { stage++ }
            19 -> npc(FaceAnim.NEUTRAL, "Oh no, no, no. Nothing so mundane! It's a vine, you", "see...").also { stage++ }
            20 -> player(FaceAnim.HALF_ASKING, "What sort of vine?").also { stage++ }
            21 -> npc(FaceAnim.NEUTRAL, "One that only grows wild in one place on Karamja just", "east of Shilo Village... at least, that's what I've heard from", "other gardeners. It's called the Jade Vine.").also { stage++ }
            22 -> player(FaceAnim.HALF_ASKING, "Oh, right. So what's the problem? Why don't you just", "go and get it?").also { stage++ }
            23 -> npc(FaceAnim.SAD, "I tried... and failed lots of times. So has Garth - the", "farmer on Karamja - he knows quite a bit about the", "vine. You see, because it's so delicate, the cutting is", "very difficult to keep alive for very long. I have an").also { stage++ }
            24 -> npc(FaceAnim.SAD, "idea, though: go talk to that mad Wizard Cromperty. He", "has been boasting recently that he has discovered", "preservation magic. I'm not sure I believe him, though.").also { stage++ }
            25 -> player(FaceAnim.HAPPY, "Okay, I'm off to see the wizard... so long as he's not", "going to teleport me places again, we should be fine!").also { stage++ }
            26 -> npc(FaceAnim.FRIENDLY, "That would be excellent!").also {
                // Unlocks: Crmoperty dialogue.
                setQuestStage(player, Quests.BACK_TO_MY_ROOTS, 1)
                setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 1, true)
                stage = END_DIALOGUE
            }

            27 -> when(quest.getStage(player)){
                8 -> npcl(FaceAnim.FRIENDLY, "You call yourself a farmer? How could you let the vine go wild like that?").also { stage = 44 }
                7 -> player("Guess what? I got you a cutting... and it should still be alive, too!").also { stage++ }
                6 -> npcl(FaceAnim.HALF_ASKING, "Umm. Have you had time to plant that vine yet?").also { stage = 39 }
                else -> playerl(FaceAnim.FRIENDLY, "Well... simply de vine... I haven't got the Jade Vine cutting yet. In fact... I've forgotten what I'm doing.").also { stage = 42 }
            }

            28 -> npcl(FaceAnim.FRIENDLY, "Wow! You managed it - you must be an incredibly accomplished farmer, and quite handy in a muddle too. How did you find the trip?").also { stage++ }
            29 -> playerl(FaceAnim.FRIENDLY, " Oh, it wasn't that hard.").also { stage++ }
            30 -> npcl(FaceAnim.FRIENDLY, "Well, I think it was pretty amazing. As me old dad says, 'Looking for oranges on an apple tree will be fruitless searchin', but I appear to have picked a real ripe one in you.").also { stage++ }
            31 -> playerl(FaceAnim.FRIENDLY, "Yeeeesss?").also { stage++ }
            32 -> npcl(FaceAnim.FRIENDLY, "Well I kind of thought that you might...").also { stage++ }
            33 -> playerl(FaceAnim.FRIENDLY, " Might?").also { stage++ }
            34 -> npcl(FaceAnim.FRIENDLY, "Err... plant and grow it?").also { stage++ }
            35 -> playerl(FaceAnim.FRIENDLY, " WHAT? After all I just went through to get you that wretched cutting? You now want me to grow it, too? Who's the gardener here?").also { stage++ }
            36 -> npcl(FaceAnim.FRIENDLY, "Hold your nasturtiums. I didn't ask you to chop your head off or anything. Just plant and grow the vine till it's adult. I can take over then. You see I've got all those ugly beds to dig and replant").also { stage++ }
            37 -> npcl(FaceAnim.FRIENDLY, "with new flowers.").also { stage++ }
            38 -> playerl(FaceAnim.FRIENDLY, "I have a feeling I'm going to regret this but okay. Just this once. And only because I wouldn't want to see you kill it and waste all my hard work.").also { stage = END_DIALOGUE }

            39 -> playerl(FaceAnim.SAD, "No, but I will, eventually.").also { stage++ }
            40 -> npcl(FaceAnim.FRIENDLY, "Well, I've managed to keep one patch free of weeds, you can use that one.").also { stage++ }
            41 -> playerl(FaceAnim.HAPPY, "Okay.").also { stage = END_DIALOGUE }

            42 -> npcl(FaceAnim.FRIENDLY, "Oh dear! You were getting a special Jade Vine plant for me, but to protect it you'll need to talk to the Wizard Cromperty first.").also { stage++ }
            43 -> player(FaceAnim.HAPPY, "Aha! I remember now.").also { stage = END_DIALOGUE }

            44 -> playerl(FaceAnim.FRIENDLY, "Oh, stop vining. I didn't know it would go wild and try to kill you.").also { stage++ }
            45 -> npcl(FaceAnim.FRIENDLY, "Oh, I don't think I can take much more of this.").also { stage++ }
            46 -> playerl(FaceAnim.FRIENDLY, "Do they always do that?").also { stage++ }
            47 -> npc(FaceAnim.FRIENDLY, "Why, yes, they do. If you let this species get wild by", "leaving it unpruned, it will attack anything. Some closely", "related species that can pop up pretty much anywhere", "has a large purple fruit, you've probably seen it.").also { stage++ }
            48 -> npc(FaceAnim.FRIENDLY,"Anyways, thank you for saving my life.").also { stage++ }
            49 -> playerl(FaceAnim.FRIENDLY, "You're welcome.").also { stage++ }
            50 -> npc(FaceAnim.FRIENDLY, "Here, take this seed that the vine dropped and if you", "need another, let me know. You can use the other vine", "patch here if you want. Remember that if you don't", "prune your vine every couple of days, it will grow wild").also { stage++ }
            51 -> npc(FaceAnim.FRIENDLY, "and you'll have to kill it...or get me to do it. I'm going", "to go replant my vine - now we have seeds it'll be easy!", "Oh, I'm so glad you managed to help, I'm going to contact the Slayer Masters about this, I bet they").also { stage++ }
            52 -> npc(FaceAnim.FRIENDLY, "haven't fought a strongylodon macrobotrys vine for a", "long time, we thought they'd been hunted to extinction", "at one point. Still, your jade vine may prove useful for", "training your Slayer abilities.").also { stage++ }
            53 -> {
                end()
                finishQuest(player, Quests.BACK_TO_MY_ROOTS)
                stage = END_DIALOGUE
            }

        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HORACIO_845)
}

//// Pay / Protect vine
//100 -> {
//    if (isVineFullyGrown()) {
//        npc("The vine is already fully grown! I don't know what you want me to do with it!").also { stage = END_DIALOGUE }
//    } else if (hasPaidForProtection()) {
//        npc("I'm already looking after your vine! You have ears like corn!").also { stage = END_DIALOGUE }
//    } else if (isVineGrowing()) {
//        npc("Yes, of course, but it will cost you. My thyme is valuable.").also { stage = 101 }
//    } else {
//        npc("Err... you haven't planted a vine yet!").also { stage = END_DIALOGUE }
//    }
//}
//
//101 -> showTopics(
//    Topic("How much?", 102, false),
//    Topic("No thanks.", 104, false)
//)
//
//102 -> npc("I'll want 10 wildblood hops so that I can brew myself some tasty beer.").also { stage = 103 }
//
//103 -> {
//    if (!hasWildbloodHops()) {
//        player("I don't have enough of those. I guess I'll come back later.").also { stage = END_DIALOGUE }
//    } else {
//        showTopics(
//            Topic("Okay, it's a deal.", 105, false),
//            Topic("No, that's too much.", 107, false)
//        )
//    }
//}
//
//105 -> player("Okay, it's a deal.").also { stage = 106 }
//106 -> {
//    player.inventory.remove("wildblood hops", 10)
//    npc("That'll do nicely. Never fear, Horacio is here, your vine shall grow safely in my hands.").also {
//        setVarbit(player, Vars.VARBIT_VINE_PROTECTION_PAID, 1, true)
//        stage = END_DIALOGUE
//    }
//}
//
//107 -> player("No, that's too much.").also { stage = 108 }
//108 -> npc("Well, I'm not risking my life for free.").also { stage = END_DIALOGUE }
//
//// Clear wild vine patch
//310 -> {
//    if (!hasWildJadeVine()) {
//        npc("You've got a fine young vine growing there. Probably best not to disturb it. Wait till it's grown.").also { stage = 311 }
//    } else {
//        npc("ARG! Not again! Would you like me to remove it for you?").also { stage = 312 }
//    }
//}
//311 -> player("Okay. I'll wait then.").also { stage = END_DIALOGUE }
//312 -> showTopics(
//    Topic("Yes, please!", 313, false),
//    Topic("No, thanks - I can handle it.", END_DIALOGUE, false)
//)
//313 -> npc("Okay, but I'm not doing it for nothing. Five coconuts and five watermelons to do that.").also { stage = 314 }
//314 -> {
//    if (player.inventory.count("coconuts") < 5 || player.inventory.count("watermelon") < 5) {
//        player("I don't have that - I guess I'll come back, or deal with it by myself.").also { stage = END_DIALOGUE }
//    } else {
//        showTopics(
//            Topic("Okay, it's a deal.", 315, false)
//        )
//    }
//}
//315 -> player("Okay, it's a deal.").also { stage = 316 }
//316 -> {
//    player.inventory.remove("coconuts", 5)
//    player.inventory.remove("watermelon", 5)
//    setVarbit(player, Vars.VARBIT_WILD_JADE_VINE_PRESENT, 0, true)
//    npc("There, all done for you.").also { stage = 317 }
//}
//317 -> player("Thanks!").also { stage = END_DIALOGUE }
//
//// Learn about jade vine history
//320 -> showTopics(
//    Topic("Tell me about propagation.", 321, false),
//    Topic("Can you tell me anything about its history?", 325, false)
//)
//321 -> player("Tell me about propagation.").also { stage = 322 }
//322 -> npc("These plants have gender, the same as you or I, and need one of each gender to propagate the species by seed, which is why we didn't get seeds until you successfully grew another vine in my patch. They are usually pollinated by nocturnal bats which can fly vast distances.").also { stage = 323 }
//323 -> player("So, the one on Karamja would never have given seed until we had another of a different gender growing?").also { stage = 324 }
//324 -> npc("Maybe, maybe not, but it would never have grown, even if it had given seed. Needs the other gender to be fertile.").also { stage = END_DIALOGUE }
//
//325 -> player("Can you tell me anything about its history?").also { stage = 326 }
//326 -> npc("The jade vine, or 'strongylodon macrobotrys' was once thought hunted to extinction by the Slayer Masters when they thought them the best thing to train on without needing an assignment. As we have found out, there seems to have been one that slipped through the net and grew on its own, undisturbed on Karamja. It can be a formidable opponent when fully wild - I don't think I'd have liked killing it for fun!").also { stage = 327 }
//327 -> player("It wasn't that hard.").also { stage = END_DIALOGUE }
//
//// Sell items (2022+)
//340 -> player("Can you sell me something?").also { stage = 341 }
//341 -> npc("That depends on what I have to sell. What is it that you're looking for?").also { stage = 342 }
//342 -> openShop(player, "Farmer's Supplies").also { stage = END_DIALOGUE }
//
//// Retrieve seed
//200 -> {
//    when {
//        hasPlantedVine() -> npc("I don't have any seeds to give you? Hope you're having fun growing your vine, mine is looking particularly fine.").also { stage = END_DIALOGUE }
//        hasJadeVineSeed() -> npc("I have already gave you a seed. Maybe you have stored it somewhere?").also { stage = END_DIALOGUE }
//        else -> {
//            npc("Here you go.").also { stage = 201 }
//        }
//    }
//}
//201 -> {
//    player.inventory.add("jade vine seed", 1)
//    stage = END_DIALOGUE
//}