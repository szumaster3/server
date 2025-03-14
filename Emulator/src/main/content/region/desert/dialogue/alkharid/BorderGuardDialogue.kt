package content.region.desert.dialogue.alkharid

import core.api.getScenery
import core.api.quest.getQuestStage
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class BorderGuardDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val gates = if (player!!.location.y == 3227) getScenery(3268, 3227, 0) else getScenery(3268, 3227, 0)
        npc = NPC(NPCs.BORDER_GUARD_7912)
        when (stage) {
            0 -> player("Can I come through this gate?").also { stage++ }
            1 ->
                if (getQuestStage(player!!, Quests.PRINCE_ALI_RESCUE) < 50) {
                    npc("You must pay a toll of 10 gold coins to pass.").also { stage++ }
                } else {
                    npc("You may pass for free, you are a friend of Al-Kharid.").also { stage = 100 }
                }

            2 -> options("Okay, I'll pay.", "Who does my money go to?", "No thanks, I'll walk around.").also { stage++ }
            3 ->
                when (buttonID) {
                    1 -> {
                        end()
                        if (!removeItem(player!!, Item(Items.COINS_995, 10))) {
                            sendMessage(player!!, "You need 10 gold coins to pay the toll.")
                        } else {
                            DoorActionHandler.handleAutowalkDoor(player!!, gates!!)
                        }
                    }

                    2 -> player("Who does my money go to?").also { stage = 20 }
                    3 -> player("No thanks, I'll walk around.").also { stage++ }
                }

            4 -> npc("As you wish. Don't go too near the scorpions.").also { stage = END_DIALOGUE }
            20 -> npc("The money goes to the city of Al-Kharid.", "Will you pay the toll?").also { stage++ }
            21 -> options("Okay, I'll pay.", "No thanks, I'll walk around.").also { stage++ }
            22 ->
                when (buttonID) {
                    1 -> {
                        end()
                        if (!removeItem(player!!, Item(Items.COINS_995, 10))) {
                            sendMessage(player!!, "You need 10 gold coins to pay the toll.")
                        } else {
                            DoorActionHandler.handleAutowalkDoor(player!!, gates!!)
                        }
                    }

                    2 -> player("No thanks, I'll walk around.").also { stage = 4 }
                }

            100 -> {
                end()
                DoorActionHandler.handleAutowalkDoor(player!!, gates!!)
            }
        }
    }
}
