package content.global.ame.eviltwin

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.ui.restoreTabs
import core.api.ui.setMinimapState
import core.api.utils.PlayerCamera
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Music

object EvilTwinUtils {
    val rewards =
        arrayOf(
            Item(Items.UNCUT_DIAMOND_1618, 2),
            Item(Items.UNCUT_RUBY_1620, 3),
            Item(Items.UNCUT_EMERALD_1622, 3),
            Item(Items.UNCUT_SAPPHIRE_1624, 4),
        )

    val region: DynamicRegion = DynamicRegion.create(7504)

    var success = false
    var tries = 3
    const val offsetX = 0
    const val offsetY = 0

    var mollyNPC: NPC? = null
    var craneNPC: NPC? = null
    var currentCrane: Scenery? = null

    fun start(player: Player): Boolean {
        region.add(player)
        region.setMusicId(Music.HEAD_TO_HEAD_612)
        currentCrane = Scenery(14976, region.baseLocation.transform(14, 12, 0), 10, 0)

        val color: EvilTwinColors = RandomFunction.getRandomElement(EvilTwinColors.values())
        val model = RandomFunction.random(5)
        val hash = color.ordinal or (model shl 16)

        val npcId = getMollyId(hash)
        setAttribute(player, GameAttributes.RE_TWIN_START, hash)
        mollyNPC = NPC.create(npcId, Location.getRandomLocation(player.location, 1, true)).apply { init() }

        sendChat(mollyNPC!!, "I need your help, ${player.username}.")
        mollyNPC!!.faceTemporary(player, 3)

        setAttribute(player, RandomEvent.save(), player.location)

        queueScript(player, 3, QueueStrength.SOFT) { stage ->
            when (stage) {
                0 -> {
                    setMinimapState(player, 2)
                    mollyNPC!!.properties.teleportLocation = region.baseLocation.transform(4, 15, 0)
                    player.properties.teleportLocation = region.baseLocation.transform(4, 16, 0)
                    registerLogoutListener(player, RandomEvent.logout()) { p ->
                        p.location = getAttribute(p, RandomEvent.save(), player.location)
                    }
                    spawnSuspects(hash)
                    showNPCs(true).let {
                        this.mollyNPC!!.locks.lockMovement(10000)
                    }
                    return@queueScript delayScript(player, 1)
                }
                1 -> {
                    openDialogue(player, MollyDialogue(3))
                    return@queueScript stopExecuting(player)
                }
            }
            return@queueScript stopExecuting(player)
        }
        return true
    }

    fun cleanup(player: Player) {
        craneNPC = null
        success = false
        mollyNPC?.clear()
        PlayerCamera(player).reset()
        restoreTabs(player)

        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        setMinimapState(player, 0)
        removeAttributes(
            player,
            GameAttributes.RE_TWIN_START,
            RandomEvent.save(),
            GameAttributes.RE_TWIN_OBJ_LOC_X,
            GameAttributes.RE_TWIN_OBJ_LOC_Y,
        )
        clearLogoutListener(player, RandomEvent.logout())
    }

    fun attempts(player: Player) {
        if (--tries < 1) {
            lock(player, 20)
            closeTabInterface(player)
            openDialogue(player, MollyDialogue(1))
        }
        sendString(player, "Tries: $tries", Components.CRANE_CONTROL_240, 27)
    }

    @JvmStatic
    fun locationUpdate(
        player: Player,
        entity: Entity,
        last: Location?,
    ) {
        when {
            entity == craneNPC && entity.walkingQueue.queue.size > 1 && player.interfaceManager.singleTab != null -> {
                updateCraneCamera(player, entity.location)
            }
            entity == player -> {
                toggleMollyVisibility(entity, player)
            }
        }
    }

    private fun updateCraneCamera(
        player: Player,
        location: Location,
    ) {
        PacketRepository.send(
            CameraViewPacket::class.java,
            CameraContext(player, CameraContext.CameraType.POSITION, location.x + 2, location.y + 3, 520, 1, 5),
        )
        PacketRepository.send(
            CameraViewPacket::class.java,
            CameraContext(player, CameraContext.CameraType.ROTATION, location.x - 3, location.y - 3, 420, 1, 5),
        )
    }

