package content.region.karamja.plugin

import content.data.skill.SkillingTool
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.cache.def.impl.SceneryDefinition
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
 * Handles jade vine maze interactions.
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
         * The quest items.
         */
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
    /**
     * Checks if the player is within striking range of any Tree (snake).
     * A nearby snake may strike, potentially poisoning the player.
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
         * Handles climb up the vines (5 + entrance interaction).
         */

        on(Scenery.VINE_27126, IntType.SCENERY, "climb-up") { player, node ->
            when (node.location.y) {
                2987 -> forceMove(player, player.location, Location.create(2885, 2987, 1), 0, 30, null, 3599)
                2980 -> forceMove(player, player.location, Location.create(2921, 2981, 1), 0, 30, null, 3599)
                2978 -> forceMove(player, player.location, Location.create(2906, 2978, 1), 0, 30, null, 3599)
                2974 -> forceMove(player, player.location, Location.create(2926, 2974, 1), 0, 30, null, 3599)
                2971 -> forceMove(player, player.location, Location.create(2909, 2971, 1), 0, 30, null, 3599)
                else -> forceMove(player, player.location, Location.create(2888, 3005, 1), 0, 30, null, 3599)

            }
            return@on true
        }

        /*
         * Handles climb up the vines (4 interactions).
         */

        on(Scenery.VINE_27128, IntType.SCENERY, "climb-up") { player, node ->
            when (node.location.y) {
                2990 -> forceMove(player, player.location, Location.create(2903, 2991, 1), 0, 30, null, 3599)
                2988 -> forceMove(player, player.location, Location.create(2895, 2988, 1), 0, 30, null, 3599)
                2993 -> forceMove(player, player.location, Location.create(2898, 2994, 1), 0, 30, null, 3599)
                else -> forceMove(player, player.location, Location.create(2891, 3000, 1), 0, 30, null, 3599)
            }
            return@on true
        }

        /*
         * Handles climb down the vine (14 interactions).
         */

        on(Scenery.VINE_27129, IntType.SCENERY, "climb-down") { player, node ->
            when (node.location.y) {
                3004 -> forceMove(player, player.location, Location.create(2890, 3004, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2998 -> forceMove(player, player.location, Location.create(2896, 2997, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2990 -> forceMove(player, player.location, Location.create(2900, 2991, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2987 -> forceMove(player, player.location, Location.create(2894, 2988, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2985 -> forceMove(player, player.location, Location.create(2884, 2986, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2983 -> forceMove(player, player.location, Location.create(2884, 2984, 2), 0, 30, null, Animations.JUMP_OVER_7268)
                2982 -> forceMove(player, player.location, Location.create(2894, 2982, 2), 0, 30, null, Animations.JUMP_OVER_7268)
                2980 -> forceMove(player, player.location, Location.create(2894, 2979, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2978 -> if(node.location.x == 2907) {
                    forceMove(player, player.location, Location.create(2906, 2978, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                } else {
                    forceMove(player, player.location, Location.create(2918, 2978, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                }
                2975 -> forceMove(player, player.location, Location.create(2918, 2975, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2973 -> forceMove(player, player.location, Location.create(2909, 2973, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                2972 -> if(node.location.x == 2919) {
                    forceMove(player, player.location, Location.create(2918, 2972, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                } else {
                    forceMove(player, player.location, Location.create(2923, 2972, 1), 0, 30, null, Animations.JUMP_OVER_7268)
                }
            }
            return@on true
        }

        /*
         * Handles climb down the vine (10 interactions).
         */

        on(Scenery.VINE_27130, IntType.SCENERY, "climb-down") { player, node ->
            when (node.location.y) {
                3006 -> forceMove(player, player.location, Location.create(2888, 3007, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                3000 -> forceMove(player, player.location, Location.create(2889, 3000, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2993 -> forceMove(player, player.location, Location.create(2898, 2992, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2990 -> forceMove(player, player.location, Location.create(2903, 2989, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2988 -> forceMove(player, player.location, Location.create(2897, 2988, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2987 -> forceMove(player, player.location, Location.create(2887, 2987, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2980 -> forceMove(player, player.location, Location.create(2921, 2979, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2978 -> forceMove(player, player.location, Location.create(2904, 2978, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2974 -> forceMove(player, player.location, Location.create(2924, 2974, 0), 0, 30, null, Animations.JUMP_OVER_7268)
                2971 -> forceMove(player, player.location, Location.create(2907, 2971, 0), 0, 30, null, Animations.JUMP_OVER_7268)
            }
            return@on true
        }

        /*
         * Handles climb up the vines (4 interactions)
         */

        on(Scenery.VINE_27151, IntType.SCENERY, "climb-up") { player, node ->
            when (node.location.y) {
                2987 -> forceMove(player, player.location, Location.create(2894, 2987, 2), 0, 30, null, 3599) { player.moveStep() }
                2983 -> forceMove(player, player.location, Location.create(2884, 2983, 3), 0, 30, null, 3599) { player.moveStep() }
                2982 -> forceMove(player, player.location, Location.create(2892, 2982, 3), 0, 30, null, 3599) { player.moveStep() }
                2980 -> forceMove(player, player.location, Location.create(2894, 2980, 2), 0, 30, null, 3599) { player.moveStep() }
            }
            return@on true
        }

        /*
         * Handles climb up the vines (10 interactions).
         */

        on(Scenery.VINE_27152, IntType.SCENERY, "climb-up") { player, node ->
            when (node.location.y) {
                3004 -> forceMove(player, player.location, Location.create(2891, 3004, 2), 0, 30, null, 3599) { player.moveStep() }
                2998 -> forceMove(player, player.location, Location.create(2896, 2998, 2), 0, 30, null, 3599) { player.moveStep() }
                2990 -> forceMove(player, player.location, Location.create(2900, 2990, 2), 0, 30, null, 3599) { player.moveStep() }
                2985 -> forceMove(player, player.location, Location.create(2884, 2985, 2), 0, 30, null, 3599) { player.moveStep() }
                2978 -> if(node.location.x == 2917) {
                    forceMove(player, player.location, Location.create(2917, 2978, 2), 0, 30, null, 3599) { player.moveStep() }
                } else {
                    forceMove(player, player.location, Location.create(2907, 2978, 2), 0, 30, null, 3599) { player.moveStep() }
                }
                2975 -> forceMove(player, player.location, Location.create(2917, 2975, 2), 0, 30, null, 3599) { player.moveStep() }
                2973 -> forceMove(player, player.location, Location.create(2908, 2973, 2), 0, 30, null, 3599) { player.moveStep() }
                2972 -> if(node.location.x == 2919) {
                    forceMove(player, player.location, Location.create(2919, 2972, 2), 0, 30, null, 3599) { player.moveStep() }
                } else {
                    forceMove(player, player.location, Location.create(2922, 2972, 2), 0, 30, null, 3599) { player.moveStep() }
                }
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
                replaceScenery(node.asScenery(), Scenery.CUT_VINES_27174, 6)
            }
            return@on true
        }

        /*
         * Handles crawl through the vines.
         */

        on(Scenery.CUT_VINES_27174, IntType.SCENERY, "crawl-through") { player, node ->
            val dir = Direction.getDirection(player.location, node.location)
            val destination = player.location.transform(dir, 2)
            forceMove(player, player.location, destination, 0, 60, null, Animations.CRAWLING_2796)
            return@on true
        }

        /*
         * Handles squeeze through the vines.
         */

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
            forceMove(player, player.location, Location.create(2901, 2985, 2), 30, 90, null, Animations.SWING_ACROSS_OBSTACLE_3130) {
                sendMessage(player, "You skillfully swing across.")
            }
            return@on true
        }

        /*
         * Handles crossing the vine.
         */

        on(Scenery.VINE_27185, IntType.SCENERY, "cross") { player, node ->
            val xOffset = when (node.location.x) {
                2911 -> 5
                2915 -> -5
                else -> {
                    sendMessage(player, "I can't reach that!")
                    return@on false
                }
            }

            val destination = Location.create(node.location.x + xOffset, node.location.y, 2)
            val fail = AgilityHandler.hasFailed(player, 1, failChance = 0.3)
            lock(player, 8)

            if (!fail) {
                AgilityHandler.walk(player, -1, player.location, destination, Animation.create(155), 0.0, "You skillfully cross the vine.")
            } else {
                AgilityHandler.walk(player, -1, player.location, destination, Animation.create(155), 0.0, null)
                AgilityHandler.fail(player, 0, Location.create(2913, 2979, 0), Animation.create(Animations.FALL_BALANCE_764), 0, "You lose your footing and fall into the water.")
                runTask(player, 4) {
                    player.animate(Animation.create(3641))
                    forceMove(player, player.location, Location.create(2912, 2980, 0), 0, 60, null, 7267) {
                        sendMessage(player, "You scramble out of the water before the crocodiles take an interest.")
                    }
                }
            }

            return@on true
        }

        /*
         * Handles random travel around jade vine maze and outside.
         */

        on(Scenery.HOLE_27186, IntType.SCENERY, "enter") { player, _ ->
            val destinations = HOLE_LOCATIONS.filter { it != player.location }
            val destination = destinations.random()

            lock(player, 3)
            player.animate(Animation(3593))
            player.teleport(destination, 1)
            player.animate(Animation(3592), 1)
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
         * The player has up to 5 root cuttings available in total to use for next interaction.
         */

        on(intArrayOf(Scenery.LOOSE_SOIL_27058, Scenery.ROOTS_27059), IntType.SCENERY, "dig", "cut") { player, node ->
            val option = getUsedOption(player)
            val objectData = SceneryDefinition.forId(node.id)

            when (option) {
                "dig" -> {
                    if (!inInventory(player, Items.SPADE_952)) {
                        sendDialogue(player, "You need a spade to do that.")
                        return@on true
                    }

                    lock(player, 2)
                    setVarbit(player, objectData.varbitID, 1) // Replace scenery (+1) and enable cut option.
                    player.animate(Animation(Animations.DIG_SPADE_830))
                    sendDialogue(player, "You dig at the soil and expose the vine's root.")
                }

                "cut" -> {
                    if (!inInventory(player, Items.TROWEL_676)) {
                        sendDialogue(player, "You need a trowel to do that.")
                        return@on true
                    }

                    if(getVarbit(player, objectData.varbitID) == 1) {
                        lock(player, 2)
                        setVarbit(player, objectData.varbitID, 2) // Removes options.
                        player.animate(Animation(Animations.GARDENING_TROWEL_2272))
                        sendDialogue(player, "You carefully take a root cutting.")
                        addItem(player, Items.ROOT_CUTTING_11770)
                    }
                }
            }

            return@on true
        }

        /*
         * Handles planting root cuttings into a plant pot.
         * Player has a 1/5 chance for success, but the 5th attempt is guaranteed to succeed.
         * The number of failed attempts is tracked until success.
         */

        onUseWith(IntType.ITEM, ROOT_CUTTINGS, Items.PLANT_POT_5357) { player, used, with ->
            if (!inInventory(player, used.id) || !inInventory(player, with.id))
                return@onUseWith false

            val fails = "root_fail_count"
            val failCount = player.getAttribute(fails, 0)

            if (removeItem(player, used.id) && removeItem(player, with.id)) {
                addItem(player, Items.POTTED_ROOT_11776, 1)
                player.dialogueInterpreter.sendItemMessage(
                    Items.POTTED_ROOT_11776,
                    "You carefully plant the cutting in the pot. Now to wait",
                    "and see if it grows!"
                )

                queueScript(player, 6, QueueStrength.SOFT) {
                    val guaranteedSuccess = failCount >= 4
                    val randomSuccess = RandomFunction.random(1, 5) == 1
                    val success = guaranteedSuccess || randomSuccess

                    if (success) {
                        player.removeAttribute(fails)
                        sendDialogue(player, "Your cutting seems to have taken successfully.")
                    } else {
                        player.setAttribute(fails, failCount + 1)
                        removeItem(player, Items.POTTED_ROOT_11776)
                        sendDialogueLines(player, "The cutting fails to take properly and wilts. You remove it from the", "plant pot.")
                        sendMessage(player, "The cutting fails to take properly and wilts. You remove it from the plant pot.")
                        addItemOrDrop(player, Items.PLANT_POT_5357, 1)
                        addItemOrDrop(player, Items.WILTED_CUTTING_11775, 1)
                    }

                    return@queueScript stopExecuting(player)
                }
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