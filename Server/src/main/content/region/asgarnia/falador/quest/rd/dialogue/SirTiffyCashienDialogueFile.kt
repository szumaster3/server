package content.region.asgarnia.falador.quest.rd.dialogue

import content.region.asgarnia.falador.quest.rd.cutscene.StartTestCutscene
import content.region.asgarnia.falador.quest.rd.plugin.RDUtils
import core.api.finishQuest
import core.api.getQuestStage
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.Quests

class SirTiffyCashienDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.RECRUITMENT_DRIVE)) {
            1,2,3 -> handleTestDialogue()
        }
    }
    private fun handleTestDialogue() {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Sir Amik Varze sent me to meet you here for some sort of testing...").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Ah, ${player?.username}! Amik told me all about you, dontchaknow! Spliffing job you you did with the old Black Knights there, absolutely first class.").also { stage++ }
            2 -> playerl(FaceAnim.GUILTY, "...Thanks I think.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Well, not in those exact words, but you get my point, what?").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "A top-notch filly like yourself is just the right sort we've been looking for for our organisation.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "So, are you ready to begin testing?").also { stage++ }
            6 -> showTopics(
                Topic("Testing..?",7),
                Topic("Organisation?",1),
                Topic("Yes, let's go!",33, true),
                Topic("No, I've changed my mind.", END_DIALOGUE),
            )
            7  -> playerl(FaceAnim.THINKING, "Testing? What exactly do you mean by testing?").also { stage++ }
            8  -> npcl(FaceAnim.FRIENDLY, "Jolly bad show! Varze was supposed to have informed you about all this before sending you here!").also { stage++ }
            9  -> npcl(FaceAnim.FRIENDLY, "Well, not your fault I suppose, what? Anywho, our organisation is looking for a certain specific type of person to join.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "So... You want me to go kill some monster or something for you?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Not at all, old bean. There's plenty of warriors around should we require dumb muscle.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "That's really not the kind of thing our organisation is after, what?").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "So you want me to go and fetch you some kind of common item, and then take it for delivery somewhere on the other side of the country?").also { stage++ }
            14 -> playerl(FaceAnim.SAD, "Because I really hate doing that!").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "Haw, haw, haw! What a dull thing to ask of someone, what?").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "I know what you mean, though. I did my fair share of running errands when I was a young adventurer, myself!").also { stage++ }
            17 -> playerl(FaceAnim.FRIENDLY, "So what exactly will this test consist of?").also { stage++ }
            18 -> npcl(FaceAnim.FRIENDLY, "Can't let just any old riff-raff in, what? The mindless thugs and bully boys are best left in the White Knights or the city guard. We look for the top-shelf brains to join us.").also { stage++ }
            19 -> playerl(FaceAnim.HALF_ASKING, "So you want to test my brains? Will it hurt?").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "Haw, haw, haw! That's a good one!").also { stage++ }
            21 -> npcl(FaceAnim.FRIENDLY, "Not in the slightest.. Well, maybe a bit, but we all have to make sacrifices occasionally, what?").also { stage++ }
            22 -> playerl(FaceAnim.FRIENDLY, "What do you want me to do then?").also { stage++ }
            23 -> npcl(FaceAnim.FRIENDLY, "It's a test of wits, what? I'll take you to our secret training grounds, and you will have to pass through a series of five separate intelligence test to prove you're our sort of adventurer.").also { stage++ }
            24 -> npcl(FaceAnim.FRIENDLY, "Standard puzzle room rules will apply.").also { stage++ }
            25 -> playerl(FaceAnim.THINKING, "Erm... What are standard puzzle room rules exactly?").also { stage++ }
            26 -> npcl(FaceAnim.HAPPY, "Never done this sort of thing before, what?").also { stage++ }
            27 -> npcl(FaceAnim.HAPPY, "The simple rules are: No items or equipment to be brought with you. Each room is a self-contained puzzle. You may quit at any time.").also { stage++ }
            28 -> npcl(FaceAnim.HAPPY, "Of course, if you quit a room, then all your progress up to that point will be cleared, and you'll have to start again from scratch.").also { stage++ }
            29 -> npcl(FaceAnim.HAPPY, "Our organisation manages to filter all the top-notch adventurers this way. So, are you ready to go?").also { stage++ }
            30 -> player(FaceAnim.FRIENDLY, "Yeah. this sounds right up my street.", "Let's go!").also { stage++ }
            31 -> if (player!!.inventory.isEmpty && player!!.equipment.isEmpty && !(player!!.familiarManager.hasFamiliar() || player!!.familiarManager.hasPet())) {
                npcl(FaceAnim.FRIENDLY, "Jolly good show! Now, the training grounds location is a secret, so...")
                RDUtils.shuffleTask(player!!)
                StartTestCutscene(player!!).start()
            } else {
                end()
                sendMessage(player!!,"To start the test you can't have anything in the inventory and equipment.")
                npcl(FaceAnim.HAPPY, "Don't want people cheating by smuggling stuff in, what? That includes things carried by familiars, too! Come and see me again after you've been to the old bank to drop your stuff off, what?")
            }
            32 -> playerl(FaceAnim.FRIENDLY, "No, I've changed my mind.").also { stage = END_DIALOGUE }
            33 -> player(FaceAnim.FRIENDLY, "Yeah. this sounds right up my street.", "Let's go!").also { stage = 31 }
            34 -> {
                npc(FaceAnim.HAPPY, "Oh, jolly well done!", "Your performance will need to be evaluated by Sir Vey", "personally, but I don't think it's going too far ahead of", "myself to welcome you to the team!")
                finishQuest(player!!, Quests.RECRUITMENT_DRIVE)
                end()
            }
        }
    }
}
