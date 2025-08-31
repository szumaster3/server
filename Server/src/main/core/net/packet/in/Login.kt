package core.net.packet.`in`

import com.moandjiezana.toml.Toml
import core.ServerConstants
import core.ServerStore
import core.ServerStore.Companion.addToList
import core.ServerStore.Companion.getList
import core.api.log
import core.auth.AuthResponse
import core.cache.secure.ISAACCipher
import core.cache.secure.ISAACPair
import core.cache.util.ByteBufferExtensions
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.*
import core.game.node.entity.player.info.login.LoginParser
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.net.IoSession
import core.tools.Log
import core.tools.StringUtils
import core.worker.ManagementEvents.publish
import proto.management.JoinClanRequest
import proto.management.PlayerStatusUpdate
import proto.management.RequestContactInfo
import java.io.File
import java.math.BigInteger
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

/**
 * Handles the login process, including packet decoding, authentication,
 * session handling, and account verification.
 */
object Login {
    private const val ENCRYPTION_VERIFICATION_BYTE: Int = 10
    private const val NORMAL_LOGIN_OP = 16
    private const val RECONNECT_LOGIN_OP = 18
    const val CACHE_INDEX_COUNT = 29

    private var exceptionData: Toml? = null
    private var lastModifiedData = 0L

    /**
     * Decodes login data from the provided [ByteBuffer].
     *
     * @param buffer The buffer containing login data.
     * @return A pair containing an [AuthResponse] and optionally a [LoginInfo] object.
     */
    fun decodeFromBuffer(buffer: ByteBuffer): Pair<AuthResponse, LoginInfo?> {
        try {
            val info = LoginInfo.createDefault()

            info.opcode = buffer.get().toInt()
            if (buffer.short.toInt() != buffer.remaining()) {
                return Pair(AuthResponse.BadSessionID, null)
            }
            val revision = buffer.int
            if (revision != ServerConstants.REVISION) {
                return Pair(AuthResponse.Updated, null)
            }
            if (info.opcode != NORMAL_LOGIN_OP && info.opcode != RECONNECT_LOGIN_OP) {
                log(this::class.java, Log.WARN, "Invalid Login Opcode: ${info.opcode}")
                return Pair(AuthResponse.InvalidLoginServer, null)
            }

            noop(buffer)
            info.showAds = buffer.get().toInt() == 1
            noop(buffer)
            info.windowMode = buffer.get().toInt()
            info.screenWidth = buffer.short.toInt()
            info.screenHeight = buffer.short.toInt()
            info.displayMode = buffer.get().toInt()
            noop(buffer, 24) // Skip past a bunch of random (actually random) data the client sends
            ByteBufferExtensions.getString(buffer) // same as above
            info.adAffiliateId = buffer.int
            info.settingsHash = buffer.int
            info.currentPacketCount = buffer.short.toInt()

            // Read client-reported CRC sums
            for (i in 0 until CACHE_INDEX_COUNT) info.crcSums[i] = buffer.int

            val decryptedBuffer = decryptRSABuffer(buffer, ServerConstants.EXPONENT, ServerConstants.MODULUS)
            decryptedBuffer.rewind()

            if (decryptedBuffer.get().toInt() != ENCRYPTION_VERIFICATION_BYTE) {
                return Pair(AuthResponse.UnexpectedError, info)
            }

            info.isaacPair = produceISAACPairFrom(decryptedBuffer)
            info.username = StringUtils.longToString(decryptedBuffer.long)
            info.password = ByteBufferExtensions.getString(decryptedBuffer)

            if (Repository.getPlayerByName(info.username) != null) {
                return Pair(AuthResponse.AlreadyOnline, info)
            }

            return Pair(AuthResponse.Success, info)
        } catch (buf: BufferUnderflowException) {
            return Pair(AuthResponse.UnexpectedError, null)
        } catch (e: Exception) {
            log(this::class.java, Log.ERR, "Exception encountered during login packet parsing! See stack trace below.")
            e.printStackTrace()
            return Pair(AuthResponse.UnexpectedError, null)
        }
    }

    /**
     * Generates an [ISAACPair] for encrypting and decrypting communication.
     *
     * @param buffer The buffer containing encryption seeds.
     * @return The generated [ISAACPair].
     */
    private fun produceISAACPairFrom(buffer: ByteBuffer): ISAACPair {
        val incomingSeed = IntArray(4)
        for (i in incomingSeed.indices) {
            incomingSeed[i] = buffer.int
        }
        val inCipher = ISAACCipher(incomingSeed)
        for (i in incomingSeed.indices) {
            incomingSeed[i] += 50
        }
        val outCipher = ISAACCipher(incomingSeed)
        return ISAACPair(inCipher, outCipher)
    }

