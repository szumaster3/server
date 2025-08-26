package content.region.kandarin.gnome.plugin

import content.region.kandarin.gnome.dialogue.GnomeWomanDialogue
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.NPCs

class GnomeStrongholdListener : InteractionListener {

    override fun defineListeners() {
        on(intArrayOf(NPCs.GNOME_WOMAN_168,NPCs.GNOME_WOMAN_169), IntType.NPC, "Talk-to") { player, node ->
            openDialogue(player, GnomeWomanDialogue(), node.id)
            return@on true
        }
    }
}