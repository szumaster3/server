package core.net.event

import core.ServerConstants
import core.api.log
import core.net.IoReadEvent
import core.net.IoSession
import core.net.lobby.WorldList
import core.net.registry.AccountRegister
import core.tools.Log
import core.tools.RandomFunction
import shared.consts.Network
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap

/**
 * Handles handshake read events.
 *
 * @author Emperor
 */
class HSReadEvent(session: IoSession, buffer: ByteBuffer) : IoReadEvent(session, buffer) {

    companion object {
        // debug
        private val count: MutableMap<String, Int> = ConcurrentHashMap()
    }

    override fun read(session: IoSession, buffer: ByteBuffer) {
        val address = session.getAddress()
        val amount = count.getOrDefault(address, 0)
        count[address] = amount + 1

        val opcode = buffer.get().toInt() and 0xFF
        when (opcode) {
            Network.LOGIN_REQUEST -> {
                session.setNameHash(buffer.get().toInt() and 0xFF)
                session.setServerKey(RandomFunction.RANDOM.nextLong())
                session.write(true)
            }
            Network.HANDSHAKE_REQUEST -> {
                val revision = buffer.int
                // val subRevision = buffer.int
                buffer.flip()

                if (revision != ServerConstants.REVISION) { // || subRevision != Constants.CLIENT_BUILD) {
                    session.disconnect()
                    return
                }
                session.write(false)
            }
            Network.VERIFY_BIRTHDAY_INFORMATION,
            Network.VERIFY_USERNAME_INFORMATION,
            Network.SUBMIT_ACCOUNT_REGISTRATION -> {
                AccountRegister.read(session, opcode, buffer)
            }
            255 -> { // World list
                val updateStamp = buffer.int
                WorldList.sendUpdate(session, updateStamp)
            }
            else -> {
                log(this::class.java, Log.FINE, "PKT $opcode")
                session.disconnect()
            }
        }
    }
}
