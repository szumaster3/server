package core.game.worldevents.events.christmas.randoms

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.emote.Emotes
import core.game.worldevents.events.HolidayRandoms
import org.rs.consts.Items

class SnowmanHolidayRandomDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (HolidayRandoms.getEventNpc(player!!) == null) {
            player!!.dialogueInterpreter.close()
        }

        when (stage) {
            0 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "Greetings, ${player!!.username.replaceFirstChar {
                        if (it.isLowerCase()) {
                            it.titlecase()
                        } else {
                            it
                                .toString()
                        }
                    }}. I'm visiting from the Land of Snow, but I need some help.",
                ).also {
                    stage++
                }

            1 -> options("What do you need?", "No, sorry.").also { stage++ }
            2 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "What do you need?").also { stage = 10 }
                    2 -> playerl(FaceAnim.NEUTRAL, "No, sorry.").also { stage = 30 }
                }

            10 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "I visit Gielinor every year during this time when it is the coldest here, but it is still a little warm for us snowmen.",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "I'm really enjoying my visit, but I have started to melt. Would you happen to have a snowball?",
                ).also {
                    stage++
                }

            12 -> {
                if (inInventory(player!!, Items.SNOWBALL_11951, 1)) {
                    playerl(FaceAnim.HAPPY, "Actually, I do! You can have it.").also { stage = 20 }
                } else {
                    playerl(FaceAnim.NEUTRAL, "I don't have a snowball, sorry.").also { stage = 30 }
                }
            }

            20 -> {
                npcl(FaceAnim.CHILD_NEUTRAL, "Thank you, ${player!!.username}!").also { stage++ }
            }

            21 -> {
                if (player!!.emoteManager.isUnlocked(Emotes.SNOWMAN_DANCE)) {
                    npcl(
                        FaceAnim.CHILD_NEUTRAL,
                        "Please take this lamp I found on my travels. Happy holidays, ${player!!.username}!",
                    ).also {
                        stage =
                            22
                    }
                } else {
                    npcl(
                        FaceAnim.CHILD_NEUTRAL,
                        "Before I leave I will show you how to dance like us snowmen. Happy holidays, ${player!!.username}!",
                    ).also {
                        stage =
                            23
                    }
                }
            }

            22 -> {
                if (removeItem(player!!, Items.SNOWBALL_11951)) {
                    addItem(player!!, Items.LAMP_2528)
                }

                end()
                HolidayRandoms.terminateEventNpc(player!!)
            }

            23 -> {
                if (removeItem(player!!, Items.SNOWBALL_11951)) {
                    player!!.emoteManager.unlock(Emotes.SNOWMAN_DANCE)
                }

                queueScript(player!!, 2, QueueStrength.SOFT) {
                    sendDialogue(player!!, "You have unlocked the Snowman Dance emote!")
                    return@queueScript stopExecuting(player!!)
                }
                end()
                HolidayRandoms.terminateEventNpc(player!!)
            }

            30 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "I must be going before I melt. Happy Holidays, ${player!!.username}.",
                ).also {
                    stage++
                }

            31 -> {
                end()
                HolidayRandoms.terminateEventNpc(player!!)
            }
        }
    }
}
