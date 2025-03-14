package content.region.kandarin.quest.sheepherder.handler

import core.api.inEquipment
import core.api.sendNPCDialogueLines
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class SheepHerderGateListener : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Scenery.GATE_166, Scenery.GATE_167), IntType.SCENERY, "open") { player, _ ->
            if (!inEquipment(player, Items.PLAGUE_JACKET_284, Items.PLAGUE_TROUSERS_285)) {
                sendNPCDialogueLines(
                    player,
                    NPCs.FARMER_BRUMTY_291,
                    FaceAnim.SUSPICIOUS,
                    false,
                    "You can't enter without your protective gear!",
                    "Can't have you spreading the plague!",
                )
            }
            return@on false
        }
    }
}
