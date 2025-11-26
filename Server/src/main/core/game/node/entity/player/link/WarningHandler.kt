package core.game.node.entity.player.link

import content.global.travel.FairyRing
import content.region.kandarin.yanille.quest.itwatchtower.cutscene.EnclaveCutscene
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.event.FairyRingDialEvent
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.DoorActionHandler
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import shared.consts.*

object WarningHandler {
    private val ladderZones = listOf(
        ZoneBorders(1836, 5174, 1930, 5257) to Location.create(2042, 5245, 0),
        ZoneBorders(1977, 5176, 2066, 5265) to Location.create(2123, 5252, 0),
        ZoneBorders(2090, 5246, 2197, 5336) to Location.create(2358, 5215, 0)
    )

    /*
     * Handles Wilderness Ditch jump interaction.
     */

    @JvmStatic
    fun handleWildernessJump(player: Player) {
        val ditch = player.getAttribute<core.game.node.scenery.Scenery>("wildy_ditch") ?: return
        player.removeAttribute("wildy_ditch")

        val (start, end) = getDitchLocations(player.location, ditch.location, ditch.rotation)

        if (player.location.getDistance(ditch.location) < 3) {
            forceMove(player, start, end, 0, 60, null, Animations.HUMAN_JUMP_FENCE_6132)
            playAudio(player, Sounds.JUMP2_2462)
        } else {
            queueScript(player, 0, QueueStrength.NORMAL) {
                forceMove(player, start, end, 0, 60, null, Animations.HUMAN_JUMP_FENCE_6132)
                playAudio(player, Sounds.JUMP2_2462, 1)
                return@queueScript stopExecuting(player)
            }
        }
    }

    @JvmStatic
    fun getDitchLocations(playerLoc: Location, ditchLoc: Location, rotation: Int): Pair<Location, Location> {
        val (x, y) = playerLoc.x to playerLoc.y
        return if (rotation % 2 == 0) {
            if (y <= ditchLoc.y)
                Location.create(x, ditchLoc.y - 1, 0) to Location.create(x, ditchLoc.y + 2, 0)
            else
                Location.create(x, ditchLoc.y + 2, 0) to Location.create(x, ditchLoc.y - 1, 0)
        } else {
            if (x > ditchLoc.x)
                Location.create(ditchLoc.x + 2, y, 0) to Location.create(ditchLoc.x - 1, y, 0)
            else
                Location.create(ditchLoc.x - 1, y, 0) to Location.create(ditchLoc.x + 2, y, 0)
        }
    }

    /*
     * Handles Corporeal Beast warning interaction.
     */

    fun handleCorporalBeast(player: Player) {
        if (hasRequirement(player, Quests.SUMMERS_END) &&
            player.getAttribute("corp-beast-cave-delay", 0) <= GameWorld.ticks
        ) {
            val offset = if (player.location.x > 2970) -4 else 4
            player.properties.teleportLocation = player.location.transform(offset, 0, 0)
            player.setAttribute("corp-beast-cave-delay", GameWorld.ticks + 5)
        }
    }

    /*
     * Handles Mort Myre Gate warning interaction.
     */

    fun handleMortMyreGate(player: Player) {
        val gate = player.getAttribute<core.game.node.scenery.Scenery>("myre_gate") ?: return
        player.removeAttribute("myre_gate")
        DoorActionHandler.handleAutowalkDoor(player, gate.asScenery())
        sendMessage(player, "You walk into the gloomy atmosphere of Mort Myre.", 3)
    }

    /*
     * Handles Fairy Ring warning interaction.
     */

    fun handleFairyRing(player: Player) {
        player.dispatch(FairyRingDialEvent(FairyRing.AJQ))
        teleport(player, FairyRing.AJQ.tile!!, TeleportManager.TeleportType.FAIRY_RING)
        if (!player.savedData.globalData.hasTravelLog(2)) {
            player.savedData.globalData.setTravelLog(2)
        }
    }

    /*
     * Handles Lumbridge Swamp Cave warning interaction.
     */

    fun handleSwampCave(player: Player) {
        if (!player.savedData.globalData.hasTiedLumbridgeRope()) {
            sendDialogue(player, "There is a sheer drop below the hole. You will need a rope.")
        } else {
                climb(
                    player,
                    Animation(Animations.HUMAN_BURYING_BONES_827),
                    Location.create(3168, 9572, 0)
                )
        }
    }

    /*
     * Handles Shantay Pass warning interaction.
     */

    fun handleShantayPass(player: Player) {
        if (!removeItem(player, Item(Items.SHANTAY_PASS_1854, 1))) {
            sendNPCDialogue(
                player,
                NPCs.SHANTAY_GUARD_838,
                "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.",
                FaceAnim.NEUTRAL
            )
        } else {
            sendMessage(player, "You go through the gate.")
            val offset = if (player.location.y > 3116) -2 else 2
            forceMove(player, player.location, player.location.transform(0, offset, 0), 30, 120, null)
        }
    }

    /*
     * Handles Stronghold of Security ladder warning interaction.
     */

    fun handleStrongholdLadder(player: Player) {
        ladderZones.firstOrNull { (zone, _) -> inBorders(player, zone) }?.second?.let {
            climb(player, Animation(Animations.HUMAN_BURYING_BONES_827), it)
        }
    }

    /*
     * Handles Falador Mole Tunnel warning interaction.
     */

    fun handleMoleTunnel(player: Player) {
        teleport(player, Location.create(1752, 5237, 0))
        playAudio(player, Sounds.ROOF_COLLAPSE_1384)
        sendMessage(player, "You seem to have dropped down into a network of mole tunnels.")
        if (!hasDiaryTaskComplete(player, DiaryType.FALADOR, 0, 5)) {
            finishDiaryTask(player, DiaryType.FALADOR, 0, 5)
        }
    }

    /*
     * Handles Watchtower warning interaction.
     */

    fun handleWatchtower(player: Player) {
        if (isQuestComplete(player, Quests.WATCHTOWER)) {
            teleport(player, Location.create(2588, 9410, 0), TeleportManager.TeleportType.INSTANT)
            sendMessage(player, "You run past the guard while he's busy.")
        } else {
            EnclaveCutscene(player).start(true)
        }
    }

}