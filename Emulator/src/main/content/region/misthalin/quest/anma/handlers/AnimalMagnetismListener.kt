package content.region.misthalin.quest.anma.handlers

import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Quests

class AnimalMagnetismListener : InteractionListener {
    override fun defineListeners() {
        onEquip(Items.CRONE_MADE_AMULET_10500) { player, _ ->
            if (!isQuestComplete(player, Quests.ANIMAL_MAGNETISM)) {
                sendMessage(player, "Your ghostliness isn't ethereal enough to wear this.")
                return@onEquip false
            } else {
                return@onEquip true
            }
        }
    }
}
