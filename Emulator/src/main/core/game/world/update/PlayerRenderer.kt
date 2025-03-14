package core.game.world.update

import core.game.node.entity.player.Player
import core.game.node.entity.player.info.RenderInfo
import core.game.world.map.RegionManager
import core.net.packet.IoBuffer
import core.net.packet.PacketHeader

object PlayerRenderer {
    private const val MAX_ADD_COUNT = 10

    @JvmStatic
    fun render(player: Player) {
        val buffer = IoBuffer(225, PacketHeader.SHORT)
        val flags = IoBuffer(-1, PacketHeader.NORMAL)
        val info = player.renderInfo
        updateLocalPosition(player, buffer, flags)
        buffer.putBits(8, info.localPlayers.size)
        val it = info.localPlayers.iterator()

        // Loop through current local players and update or remove them
        while (it.hasNext()) {
            val other = it.next()
            if (!other.isActive ||
                !other.location.withinDistance(player.location) ||
                other.properties.isTeleporting ||
                other.isInvisible
            ) {
                buffer.putBits(1, 1)
                buffer.putBits(2, 3)
                it.remove()
                continue
            }
            renderLocalPlayer(player, other, buffer, flags)
        }

        // Add new players that are in range but not yet local
        var count = 0
        for (other in RegionManager.getLocalPlayers(player, 15)) {
            if (other === player || !other.isActive || info.localPlayers.contains(other) || other.isInvisible) {
                continue
            }
            if (info.localPlayers.size >= 255 || ++count == MAX_ADD_COUNT) {
                break
            }
            addLocalPlayer(player, other, info, buffer, flags)
        }

        // Finalize and send the update
        val masks = flags.toByteBuffer()
        masks.flip()
        if (masks.hasRemaining()) {
            buffer.putBits(11, 2047)
            buffer.setByteAccess()
            buffer.put(masks)
        } else {
            buffer.setByteAccess()
        }
        player.details.session.write(buffer)
    }

    private fun renderLocalPlayer(
        player: Player,
        other: Player,
        buffer: IoBuffer,
        flags: IoBuffer,
    ) {
        if (other.walkingQueue.runDir != -1) {
            buffer.putBits(1, 1) // Updating
            buffer.putBits(2, 2) // Sub opcode for running
            buffer.putBits(1, 1)
            buffer.putBits(3, other.walkingQueue.walkDir)
            buffer.putBits(3, other.walkingQueue.runDir)
            flagMaskUpdate(player, other, buffer, flags, false, false)
        } else if (other.walkingQueue.walkDir != -1) {
            buffer.putBits(1, 1) // Updating
            buffer.putBits(2, 1) // Sub opcode for walking
            buffer.putBits(3, other.walkingQueue.walkDir)
            flagMaskUpdate(player, other, buffer, flags, false, false)
        } else if (other.updateMasks.isUpdateRequired) {
            buffer.putBits(1, 1)
            buffer.putBits(2, 0) // No movement, but update is required
            writeMaskUpdates(player, other, flags, false, false)
        } else {
            buffer.putBits(1, 0) // No update
        }
    }

    private fun addLocalPlayer(
        player: Player,
        other: Player,
        info: RenderInfo,
        buffer: IoBuffer,
        flags: IoBuffer,
    ) {
        buffer.putBits(11, other.index)
        var offsetX = other.location.x - player.location.x
        var offsetY = other.location.y - player.location.y
        if (offsetY < 0) {
            offsetY += 32
        }
        if (offsetX < 0) {
            offsetX += 32
        }

        // Check if the player's appearance or update mask has changed
        val appearance = info.appearanceStamps[other.index and 0x800] != other.updateMasks.appearanceStamp
        val update = appearance || other.updateMasks.isUpdateRequired || other.updateMasks.hasSynced()
        buffer.putBits(1, if (update) 1 else 0)
        buffer.putBits(5, offsetX)
        buffer.putBits(3, other.direction.ordinal)
        buffer.putBits(1, if (other.properties.isTeleporting) 1 else 0)
        buffer.putBits(5, offsetY)
        info.localPlayers.add(other)

        // Update appearance and write masks if needed
        if (update) {
            if (appearance) {
                info.appearanceStamps[other.index and 0x800] = other.updateMasks.appearanceStamp
            }
            writeMaskUpdates(player, other, flags, appearance, true)
        }
    }

    private fun updateLocalPosition(
        local: Player,
        buffer: IoBuffer,
        flags: IoBuffer,
    ) {
        if (local.playerFlags.isUpdateSceneGraph || local.properties.isTeleporting) {
            buffer.putBits(1, 1) // Updating position
            buffer.putBits(2, 3) // Sub opcode for teleport
            buffer.putBits(7, local.location.getSceneY(local.playerFlags.lastSceneGraph))
            buffer.putBits(1, if (local.properties.isTeleporting) 1 else 0)
            buffer.putBits(2, local.location.z)
            flagMaskUpdate(local, local, buffer, flags, false, false)
            buffer.putBits(7, local.location.getSceneX(local.playerFlags.lastSceneGraph))
        } else {
            renderLocalPlayer(local, local, buffer, flags)
        }
    }

    private fun flagMaskUpdate(
        local: Player,
        player: Player,
        buffer: IoBuffer,
        maskBuffer: IoBuffer,
        sync: Boolean,
        appearance: Boolean,
    ) {
        if (player.updateMasks.isUpdateRequired) {
            buffer.putBits(1, 1)
            writeMaskUpdates(local, player, maskBuffer, appearance, sync)
        } else {
            buffer.putBits(1, 0)
        }
    }

    private fun writeMaskUpdates(
        local: Player,
        player: Player,
        flags: IoBuffer,
        appearance: Boolean,
        sync: Boolean,
    ) {
        if (sync) {
            player.updateMasks.writeSynced(local, player, flags, appearance)
        } else if (player.updateMasks.isUpdateRequired) {
            player.updateMasks.write(local, player, flags)
        }
    }
}
