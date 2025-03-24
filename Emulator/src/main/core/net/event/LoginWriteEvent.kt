package core.net.event

import core.auth.AuthResponse
import core.game.node.entity.player.Player
import core.net.EventProducer
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.producer.GameEventProducer
import java.nio.ByteBuffer

/**
 * The Login write event.
 */
class LoginWriteEvent(
    session: IoSession,
    context: Any,
) : IoWriteEvent(session, context) {
    companion object {
        private val GAME_PRODUCER: EventProducer = GameEventProducer()

        private fun getWorldResponse(session: IoSession): ByteBuffer {
            val buffer = ByteBuffer.allocate(150)
            val player: Player? = session.getPlayer()
            player?.details?.rights?.ordinal?.toByte()?.let {
                    buffer.put(
                        it,
                    )
                }
            buffer.put(0)
            buffer.put(0)
            buffer.put(0)
            buffer.put(1)
            buffer.put(0)
            buffer.put(0)
            player?.index?.toShort()?.let { buffer.putShort(it) }
            buffer.put(1) // Enable all G.E boxes
            buffer.put(1)
            buffer.flip()
            return buffer
        }
    }

    override fun write(
        session: IoSession,
        context: Any,
    ) {
        val response = context as AuthResponse
        val buffer = ByteBuffer.allocate(500)
        buffer.put(response.ordinal.toByte())
        when (response.ordinal) {
            2 -> {
                // Successful login
                buffer.put(getWorldResponse(session))
                session.producer = GAME_PRODUCER
            }

            21 -> {
                // Moving world
                buffer.put(session.serverKey.toByte())
            }
        }
        buffer.flip()
        session.queue(buffer)
    }
}
