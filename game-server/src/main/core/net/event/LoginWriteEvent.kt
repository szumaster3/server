package core.net.event

import core.auth.AuthResponse
import core.game.node.entity.player.Player
import core.net.EventProducer
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.producer.GameEventProducer
import java.nio.ByteBuffer

/**
 * Handles login writing events.
 *
 * @author Emperor
 */
class LoginWriteEvent(session: IoSession, context: Any) : IoWriteEvent(session, context) {

    companion object {
        private val GAME_PRODUCER: EventProducer = GameEventProducer()

        /**
         * Gets the world response buffer.
         *
         * @param session The session.
         * @return The buffer.
         */
        private fun getWorldResponse(session: IoSession): ByteBuffer {
            val buffer = ByteBuffer.allocate(150)
            val player: Player = session.getPlayer() ?: throw IllegalStateException("Player not found in session")
            buffer.put(player.details.rights.ordinal.toByte())
            buffer.put(0)
            buffer.put(0)
            buffer.put(0)
            buffer.put(1)
            buffer.put(0)
            buffer.put(0)
            buffer.putShort(player.index.toShort())
            buffer.put(1) // Enable all G.E boxes
            buffer.put(1)
            buffer.flip()
            return buffer
        }
    }

    override fun write(session: IoSession, context: Any) {
        val response = context as AuthResponse
        val buffer = ByteBuffer.allocate(500)
        buffer.put(response.ordinal.toByte())

        when (response.ordinal) {
            2 -> { // successful login
                buffer.put(getWorldResponse(session))
                session.setProducer(GAME_PRODUCER)
            }

            21 -> { // Moving world
                buffer.put(session.getServerKey().toByte())
            }
        }
        buffer.flip()
        session.queue(buffer)
    }
}
