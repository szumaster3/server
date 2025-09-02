package content.region.karamja.shilo.dialogue

import content.region.karamja.shilo.plugin.ShiloVillagePlugin
import core.api.inInventory
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import shared.consts.Items
import shared.consts.NPCs

class ShiloTravelDialogue : DialogueFile() {

    init {
        stage = 0
    }

    override fun handle(componentID: Int, buttonID: Int) {
        val isShilo = npc?.id == NPCs.HAJEDY_510
        val destination = if (isShilo) "Shilo Village" else "Brimhaven"
        when (stage) {
            0 -> {
                npc("I am offering a cart ride to $destination if you're interested? It will cost 10 gold coins. Is that Ok?")
                stage = 1
            }
            1 -> {
                options("Yes please, I'd like to go to $destination.", "No, thanks.")
                stage = 2
            }
            2 -> {
                when (buttonID) {
                    1 -> {
                        end()
                        if (!inInventory(player!!, Items.COINS_995, 10)) {
                            playerl(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
                        } else {
                            sendMessage(player!!, "You hop into the cart and the driver urges the horses on. You take a taxing journey through the jungle to $destination.")
                            ShiloVillagePlugin.quickTravel(player!!, npc!!.id)
                        }
                    }
                    2 -> player("No, thanks.").also { stage++ }
                }
            }
            3 -> end()
        }
    }
}