    private fun toggleMollyVisibility(
        entity: Entity,
        player: Player,
    ) {
        val distance = entity.location.localX - player.location.localX
        showNPCs(distance <= 9)
    }

    @JvmStatic
    fun updateCraneCam(
        player: Player,
        x: Int,
        y: Int,
    ) {
        if (player.interfaceManager.singleTab != null) {
            var loc = region.baseLocation.transform(14, 20, 0)
            sendCameraView(player, loc, 520)

            loc = region.baseLocation.transform(x, 4 + y - (if (x < 14 || x > 14) (y / 4) else 0), 0)
            sendCameraView(player, loc, 420)
        }
        setAttribute(player, GameAttributes.RE_TWIN_OBJ_LOC_X, x)
        setAttribute(player, GameAttributes.RE_TWIN_OBJ_LOC_Y, y)
    }

    private fun sendCameraView(
        player: Player,
        location: Location,
        zoom: Int,
    ) {
        PacketRepository.send(
            CameraViewPacket::class.java,
            CameraContext(player, CameraContext.CameraType.POSITION, location.x, location.y, zoom, 1, 100),
        )
    }

    @JvmStatic
    fun moveCrane(
        player: Player,
        direction: Direction,
    ) {
        submitWorldPulse(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    val newLocation = currentCrane!!.location.transform(direction)
                    if (!direction.canMove(newLocation)) {
                        sendMessage(player, "You can't move the target outside of the gallery.")
                        return true
                    }

                    val craneX = player.getAttribute(GameAttributes.RE_TWIN_OBJ_LOC_X, 14) + direction.stepX
                    val craneY = player.getAttribute(GameAttributes.RE_TWIN_OBJ_LOC_Y, 12) + direction.stepY
                    updateCraneCam(player, craneX, craneY)

                    removeScenery(currentCrane!!)
                    addScenery(Scenery(66, currentCrane!!.location, 22, 0))

                    currentCrane =
                        currentCrane!!.transform(
                            currentCrane!!.id,
                            currentCrane!!.rotation,
                            region.baseLocation.transform(craneX, craneY, 0),
                        )
                    addScenery(Scenery(14977, currentCrane!!.location, 22, 0))
                    addScenery(currentCrane!!)

                    return true
                }
            },
        )
    }

    private fun showNPCs(showMolly: Boolean) {
        region.planes[0].npcs.filter { it.id in 3852..3891 }.forEach { npc ->
            npc.isInvisible = !showMolly
        }
    }

    fun isEvilTwin(
        npc: NPC,
        hash: Int,
    ): Boolean {
        val npcId = npc.id - 3852
        val type = npcId / EvilTwinColors.values().size
        val color = npcId - (type * EvilTwinColors.values().size)
        return hash == (color or (type shl 16))
    }

    fun removeSuspects(player: Player) {
        val hash = player.getAttribute(GameAttributes.RE_TWIN_START, 0)
        region.planes[0]
            .npcs
            .filter { it.id in 3852..3891 && !isEvilTwin(it, hash) }
            .forEach { npc ->
                Graphics.send(Graphics.create(86), npc.location)
                npc.clear()
            }
    }

    private fun spawnSuspects(hash: Int) {
        if (region.planes[0].npcs.size > 3) return

        val npcId = 3852 + (hash and 0xFF)
        repeat(5) {
            val location = region.baseLocation.transform(11 + RandomFunction.random(8), 6 + RandomFunction.random(6), 0)
            NPC.create(npcId + (it * EvilTwinColors.values().size), location).apply {
                isWalks = true
                walkRadius = 6
                init()
            }
        }
    }

    private fun getMollyId(hash: Int): Int =
        3892 + (hash and 0xFF) + (((hash shr 16) and 0xFF) * EvilTwinColors.values().size)
}
