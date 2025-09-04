package content.region.fremennik.dialogue

import content.region.fremennik.plugin.FremennikShipHelper
import content.region.fremennik.plugin.Travel
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.isQuestComplete
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Handles travel options for Jarvald NPC.
 */
class JarvaldTravelDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> options("YES", "NO", title = "Leave island?").also { stage++ }
            1 -> {
                if (npc?.id == NPCs.JARVALD_2438 && buttonID == 1) {
                    npcl(FaceAnim.FRIENDLY, "Then let us away; There will be death to bring here another day!")
                    FremennikShipHelper.sail(player!!, Travel.WATERBIRTH_TO_RELLEKKA)
                    end()
                } else if (!isQuestComplete(player!!, Quests.THE_FREMENNIK_TRIALS)) {
                    npcl(FaceAnim.HALF_ASKING, "So do you have the 1000 coins for my service, and are you ready to leave now?")
                    stage++
                } else {
                    npcl(FaceAnim.LAUGH, "Ha Ha Ha! A true huntsman at heart!").also { stage = 4 }
                }
            }
            2 -> options("YES", "NO").also { stage++ }
            3 -> {
                end()
                if (buttonID != 1) return
                val cost = Item(Items.COINS_995, 1000)
                if (!removeItem(player!!, cost)) {
                    sendMessage(player!!, "You cannot afford that.")
                    npcl(FaceAnim.NEUTRAL, "Ah, outerlander... I go beyond my duty to allow you to accompany me, and you attempt to swindle me out of a reasonable fee? This is no way even for an outerlander to behave...")
                    return
                }
                if (!isQuestComplete(player!!, Quests.THE_FREMENNIK_TRIALS)) {
                    npcl(FaceAnim.NEUTRAL, "I suggest you head to the cave with some urgency outerlander, the cold air out here might be too much for the likes of you...")
                } else {
                    npcl(FaceAnim.NEUTRAL, "I would head straight for the cave and not tarry too long ${FremennikTrials.getFremennikName(player!!)}, the cold winds on this island can cut right through you should you spend too long in them.")
                }
                FremennikShipHelper.sail(player!!, Travel.RELLEKKA_TO_WATERBIRTH)
            }
            4 -> npcl(FaceAnim.HAPPY, "I myself have killed over a hundred of the daggermouths, and did not think it too many!").also { stage = END_DIALOGUE }
        }
    }
}
