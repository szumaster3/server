package content.region.misc.quest.giantdwarf.dialogue

import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CommanderVeldabanDDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, "The Giant Dwarf")) {
            in 2..3 ->
                when (stage) {
                    0 -> npcl(FaceAnim.OLD_DEFAULT, "Identify yourself, human!").also { stage++ }
                    1 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I err, I'm ${player.name}. I was told I was under arrest. I had a little accident on the way in here.",
                        ).also { stage++ }

                    2 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "Ah, so you're the one who knocked over the statue of our glorious king!",
                        ).also { stage++ }

                    3 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I suppose so... I mean, I didn't do it on purpose or anything...",
                        ).also { stage++ }

                    4 -> npcl(FaceAnim.OLD_DEFAULT, "Ah, don't worry about it!").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "We haven't been ruled by a king for many a year... that statue was long overdue for a replacement anyway. You're not under arrest, I merely wanted to talk to you.",
                        ).also { stage++ }

                    6 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "But let me introduce myself. I am Veldaban, son of Dondakan. I am the commander of the Black Guard in Keldagrim.",
                        ).also { stage++ }

                    7 -> playerl(FaceAnim.FRIENDLY, "Well, ehm... nice to meet you, Veldaban.").also { stage++ }
                    8 -> npcl(FaceAnim.OLD_DEFAULT, "Now listen carefully. I have a task for you.").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "I want you to go and see the local sculptor and help him build a new statue.",
                        ).also { stage++ }

                    10 -> npcl(FaceAnim.OLD_DEFAULT, "Will you do this for us?").also { stage++ }
                    11 -> options("No, I can't be bothered.", "Yes, I will do this.").also { stage++ }
                    12 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "No, I can't be bothered.").also { stage = 20 }
                            2 -> playerl(FaceAnim.FRIENDLY, "Yes, I will do this.").also { stage = 13 }
                        }

                    13 -> npcl(FaceAnim.OLD_DEFAULT, "You are a good human, ${player.name}.").also { stage++ }
                    14 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "Now, I need you to go to Blasidar, He is a sculptor who lives in Keldagrim-East.",
                        ).also { stage++ }

                    15 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "To be honest I'm not much of a sculptor myself.",
                        ).also { stage++ }

                    16 -> npcl(FaceAnim.OLD_DEFAULT, "Oh, don't worry!").also { stage++ }
                    17 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "I just need you to help him out while he rebuilds the statue.",
                        ).also { stage++ }

                    18 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "I'm sure there are many tasks that he needs doing.",
                        ).also { stage++ }

                    19 -> playerl(FaceAnim.FRIENDLY, "I'll see what I can do.").also { stage = END_DIALOGUE }
                    20 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "You destroy our statue and you cannot be bothered to help rebuild it? By Guthix, you are rude... even for a human!",
                        ).also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COMMANDER_VELDABAN_2129)
    }
}
