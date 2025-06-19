package content.region.fremennik.island.neitiznot.plugin

import core.api.MapArea
import core.api.getRegionBorders
import core.api.interaction.openBankAccount
import core.api.openDialogue
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class NeitiznotPlugin : InteractionListener, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        getRegionBorders(9275), ZoneBorders(2313, 3786, 2331, 3802)
    )

    override fun defineListeners() {

        onUseWith(IntType.NPC, -1, NPCs.YAK_5529) { player, _, with ->
            if (with is NPC && with.id == NPCs.YAK_5529) {
                sendMessage(player, "The cow doesn't want that.")
            }
            return@onUseWith true
        }

        /*
         * Handles opening bank chest.
         */

        on(Scenery.BANK_CHEST_21301, IntType.SCENERY, "open") { player, _ ->
            openBankAccount(player)
            return@on true
        }

        /*
         * Handles cure yak-hide.
         */

        on(NPCs.THAKKRAD_SIGMUNDSON_5506, IntType.NPC, "Craft-goods") { player, _ ->
            openDialogue(player, "thakkrad-yak")
            return@on true
        }
    }
}