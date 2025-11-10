package content.region.misthalin.draynor.plugin

import core.api.runTask
import core.api.sendNPCDialogue
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Scenery

class DraynorSewerPlugin: InteractionListener {

    override fun defineListeners() {

        /*
         * Handles exit from draynor sewers (south).
         *

        on(Scenery.LADDER_26518, IntType.SCENERY, "climb-up") { player, _ ->
            player.animate(climbAnimation)
            runTask(player, 2) {
                ClimbActionHandler.climb(player, null, Location.create(3118, 3243, 0))
            }
            return@on true
        }
        */

        /*
         * Handles exit from draynor sewers (west).
         *

        on(Scenery.LADDER_32015, IntType.SCENERY, "climb-up") { player, _ ->
            player.animate(climbAnimation)
            runTask(player, 2) {
                ClimbActionHandler.climb(player, null, Location.create(3084, 3271, 0))
            }
            return@on true
        }
        */

        onUseAnyWith(IntType.NPC, NPCs.RUANTUN_1916) { player, _, with ->
            sendNPCDialogue(player, with.id, "I have no ussse for that...")
            return@onUseAnyWith true
        }
    }

    companion object {
        val climbAnimation = Animation(Animations.CLIMB_OUT_OF_LADDER_IN_DRAYNOR_SEWER_2413)
    }
}
