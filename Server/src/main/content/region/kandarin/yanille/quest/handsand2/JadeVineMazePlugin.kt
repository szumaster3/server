package content.region.kandarin.yanille.quest.handsand2

import content.data.items.SkillingTool
import core.api.*
import core.api.utils.Vector
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.Animator
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Scenery

class JadeVineMazePlugin : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles enter to vine maze.
         */

        on(Scenery.VINE_27126, IntType.SCENERY, "climb-up") { player, _ ->
            forceMove(player, player.location, Location.create(2888, 3005, 1), 0, 60, null)
            return@on true
        }

        on(Scenery.VINE_27151, IntType.SCENERY, "climb-up") { player, node ->
            when(node.location.y) {
                2982 -> forceMove(player, player.location, Location.create(2892, 2982, 3), 0, 60, null, 3599)
                2980 ->
            }
            return@on true
        }

        on(Scenery.VINE_27152, IntType.SCENERY, "climb-up") { player, node ->
            when(node.location.y) {
                2998 -> forceMove(player, player.location, Location.create(2896, 2999, 2), 0, 60, null, 3599)
                2978 -> forceMove(player, player.location, Location.create(2916, 2978, 2), 0, 60, null, 3599)
                2975 -> forceMove(player, player.location, Location.create(2916, 2975, 2), 0, 60, null, 3599)
                2971 -> forceMove(player, player.location, Location.create(2907, 2973, 2), 0, 60, null, 3599)

            }
            return@on true
        }

        on(Scenery.VINE_27129, IntType.SCENERY, "climb-down") { player, node ->
            when(node.location.y) {
                3004 -> forceMove(player, player.location, Location.create(2892, 3004, 2), 0, 60, null, 3599)
                2990 -> forceMove(player, player.location, Location.create(2900, 2991, 1), 0, 60, null, 3599)

                2980 -> forceMove(player, player.location, Location.create(2894, 2979, 1), 0, 60, null, 3599)
                2982 -> forceMove(player, player.location, Location.create(2894, 2982, 2), 0, 60, null, 3599)
                2978 -> forceMove(player, player.location, Location.create(2906, 2978, 1), 0, 60, null, 3599)
                2973 -> forceMove(player, player.location, Location.create(2909, 2973, 1), 0, 60, null, 3599)

                else -> forceMove(player, player.location, Location.create(2896, 2997, 1), 0, 60, null, Animations.JUMP_OVER_7268)
            }
            return@on true
        }

        on(Scenery.VINE_27130, IntType.SCENERY, "climb-down") { player, _ ->
            forceMove(player, player.location, Location.create(2898, 2992, 0), 0, 30, null, Animations.JUMP_OVER_7268)
            return@on true
        }

        on(Scenery.VINES_27173, IntType.SCENERY, "cut") { player, node ->
            val tool = SkillingTool.getMachete(player)

            if (tool == null || !inEquipment(player, tool.id)) {
                sendMessage(player, "You need to be holding a machete to cut away this jungle.")
                return@on true
            }

            lock(player, 3)
            player.animate(Animation(tool.animation, Animator.Priority.HIGH))
            runTask(player, 3) {
                replaceScenery(node.asScenery(), node.id + 1, 6)
            }

            return@on true
        }

        on(Scenery.CUT_VINES_27174, IntType.SCENERY, "crawl-through") { player, node ->
            val dir = Direction.getDirection(player.location, node.location)
            val dest = player.location.transform(dir, 2)

            forceMove(
                player,
                player.location,
                dest,
                0,
                60,
                null,
                Animations.CRAWLING_2796
            )
            return@on true
        }

    }
}