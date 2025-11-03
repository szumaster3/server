package content.region.karamja.plugin

import content.data.items.SkillingTool
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.cache.def.impl.VarbitDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.*
import kotlin.math.sqrt

/**
 * Handles player interactions primarily related to quests in the Jade Vine Maze.
 *
 * Quest chronology:
 * 1. The Hand in the Sand  - prequel quest
 * 2. Back to my Roots      - main quest
 * 3. One Foot in the Grave - miniquest / sequel
 */
class JadeVineMazePlugin : MapArea, InteractionListener {

    companion object {
        /**
         * The tree snake scenery ids.
         *///https://runescape.wiki/w/Tree_(snake)
        private val STRIKE_OBJECTS = mapOf(
            27089 to listOf(
                Location.create(2888, 2998),
                Location.create(2888, 2987),
                Location.create(2923, 2980),
                Location.create(2920, 2975)
            )
        )

        /**
         * Maximum distance from the tree at which the snake can strike.
         */
        private const val STRIKE_RANGE = 1.0

        /**
         * The locations for climbing up vines.
         * - Key: The Y-coordinate of the vine.
         * - Value: The location player will arrive at when climbing up.
         */
        private val CLIMB_UP_DESTINATION = mapOf(
            2998 to Location.create(2896, 2998, 2),
            2978 to Location.create(2907, 2978, 2),
            2975 to Location.create(2917, 2975, 2),
            2973 to Location.create(2908, 2973, 2)
        )

        /**
         * Destination locations for climbing down vines.
         * - Key: The Y-coordinate of the vine.
         * - Value: The location player will arrive at when climbing down.
         */
        private val CLIMB_DOWN_DESTINATION = mapOf(
            3004 to Location.create(2892, 3004, 2),
            2990 to Location.create(2900, 2991, 1),
            2987 to Location.create(2894, 2988, 1),
            2980 to Location.create(2894, 2979, 1),
            2982 to Location.create(2894, 2982, 2),
            2978 to Location.create(2918, 2978, 1),
            2973 to Location.create(2909, 2973, 1)
        )

        /**
         * The predefined locations for transport to another random vine
         * or hole within the maze via Hole (jade vine maze).
         */// https://runescape.wiki/w/Hole_(jade_vine_maze)
        private val HOLE_LOCATIONS = arrayOf(
            Location.create(2883, 2995),
            Location.create(2896, 2982),
            Location.create(2897, 2994),
            Location.create(2905, 2983),
            Location.create(2908, 2960),
            Location.create(2909, 2986)
        )

        /**
         * Root object locations used in Back to my Roots.
         */
        private val ROOTS_LOCATIONS = mapOf(
            27059 to listOf( // Roots
                Location.create(2904, 2973),
                Location.create(2905, 2975),
                Location.create(2908, 2975),
                Location.create(2903, 2971),
                Location.create(2907, 2976)
            ),
        )

        private val ROOT_CUTTINGS = intArrayOf(
            Items.ROOT_CUTTING_11770,
            Items.ROOT_CUTTING_11771,
            Items.ROOT_CUTTING_11772,
            Items.ROOT_CUTTING_11773,
            Items.ROOT_CUTTING_11774,
        )
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(Regions.JADE_VINE_MAZE_11566))
    }

    /**
     * The movement watcher in this area.
     *
     * Checks if the player is within striking range of any Tree (snake).
     * A nearby snake may strike, potentially poisoning the player.
     */
    /* Hit chance decreases with Agility (1 = 99% hit, 99 = 0% hit).
     * ╔═════════╦════════════════╗
     * ║ Agility ║ chanceToHit (%)║
     * ╠═════════╬════════════════╣
     * ║ 1       ║ 98.99          ║
     * ║ 25      ║ 74.75          ║
     * ║ 50      ║ 49.49          ║
     * ║ 75      ║ 24.24          ║
     * ║ 99      ║ 0              ║
     * ╚═════════╩════════════════╝
     */
    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        super.entityStep(entity, location, lastLocation)
        if (entity !is Player) return
        val player = entity
        val delay = "jade_vine_maze:strike_delay"
        if (getAttribute(player, delay, -1) > GameWorld.ticks) return
        for ((_, locations) in STRIKE_OBJECTS)
        {
            for (treeLocation in locations)
            {
                val dx = (player.location.x - treeLocation.x).toDouble()
                val dy = (player.location.y - treeLocation.y).toDouble()
                val distance = sqrt(dx * dx + dy * dy)

                if (distance <= STRIKE_RANGE)
                {
                    val agility = getDynLevel(player, Skills.AGILITY)
                    val chance = ((99 - agility).toDouble() / 99) * 100
                    val roll = Math.random() * 100
                    if (roll < chance) {
                        applyPoison(player, player, 6)
                        sendMessage(player, "The snake strikes and poisons you!")
                    } else {
                        sendMessage(player, "Your Agility enables you to evade the snake strike.")
                    }
                    setAttribute(player, delay, GameWorld.ticks + 3)
                    break
                }
            }
        }
    }

    override fun defineListeners() {

        /*
         * Handles enter to vine maze.
         */

        on(Scenery.VINE_27126, IntType.SCENERY, "climb-up") { player, node ->
            when (node.location.y) {
                2987 -> forceMove(player, player.location, Location.create(2885, 2987, 1), 0, 60, null, 3599)
                else -> forceMove(player, player.location, Location.create(2888, 3005, 1), 0, 60, null)

            }
            return@on true
        }

        on(Scenery.VINE_27151, IntType.SCENERY, "climb-up") { player, node ->
            when (node.location.y) {
                2982 -> forceMove(player, player.location, Location.create(2892, 2982, 3), 0, 60, null, 3599)
                2987 -> forceMove(
                    player,
                    player.location,
                    Location.create(2894, 2987, 2),
                    0,
                    60,
                    null,
                    3599
                ).also { player.moveStep() }
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

        on(Scenery.VINE_27128, IntType.SCENERY, "climb-up") { player, _ ->
            when (player.location.y) {
                2988 -> forceMove(player, player.location, Location.create(2895, 2988, 1), 0, 60, null, 819)
                2993 -> forceMove(player, player.location, Location.create(2898, 2994, 1), 0, 60, null, 819)
                else -> forceMove(player, player.location, Location.create(2891, 3000, 1), 0, 60, null, 819)
            }
            return@on true
        }

        on(Scenery.VINE_27129, IntType.SCENERY, "climb-down") { player, node ->
            val destination = CLIMB_DOWN_DESTINATION[node.location.y] ?: Location.create(2896, 2997, 1)
            forceMove(player, player.location, destination, 0, 60, null, Animations.JUMP_OVER_7268)
            return@on true
        }

        on(Scenery.VINE_27130, IntType.SCENERY, "climb-down") { player, node ->
            when (node.location.y) {
                3006 -> forceMove(
                    player,
                    player.location,
                    Location.create(2888, 3007, 0),
                    0,
                    30,
                    null,
                    Animations.JUMP_OVER_7268
                )

                3000 -> forceMove(
                    player,
                    player.location,
                    Location.create(2889, 3000, 0),
                    0,
                    30,
                    null,
                    Animations.JUMP_OVER_7268
                )

                2988 -> forceMove(
                    player,
                    player.location,
                    Location.create(2897, 2988, 0),
                    0,
                    30,
                    null,
                    Animations.JUMP_OVER_7268
                )

                else -> forceMove(
                    player,
                    player.location,
                    Location.create(2898, 2992, 0),
                    0,
                    30,
                    null,
                    Animations.JUMP_OVER_7268
                )
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
         */

        on(Scenery.VINE_27180, IntType.SCENERY, "swing-on") { player, _ ->
            lock(player, 3)
            playAudio(player, Sounds.SWING_ACROSS_2494)
            forceMove(
                player,
                player.location,
                Location.create(2901, 2985, 2),
                30,
                90,
                null,
                Animations.SWING_ACROSS_OBSTACLE_3130
            ) {
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
                AgilityHandler.walk(player, -1, player.location, destination, Animation.create(762), 0.0, null)
                AgilityHandler.fail(
                    player,
                    0,
                    Location.create(2913, 2979, 0),
                    Animation.create(Animations.FALL_BALANCE_764),
                    0,
                    "You lose your footing and fall into the water."
                )
                runTask(player, 3) {
                    player.animate(Animation.create(Animations.DROWN_765))
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
         * Handles travel through jade vine maze.
         */

        on(Scenery.HOLE_27186, IntType.SCENERY, "enter") { player, _ ->
            val randomTravelLocation = HOLE_LOCATIONS.random()
            player.teleport(randomTravelLocation)
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

        /*
         * Handles player interaction with Loose Soil in the "Back to my Roots" quest.
         */

        on(27194, IntType.SCENERY, "dig", "cut") { player, _ ->
            val opt = getUsedOption(player)
            if (opt == "dig") {
                if (!inInventory(player, Items.SPADE_952)) {
                    sendDialogue(player, "You need a spade to do that.")
                } else {
                    sendDialogue(player, "You dig at the soil and expose the Vine's root.")
                }
            }
            if (opt == "cut" && inInventory(player, Items.TROWEL_676)) {
                player.animate(Animation(Animations.GARDENING_TROWEL_2272))
                sendDialogue(player, "You carefully take a root cutting.")
                addItem(player, Items.ROOT_CUTTING_11770)
            }
            return@on true
        }

        /*
         * Handles getting potted root.
         */

        onUseWith(IntType.ITEM, ROOT_CUTTINGS, Items.PLANT_POT_5357) { player, used, with ->
            if (removeItem(player, used.id) && removeItem(player, with.id)) {
                player.dialogueInterpreter.sendItemMessage(Items.POTTED_ROOT_11776, "You carefully plant the cutting in the pot. Now to wait", "and see if it grows!")
                addItem(player, Items.POTTED_ROOT_11776, 1)
            }
            queueScript(player, 3, QueueStrength.SOFT) {
                lock(player, 3)
                val rand = RandomFunction.random(4060, 4064)
                if (rand == 4062) {
                    sendDialogue(player, "Your cutting seems to have taken successfully.")
                    setVarbit(player, 4062, 2, true)
                } else {
                    removeItem(player, Items.POTTED_ROOT_11776)
                    sendDialogueLines(player, "The cutting fails to take properly and wilts. You remove it from the", "plant pot.")
                    sendMessage(player, "The cutting fails to take properly and wilts. You remove it from the plant pot.")
                    addItem(player, Items.PLANT_POT_5357)
                    addItem(player, Items.WILTED_CUTTING_11775)
                }
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }

        /*
         * Handles creating sealed pot.
         * If lost re-take after speak to Garth.
         */

        onUseWith(IntType.ITEM, Items.POTTED_ROOT_11776, Items.EMPTY_POT_1931) { player, used, with ->
            if(!inInventory(player, Items.POT_LID_4440)) {
                sendMessage(player, "You don't have required item to do this.")
                return@onUseWith false
            }

            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                removeItem(player, Items.POT_LID_4440)
                addItemOrDrop(player, Items.SEALED_POT_11777)
                setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 50)
                setQuestStage(player, Quests.BACK_TO_MY_ROOTS, 7)
            }
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.VINE_27180) { _, _ ->
            return@setDest Location(2894, 2985, 2)
        }
    }
}