package content.minigame.mta.handlers

import core.api.getUsedOption
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class MTAListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.MAZE_GUARDIAN_3102, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, node.id, node)
            return@on true
        }

        on(ProgressHat.hats, IntType.ITEM, "talk-to", "destroy") { player, node ->
            val option = getUsedOption(player)
            if (option == "destroy") {
                openDialogue(player, NPCs.PIZZAZ_HAT_3096, node, true, true)
            } else {
                openDialogue(player, NPCs.PIZZAZ_HAT_3096)
            }
            return@on true
        }

        setDest(IntType.NPC, intArrayOf(NPCs.MAZE_GUARDIAN_3102), "talk-to") { player, node ->
            return@setDest node.location.transform(Direction.getDirection(player.location, node.location), -1)
        }

        setDest(IntType.SCENERY, mtaTeleports, "enter") { _, node ->
            when (node.id) {
                Scenery.TELEKINETIC_TP_10778 -> return@setDest Location.create(3363, 3316, 0)
                Scenery.ENCHANTERS_TP_10779 -> return@setDest Location.create(3361, 3318, 0)
                Scenery.ALCHEMISTS_TP_10780 -> return@setDest Location.create(3363, 3320, 0)
                else -> return@setDest Location.create(3365, 3318, 0)
            }
        }
    }

    companion object {
        val mtaTeleports =
            intArrayOf(
                Scenery.TELEKINETIC_TP_10778,
                Scenery.ENCHANTERS_TP_10779,
                Scenery.ALCHEMISTS_TP_10780,
                Scenery.GRAVEYARD_TP_10781,
            )
    }
}
