package content.region.islands.quests.icemt

import core.api.sendDialogueLines
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class PerilsOfIceMountainListener : InteractionListener {

    override fun defineListeners() {
        on(Items.LETTER_13229, IntType.ITEM, "read") { player, _ ->
            sendDialogueLines(player, "The letter is written in an impenetrable dwarven script.", "Only a dwarf would be able to decipher it.")
            return@on true
        }
    }
}