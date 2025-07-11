package content.global.ame.eviltwin

import core.api.*
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
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.CameraViewPacket
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Evil twin utils.
 */
object EvilTwinUtils {

    // Constants
    const val randomEvent = "/save:evil_twin:random"
    const val originalLocation = "/save:original-loc"
    const val logout = "/save:evil_twin:logout"
    const val crane_x_loc = "/save:evil_twin:ccx"
    const val crane_y_loc = "/save:evil_twin:ccy"

    // Rewards
    val rewards = arrayOf(
        Item(Items.UNCUT_DIAMOND_1618, 2),
        Item(Items.UNCUT_RUBY_1620, 3),
        Item(Items.UNCUT_EMERALD_1622, 3),
        Item(Items.UNCUT_SAPPHIRE_1624, 4)
    )

    // Dynamic Region
    val region: DynamicRegion = DynamicRegion.create(7504) //Regions.RANDOM_EVENT_EVIL_TWIN_7504

    // Variables
    var success = false
    var tries = 3
    const val offsetX = 0
    const val offsetY = 0

    var mollyNPC: NPC? = null
    var craneNPC: NPC? = null
    var currentCrane: Scenery? = null

    /**
     * Starts the Evil Twin random event for the player.
     *
     * @param [player] The player starting the event.
     * @return `true` if the event was started successfully, `false` otherwise.
     */
    fun start(player: Player): Boolean {
        region.add(player)
        region.setMusicId(612)
        currentCrane = Scenery(14976, region.baseLocation.transform(14, 12, 0), 10, 0)
        val color: EvilTwinColors = RandomFunction.getRandomElement(EvilTwinColors.values())
        val model = RandomFunction.random(5)
        val hash = color.ordinal or (model shl 16)
        val npcId = getMollyId(hash)
        setAttribute(player, randomEvent, hash)
        mollyNPC = NPC.create(npcId, Location.getRandomLocation(player.location, 1, true))
        mollyNPC!!.init()
        sendChat(mollyNPC!!, "I need your help, ${player.username}.")
        mollyNPC!!.faceTemporary(player, 3)
        setAttribute(player, originalLocation, player.location)
        queueScript(player, 4, QueueStrength.SOFT) {
            teleport(player, mollyNPC!!, hash)
            mollyNPC!!.locks.lockMovement(300000)
            openDialogue(player, MollyDialogue(3))
            return@queueScript stopExecuting(player)
        }
        return true
    }

    fun teleport(player: Player, npc: NPC, hash: Int) {
        setMinimapState(player, 2)
        npc.properties.teleportLocation = region.baseLocation.transform(4, 15, 0)
        npc.direction = Direction.NORTH
        player.properties.teleportLocation = region.baseLocation.transform(4, 16, 0)
        registerLogoutListener(player, logout) { p ->
            p.location = getAttribute(p, originalLocation, player.location)
        }
        spawnSuspects(hash)
        showNPCs(true)
    }

    fun cleanup(player: Player) {
        craneNPC = null
        success = false
        mollyNPC!!.clear()
        PlayerCamera(player).reset()
        restoreTabs(player)
        player.properties.teleportLocation = getAttribute(player, originalLocation, null)
        setMinimapState(player, 0)
        removeAttributes(player, randomEvent, originalLocation, crane_x_loc, crane_y_loc)
        clearLogoutListener(player, logout)
    }

    /**
     * Decreases the number of tries the player has left.
     *
     * @param [player] the player.
     */
    fun decreaseTries(player: Player) {
        tries--
        sendString(player, "Tries: $tries", Components.CRANE_CONTROL_240, 27)
        if (tries < 1) {
            lock(player, 20)
            closeTabInterface(player)
            openDialogue(player, MollyDialogue(1))
        }
    }

    /**
     * Updates the location of the player and entity.
     *
     * @param [player] the player.
     * @param [entity] the entity.
     * @param [last]   the last location.
     */
    fun locationUpdate(player: Player, entity: Entity, last: Location?) {
        if (entity == craneNPC && entity.walkingQueue.queue.size > 1 && player.interfaceManager.singleTab != null) {
            val l: Location = entity.location
            PacketRepository.send(CameraViewPacket::class.java, OutgoingContext.Camera(player, OutgoingContext.CameraType.POSITION, l.x + 2, l.y + 3, 520, 1, 5))
            PacketRepository.send(CameraViewPacket::class.java, OutgoingContext.Camera(player, OutgoingContext.CameraType.ROTATION, l.x - 3, l.y - 3, 420, 1, 5))
        } else if (entity == player) {
            if (mollyNPC!!.isHidden(player) && entity.location.localX < 9) {
                showNPCs(true)
            } else if (!mollyNPC!!.isHidden(player) && entity.location.localX > 8) {
                showNPCs(false)
            }
        }
        return locationUpdate(player, entity, last)
    }

