package content.global.plugin.iface

import content.region.kandarin.yanille.quest.itwatchtower.cutscene.EnclaveCutscene
import core.api.*
import core.game.component.Component
import core.game.global.action.DoorActionHandler
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.DARK_PURPLE
import shared.consts.*
import core.game.dialogue.FaceAnim
import core.game.event.FairyRingDialEvent
import core.game.global.action.ClimbActionHandler.climb
import core.game.interaction.IntType
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.GameWorld

enum class Warnings(val varbit: Int, val component: Int, val buttonId: Int, val action: (Player) -> Unit) {
    DAGANNOTH_KINGS_LADDER(Vars.VARBIT_CWS_WARNING_1_3851, Components.CWS_WARNING_1_574, 50, { teleport(it, Location.create(2899, 4449, 0)) }),
    CONTACT_DUNGEON_LADDER(Vars.VARBIT_CWS_WARNING_2_3852, Components.CWS_WARNING_2_562, 56, {}),
    FALADOR_MOLE_LAIR(Vars.VARBIT_CWS_WARNING_3_3853, Components.CWS_WARNING_3_568, 53, { WarningListener.handleMoleTunnelWarning(it) }),
    STRONGHOLD_OF_SECURITY_LADDERS(Vars.VARBIT_CWS_WARNING_4_3854, Components.CWS_WARNING_4_579, 52, { WarningListener.handleStrongholdLadderWarning(it) }),
    PLAYER_OWNED_HOUSES(Vars.VARBIT_CWS_WARNING_5_3855, Components.CWS_WARNING_5_563, 55, { it.houseManager.toggleBuildingMode(it, true) }),
    DROPPED_ITEMS_IN_RANDOM_EVENTS(Vars.VARBIT_CWS_WARNING_6_3856, Components.CWS_WARNING_6_566, 54, {}),
    WILDERNESS_DITCH(Vars.VARBIT_WILDERNESS_WARNING_382_3857, Components.WILDERNESS_WARNING_382, 67, { WarningListener.handleWildernessWarnings(it) }),
    TROLLHEIM_WILDERNESS_ENTRANCE(Vars.VARBIT_CWS_WARNING_13_3858, Components.CWS_WARNING_13_572, 66, {}),
    OBSERVATORY_STAIRS(Vars.VARBIT_CWS_WARNING_9_3859, Components.CWS_WARNING_9_560, 62, { teleport(it, Location(2355, 9394, 0)) }),
    SHANTAY_PASS(Vars.VARBIT_CWS_WARNING_10_3860, Components.CWS_WARNING_10_565, 63, { WarningListener.handleShantayPassWarning(it) }),
    ICY_PATH_AREA(Vars.VARBIT_ICY_PATH_AREA_3861, -1, 57, {}), WATCHTOWER_SHAMAN_CAVE(Vars.VARBIT_CWS_WARNING_12_3862, Components.CWS_WARNING_12_573, 65, { WarningListener.handleWatchTowerWarning(it) }),
    LUMBRIDGE_SWAMP_CAVE_ROPE(Vars.VARBIT_CWS_WARNING_17_3863, Components.CWS_WARNING_17_570, 51, { WarningListener.handleSwampCaveWarning(it) }),
    HAM_TUNNEL_FROM_MILL(Vars.VARBIT_CWS_WARNING_19_3864, Components.CWS_WARNING_19_571, 58, {}),
    FAIRY_RING_TO_DORGESH_KAAN(Vars.VARBIT_CWS_WARNING_15_3865, Components.CWS_WARNING_15_578, 59, { WarningListener.handleFairyRingWarning(it) }),
    LUMBRIDGE_CELLAR(Vars.VARBIT_CWS_WARNING_14_3866, Components.CWS_WARNING_14_567, 60, {}),
    ELID_GENIE_CAVE(Vars.VARBIT_CWS_WARNING_18_3867, Components.CWS_WARNING_18_577, 64, {}),
    DORGESH_KAAN_TUNNEL_TO_KALPHITES(Vars.VARBIT_CWS_WARNING_21_3868, Components.CWS_WARNING_21_561, 69, {}),
    DORGESH_KAAN_CITY_EXIT(Vars.VARBIT_CWS_WARNING_16_3869, Components.CWS_WARNING_16_569, 68, {}),
    MORT_MYRE(Vars.VARBIT_CWS_WARNING_20_3870, Components.CWS_WARNING_20_580, 61, { WarningListener.handleMortMyreGateWarning(it) }),
    RANGING_GUILD(Vars.VARBIT_CWS_WARNING_23_3871, Components.CWS_WARNING_23_564, 70, { WarningListener.handleRaningGuildWarning(it) }),
    DEATH_PLATEAU(Vars.VARBIT_CWS_WARNING_24_3872, Components.CWS_WARNING_24_581, 71, {}),
    DUEL_ARENA(Vars.VARBIT_CWS_WARNING_26_4132, Components.CWS_WARNING_26_627, 73, {}),
    BOUNTY_AREA(Vars.VARBIT_BOUNTY_WARNING_4199, Components.BOUNTY_WARNING_657, 74, {}),
    CHAOS_TUNNELS_EAST(Vars.VARBIT_CHAOS_TUNNELS_EAST_4307, Components.CWS_WARNING_27_676, 75, {}),
    CHAOS_TUNNELS_CENTRAL(Vars.VARBIT_CHAOS_TUNNELS_CENTRAL_4308, Components.CWS_WARNING_28_677, 76, {}),
    CHAOS_TUNNELS_WEST(Vars.VARBIT_CHAOS_TUNNELS_WEST_4309, Components.CWS_WARNING_29_678, 77, {}),
    CORPOREAL_BEAST_DANGEROUS(Vars.VARBIT_CORPOREAL_BEAST_DANGEROUS_5366, Components.CWS_WARNING_30_650, 78, { WarningListener.handleCorporalBeastWarning(it) }),
    CLAN_WARS_FFA_SAFETY(Vars.VARBIT_CLAN_WARS_FFA_SAFETY_5294, -1, 79, {}),
    CLAN_WARS_FFA_DANGEROUS(Vars.VARBIT_CLAN_WARS_FFA_DANGEROUS_5295, Components.CWS_WARNING_8_576, 80, {}), PVP_WORLDS(Vars.VARBIT_PVP_WORLDS_5296, -1, 81, {});

