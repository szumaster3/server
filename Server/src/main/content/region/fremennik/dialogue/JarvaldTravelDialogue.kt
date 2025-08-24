package content.region.fremennik.dialogue

import content.region.fremennik.plugin.FremennikShipHelper
import content.region.fremennik.plugin.Travel
import core.api.isQuestComplete
import core.api.removeItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Quests

class JarvaldTravelDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                options("Leave island?", "YES", "NO")
                stage++
            }

            1 -> if (npc!!.id == 2438 && buttonID == 1) {
                end()
                FremennikShipHelper.sail(player!!, Travel.WATERBIRTH_TO_RELLEKKA)
            } else if (!isQuestComplete(player!!, Quests.THE_FREMENNIK_TRIALS)) {
                npc(FaceAnim.HALF_ASKING, "So do you have the 1000 coins for my service, and are you ready to leave now?")
                stage++
            }
            2 -> {
                options("YES", "NO")
                stage++
            }
            3 -> {
                end()
                if (buttonID == 1) {
                    removeItem(player!!, Item(Items.COINS_995, 1000))
                    FremennikShipHelper.sail(player!!, Travel.RELLEKKA_TO_WATERBIRTH)
                }
            }
        }
    }
}