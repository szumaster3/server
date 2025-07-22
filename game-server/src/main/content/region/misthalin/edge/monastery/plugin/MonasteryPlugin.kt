package content.region.misthalin.edge.monastery.plugin

import core.api.sendMessage
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class MonasteryPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles ladders to Monastery 1st floor.
         */

        on(Scenery.LADDER_2641, IntType.SCENERY, "climb-up") { player, node ->
            val option = "climb-up"
            val abbot = node.location.equals(Location(3057, 3483, 0))
            if (!player.getSavedData().globalData.isJoinedMonastery()) {
                player.dialogueInterpreter.open(if (abbot) NPCs.ABBOT_LANGLEY_801 else NPCs.MONK_7727, true)
            } else {
                ClimbActionHandler.climbLadder(player, node.asScenery(), option)
                sendMessage(player, "You climb up the ladder.")
            }
            return@on true
        }
    }
}