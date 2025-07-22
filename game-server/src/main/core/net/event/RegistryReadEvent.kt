package core.net.event

import core.api.log
import core.game.world.GameWorld.settings
import core.net.IoReadEvent
import core.net.IoSession
import core.net.amsc.ManagementServerState
import core.net.amsc.WorldCommunicator.state
import core.net.producer.MSEventProducer
import core.tools.Log
import java.nio.ByteBuffer

/**
 * Handles world registry read events.
 *
 * @author Emperor
 */
class RegistryReadEvent(session: IoSession, buffer: ByteBuffer) : IoReadEvent(session, buffer) {
    override fun read(session: IoSession, buffer: ByteBuffer) {
        val opcode = buffer.get().toInt() and 0xFF
        when (opcode) {
            0 -> {
                state = ManagementServerState.NOT_AVAILABLE
                log(
                    this.javaClass,
                    Log.ERR,
                    "Failed registering world to AMS - [id=" + settings!!.worldId + ", cause=World id out of bounds]!"
                )
            }

            1 -> {
                session.setProducer(PRODUCER)
                state = ManagementServerState.AVAILABLE
            }

            2, 3 -> state = ManagementServerState.NOT_AVAILABLE
            else -> {}
        }
    }

    companion object {
        /**
         * The event producer.
         */
        private val PRODUCER = MSEventProducer()
    }
}