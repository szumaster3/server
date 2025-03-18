package content.region.fremennik.quest.horror.handlers

import content.data.GameAttributes
import content.region.fremennik.quest.horror.JossikLighthouseDialogue
import content.region.fremennik.quest.horror.handlers.bookcase.LighthouseBookcase
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.ForceMovement
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class HorrorFromTheDeepListener : InteractionListener {
    private val brokenBridge = intArrayOf(Scenery.BROKEN_BRIDGE_4615, Scenery.BROKEN_BRIDGE_4616)
    private val strangeWalls = intArrayOf(Scenery.STRANGE_WALL_4544, Scenery.STRANGE_WALL_4543)
    private val strangeDoors = intArrayOf(Scenery.STRANGE_WALL_4545, Scenery.STRANGE_WALL_4546)
    private val requiredItems =
        intArrayOf(
            Items.BRONZE_ARROW_882,
            Items.BRONZE_SWORD_1277,
            Items.AIR_RUNE_556,
            Items.FIRE_RUNE_554,
            Items.EARTH_RUNE_557,
            Items.WATER_RUNE_555,
        )

    override fun defineListeners() {
        /*
         * Handles lighthouse bookcase.
         */

        on(Scenery.BOOKCASE_4617, IntType.SCENERY, "search") { player, node ->
            openDialogue(player, LighthouseBookcase(), node.asScenery())
            return@on true
        }

        /*
         * Handles interaction talk with Jossik.
         */

        on(NPCs.JOSSIK_1335, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, JossikLighthouseDialogue())
            return@on true
        }

        /*
         * Handles ladders down to dagannoth lair.
         */

        on(Scenery.IRON_LADDER_4383, IntType.SCENERY, "climb") { player, _ ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            val teleportLocation =
                when {
                    isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> Location(2519, 9994, 1)
                    questStage >= 40 -> Location(2519, 4618, 1)
                    else -> null
                }

            if (teleportLocation != null) {
                animate(player, Animations.MULTI_BEND_OVER_827)
                queueScript(player, 1, QueueStrength.SOFT) {
                    teleport(player, teleportLocation)
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendPlayerDialogue(player, "I have no reason to go down there.", FaceAnim.HALF_THINKING)
            }
            return@on true
        }

        /*
         * Handles the interaction with lighthouse door.
         * The player, after crossing the doors, teleports to the second lighthouse.
         */

        on(Scenery.DOORWAY_4577, IntType.SCENERY, "walk-through") { player, node ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)

            when {
                isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }

                questStage >= 20 -> {
                    val insideLighthouse = inBorders(player, 2508, 3634, 2510, 3635)
                    val teleportLocation = if (insideLighthouse) location(2445, 4596, 0) else location(2509, 3635, 0)

                    submitIndividualPulse(
                        player,
                        object : Pulse(2) {
                            override fun pulse(): Boolean {
                                if (insideLighthouse) {
                                    sendMessage(player, "You unlock the Lighthouse front door.")
                                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 30)
                                }
                                runTask(player, 3) { teleport(player, teleportLocation) }
                                return true
                            }
                        },
                    )
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }

                questStage < 20 -> {
                    sendNPCDialogue(
                        player,
                        NPCs.LARRISSA_1336,
                        "Please adventurer... We are both curious as to what has happened in that lighthouse, but you need to fix the bridge for me!",
                    )
                }

                else -> {
                    sendDialogue(player, "You can't see any way to open the door.")
                }
            }
            return@on true
        }

        /*
         * Handles first fix for the light mechanism.
         */

        onUseWith(IntType.SCENERY, Items.SWAMP_TAR_1939, Scenery.LIGHTING_MECHANISM_4588) { player, _, _ ->
            if (removeItem(player, Items.SWAMP_TAR_1939)) {
                sendMessage(player, "You use the swamp tar to make the torch flammable again.")
                setAttribute(player, GameAttributes.QUEST_HFTD_LIGHTHOUSE_MECHANISM, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles second fix for the light mechanism.
         */

        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.LIGHTING_MECHANISM_4588) { player, _, _ ->
            if (getAttribute(player, GameAttributes.QUEST_HFTD_LIGHTHOUSE_MECHANISM, 0) < 1) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                sendMessage(player, "You light the torch with your tinderbox.")
                player.incrementAttribute(GameAttributes.QUEST_HFTD_LIGHTHOUSE_MECHANISM)
            }
            return@onUseWith true
        }

        /*
         * Handles last fix for the light mechanism.
         */

        onUseWith(
            IntType.SCENERY,
            Items.MOLTEN_GLASS_1775,
            Scenery.LIGHTING_MECHANISM_4588,
        ) { player, item, mechanism ->
            if (getAttribute(player, GameAttributes.QUEST_HFTD_LIGHTHOUSE_MECHANISM, 0) < 2) return@onUseWith false
            if (removeItem(player, item.asItem())) {
                replaceScenery(mechanism.asScenery(), Scenery.LIGHTING_MECHANISM_4587, 80)
                setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 40)
                sendMessage(player, "You use the molten glass to repair the lens.")
                sendMessage(player, "You have managed to repair the lighthouse torch!")
            }
            return@onUseWith true
        }

        /*
         * Handles study the strange wall.
         */

        on(strangeWalls, IntType.SCENERY, "study") { player, _ ->
            when (player.location.y) {
                4626 -> {
                    if (getQuestStage(player, Quests.HORROR_FROM_THE_DEEP) >= 50) {
                        openInterface(player, Components.HORROR_METALDOOR_142)
                    } else {
                        openInterface(player, Components.HORROR_METALDOOR_142)
                        setAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_DISCOVER, true)
                    }
                }

                10002 -> openInterface(player, Components.HORROR_METALDOOR_142)
                4627, 10003 -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        /*
         * Handles using the required items on the strange wall.
         */

        onUseWith(IntType.SCENERY, requiredItems, *strangeWalls) { player, used, _ ->
            openDialogue(player, StrangeWallDialogue(items = used.id))
            return@onUseWith true
        }

        /*
         * Handles opening the strange door.
         */

        on(strangeDoors, IntType.SCENERY, "open") { player, node ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            if (questStage >= 50) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                playAudio(player, Sounds.STRANGEDOOR_OPEN_1626)
                playAudio(player, Sounds.STRANGEDOOR_CLOSE_1625, 2)
                if (!isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 55)
                    removeAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_DISCOVER)
                }
                return@on true
            }
            when (player.location.y) {
                4626, 10002 -> sendMessage(player, "You cannot see any way to move this part of the wall....")
                4627, 10003 -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        /*
         * Handles fix the bridge (both sides).
         */

        onUseWith(IntType.SCENERY, Items.PLANK_960, *brokenBridge) { player, _, bridge ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            val bridgeUnlocked = getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0)

            val isFirstHalf = bridge.id == Scenery.BROKEN_BRIDGE_4615
            val requiredStage = if (isFirstHalf) 5..10 else 10..20
            val newUnlockValue = if (isFirstHalf) 1 else 2
            val newQuestStage = if (isFirstHalf) 10 else 20
            val message =
                if (isFirstHalf) "You create half a makeshift walkway out of the plank." else "You have now made a makeshift walkway over the bridge."

            when {
                questStage !in requiredStage -> {
                    sendDialogue(player, "That won't help fix the bridge.")
                    return@onUseWith false
                }

                isFirstHalf && bridgeUnlocked == 1 -> {
                    sendDialogue(player, "You have already fixed this half of the bridge.")
                    return@onUseWith false
                }

                !isFirstHalf && bridgeUnlocked < 1 -> {
                    sendMessage(player, "I might be able to make it to the other side.")
                    return@onUseWith false
                }

                !inInventory(player, Items.HAMMER_2347, 1) -> {
                    sendDialogue(player, "You need a hammer to force the nails in with.")
                    return@onUseWith false
                }

                amountInInventory(player, Items.STEEL_NAILS_1539) < 30 -> {
                    sendDialogue(player, "You need 30 steel nails to attach the plank with.")
                    return@onUseWith false
                }
            }

            if (removeItem(player, Item(Items.STEEL_NAILS_1539, 30)) && removeItem(player, Item(Items.PLANK_960, 1))) {
                lock(player, 1)
                animate(player, Animations.SMITH_HAMMER_898)
                queueScript(player, 3, QueueStrength.SOFT) {
                    animate(player, Animations.SMITH_HAMMER_898)
                    setAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, newUnlockValue)
                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, newQuestStage)
                    sendDialogue(player, message)
                    sendMessage(player, message)
                    return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles crossing the bridge.
         */

        on(brokenBridge, IntType.SCENERY, "Cross") { player, bridge ->
            val bridgeUnlock = getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0)
            val questComplete = isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)
            val fromWest = bridge.id == Scenery.BROKEN_BRIDGE_4615

            val baseLocationX = if (fromWest) 2595 else 2598
            val locations = (0..3).map { Location(baseLocationX + it * (if (fromWest) 1 else -1), 3608, 0) }

            val animations =
                if (fromWest) {
                    listOf(Animation(753), Animation(756), Animation(757), Animation(759))
                } else {
                    listOf(Animation(752), Animation(754), Animation(755), Animation(758))
                }

            val direction = if (fromWest) Direction.EAST else Direction.WEST

            when {
                // Crossing the bridge before fix.
                bridgeUnlock == 1 && fromWest -> {
                    lock(player, 6)
                    submitIndividualPulse(
                        player,
                        object : Pulse() {
                            var count = 0

                            override fun pulse(): Boolean {
                                when (count++) {
                                    0 -> forceMove(player, locations[0], locations[2], 0, 60, direction)
                                    2 -> animate(player, Animations.JUMP_BRIDGE_769)
                                    3 -> teleport(player, locations[3])
                                    5 -> forceWalk(player, locations[3], "").also { return true }
                                }
                                return false
                            }
                        },
                    )
                }

                // Crossing the bridge after fix both sides.
                bridgeUnlock == 2 || questComplete -> {
                    lock(player, 4)
                    submitIndividualPulse(
                        player,
                        object : Pulse() {
                            var count = 0

                            override fun pulse(): Boolean {
                                if (count in 0..2) {
                                    ForceMovement.run(
                                        player,
                                        locations[count],
                                        locations[count + 1],
                                        animations[count],
                                        animations.getOrElse(count + 1) { animations.last() },
                                        direction,
                                    )
                                    count++
                                    return false
                                }
                                return true
                            }
                        },
                    )
                }

                else -> sendMessage(player, "I might be able to make it to the other side.")
            }
            return@on true
        }
    }
}