    companion object {
        val values = enumValues<Warnings>()
        val button = values.associateBy { it.varbit }
    }
}

class WarningListener : InteractionListener, InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(Components.CWS_WARNING_24_581) { player, component ->
            increment(player, component.id)
            return@onOpen true
        }

        Warnings.values.forEach { warning ->
            if (warning.component != -1) {
                on(warning.component) { player, _, _, buttonId, _, _ ->
                    handleInterfaceAction(player, warning, buttonId)
                    return@on true
                }
            }
        }
    }

    private fun handleInterfaceAction(player: Player, warning: Warnings, buttonId: Int) {
        closeOverlay(player)
        closeInterface(player)
        when (buttonId) {
            17 -> handleConfirm(player, warning)
            18 -> handle(player, warning.component)
            20, 28 -> toggle(player, warning.component)
            else -> return
        }
    }

    private fun handleConfirm(player: Player, warning: Warnings) {
        warning.action(player)
        increment(player, warning.component)
    }

    private val cwsWarnings = mapOf(
        Components.CWS_WARNING_24_581 to Vars.VARBIT_CWS_WARNING_24_3872,
        Components.CWS_WARNING_26_627 to Vars.VARBIT_CWS_WARNING_26_4132
    )

    fun handle(player: Player, component: Int) {
        when (component) {
            Components.WILDERNESS_WARNING_382 -> handleWilderness(player)
            else -> handleCws(player, component)
        }
    }

    private fun handleWilderness(player: Player) {
        player.interfaceManager.close()
        player.getAttribute<core.game.node.scenery.Scenery>("wildy_ditch")
            ?.let { handleWildernessWarnings(player) }
        increment(player, Components.WILDERNESS_WARNING_382)
    }

    private fun handleCws(player: Player, component: Int) {
        val varbit = cwsWarnings[component]
        if (varbit != null && getVarbit(player, varbit) >= 6) {
            toggle(player, component)
        } else {
            increment(player, component)
        }
    }

    private fun handleScenery(player: Player, warning: Warnings, node: core.game.node.scenery.Scenery) {
        when (warning) {
            Warnings.MORT_MYRE -> player.setAttribute("myre_gate", node)
            Warnings.WILDERNESS_DITCH -> player.setAttribute("wildy_gate", node)
            else -> {}
        }
        if (!isDisabled(player, warning)) openWarning(player, warning)
        else warning.action(player)
    }

    override fun defineListeners() {
        sceneryWarnings.forEach { (sceneryID, warning) ->
            on(sceneryID, IntType.SCENERY, "go-through","climb-down","climb","open","climb-up","cross") { player, node ->
                if ((sceneryID == 3506 || sceneryID == 3507) && player.location.y < 3458) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    return@on true
                }

                handleScenery(player, warning, node.asScenery())
                return@on true
            }
        }
    }

    companion object {
        private val sceneryWarnings = listOf(
            Scenery.PASSAGE_37929 to Warnings.CORPOREAL_BEAST_DANGEROUS,
            Scenery.PASSAGE_38811 to Warnings.CORPOREAL_BEAST_DANGEROUS,
            Scenery.STAIRS_25432 to Warnings.OBSERVATORY_STAIRS,
            Scenery.CLIMBING_ROPE_5946 to Warnings.LUMBRIDGE_SWAMP_CAVE_ROPE,
            Scenery.DARK_HOLE_5947 to Warnings.LUMBRIDGE_SWAMP_CAVE_ROPE,
            Scenery.GATE_3506 to Warnings.MORT_MYRE,
            Scenery.GATE_3507 to Warnings.MORT_MYRE,
            Scenery.TOWER_LADDER_2511 to Warnings.RANGING_GUILD,
            Scenery.WILDERNESS_DITCH_23271 to Warnings.WILDERNESS_DITCH
        )

        private val ladderZones = listOf(
            ZoneBorders(1836, 5174, 1930, 5257) to Location.create(2042, 5245, 0),
            ZoneBorders(1977, 5176, 2066, 5265) to Location.create(2123, 5252, 0),
            ZoneBorders(2090, 5246, 2197, 5336) to Location.create(2358, 5215, 0)
        )

        @JvmStatic
        fun openWarning(player: Player, warning: Warnings) {
            if (isDisabled(player, warning)) return
            player.interfaceManager.open(Component(warning.component))
            increment(player, warning.varbit)
        }

        @JvmStatic
        fun isDisabled(player: Player, warning: Warnings): Boolean {
            return getVarbit(player, warning.varbit) == 7
        }

        fun toggle(player: Player, componentId: Int) {
            val warning = Warnings.values().find { it.component == componentId } ?: return
            toggleWarning(player, warning)
        }

        fun toggleWarning(player: Player, warning: Warnings) {
            val current = getVarbit(player, warning.varbit)
            if (current == 6) {
                setVarbit(player, warning.varbit, 7, true)
                sendMessage(
                    player, "You have toggled this warning screen off. You will no longer see this warning screen."
                )
            } else {
                setVarbit(player, warning.varbit, 6, true)
                sendMessage(player, "You have toggled this warning screen on. You will see this interface again.")
            }
        }

        fun increment(player: Player, varbitId: Int) {
            Warnings.values().find { it.varbit == varbitId }?.let { warning ->
                val currentStatus = getVarbit(player, warning.varbit)
                if (currentStatus < 6) {
                    val newStatus = (currentStatus + 1).coerceAtMost(6)
                    setVarbit(player, warning.varbit, newStatus, true)
                    player.debug("Component varbit: [$DARK_PURPLE$warning</col>] increased to: [$DARK_PURPLE$newStatus</col>].")
                    if (newStatus == 6) {
                        enableToggleButton(player, warning)
                        sendMessage(player, "You can now disable this warning in the settings.")
                    }
                } else if (currentStatus == 6) {
                    enableToggleButton(player, warning)
                }
            }
        }

        private fun enableToggleButton(player: Player, warning: Warnings) {
            val toggleButton = when (warning.component) {
                Components.WILDERNESS_WARNING_382 -> 26
                Components.CWS_WARNING_24_581 -> 19
                else -> 21
            }
            sendInterfaceConfig(player, warning.component, toggleButton, false)
        }

        fun handleWildernessWarnings(player: Player) {
            val ditch = player.getAttribute<core.game.node.scenery.Scenery>("wildy_ditch")
            if (ditch != null) {
                player.removeAttribute("wildy_ditch")

                val (start, end) = getDitchLocations(player.location, ditch.location, ditch.rotation)
                if (player.location.getDistance(ditch.location) < 3) {
                    forceMove(player, start, end, 0, 60, null, Animations.JUMP_OVER_OBSTACLE_6132)
                    playAudio(player, Sounds.JUMP2_2462, 3)
                } else {
                    queueScript(player, 0, QueueStrength.NORMAL) {
                        forceMove(player, start, end, 0, 60, null, Animations.JUMP_OVER_OBSTACLE_6132)
                        playAudio(player, Sounds.JUMP2_2462, 3)
                        return@queueScript stopExecuting(player)
                    }
                }
                return
            }
        }

        private fun getDitchLocations(playerLocation: Location, ditchLocation: Location, rotation: Int): Pair<Location, Location> {
            val (x, y) = playerLocation.x to playerLocation.y
            return if (rotation % 2 == 0) {
                if (y <= ditchLocation.y) Location.create(x, ditchLocation.y - 1, 0) to Location.create(x, ditchLocation.y + 2, 0) else Location.create(x, ditchLocation.y + 2, 0) to Location.create(x, ditchLocation.y - 1, 0)
            } else {
                if (x > ditchLocation.x) Location.create(ditchLocation.x + 2, y, 0) to Location.create(ditchLocation.x - 1, y, 0) else Location.create(ditchLocation.x - 1, y, 0) to Location.create(ditchLocation.x + 2, y, 0)
            }
        }

        fun handleCorporalBeastWarning(player: Player) {
            if (hasRequirement(player, Quests.SUMMERS_END) && player.getAttribute("corp-beast-cave-delay", 0) <= GameWorld.ticks) {
                player.properties.teleportLocation = player.location.transform(4, 0, 0)
                player.setAttribute("corp-beast-cave-delay", GameWorld.ticks + 5)
            }
        }

        fun handleRaningGuildWarning(player: Player) {
            climb(player, core.game.world.update.flag.context.Animation(Animations.USE_LADDER_828), Location.create(2668, 3427, 1))
        }

        fun handleMortMyreGateWarning(player: Player) {
            val gate = player.getAttribute<core.game.node.scenery.Scenery>("myre_gate")
            if (gate != null) {
                player.removeAttribute("myre_gate")
            DoorActionHandler.handleAutowalkDoor(player, gate.asScenery())
            sendMessage(player, "You walk into the gloomy atmosphere of Mort Myre.", 3)
            }
        }

        fun handleFairyRingWarning(player: Player) {
            player.dispatch(FairyRingDialEvent(FairyRing.AJQ))
            teleport(player, FairyRing.AJQ.tile!!, TeleportManager.TeleportType.FAIRY_RING)
            if (!player.savedData.globalData.hasTravelLog(2)) player.savedData.globalData.setTravelLog(2)
        }

        fun handleSwampCaveWarning(player: Player) {
            if (!player.getSavedData().globalData.hasTiedLumbridgeRope()) {
                sendDialogue(player, "There is a sheer drop below the hole. You will need a rope.")
            } else {
                climb(
                    player,
                    core.game.world.update.flag.context.Animation(Animations.MULTI_BEND_OVER_827),
                    Location.create(3168, 9572, 0)
                )
            }
        }

        fun handleShantayPassWarning(player: Player) {
            if (!removeItem(player, Item(Items.SHANTAY_PASS_1854, 1))) {
                sendNPCDialogue(player, NPCs.SHANTAY_GUARD_838, "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.", FaceAnim.NEUTRAL)
            } else {
                sendMessage(player, "You go through the gate.")
                forceMove(player, player.location, player.location.transform(0, if (player.location.y > 3116) -2 else 2, 0), 30, 120, null)
            }
        }

        @JvmStatic
        fun handleStrongholdLadderWarning(player: Player) {
            ladderZones.firstOrNull { (zone, _) -> inBorders(player, zone) }?.second?.let {
                climb(player, core.game.world.update.flag.context.Animation(Animations.MULTI_BEND_OVER_827), it)
            }
        }

        @JvmStatic
        fun handleMoleTunnelWarning(player: Player) {
            teleport(player, Location.create(1752, 5237, 0))
            playAudio(player, Sounds.ROOF_COLLAPSE_1384)
            sendMessage(player, "You seem to have dropped down into a network of mole tunnels.")
            if (!hasDiaryTaskComplete(player, DiaryType.FALADOR, 0, 5)) {
                finishDiaryTask(player, DiaryType.FALADOR, 0, 5)
            }
        }

        @JvmStatic
        fun handleWatchTowerWarning(player: Player) {
            if (isQuestComplete(player, Quests.WATCHTOWER) || getQuestStage(player, Quests.WATCHTOWER) >= 60) {
                teleport(player, Location.create(2588, 9410, 0), TeleportManager.TeleportType.INSTANT)
            } else {
                EnclaveCutscene(player).start(true)
            }
            sendMessage(player, "You run past the guard while he's busy.")
        }
    }
}

