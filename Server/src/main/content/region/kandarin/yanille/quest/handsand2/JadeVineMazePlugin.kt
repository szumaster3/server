package content.region.kandarin.yanille.quest.handsand2

import content.data.items.SkillingTool
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.Scenery

class JadeVineMazePlugin : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles enter to vine maze.
         */

        on(Scenery.VINE_27126, IntType.SCENERY, "climb-up") { player, node ->
            teleport(player, Location.create(2888, 3007, 1), TeleportManager.TeleportType.INSTANT)
            forceMove(player, player.location, Location.create(2888, 3005, 1), 30, 90, null)
            return@on true
        }

        on(Scenery.VINE_27152, IntType.SCENERY, "climb-up") { player, node ->
            when(node.location.y) {
                3004 -> forceMove(player, player.location, Location.create(2892, 3004, 2), 30, 90, null, 3599)
                2998 -> forceMove(player, player.location, Location.create(2896, 2999, 2), 30, 90, null, 3599)
            }
            return@on true
        }

        on(Scenery.VINE_27129, IntType.SCENERY, "climb-down") { player, _ ->
            forceMove(player, player.location, Location.create(2896, 2997, 1), 30, 90, null, Animations.JUMP_OVER_7268)
            return@on true
        }

        on(Scenery.VINE_27130, IntType.SCENERY, "climb-down") { player, _ ->
            forceMove(player, player.location, Location.create(2898, 2992, 0), 30, 90, null, Animations.JUMP_OVER_7268)
            return@on true
        }

        on(Scenery.VINES_27173, IntType.SCENERY, "cut") { player, _ ->
            val hasMachete = SkillingTool.getMachete(player)
            if (hasMachete != null) {
                if(!inEquipment(player, hasMachete.id)) {
                    sendMessage(player, "You need to be holding a machete to cut away this jungle.")
                } else {
                    //
                }
            }
            return@on true
        }



    }
}