    /**
     * Decrypts an RSA-encrypted buffer.
     *
     * @param buffer The encrypted buffer.
     * @param exponent The RSA exponent.
     * @param modulus The RSA modulus.
     * @return A decrypted [ByteBuffer].
     */
    @JvmStatic
    fun decryptRSABuffer(
        buffer: ByteBuffer,
        exponent: BigInteger,
        modulus: BigInteger,
    ): ByteBuffer =
        try {
            val numBytes = buffer.get().toInt() and 0xFF
            val encryptedBytes = ByteArray(numBytes)
            buffer.get(encryptedBytes)

            val encryptedBigInt = BigInteger(encryptedBytes)
            ByteBuffer.wrap(encryptedBigInt.modPow(exponent, modulus).toByteArray())
        } catch (e: BufferUnderflowException) {
            ByteBuffer.wrap(byteArrayOf(-1))
        }

    /**
     * Skips over a given number of bytes in the buffer.
     *
     * @param buffer The buffer to modify.
     * @param amount The number of bytes to skip (default is 1).
     */
    private fun noop(
        buffer: ByteBuffer,
        amount: Int = 1,
    ) {
        buffer.get(ByteArray(amount))
    }

    /**
     * Proceeds with a player's login session.
     *
     * @param session The player's session.
     * @param details The player's details.
     * @param opcode The login opcode.
     */
    fun proceedWith(
        session: IoSession,
        details: PlayerDetails,
        opcode: Int,
    ) {
        if (Repository.uid_map.contains(details.uid)) {
            session.write(AuthResponse.AlreadyOnline)
            return
        }
        details.session = session
        details.info.translate(UIDInfo(details.ipAddress, "DEPRECATED", "DEPRECATED", "DEPRECATED"))

        val archive = ServerStore.getArchive("flagged-ips")
        val flaggedIps = archive.getList<String>("ips")
        if (flaggedIps.contains(details.ipAddress)) {
            // Discord.postPlayerAlert(details.username, "Login from flagged IP ${details.ipAddress}")
        }

        val player = Player(details)
        PlayerMonitor.log(player, LogType.IP_LOG, details.ipAddress)
        if (canBypassAccountLimitCheck(player)) {
            proceedWithAcceptableLogin(session, player, opcode)
        } else {
            if (checkAccountLimit(details.ipAddress, details.username)) {
                proceedWithAcceptableLogin(session, player, opcode)
            } else {
                session.write(AuthResponse.LoginLimitExceeded)
            }
        }
    }

    /**
     * Checks if a player can bypass account login limits.
     *
     * @param player The player to check.
     * @return `true` if they can bypass limits, otherwise `false`.
     */
    private fun canBypassAccountLimitCheck(player: Player): Boolean =
        player.rights == Rights.ADMINISTRATOR || player.rights == Rights.PLAYER_MODERATOR

    /**
     * Completes the login process for a validated player.
     *
     * @param session The player's session.
     * @param player The player object.
     * @param opcode The login opcode.
     */
    private fun proceedWithAcceptableLogin(
        session: IoSession,
        player: Player,
        opcode: Int,
    ) {
        Repository.addPlayer(player)
        session.setLastPing(System.currentTimeMillis())
        try {
            LoginParser(player.details).initialize(player, opcode == RECONNECT_LOGIN_OP)
            sendMSEvents(player.details)
        } catch (e: Exception) {
            e.printStackTrace()
            session.disconnect()
        }
    }

    private fun checkAccountLimit(
        ipAddress: String,
        username: String,
    ): Boolean {
        var accountLimit = ServerConstants.DAILY_ACCOUNT_LIMIT

        if (File(ServerConstants.CONFIG_PATH + "account_limit_exceptions.conf").exists()) {
            try {
                val f = File(ServerConstants.CONFIG_PATH + "account_limit_exceptions.conf")
                if (f.lastModified() != lastModifiedData) {
                    exceptionData = Toml().read(f)
                    lastModifiedData = f.lastModified()
                }

                if (exceptionData?.contains("exceptions.\"${ipAddress}\"") == true) {
                    accountLimit = exceptionData?.getLong("exceptions.\"${ipAddress}\"", 0L)?.toInt() ?: accountLimit
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val archive = ServerStore.getArchive("daily-accounts")
        val accounts = archive.getList<String>(ipAddress)
        if (username in accounts) return true

        if (accounts.size >= accountLimit) {
            return false
        }

        archive.addToList(ipAddress, username)
        return true
    }

    /**
     * Sends management service events for the player.
     *
     * @param details The player's details.
     */
    private fun sendMSEvents(details: PlayerDetails) {
        val statusEvent = PlayerStatusUpdate.newBuilder()
        statusEvent.username = details.username
        statusEvent.world = GameWorld.settings!!.worldId
        statusEvent.notifyFriendsOnly = false
        publish(statusEvent.build())

        val contactEvent = RequestContactInfo.newBuilder()
        contactEvent.username = details.username
        contactEvent.world = GameWorld.settings!!.worldId
        publish(contactEvent.build())

        if (!details.communication.currentClan.isNullOrEmpty() && details.communication.clan == null) {
            val clanEvent = JoinClanRequest.newBuilder()
            clanEvent.username = details.username
            clanEvent.clanName = details.communication.currentClan
            publish(clanEvent.build())
        }
    }
}