    /**
     * Updates the camera view of the crane.
     *
     * @param [player] the player.
     * @param [x]      the x-coordinate of the crane.
     * @param [y]      the y-coordinate of the crane.
     */
    fun updateCraneCam(player: Player, x: Int, y: Int) {
        if (player.interfaceManager.singleTab != null) {
            var loc = region.baseLocation.transform(14, 20, 0)
            PacketRepository.send(CameraViewPacket::class.java, OutgoingContext.Camera(player, OutgoingContext.CameraType.POSITION, loc.x, loc.y, 520, 1, 100))
            loc = region.baseLocation.transform(x, 4 + y - (if (x < 14 || x > 14) (y / 4) else 0), 0)
            PacketRepository.send(CameraViewPacket::class.java, OutgoingContext.Camera(player, OutgoingContext.CameraType.ROTATION, loc.x, loc.y, 420, 1, 100))
        }
        setAttribute(player, crane_x_loc, x)
        setAttribute(player, crane_y_loc, y)
    }

    /**
     * Moves the crane in the specified direction.
     *
     * @param [player]    the player.
     * @param [direction] the direction to move the crane.
     */
    fun moveCrane(player: Player, direction: Direction) {
        submitWorldPulse(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    if (!direction.canMove(currentCrane!!.location.transform(direction))) {
                        return true
                    }
                    val craneX: Int = player.getAttribute(crane_x_loc, 14) + direction.stepX
                    val craneY: Int = player.getAttribute(crane_y_loc, 12) + direction.stepY
                    updateCraneCam(player, craneX, craneY)
                    removeScenery(currentCrane!!)
                    addScenery(Scenery(66, currentCrane!!.location, 22, 0))
                    currentCrane = currentCrane!!.transform(
                        currentCrane!!.id,
                        currentCrane!!.rotation,
                        region.baseLocation.transform(craneX, craneY, 0)
                    )
                    addScenery(Scenery(14977, currentCrane!!.location, 22, 0))
                    addScenery(currentCrane!!)
                    return true
                }
            }
        )
    }

    /**
     * Shows or hides the NPCs in the Evil Twin region.
     *
     * @param [showMolly] True to show Molly, false to hide Molly.
     */
    fun showNPCs(showMolly: Boolean) {
        for (npc in region.planes[0].npcs) {
            if (npc.id in 3852..3891) {
                npc.isInvisible
            } else {
                mollyNPC!!.isInvisible = !showMolly
            }
        }
    }

    /**
     * Checks if an NPC is the Evil Twin.
     *
     * @param [npc]  the NPC to check.
     * @param [hash] the hash value used to identify the Evil Twin.
     * @return `true` if the NPC is the Evil Twin, `false` otherwise.
     */
    fun isEvilTwin(npc: NPC, hash: Int): Boolean {
        val npcId = npc.id - 3852
        val type: Int = npcId / EvilTwinColors.values().size
        val color: Int = npcId - (type * EvilTwinColors.values().size)
        return hash == (color or (type shl 16))
    }

    /**
     * Removes the non-Evil Twin suspects from the Evil Twin region.
     *
     * @param [player] The player.
     */
    fun removeSuspects(player: Player) {
        val hash: Int = player.getAttribute(randomEvent, 0)
        for (npc in region.planes[0].npcs) {
            if (npc.id in 3852..3891 && !isEvilTwin(npc, hash)) {
                Graphics.send(Graphics.create(86), npc.location)
                npc.clear()
            }
        }
    }

    /**
     * Spawns the Evil Twin suspects in the Evil Twin region.
     *
     * @param [hash] The hash value used to identify the Evil Twin.
     */
    fun spawnSuspects(hash: Int) {
        if (region.planes[0].npcs.size > 3) {
            return
        }
        val npcId = 3852 + (hash and 0xFF)
        for (i in 0..4) {
            val location = region.baseLocation.transform(11 + RandomFunction.random(8), 6 + RandomFunction.random(6), 0)
            val suspect = NPC.create(npcId + (i * EvilTwinColors.values().size), location)
            suspect.isWalks = true
            suspect.walkRadius = 6
            suspect.init()
        }
    }

    /**
     * Gets the Molly NPC ID based on the hash value.
     *
     * @param hash The hash value used to identify the Evil Twin.
     * @return The [MollyNPC] id.
     */
    fun getMollyId(hash: Int): Int {
        return 3892 + (hash and 0xFF) + (((hash shr 16) and 0xFF) * EvilTwinColors.values().size)
    }
}
