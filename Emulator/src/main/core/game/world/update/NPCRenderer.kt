package core.game.world.update

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.net.packet.IoBuffer
import core.net.packet.PacketHeader
import java.nio.ByteBuffer

object NPCRenderer {
    @JvmStatic
    fun render(player: Player) {
        val buffer = IoBuffer(32, PacketHeader.SHORT)
        val info = player.renderInfo
        val localNPCs = info.localNpcs
        val maskBuffer = IoBuffer(-1, PacketHeader.NORMAL, ByteBuffer.allocate(1 shl 16))
        buffer.setBitAccess()
        buffer.putBits(8, localNPCs.size)
        val toRemove: MutableList<NPC> = ArrayList()
        val it: Iterator<NPC> = localNPCs.iterator()

        // Loop through current NPCs and determine which need to be removed or updated
        while (it.hasNext()) {
            val npc = it.next()
            val withinDistance = player.location.withinDistance(npc.location)

            // Check if the NPC should be removed or updated
            if (npc.isHidden(player) || !withinDistance || npc.properties.isTeleporting) {
                buffer.putBits(1, 1).putBits(2, 3)
                toRemove.add(npc)

                // Remove tolerance if NPC is out of range
                if (!withinDistance && npc.aggressiveHandler != null) {
                    npc.aggressiveHandler.removeTolerance(player.index)
                }
            } else if (npc.walkingQueue.runDir != -1) {
                buffer
                    .putBits(
                        1,
                        1,
                    ).putBits(2, 2)
                    .putBits(3, npc.walkingQueue.walkDir)
                    .putBits(3, npc.walkingQueue.runDir)
                flagMaskUpdate(player, buffer, maskBuffer, npc, false)
            } else if (npc.walkingQueue.walkDir != -1) {
                buffer.putBits(1, 1).putBits(2, 1).putBits(3, npc.walkingQueue.walkDir)
                flagMaskUpdate(player, buffer, maskBuffer, npc, false)
            } else if (npc.updateMasks.isUpdateRequired) {
                buffer.putBits(1, 1).putBits(2, 0)
                writeMaskUpdates(player, maskBuffer, npc, false)
            } else {
                buffer.putBits(1, 0)
            }
        }

        // Remove NPCs that are no longer within range
        localNPCs.removeAll(toRemove)

        // Add any new NPCs to the local NPCs list
        for (npc in RegionManager.getLocalNpcs(player)) {
            if (localNPCs.size >= 255) {
                break
            }
            if (localNPCs.contains(npc) || npc.isHidden(player)) {
                continue
            }

            // Update NPC info and add to local NPC list
            buffer
                .putBits(
                    15,
                    npc.index,
                ).putBits(1, if (npc.properties.isTeleporting) 1 else 0)
                .putBits(3, npc.direction.ordinal)
            flagMaskUpdate(player, buffer, maskBuffer, npc, true)
            var offsetX = npc.location.x - player.location.x
            var offsetY = npc.location.y - player.location.y
            if (offsetX < 0) {
                offsetX += 32
            }
            if (offsetY < 0) {
                offsetY += 32
            }
            buffer.putBits(5, offsetY)
            buffer.putBits(14, npc.id)
            buffer.putBits(5, offsetX)

            // Update NPC aggression tolerance if necessary
            if (npc.aggressiveHandler != null) {
                npc.aggressiveHandler.playerTolerance[player.index] = GameWorld.ticks
            }
            localNPCs.add(npc)
        }

        // Finalize mask data and send the update
        val masks = maskBuffer.toByteBuffer()
        masks.flip()
        if (masks.hasRemaining()) {
            buffer.putBits(15, 32767)
            buffer.setByteAccess()
            buffer.put(masks)
        } else {
            buffer.setByteAccess()
        }
        player.session.write(buffer)
    }

    private fun flagMaskUpdate(
        player: Player,
        buffer: IoBuffer,
        maskBuffer: IoBuffer,
        npc: NPC,
        sync: Boolean,
    ) {
        if (npc.updateMasks.isUpdateRequired) {
            buffer.putBits(1, 1)
            writeMaskUpdates(player, maskBuffer, npc, sync)
        } else {
            buffer.putBits(1, 0)
        }
    }

    fun writeMaskUpdates(
        player: Player?,
        maskBuffer: IoBuffer?,
        npc: NPC,
        sync: Boolean,
    ) {
        if (sync) {
            npc.updateMasks.writeSynced(player, npc, maskBuffer!!, true)
        } else {
            npc.updateMasks.write(player, npc, maskBuffer!!)
        }
    }
}
