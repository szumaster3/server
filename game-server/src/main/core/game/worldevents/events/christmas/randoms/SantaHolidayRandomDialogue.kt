package core.game.worldevents.events.christmas.randoms

import core.api.addItemOrDrop
import core.api.getAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.worldevents.events.HolidayRandoms
import core.game.worldevents.events.christmas.Giftmas
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items

class SantaHolidayRandomDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (HolidayRandoms.getEventNpc(player!!) == null) {
            player!!.dialogueInterpreter.close()
        }

        when (stage) {
            0 -> npcl(FaceAnim.JOLLY, "Ho Ho Ho!").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Merry Christmas, ${player!!.username}! Would you like to know if you are on the nice or naughty list?",
                ).also {
                    stage =
                        10
                }

            10 -> options("Yes, please.", "No, thanks.").also { stage = 11 }
            11 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.HAPPY, "Yes, please.").also { stage = 20 }
                    2 -> {
                        playerl(FaceAnim.NEUTRAL, "No, thanks.")
                        stage = END_DIALOGUE
                        HolidayRandoms.terminateEventNpc(player!!)
                    }
                }

            20 ->
                if (getAttribute(HolidayRandoms.getEventNpc(player!!)!!, "playerisnice", false)) {
                    npcl(
                        FaceAnim.HAPPY,
                        "Let me check my list... ${player!!.username}.. You are on the nice list!",
                    ).also {
                        stage =
                            30
                    }
                } else {
                    npcl(
                        FaceAnim.SAD,
                        "Let me check my list... ${player!!.username}.. You are on the naughty list!",
                    ).also {
                        stage =
                            40
                    }
                }

            30 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Since you have been a good ${if (player!!.isMale) "boy" else "girl"} this year, you get a gift. Merry Christmas, ${player!!.username}!",
                ).also {
                    stage =
                        31
                }

            31 -> {
                val loot = Giftmas.MBOX_LOOT.roll().first()
                addItemOrDrop(player!!, loot.id, loot.amount)
                HolidayRandoms.terminateEventNpc(player!!)
                end()
            }

            40 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "If you work on being a good ${if (player!!.isMale) "boy" else "girl"} from now on you will make it on the nice list!",
                ).also {
                    stage =
                        41
                }

            41 -> npcl(FaceAnim.NEUTRAL, "Since you are on the naughty list, you get coal.").also { stage = 42 }
            42 -> {
                addItemOrDrop(player!!, Items.COAL_454, RandomFunction.random(1, 21))
                HolidayRandoms.terminateEventNpc(player!!)
                end()
            }
        }
    }
}
