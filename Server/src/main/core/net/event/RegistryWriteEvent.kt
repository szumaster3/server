package core.net.event

import core.ServerConstants
import core.cache.util.ByteBufferExtensions
import core.game.world.GameWorld
import core.net.IoSession
import core.net.IoWriteEvent
import java.nio.ByteBuffer

/**
 * Handles game world registry writing events.
 *
 * @author Emperor
 */
class RegistryWriteEvent(session: IoSession, context: Any) : IoWriteEvent(session, context) {

    companion object {
        private const val CHECK = "12x4578f5g45hrdjiofed59898"
    }

    override fun write(session: IoSession, context: Any) {
        val buffer = ByteBuffer.allocate(128)
        buffer.put(GameWorld.settings!!.worldId.toByte())
        buffer.putInt(ServerConstants.REVISION)
        buffer.put(GameWorld.settings!!.countryIndex.toByte())
        buffer.put(if (GameWorld.settings!!.isMembers) 1 else 0)
        buffer.put(if (GameWorld.settings!!.isPvp) 1 else 0)
        buffer.put(if (GameWorld.settings!!.isQuickChat) 1 else 0)
        buffer.put(if (GameWorld.settings!!.isLootshare) 1 else 0)
        ByteBufferExtensions.putString(GameWorld.settings!!.activity, buffer)
        buffer.put(CHECK.toByteArray())
        buffer.flip()
        session.queue(buffer)
    }
}
