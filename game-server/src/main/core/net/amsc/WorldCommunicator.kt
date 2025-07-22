package core.net.amsc

import core.game.node.entity.player.info.login.LoginParser
import core.game.world.GameWorld
import core.net.EventProducer
import core.net.IoSession
import core.net.NioReactor
import core.net.producer.MSHSEventProducer
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.concurrent.ConcurrentHashMap

/**
 * Handles world communication.
 *
 * @author Emperor
 */
object WorldCommunicator {

    private val HANDSHAKE_PRODUCER: EventProducer = MSHSEventProducer()

    var state: ManagementServerState = ManagementServerState.CONNECTING
        set(value) {
            if (field != value) {
                field = value
                value.set()
            }
        }

    private var session: IoSession? = null

    private val WORLDS = arrayOfNulls<WorldStatistics>(10)

    private val loginAttempts: MutableMap<String, LoginParser> = ConcurrentHashMap()

    private var reactor: NioReactor? = null

    fun register(session: IoSession) {
        this.session = session
        session.setProducer(HANDSHAKE_PRODUCER)
        session.write(true)
        val worldId = GameWorld.settings?.worldId ?: return
        WORLDS[worldId - 1] = WorldStatistics(worldId)
        session.setObject(WORLDS[worldId - 1])
    }

    fun connect() {
        try {
            state = ManagementServerState.CONNECTING
            val msAddress = GameWorld.settings?.msAddress ?: return
            reactor = NioReactor.connect(msAddress, 5555)
            reactor?.start()
        } catch (e: Throwable) {
            e.printStackTrace()
            terminate()
        }
    }

    @Throws(IOException::class)
    private fun isLocallyHosted(): Boolean {
        val msAddress = GameWorld.settings?.msAddress ?: return false
        val address = InetAddress.getByName(msAddress)
        return address.isAnyLocalAddress || address.isLoopbackAddress ||
                NetworkInterface.getByInetAddress(address) != null
    }

    fun terminate() {
        state = ManagementServerState.NOT_AVAILABLE
        reactor?.terminate()
        reactor = null
    }

    fun finishLoginAttempt(username: String): LoginParser? = loginAttempts.remove(username)

    fun getLocalWorld(): WorldStatistics? {
        val worldId = GameWorld.settings?.worldId ?: return null
        return WORLDS[worldId - 1]
    }

    fun getWorld(playerName: String): Int {
        for (i in WORLDS.indices) {
            if (WORLDS[i]?.players?.contains(playerName) == true) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun getWorld(id: Int): WorldStatistics? = WORLDS[id - 1]

    @JvmStatic
    fun getSession(): IoSession? = session

    @JvmStatic
    fun isEnabled(): Boolean = state == ManagementServerState.AVAILABLE

}