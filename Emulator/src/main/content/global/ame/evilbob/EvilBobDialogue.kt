package content.global.ame.evilbob

import content.data.GameAttributes
import content.global.ame.evilbob.EvilBobUtils.giveEventFishingSpot
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class EvilBobDialogue(
    val rewardDialogue: Boolean = false,
    val rewardXpSkill: Int = Skills.FISHING,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.EVIL_BOB_2479)
        if (getAttribute(player!!, GameAttributes.RE_BOB_ZONE, "none") == "none") giveEventFishingSpot(player!!)
        when (stage) {
            0 -> {
                if (rewardDialogue) {
                    sendPlayerDialogue(
                        player!!,
                        "That was the strangest dream I've ever had! Assuming it was a dream...",
                        FaceAnim.HALF_ASKING,
                    )
                    stage = if (rewardXpSkill == Skills.FISHING) 900 else 901
                } else if (getAttribute(player!!, GameAttributes.RE_BOB_COMPLETE, false)) {
                    sendDialogue(player!!, "Evil Bob appears to be sleeping, best not to wake him up.").also {
                        stage =
                            END_DIALOGUE
                    }
                } else if (removeItem(player!!, Items.RAW_FISHLIKE_THING_6200)) {
                    setAttribute(player!!, GameAttributes.RE_BOB_SCORE, false)
                    playerl(FaceAnim.NEUTRAL, "Here, I've brought you some fish.").also { stage = 500 }
                } else if (removeItem(player!!, Items.RAW_FISHLIKE_THING_6204)) {
                    setAttribute(player!!, GameAttributes.RE_BOB_SCORE, false)
                    setAttribute(player!!, GameAttributes.RE_BOB_ALERT, true)
                    setAttribute(player!!, GameAttributes.RE_BOB_DIAL_INDEX, true)
                    playerl(FaceAnim.NEUTRAL, "Here, I've brought you some fish.").also { stage = 600 }
                } else if (inInventory(player!!, Items.FISHLIKE_THING_6202) ||
                    inInventory(player!!, Items.FISHLIKE_THING_6206)
                ) {
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "What, are you giving me cooked fish? What am I going to do with that? Uncook it first!",
                    ).also {
                        stage =
                            700
                    }
                } else if (!getAttribute(player!!, GameAttributes.RE_BOB_START, false)) {
                    playerl(FaceAnim.ASKING, "Huh?").also { stage = 800 }
                    setAttribute(player!!, GameAttributes.RE_BOB_START, true)
                } else {
                    playerl(FaceAnim.ANNOYED, "Let me out of here!").also { stage++ }
                }
            }

            1 -> npcl(FaceAnim.CHILD_NORMAL, "I will never let you go, ${player!!.username}!").also { stage++ }
            2 ->
                options(
                    "Why not?",
                    "What's it all about?",
                    "How is it possible that you're talking?",
                    "What did you do to Bob?",
                ).also {
                    stage++
                }
            3 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.ASKING, "Why not?").also { stage = 100 }
                    2 -> playerl(FaceAnim.ASKING, "What's it all about?").also { stage = 200 }
                    3 -> playerl(FaceAnim.ASKING, "How is it possible that you're talking?").also { stage = 300 }
                    4 -> playerl(FaceAnim.ASKING, "What did you do to Bob?").also { stage = 400 }
                }

            100 -> npcl(FaceAnim.CHILD_NORMAL, "Uhm...").also { stage++ }
            101 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Because I say so! And because I can never have enough servants!",
                ).also { stage++ }
            102 -> npcl(FaceAnim.CHILD_NORMAL, "Now catch me some fish, I'm hungry.").also { stage++ }
            103 -> playerl(FaceAnim.ASKING, "What fish, where?").also { stage++ }
            104 ->
                npcl(FaceAnim.CHILD_NORMAL, "Talk to my other servants, and hurry it up!").also {
                    stage =
                        END_DIALOGUE
                }
            200 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Sit down and I'll tell you. It's a question of servant shortage.",
                ).also { stage++ }
            201 -> playerl(FaceAnim.NEUTRAL, "Go on.").also { stage++ }
            202 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You are a skilled worker. A human like you is worth a great deal as a slave.",
                ).also {
                    stage++
                }
            203 ->
                playerl(
                    FaceAnim.ANGRY,
                    "A slave?? I will have nothing to do with you. Is that clear? Absolutely nothing.",
                ).also {
                    stage++
                }
            204 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Now be reasonable little human. It's just a matter of time before you will do everything I ask you to. Play along and this can be a very nice place.",
                ).also {
                    stage++
                }
            205 ->
                playerl(
                    FaceAnim.ANGRY,
                    "I will not make any deals with you. I'm a human. I will not be pushed, numbered, meowed at, teleported or enslaved. My life is my own.",
                ).also {
                    stage++
                }
            206 -> npcl(FaceAnim.CHILD_NORMAL, "Is it?").also { stage++ }
            207 -> playerl(FaceAnim.ANNOYED, "Yes. You won't hold me.").also { stage++ }
            208 -> npcl(FaceAnim.CHILD_NORMAL, "Won't I?").also { stage++ }
            209 -> npcl(FaceAnim.CHILD_NORMAL, "Let me assure you there's no way out.").also { stage++ }
            210 -> npcl(FaceAnim.CHILD_NORMAL, "Just ask my servants!").also { stage = END_DIALOGUE }
            300 -> npcl(FaceAnim.CHILD_NORMAL, "How is it possible that you're not meowing?").also { stage++ }
            301 -> playerl(FaceAnim.HALF_ASKING, "Meowing?? Why would I be meowing?").also { stage++ }
            302 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Most humans do, that's why I'm wearing this amulet of Man speak... but I do try to train my staff in civilised speech.",
                ).also {
                    stage++
                }
            303 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Ah, but I suppose things are slightly different in your world... what a dreadful place that must be.",
                ).also {
                    stage++
                }
            304 -> playerl(FaceAnim.NEUTRAL, "I think I'm getting a headache...").also { stage = END_DIALOGUE }
            400 -> npcl(FaceAnim.CHILD_NORMAL, "Bob? I am Bob!").also { stage++ }
            401 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Oh, you mean the Bob in your world? Nothing. I am an incarnation of Bob here on Scape2009.",
                ).also {
                    stage++
                }
            402 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "What, you mean this is... like some kind of mirror land?",
                ).also { stage++ }
            403 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    " Something like that... somewhere in Scape2009 there must be a human servant called ${player!!.username}.",
                ).also {
                    stage++
                }
            404 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "But you work just as well for me! Now get to work, human! Fish for me!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            500 -> {
                if (getAttribute(player!!, GameAttributes.RE_BOB_ALERT, false)) stage = 505 else stage++
                npcl(FaceAnim.CHILD_NORMAL, "Mmm, mmm...that's delicious.")
            }

            501 -> npcl(FaceAnim.CHILD_NORMAL, "Now, let me take...a little...catnap.").also { stage++ }
            502 -> {
                end()
                setAttribute(player!!, GameAttributes.RE_BOB_COMPLETE, true)
                findNPC(NPCs.EVIL_BOB_2479)!!.sendChat("ZZZzzz")
            }

            505 -> {
                setAttribute(player!!, GameAttributes.RE_BOB_ALERT, false)
                setAttribute(player!!, GameAttributes.RE_BOB_DIAL_INDEX, true)
                giveEventFishingSpot(player!!)
                npcl(FaceAnim.CHILD_NORMAL, "Now get me another, you no-good human.").also { stage++ }
            }
            506 ->
                sendDialogue(
                    player!!,
                    "Evil Bob seems slightly less attentive of you.",
                ).also { stage = END_DIALOGUE }
            600 -> npcl(FaceAnim.CHILD_NORMAL, "What was this? That was absolutely disgusting!").also { stage++ }
            601 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Don't you know what kind of fish I like? Talk to my other servants for some advice.",
                ).also {
                    stage++
                }
            602 -> sendDialogue(player!!, "Evil Bob seems more attentive of you.").also { stage = END_DIALOGUE }
            700 -> playerl(FaceAnim.HALF_ASKING, "Errr... Uncook it?").also { stage++ }
            701 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You heard me! There's the cold fire, by the trees, now get uncooking!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            800 -> playerl(FaceAnim.ANGRY, "Where am I?").also { stage++ }
            801 -> npcl(FaceAnim.CHILD_NORMAL, "On my island.").also { stage++ }
            802 -> playerl(FaceAnim.ANGRY, "Who brought me here?").also { stage++ }
            803 -> npcl(FaceAnim.CHILD_NORMAL, "That would be telling.").also { stage++ }
            804 -> playerl(FaceAnim.ANGRY, "Take me to your leader!").also { stage++ }
            805 -> npcl(FaceAnim.CHILD_NORMAL, "I am your leader, you are but a slave.").also { stage++ }
            806 -> playerl(FaceAnim.ANGRY, "I am not a slave, I am a free man!").also { stage++ }
            807 -> npcl(FaceAnim.CHILD_NORMAL, "Ah-ha-ha-ha-ha-ha!").also { stage = END_DIALOGUE }
            900 ->
                sendDialogue(player!!, "You feel somehow that you've become better at fishing.").also {
                    stage =
                        END_DIALOGUE
                }
            901 ->
                sendDialogue(player!!, "You feel somehow that you've become better at magic.").also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
