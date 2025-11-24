package content.region.misthalin.varrock.quest.firedup.dialogue

import content.global.skill.firemaking.items.Log
import content.minigame.allfiredup.plugin.BeaconState
import core.api.getVarbit
import core.api.inInventory
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.Vars

/**
 * Represents the Squire Fyre after quest dialogue.
 */
class SquireFyreDialogueFile : DialogueFile() {

    private var validLog: Int = -1

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> options("Could you look after this beacon for me?", "Can you tell me about other beacons?", "How's it going?", "I'm done talking to you, now!").also { stage++ }
            1 -> when (buttonID) {
                1 -> playerl(FaceAnim.HALF_ASKING,"Could you look after this beacon for me?").also { stage = 10 }
                2 -> playerl(FaceAnim.HALF_ASKING,"Can you tell me about other beacons?").also { stage = 50 }
                3 -> playerl(FaceAnim.HALF_ASKING,"How's it going?").also { stage = 70 }
                4 -> playerl(FaceAnim.HALF_ASKING,"I'm done talking to you, now!").also { stage = 90 }
            }
            10 -> {
                val beaconState = getVarbit(player!!, Vars.VARBIT_BEACON_MONASTERY_5152)
                when (beaconState) {
                    BeaconState.EMPTY.ordinal -> npcl(FaceAnim.FRIENDLY, "Of course. Once the beacon is lit, just leave 5 logs with me and I'll take care of the fire for as long as I can.").also { stage = END_DIALOGUE }
                    BeaconState.DYING.ordinal -> npcl(FaceAnim.FRIENDLY, "Well, I could, but since you're here, you might as well do it yourself.").also { stage = END_DIALOGUE }
                    else -> {
                        val logs = Log.values().map { it.logId }
                        validLog = logs.firstOrNull { player!!.inventory.getAmount(it) >= 5 } ?: -1
                        if (validLog == -1) {
                            npcl(FaceAnim.FRIENDLY,"I'm afraid you don't have enough of the right type of log for me to use.").also { stage = END_DIALOGUE }
                        } else {
                            npcl(FaceAnim.FRIENDLY,"I'll need five logs to be able to tend the fire. Is that okay?").also { stage++ }
                        }
                    }
                }
            }

            12 -> options("Yes.", "No.").also { stage++ }

            13 -> when (buttonID) {
                1 -> playerl(FaceAnim.FRIENDLY,"Here are five logs for you.").also { stage = 14 }
                2 -> playerl(FaceAnim.NEUTRAL,"I think I'll keep hold of my logs for now.").also { stage = 15 }
            }

            14 -> {
                if (validLog != -1 && player!!.inventory.getAmount(validLog) >= 5) {
                    player!!.inventory.remove(Item(validLog, 5))
                    npcl(FaceAnim.FRIENDLY,"Thank you! I'll use these five logs to keep the beacon burning while you're away.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FaceAnim.HALF_GUILTY,"You don't seem to have enough logs.").also { stage = END_DIALOGUE }
                }
            }
            15 -> npcl(FaceAnim.FRIENDLY,"Well, you know where I am if you change your mind.").also { stage = END_DIALOGUE }
            50 -> npcl(FaceAnim.FRIENDLY,"But, of course! Just have a look at my map.").also {
                val hasMacaw = inInventory(player!!, Items.MACAW_POUCH_12071)
                stage = if (hasMacaw) 51 else END_DIALOGUE
            }
            51 -> npcl(FaceAnim.FRIENDLY,"Come to think of it, the macaw you've got there in your pack would be perfect.").also { stage = END_DIALOGUE }
            70 -> npcl(FaceAnim.FRIENDLY,"Very well, thank you. We're ever so grateful for your help in testing the beacon network.").also { stage = 71 }
            71 -> playerl(FaceAnim.NEUTRAL, "Oh, it was no big deal. You're doing most of the hard work, standing watch like that.").also { stage = 72 }
            72 -> npcl(FaceAnim.FRIENDLY,"To be honest, I'd intended to become an 'Arcanist', but due to an administrative error, I ended up being put forward as an 'Arsonist'.").also { stage = 73 }
            73 -> playerl(FaceAnim.NEUTRAL, "Go figure.").also { stage = 74 }
            74 -> npcl(FaceAnim.FRIENDLY,"Still, things worked out for the best. It's a great honour to play such an important role in protecting the kingdom.").also { stage = END_DIALOGUE }
            90 -> npcl(FaceAnim.FRIENDLY,"Goodbye, then. Stop by again if you're in the neighbourhood - it's nice to have the company.").also { stage = END_DIALOGUE }
        }
    }
}
