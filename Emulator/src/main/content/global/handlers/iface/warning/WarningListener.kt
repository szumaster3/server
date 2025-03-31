package content.global.handlers.iface.warning

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.hasRequirement
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs.consts.*

class WarningListener :
    InteractionListener,
    InterfaceListener {
    /*
     * Implemented warnings.
     */
    private val warningInterfaces =
        intArrayOf(
            Components.CWS_WARNING_30_650,
            Components.CWS_WARNING_5_563,
            Components.CWS_WARNING_9_560,
            Components.CWS_WARNING_17_570,
            Components.CWS_WARNING_3_568,
            Components.CWS_WARNING_20_580,
            Components.CWS_WARNING_23_564,
            Components.CWS_WARNING_10_565,
            Components.WILDERNESS_WARNING_382,
            Components.CWS_WARNING_4_579,
            Components.CWS_WARNING_1_574,
            Components.CWS_WARNING_24_581,
            Components.CWS_WARNING_26_627,
        )

    override fun defineInterfaceListeners() {
        /*
         * Handles trollheim warning.
         */

        onOpen(Components.CWS_WARNING_24_581) { player, component ->
            WarningManager.increment(player, component.id)
            return@onOpen true
        }

        /*
         * Handles each warning interface.
         */
        warningInterfaces.forEach { interfaceId ->
            on(interfaceId) { player, component, _, buttonID, _, _ ->
                when (buttonID) {
                    18 -> {
                        closeOverlay(player)
                        closeInterface(player)

                        val warnings =
                            mapOf(
                                Components.CWS_WARNING_24_581 to 3872,
                                Components.CWS_WARNING_26_627 to 4132,
                            )

                        if (component.id == Components.WILDERNESS_WARNING_382) {
                            player.interfaceManager.close()

                            player.getAttribute<core.game.node.scenery.Scenery>("wildy_ditch")
                                ?.let { handleDitch(player) }
                                ?: player.getAttribute<core.game.node.scenery.Scenery>("wildy_gate")
                                    ?.let { handleGate(player) }

                            WarningManager.increment(player, component.id)
                        } else {
                            warnings[component.id]?.let { varbit ->
                                if (getVarbit(player, varbit) >= 6) {
                                    WarningManager.toggle(player, component.id)
                                } else {
                                    WarningManager.increment(player, component.id)
                                }
                            } ?: WarningManager.increment(player, component.id)
                        }
                    }

                    20, 28, // 28 for Wilderness warning.
                        -> WarningManager.toggle(player, component.id)

                    17 -> {
                        closeOverlay(player)
                        closeInterface(player)
                        when (component.id) {
                            Components.CWS_WARNING_30_650 -> {
                                if (!hasRequirement(player, Quests.SUMMERS_END))
                                    return@on true
                                if (player.getAttribute("corp-beast-cave-delay", 0) <= GameWorld.ticks) {
                                    player.properties.teleportLocation =
                                        player.location.transform(4, 0, 0).also { closeInterface(player) }
                                    player.setAttribute("corp-beast-cave-delay", GameWorld.ticks + 5)
                                } else {
                                    closeInterface(player)
                                }
                                WarningManager.increment(player, component.id)
                            }

                            Components.CWS_WARNING_5_563 -> {
                                player.houseManager.toggleBuildingMode(player, true)
                                WarningManager.increment(player, component.id)
                            }

                            Components.CWS_WARNING_9_560 -> {
                                runTask(player, 2) {
                                    teleport(player, Location(2355, 9394, 0))
                                    WarningManager.increment(player, component.id)
                                }
                            }

                            Components.CWS_WARNING_17_570 -> {
                                if (!player.getSavedData().globalData.hasTiedLumbridgeRope()) {
                                    sendDialogue(player, "There is a sheer drop below the hole. You will need a rope.")
                                } else {
                                    climb(
                                        player,
                                        Animation(Animations.MULTI_BEND_OVER_827),
                                        Location.create(3168, 9572, 0),
                                    )
                                }
                                WarningManager.increment(player, component.id)
                            }

                            Components.CWS_WARNING_3_568 -> {
                                teleport(player, Location.create(1752, 5237, 0))
                                playAudio(player, Sounds.ROOF_COLLAPSE_1384)
                                WarningManager.increment(player, component.id)
                                sendMessage(player, "You seem to have dropped down into a network of mole tunnels.")
                                if (!hasDiaryTaskComplete(player, DiaryType.FALADOR, 0, 5)) {
                                    finishDiaryTask(player, DiaryType.FALADOR, 0, 5)
                                }
                            }

                            Components.CWS_WARNING_1_574 -> {
                                // Dagannoth kings
                                // if(player.viewport.region.id != 12181) {
                                teleport(player, Location.create(2899, 4449, 0))
                                // } else {
                                //     // Icy cavern
                                //     teleport(player, Location.create(3056, 9555, 0))
                                //     sendMessage(player, "You enter the icy cavern.")
                                // }
                                WarningManager.increment(player, component.id)
                            }

                            Components.CWS_WARNING_20_580 -> {
                                val targetScenery =
                                    if (player.location.x > 3443) {
                                        getScenery(3444, 3458, 0)!!
                                    } else {
                                        getScenery(3443, 3458, 0)!!
                                    }
                                DoorActionHandler.handleAutowalkDoor(player, targetScenery)
                                sendMessageWithDelay(player, "You walk into the gloomy atmosphere of Mort Myre.", 3)
                                WarningManager.increment(player, component.id)
                            }

                            Components.CWS_WARNING_4_579 -> {
                                val ladderLocation =
                                    when {
                                        inBorders(
                                            player,
                                            ZoneBorders(1836, 5174, 1930, 5257),
                                        ) -> Location.create(2042, 5245, 0)

                                        inBorders(
                                            player,
                                            ZoneBorders(1977, 5176, 2066, 5265),
                                        ) -> Location.create(2123, 5252, 0)

                                        inBorders(
                                            player,
                                            ZoneBorders(2090, 5246, 2197, 5336),
                                        ) -> Location.create(2358, 5215, 0)

                                        else -> null
                                    }
                                climb(player, Animation(Animations.MULTI_BEND_OVER_827), ladderLocation)
                                WarningManager.increment(player, component.id)
                            }

                            Components.CWS_WARNING_23_564 -> {
                                climb(player, Animation(Animations.USE_LADDER_828), Location(2668, 3427, 2))
                                val npc = getLocalNpcs(Location.create(2668, 3427, 2))
                                var dir = ""
                                for (n in npc) {
                                    if (n.id in NPCs.TOWER_ADVISOR_684..NPCs.TOWER_ADVISOR_687) {
                                        dir =
                                            when (n.id) {
                                                NPCs.TOWER_ADVISOR_684 -> "north"
                                                NPCs.TOWER_ADVISOR_685 -> "east"
                                                NPCs.TOWER_ADVISOR_686 -> "south"
                                                NPCs.TOWER_ADVISOR_687 -> "west"
                                                else -> dir
                                            }
                                        sendChat(n, "The $dir tower is occupied, get them!")
                                    }
                                }
                                WarningManager.increment(player, component.id)
                            }

                            Components.CWS_WARNING_10_565 -> {
                                if (!removeItem(player, Item(Items.SHANTAY_PASS_1854, 1))) {
                                    sendNPCDialogue(
                                        player,
                                        NPCs.SHANTAY_GUARD_838,
                                        "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.",
                                        FaceAnim.NEUTRAL,
                                    )
                                } else {
                                    sendMessage(player, "You go through the gate.")
                                    AgilityHandler.walk(
                                        player,
                                        0,
                                        player.location,
                                        player.location.transform(0, if (player.location.y > 3116) -2 else 2, 0),
                                        null,
                                        0.0,
                                        null,
                                    )
                                    WarningManager.increment(player, component.id)
                                }
                            }
                        }
                    }

                    else -> {
                        closeOverlay(player)
                        closeInterface(player)
                    }
                }
                return@on true
            }
        }
    }

    override fun defineListeners() {
        /*
         * Handles corp scenery entrance interaction.
         */

        on(intArrayOf(Scenery.PASSAGE_37929, Scenery.PASSAGE_38811), IntType.SCENERY, "go-through") { player, node ->
            if (player.location.x < node.location.x && !Warnings.CORPOREAL_BEAST_DANGEROUS.isDisabled) {
                WarningManager.openWarning(
                    player,
                    Warnings.CORPOREAL_BEAST_DANGEROUS,
                )
            } else {
                player.teleport(player.location.transform(if (player.location.x < node.location.x) 4 else -4, 0, 0))
            }
            return@on true
        }

        /*
         * Handles observatory stairs scenery interaction.
         */

        on(Scenery.STAIRS_25432, IntType.SCENERY, "climb-down") { player, _ ->
            if (getQuestStage(player, Quests.OBSERVATORY_QUEST) < 8) {
                sendPlayerDialogue(player, "I don't think I should try to go down there now.")
                return@on true
            }
            if (!Warnings.OBSERVATORY_STAIRS.isDisabled) {
                WarningManager.openWarning(
                    player,
                    Warnings.OBSERVATORY_STAIRS,
                )
            } else {
                runTask(player, 1) {
                    teleport(player, Location(2355, 9394, 0))
                }

            }
            return@on true
        }

        /*
         * Handles lumbridge swamp hole scenery interaction.
         */

        on(
            intArrayOf(Scenery.CLIMBING_ROPE_5946, Scenery.DARK_HOLE_5947),
            IntType.SCENERY,
            "climb-down",
            "climb",
        )
        { player, _ ->
            val option = getUsedOption(player)
            when (option) {
                "climb" -> teleport(player, Location.create(3169, 3173, 0))
                "climb-down" -> {
                    if (!player.getSavedData().globalData.hasTiedLumbridgeRope()) {
                        sendDialogue(player, "There is a sheer drop below the hole. You will need a rope.")
                        return@on true
                    }
                    if (!Warnings.LUMBRIDGE_SWAMP_CAVE_ROPE.isDisabled) {
                        WarningManager.openWarning(
                            player,
                            Warnings.LUMBRIDGE_SWAMP_CAVE_ROPE,
                        )
                        return@on true
                    }
                    climb(
                        player,
                        Animation(Animations.MULTI_BEND_OVER_827),
                        Location.create(3168, 9572, 0),
                    )
                }
            }
            return@on true
        }

        /*
         * Handles mort myre gate scenery interaction.
         */

        on(intArrayOf(Scenery.GATE_3506, Scenery.GATE_3507), IntType.SCENERY, "open")
        { player, node ->
            if (player.location.y == 3457) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "You skip gladly out of murky Mort Myre.")
                GlobalScope.launch {
                    findLocalNPC(player, NPCs.ULIZIUS_1054)?.sendChat("Oh my! You're still alive!", 2)
                }
            } else {
                if (player.questRepository.hasStarted(Quests.NATURE_SPIRIT)) {
                    if (!Warnings.MORT_MYRE.isDisabled) {
                        WarningManager.openWarning(
                            player,
                            Warnings.MORT_MYRE,
                        )
                    } else {
                        val targetScenery =
                            if (player.location.x > 3443) {
                                getScenery(3444, 3458, 0)!!
                            } else {
                                getScenery(3443, 3458, 0)!!
                            }
                        DoorActionHandler.handleAutowalkDoor(player, targetScenery)
                        sendMessageWithDelay(player, "You walk into the gloomy atmosphere of Mort Myre.", 3)
                    }
                } else {
                    sendNPCDialogue(
                        player,
                        NPCs.ULIZIUS_1054,
                        "I'm sorry, but I'm afraid it's too dangerous to let you through this gate right now.",
                    )
                }
            }
            return@on true
        }

        /*
         * Handles interaction with ranging guild tower ladder.
         */

        on(
            intArrayOf(Scenery.TOWER_LADDER_2511, Scenery.TOWER_LADDER_2512),
            IntType.SCENERY,
            "climb-up",
            "climb-down",
        )
        { player, _ ->
            val option = getUsedOption(player)
            when (option) {
                "climb-up" -> {
                    if (!Warnings.RANGING_GUILD.isDisabled) {
                        WarningManager.openWarning(player, Warnings.RANGING_GUILD)
                    } else {
                        climb(player, Animation(Animations.USE_LADDER_828), Location(2668, 3427, 2))
                        val npc = getLocalNpcs(Location.create(2668, 3427, 2))
                        var dir = ""
                        for (n in npc) {
                            if (n.id >= NPCs.TOWER_ADVISOR_684 && n.id <= NPCs.TOWER_ADVISOR_687) {
                                when (n.id) {
                                    NPCs.TOWER_ADVISOR_684 -> dir = "north"
                                    NPCs.TOWER_ADVISOR_685 -> dir = "east"
                                    NPCs.TOWER_ADVISOR_686 -> dir = "south"
                                    NPCs.TOWER_ADVISOR_687 -> dir = "west"
                                }
                                sendChat(n, "The $dir tower is occupied, get them!")
                            }
                        }
                    }
                }

                "climb-down" -> climb(player, null, Location.create(2668, 3427, 0))
            }
            return@on true
        }

        on(intArrayOf(Scenery.METAL_DOOR_29319, Scenery.METAL_DOOR_29320), IntType.SCENERY, "open")
        { player, node ->
            if (!Warnings.WILDERNESS_DITCH.isDisabled && player.location.y < 9918) {
                WarningManager.openWarning(
                    player,
                    Warnings.WILDERNESS_DITCH,
                )
                setAttribute(player, "wildy_gate", node)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        on(Scenery.WILDERNESS_DITCH_23271, IntType.SCENERY, "cross")
        { player, node ->
            if (player.location.getDistance(node.location) < 3) {
                handleDitch(player, node.asScenery())
                return@on true
            }
            queueScript(player, 0, QueueStrength.NORMAL) {
                handleDitch(player, node)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }

    private fun handleDitch(
        player: Player,
        node: Node,
    ) {
        player.faceLocation(node.location)
        val ditch = node as? core.game.node.scenery.Scenery ?: return
        player.setAttribute("wildy_ditch", ditch)

        if (!player.isArtificial) {
            val shouldWarn = shouldWarnCrossing(player, ditch)
            if (shouldWarn) {
                val warning = Warnings.WILDERNESS_DITCH
                if (!warning.isDisabled) {
                    WarningManager.openWarning(
                        player,
                        warning,
                    )
                    handleDitch(player)
                } else {
                    handleDitch(player)
                }
                return
            }
        }

        handleDitch(player)
    }

    private fun shouldWarnCrossing(
        player: Player,
        ditch: core.game.node.scenery.Scenery,
    ): Boolean =
        (ditch.rotation % 2 == 0 && player.location.y <= ditch.location.y) ||
                (ditch.rotation % 2 != 0 && player.location.x > ditch.location.x)

    private fun getDitchLocations(
        playerLocation: Location,
        ditchLocation: Location,
        rotation: Int,
    ): Pair<Location, Location> {
        val (x, y) = playerLocation.x to playerLocation.y
        return if (rotation % 2 == 0) {
            if (y <= ditchLocation.y) {
                Location.create(x, ditchLocation.y - 1, 0) to Location.create(x, ditchLocation.y + 2, 0)
            } else {
                Location.create(x, ditchLocation.y + 2, 0) to Location.create(x, ditchLocation.y - 1, 0)
            }
        } else {
            if (x > ditchLocation.x) {
                Location.create(ditchLocation.x + 2, y, 0) to Location.create(ditchLocation.x - 1, y, 0)
            } else {
                Location.create(ditchLocation.x - 1, y, 0) to Location.create(ditchLocation.x + 2, y, 0)
            }
        }
    }

    private fun handleDitch(player: Player) {
        val ditch = player.getAttribute<core.game.node.scenery.Scenery>("wildy_ditch") ?: return
        player.removeAttribute("wildy_ditch")
        val (start, end) = getDitchLocations(player.location, ditch.location, ditch.rotation)
        forceMove(player, start, end, 0, 60, null, Animations.JUMP_OVER_OBSTACLE_6132)
        playAudio(player, Sounds.JUMP2_2462, 30)
    }

    private fun handleGate(player: Player) {
        val gate = player.getAttribute<core.game.node.scenery.Scenery>("wildy_gate") ?: return
        player.removeAttribute("wildy_gate")
        DoorActionHandler.handleAutowalkDoor(player, gate)
    }
}
