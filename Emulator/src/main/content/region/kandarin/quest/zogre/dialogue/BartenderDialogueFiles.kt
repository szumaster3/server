package content.region.kandarin.quest.zogre.dialogue

import content.region.kandarin.quest.zogre.handlers.ZUtils
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

class BartenderDialogueFiles : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BARTENDER_739)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    Items.DRAGON_INN_TANKARD_4811,
                    "You show the tankard to the Inn Keeper.",
                ).also {
                    stage++
                }
            1 ->
                if (getAttribute(player!!, ZUtils.TALK_ABOUT_TANKARD_AGAIN, false)) {
                    player("Hello again. Can you tell me what you know about", "this tankard again please?").also {
                        stage =
                            10
                    }
                } else {
                    player(
                        "Hello there, I found this tankard in an ogre tomb",
                        "cavern. It has the emblem of this Inn on it and I",
                        "wondered if you knew anything about it?",
                    ).also {
                        stage++
                    }
                }
            2 ->
                npc(
                    "Oh yes, this is Brentle's mug...I'm surprised he left it",
                    "just lying around down some cave. He's quite protective",
                    "of it.",
                ).also {
                    stage++
                }
            3 -> player("Brentle you say? So you knew him then?").also { stage++ }
            4 ->
                npc(
                    "Yeah, this belongs to 'Brentle Vahn', he's quite a",
                    "common customer, though I've not seen him in a while.",
                ).also {
                    stage++
                }
            5 ->
                npc(
                    "He was talking to some shifty looking wizard the other",
                    "day. I don't know his name, but I'd recognise him if I",
                    "saw him.",
                ).also {
                    stage++
                }
            6 ->
                player(
                    "Hmm, I'm sorry to tell you this, but Brentle Vahn is",
                    "dead - I believe he was murdered.",
                ).also {
                    stage++
                }
            7 -> npc(FaceAnim.SCARED, "Noooo! I'm shocked...").also { stage++ }
            8 ->
                npc(
                    "...but not surprised. He was a good customer...but I",
                    "knew he would sell his sword arm and do many a dark",
                    "deed if paid enough.",
                ).also {
                    stage++
                }
            9 ->
                npc("If you need help bringing the culprit to justice, you let", "me know.").also {
                    stage =
                        END_DIALOGUE
                }
            10 ->
                npc(
                    "Oh yes, Brentle's tankard. Yeah, you've shown me this",
                    "already. It belonged to Brentle Vahn, he was quite a",
                    "common customer, though I've not seen him in a while.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    "He was talking to some shifty looking wizard the other",
                    "day. I don't know his name, but I'd recognise him if",
                    "I saw him.",
                ).also {
                    setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 4)
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class BartenderBlackPrismDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BARTENDER_739)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    Items.BLACK_PRISM_4808,
                    "You show the bar tender the black prism.",
                ).also { stage++ }
            1 ->
                player(
                    "Hello there, I found this black prism,",
                    "I wondered if you knew anything",
                    "about it.",
                ).also { stage++ }
            2 ->
                npc(
                    "Hmmm, it's not really familiar to me, sorry",
                    "I don't know what it's for, looks magical to me...maybe someone else in",
                    "Yanille can help you?",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class BartenderTornPageDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BARTENDER_739)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    Items.TORN_PAGE_4809,
                    "You show the bar tender the torn page.",
                ).also { stage++ }
            1 -> player("Do you have any clue what this might be?").also { stage++ }
            2 ->
                npc(
                    "Oooh, don't show me that sort of stuff, it's probably all magical",
                    "and wizardy, probably turn me into a frog as soon as I look",
                    "at it...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}

class BartenderWrongPortraitDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BARTENDER_739)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    ZUtils.UNREALIST_PORTRAIT,
                    "You show the sketch to the Inn keeper.",
                ).also { stage++ }
            1 ->
                npcl(
                    "Who's that? I mean, I guess it's a picture of a person isn't it? Sorry...you've got me? And before you ask, you're not putting it up on my wall!",
                ).also {
                    stage++
                }
            2 -> playerl("It's a portrait of Sithik Ints...don't you recognise him?").also { stage++ }
            3 ->
                npcl(
                    "I'm sorry, I really am, but I just don't see it...can you make a better picture?",
                ).also { stage++ }
            4 -> playerl("I'll try...").also { stage = END_DIALOGUE }
        }
    }
}

class BartenderCorrectPortraitDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BARTENDER_739)
        when (stage) {
            0 ->
                sendItemDialogue(
                    player!!,
                    ZUtils.REALIST_PORTRAIT,
                    "You show the portrait to the Inn keeper.",
                ).also { stage++ }
            1 ->
                npc(
                    "Yeah, that's the guy who was talking to Brentle Vahn",
                    "the other day! Look at those eyes, never a more shifty",
                    "looking pair will you ever see!",
                ).also {
                    stage++
                }
            2 ->
                player(
                    "Hmm, you've just identified the man who I think sent",
                    "Brentle Vahn to his death.",
                ).also { stage++ }
            3 ->
                player(
                    "I'm trying to bring him to justice with the wizards",
                    "guild grand secretary. Do you think you could sign",
                    "this portrait to say that he was talking to Brentle Vahn.",
                ).also {
                    stage++
                }
            4 ->
                npcl("I can and I will!").also {
                    removeItem(player!!, ZUtils.REALIST_PORTRAIT)
                    addItem(player!!, ZUtils.SIGNED_PORTRAIT)
                    stage++
                }
            5 ->
                sendItemDialogue(
                    player!!,
                    ZUtils.SIGNED_PORTRAIT,
                    "The Dragon Inn bartender signs the portrait.",
                ).also { stage++ }
            6 -> player("Many thanks for your help, it's really very good of", "you.").also { stage++ }
            7 ->
                npc("Not at all, just doing my part.").also {
                    setAttribute(player!!, ZUtils.TALK_ABOUT_SIGN_PORTRAIT, true)
                    stage = END_DIALOGUE
                }
        }
    }
}
