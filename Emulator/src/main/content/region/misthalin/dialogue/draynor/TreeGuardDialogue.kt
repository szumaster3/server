package content.region.misthalin.dialogue.draynor

import content.region.misthalin.handlers.draynor.DraynorUtils
import core.api.*
import core.api.item.produceGroundItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class TreeGuardDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GUARD_345)
        when (stage) {
            0 -> {
                if (getAttribute(player!!, DraynorUtils.feedGuardAttribute, false)) {
                    playerl("I think I should leave him in peace to finish his stew!").also { stage = END_DIALOGUE }
                } else if (!inInventory(player!!, Items.STEW_2003)) {
                    npcl(FaceAnim.AFRAID, "Ssshhh! What do you want?").also { stage++ }
                } else {
                    npcl("Yes, what do you do... I say, is that stew for me?").also { stage = 17 }
                }
            }

            1 -> playerl("Well, it's not every day you see a man up a tree.").also { stage++ }
            2 -> npcl("I'm trying to observe a suspect. Leave me alone!").also { stage++ }
            3 ->
                options(
                    "This is about the bank robbery, right?",
                    "You're not being very subtle up there.",
                    "Can I do anything to help?",
                ).also { stage++ }

            4 ->
                when (buttonID) {
                    1 -> playerl("This is about the bank robbery, right?").also { stage++ }
                    2 -> playerl("You're not being very subtle up there.").also { stage = 9 }
                    3 -> playerl("Can I do anything to help?").also { stage = 13 }
                }

            5 ->
                npcl(
                    "Yes, that's right. We're keeping the suspect under tight observation for the moment.",
                ).also { stage++ }
            6 -> playerl("Can't you just... I dunno.... arrest him?").also { stage++ }
            7 ->
                npcl(
                    "I'm not meant to discuss the case. You know what confidentiality rules are like.",
                ).also { stage++ }
            8 -> playerl("Fair enough.").also { stage = END_DIALOGUE }

            9 ->
                npcl(
                    "I'd be doing a lot better if nits like you didn't come crowding around me all day.",
                ).also { stage++ }
            10 -> playerl(FaceAnim.HALF_GUILTY, "But your legs are hanging down!").also { stage++ }
            11 -> npcl(FaceAnim.ANNOYED, "Go away!").also { stage++ }
            12 -> playerl("Please yourself!").also { stage = END_DIALOGUE }

            13 ->
                npcl(
                    "That's very kind of you. I'd rather like a nice bowl of stew, if you could fetch me one. I don't get many meal breaks.",
                ).also {
                    stage++
                }
            14 -> playerl(FaceAnim.HALF_ASKING, "You want a bowl of stew?").also { stage++ }
            15 -> npcl(FaceAnim.ROLLING_EYES, "If you wouldn't mind...").also { stage++ }
            16 -> playerl(FaceAnim.FRIENDLY, "I'll think about it!").also { stage = END_DIALOGUE }

            17 -> options("Okay, take the stew.", "No, I want to keep this stew.").also { stage++ }
            18 ->
                when (buttonID) {
                    1 -> playerl("Okay, take the stew.").also { stage++ }
                    2 -> playerl("No, I want to keep this stew!").also { stage = 20 }
                }

            19 -> {
                sendNPCDialogue(
                    player!!,
                    NPCs.GUARD_345,
                    "Gosh, that's very kind of you! Here, have a few coins for your trouble.",
                    FaceAnim.HAPPY,
                )
                end()
                removeItem(player!!, Items.STEW_2003)
                addItemOrDrop(player!!, Items.COINS_995, 30)
                setAttribute(player!!, DraynorUtils.feedGuardAttribute, true)
                submitWorldPulse(
                    object : Pulse(80) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    produceGroundItem(player!!, Items.BOWL_1923, 1, Location.create(3086, 3245, 0))
                                    removeAttribute(player!!, DraynorUtils.feedGuardAttribute)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }

            20 ->
                npcl("Fair enough. But if you change your mind, I'll probably still be here.").also {
                    stage = END_DIALOGUE
                }
        }
    }
}
