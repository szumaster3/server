package content.region.karamja.plugin

import content.data.items.SkillingTool
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.*

class JadeVineMazePlugin : MapArea, InteractionListener {

    /**
     * The trees with snakes objects.
     */
    private val STRIKE_OBJECTS = mapOf(
        27089 to listOf(
            Location.create(2888, 2998),
            Location.create(2888, 2987),
            Location.create(2923, 2980),
            Location.create(2920, 2975)
        )
    )

    private val CLIMB_UP_DESTINATION = mapOf(
        2998 to Location.create(2896, 2998, 2),
        2978 to Location.create(2907, 2978, 2),
        2975 to Location.create(2917, 2975, 2),
        2973 to Location.create(2908, 2973, 2)
    )

    private val CLIMB_DOWN_DESTINATION = mapOf(
        3004 to Location.create(2892, 3004, 2),
        2990 to Location.create(2900, 2991, 1),
        2987 to Location.create(2894, 2988, 1),
        2980 to Location.create(2894, 2979, 1),
        2982 to Location.create(2894, 2982, 2),
        2978 to Location.create(2918, 2978, 1),
        2973 to Location.create(2909, 2973, 1)
    )

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(Regions.JADE_VINE_MAZE_11566))
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        super.entityStep(entity, location, lastLocation)

        if (entity !is Player) return
        val p = entity
        val delay = "jade_vine_maze:strike_delay"
        if (p.getAttribute(delay, -1) > GameWorld.ticks) return
        for ((_, locations) in STRIKE_OBJECTS) {
            if (locations.any { it.x == location.x && it.y == location.y && it.z == location.z }) {

                val agility = p.getSkills().getStaticLevel(Skills.AGILITY)
                val roll = (agility / 99.0 * 100).toInt()
                val success = !RandomFunction.roll(roll)

                if (success) {
                    applyPoison(p, p, 6)
                } else {
                    p.sendMessage("Your Agility enables you to evade the snike strike.")
                }

                p.setAttribute(delay, GameWorld.ticks + 3)
                break
            }
        }
    }

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
                2987 -> forceMove(player, player.location, Location.create(2894, 2987, 2), 0, 60, null, 3599).also { player.moveStep() }
            }
            return@on true
        }

        on(Scenery.VINE_27152, IntType.SCENERY, "climb-up") { player, node ->
            val destination = CLIMB_UP_DESTINATION[node.location.y]
            if (destination != null) {
                forceMove(player, player.location, destination, 0, 60, null, 3599)
                player.moveStep()
            }
            return@on true
        }

        on(Scenery.VINE_27128, IntType.SCENERY, "climb-up") { player, node ->
            when(player.location.y) {
                2988 -> forceMove(player, player.location, Location.create(2895, 2988, 1), 0, 60, null, 3599)
                else -> forceMove(player, player.location, Location.create(2891, 3000, 1), 0, 60, null, 3599)
            }
            return@on true
        }

        on(Scenery.VINE_27129, IntType.SCENERY, "climb-down") { player, node ->
            val destination = CLIMB_DOWN_DESTINATION[node.location.y] ?: Location.create(2896, 2997, 1)
            forceMove(player, player.location, destination, 0, 60, null, Animations.JUMP_OVER_7268)
            return@on true
        }

        on(Scenery.VINE_27130, IntType.SCENERY, "climb-down") { player, node ->
            when(node.location.y) {
                3000 -> forceMove(player, player.location, Location.create(2889, 3000, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2988 -> forceMove(player, player.location, Location.create(2897, 2988, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                else -> forceMove(player, player.location, Location.create(2898, 2992, 0), 0, 30, null, Animations.JUMP_OVER_7268)
            }
            return@on true
        }

        /*
         * Handles going through vines using machete.
         */

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
            val destination = player.location.transform(dir, 2)
            forceMove(player, player.location, destination, 0, 60, null, Animations.CRAWLING_2796)
            return@on true
        }

        on(Scenery.VINES_27175, IntType.SCENERY, "squeeze-through") { player, node ->
            val dir = Direction.getDirection(player.location, node.location)
            val destination = player.location.transform(dir, 2)
            forceMove(player, player.location, destination, 0, 60, null, 3844)
            return@on true
        }

        /*
         * Handles swing on the vine.
         * TODO: GFX
         */

        on(Scenery.VINE_27180, IntType.SCENERY, "swing-on") { player, _ ->
            lock(player, 3)
            playAudio(player, Sounds.SWING_ACROSS_2494)
            forceMove(player, player.location, Location.create(2901, 2985, 2), 30, 120, null, Animations.ROPE_SWING_751) {
                sendMessage(player, "You skillfully swing across.")
            }
            return@on true
        }

        /*
         * Handles crossing the vine.
         */

        on(Scenery.VINE_27185, IntType.SCENERY, "cross") { player, node ->
            val dir = Direction.getDirection(player.location, node.location)
            val destination = player.location.transform(dir, 5)
            val fail = AgilityHandler.hasFailed(player, 1, failChance = 0.3)

            lock(player, 8)
            if (!fail) {
                AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    destination,
                    Animation.create(762),
                    0.0,
                    "You skillfully cross the vine."
                )
            } else {
                AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    destination,
                    Animation.create(762),
                    0.0,
                    null
                )
                AgilityHandler.fail(
                    player,
                    0,
                    Location.create(2913, 2979, 0),
                    Animation.create(Animations.FALL_BALANCE_764),
                    0,
                    "You lose your footing and fall into the water."
                )
                runTask(player, 3) {
                    player.animate(Animation.create(765))
                    forceMove(
                        player,
                        player.location,
                        Location.create(2912, 2980, 0),
                        0,
                        90,
                        null,
                        Animations.CLIMB_UP_OUT_OF_WATER_7273
                    ) {
                        sendMessage(player, "You scramble out of the water before the crocodiles take an interest.")
                    }
                }
            }

            return@on true
        }

        /*
         * Handles shortcut to waterfall.
         * TODO: Animation + Map other locations.
         */

        on(Scenery.HOLE_27186, IntType.SCENERY, "enter") { player, node ->
            if(node.location.x == 2908 && node.location.y == 2960) {
                teleport(player, Location.create(2909, 2985, 0), TeleportManager.TeleportType.INSTANT)
            }
            if(node.location.x == 2909 && node.location.y == 2986) {
                teleport(player, Location.create(2908, 2961, 0), TeleportManager.TeleportType.INSTANT)
            }
            return@on true
        }

        /*
         * Handles shortcut from waterfall to jade vine maze.
         */

        on(Scenery.VINE_27182, IntType.SCENERY, "enter") { player, _ ->
            player.animate(Animation(832))
            player.teleport(Location.create(2883, 2986, 0), 1)
            return@on true
        }

        /*
         * Handles shortcut backward.
         */

        on(Scenery.VINE_27181, IntType.SCENERY, "enter") { player, _ ->
            player.animate(Animation(832))
            player.teleport(Location.create(2908, 2964, 0), 1)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.VINE_27180) { _, _ ->
            return@setDest Location(2894, 2985, 2)
        }
    }
}