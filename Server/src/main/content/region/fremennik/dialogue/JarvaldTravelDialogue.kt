package content.region.fremennik.dialogue

import content.region.fremennik.plugin.FremennikShipHelper
import content.region.fremennik.plugin.Travel
import core.api.isQuestComplete
import core.api.removeItem
import core.api.sendMessage
import core.api.sendNPCDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
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
        val questComplete = isQuestComplete(player!!, Quests.THE_FREMENNIK_TRIALS)
        val atWaterbirdIsland = player!!.location.isInRegion(10042)
        when (stage) {
            0 -> {
                if (!questComplete) {
                    sendNPCDialogue(player!!, NPCs.JARVALD_2438, "So do you have the 1000 coins for my service, and are you ready to leave now?", FaceAnim.HALF_ASKING).also { stage = 4 }
                } else {
                    showTopics(
                        Topic("YES", 1, true),
                        Topic("NO", if (atWaterbirdIsland) 2 else 6, true),
                        title = if (atWaterbirdIsland) "Leave island?" else "Select an Option"
                    )
                }
            }
            1 -> {
                end()
                if (atWaterbirdIsland) {
                    sendNPCDialogue(player!!, NPCs.JARVALD_2438, "Then let us away; There will be death to bring here another day!")
                    FremennikShipHelper.sail(player!!, Travel.WATERBIRTH_TO_RELLEKKA)
                } else {
                    FremennikShipHelper.sail(player!!, Travel.RELLEKKA_TO_WATERBIRTH)
                }
            }
            2 -> sendNPCDialogue(player!!, NPCs.JARVALD_2438, "Ha Ha Ha! A true huntsman at heart!", FaceAnim.LAUGH).also { stage++ }
            3 -> sendNPCDialogue(player!!, NPCs.JARVALD_2438, "I myself have killed over a hundred of the daggermouths, and did not think it too many!", FaceAnim.HAPPY).also { stage = END_DIALOGUE }
            4 -> showTopics(
                Topic("YES", 5, true),
                Topic("NO", if (atWaterbirdIsland) 6 else 2, true),
                title = if (atWaterbirdIsland) "Leave island?" else "Select an Option"
            )
            5 -> {
                end()
                val cost = Item(Items.COINS_995, 1000)
                if (!removeItem(player!!, cost)) {
                    sendMessage(player!!, "You cannot afford that.")
                    sendNPCDialogue(player!!, NPCs.JARVALD_2438, "Ah, outerlander... I go beyond my duty to allow you to accompany me, and you attempt to swindle me out of a reasonable fee? This is no way even for an outerlander to behave...")
                    return
                }
                if (atWaterbirdIsland) {
                    FremennikShipHelper.sail(player!!, Travel.WATERBIRTH_TO_RELLEKKA)
                    sendNPCDialogue(player!!, NPCs.JARVALD_2438, "Then let us away; There will be death to bring here another day!")
                } else {
                    FremennikShipHelper.sail(player!!, Travel.RELLEKKA_TO_WATERBIRTH)
                }
            }
            6 -> playerl(FaceAnim.NEUTRAL, "No, actually I have some stuff to do here first.").also { stage++ }
            7 -> sendNPCDialogue(player!!, NPCs.JARVALD_2438, "As you wish. Come and see me when your bloodlust needs sating.").also { stage = END_DIALOGUE }
        }
    }
}
