package core.storage

import core.auth.UserAccountInfo
import core.game.system.communication.CommunicationInfo
import core.game.world.repository.Repository
import java.lang.Long.max
import java.sql.*

/**
 * SQL-based implementation of [AccountStorageProvider].
 */
class SQLStorageProvider : AccountStorageProvider {
    var connectionString = ""
    var connectionUsername = ""
    var connectionPassword = ""

    /** Returns a database connection. */
    fun getConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")
        return DriverManager.getConnection(connectionString, connectionUsername, connectionPassword)
    }

    /** Sets database connection parameters. */
    fun configure(
        host: String,
        databaseName: String,
        username: String,
        password: String,
    ) {
        connectionString = "jdbc:mysql://$host/$databaseName?useTimezone=true&serverTimezone=UTC"
        connectionUsername = username
        connectionPassword = password
    }

    /** Returns true if the username exists. */
    override fun checkUsernameTaken(username: String): Boolean {
        val conn = getConnection()
        conn.use {
            val query = it.prepareStatement(usernameQuery)
            query.setString(1, username.lowercase())
            val result = query.executeQuery()
            return result.next()
        }
    }

    /** Loads account info or returns default if not found. */
    override fun getAccountInfo(username: String): UserAccountInfo {
        val conn = getConnection()
        conn.use { con ->
            val query = con.prepareStatement(accountInfoQuery)
            query.setString(1, username.lowercase())
            val result = query.executeQuery()
            if (result.next()) {
                val user = UserAccountInfo.createDefault().apply {
                    this.username = username
                    password = result.getString(2) ?: password
                    uid = result.getInt(3)
                    rights = result.getInt(4)
                    credits = result.getInt(5)
                    ip = result.getString(6) ?: ip
                    lastUsedIp = result.getString(7) ?: lastUsedIp
                    muteEndTime = max(0L, result.getLong(8))
                    banEndTime = max(0L, result.getLong(9))
                    contacts = result.getString(10) ?: contacts
                    blocked = result.getString(11) ?: blocked
                    clanName = result.getString(12) ?: clanName
                    currentClan = result.getString(13) ?: currentClan
                    clanReqs = result.getString(14) ?: clanReqs
                    timePlayed = max(0L, result.getLong(15))
                    lastLogin = max(0L, result.getLong(16))
                    online = result.getBoolean(17)
                    joinDate = result.getTimestamp(18) ?: Timestamp(System.currentTimeMillis())
                }
                user.setInitialReferenceValues()
                return user
            }
            return UserAccountInfo.createDefault()
        }
    }

    /** Stores new account info. */
    override fun store(info: UserAccountInfo) {
        val conn = getConnection()
        conn.use {
            val stmt = it.prepareStatement(insertInfoQuery, Statement.RETURN_GENERATED_KEYS)
            val empty = UserAccountInfo.createDefault().apply { username = info.username }

            if (info == empty) throw IllegalStateException("Tried to store empty data!")
            if (checkUsernameTaken(info.username)) throw SQLDataException("Account already exists!")

            stmt.setString(1, info.username)
            stmt.setString(2, info.password)
            stmt.setInt(3, info.rights)
            stmt.setInt(4, info.credits)
            stmt.setString(5, info.ip)
            stmt.setString(6, info.lastUsedIp)
            stmt.setLong(7, info.muteEndTime)
            stmt.setLong(8, info.banEndTime)
            stmt.setString(9, info.contacts)
            stmt.setString(10, info.blocked)
            stmt.setString(11, info.clanName)
            stmt.setString(12, info.currentClan)
            stmt.setString(13, info.clanReqs)
            stmt.setLong(14, info.timePlayed)
            stmt.setLong(15, info.lastLogin)
            stmt.setBoolean(16, info.online)
            stmt.setTimestamp(17, info.joinDate)
            stmt.execute()

            val result = stmt.generatedKeys
            if (result.next()) info.uid = result.getInt(1)
            info.setInitialReferenceValues()
        }
    }

    /** Updates changed fields for an account. */
    override fun update(info: UserAccountInfo) {
        val (fields, data) = info.getChangedFields()
        if (fields.isEmpty()) return

        val conn = getConnection()
        val query = buildUpdateInfoQuery(fields)

        conn.use {
            val stmt = it.prepareStatement(query)
            var index = 1
            for (i in fields) {
                when (val value = data[i]) {
                    is String -> stmt.setString(index++, value)
                    is Int -> stmt.setInt(index++, value)
                    is Boolean -> stmt.setBoolean(index++, value)
                    is Long -> stmt.setLong(index++, value)
                }
            }
            stmt.setInt(index, info.uid)
            stmt.execute()
            info.initialValues = data
        }
    }

    /** Deletes account by username. */
    override fun remove(info: UserAccountInfo) {
        val conn = getConnection()
        conn.use {
            val stmt = it.prepareStatement(removeInfoQuery)
            stmt.setString(1, info.username)
            stmt.execute()
        }
    }

    /** Returns online friends for the given user. */
    override fun getOnlineFriends(username: String): List<String> {
        val conn = getConnection()
        var tokens = ""
        conn.use {
            val stmt = it.prepareStatement(GET_ALL_FRIENDS_QUERY)
            stmt.setString(1, username)
            val res = stmt.executeQuery()
            if (res.next()) tokens = res.getString(1)
        }
        return CommunicationInfo.parseContacts(tokens)
            .mapNotNull { (name, _) -> if (Repository.getPlayerByName(name) != null) name else null }
    }

    /** Returns usernames linked to the given IP. */
    override fun getUsernamesWithIP(ip: String): List<String> {
        val conn = getConnection()
        val result = ArrayList<String>()
        conn.use {
            val stmt = it.prepareStatement(accountsByIPQuery)
            stmt.setString(1, ip)
            val res = stmt.executeQuery()
            while (res.next()) result.add(res.getString(1))
        }
        return result
    }

    companion object {
        private const val usernameQuery = "SELECT username FROM members WHERE username = ?;"
        private const val removeInfoQuery = "DELETE FROM members WHERE username = ?;"
        private const val accountsByIPQuery = "SELECT username FROM members WHERE lastGameIp = ?;"
        private const val accountInfoQuery =
            "SELECT username,password,UID,rights,credits,ip,lastGameIp," +
                    "muteTime,banTime,contacts,blocked,clanName,currentClan,clanReqs," +
                    "timePlayed,lastLogin,online,joined_date FROM members WHERE username = ?;"
        private const val insertInfoQuery =
            "INSERT INTO members (username,password,rights,credits,ip,lastGameIp," +
                    "muteTime,banTime,contacts,blocked,clanName,currentClan,clanReqs," +
                    "timePlayed,lastLogin,online,joined_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"

        private val GET_ALL_FRIENDS_QUERY = "SELECT contacts FROM players WHERE username = ?;"

        private fun buildUpdateInfoQuery(updatedIndices: ArrayList<Int>): String {
            val sb = StringBuilder("UPDATE members SET ")
            val valid = updatedIndices.filter { it in UPDATE_QUERY_FIELDS }
            valid.forEachIndexed { i, field ->
                sb.append("${UPDATE_QUERY_FIELDS[field]} = ?")
                if (i < valid.size - 1) sb.append(",")
            }
            sb.append(" WHERE uid = ?;")
            return sb.toString()
        }

        private val UPDATE_QUERY_FIELDS = mapOf(
            0 to "username",
            1 to "password",
            3 to "rights",
            4 to "credits",
            6 to "lastGameIp",
            7 to "muteTime",
            8 to "banTime",
            9 to "contacts",
            10 to "blocked",
            11 to "clanName",
            12 to "currentClan",
            13 to "clanReqs",
            14 to "timePlayed",
            15 to "lastLogin",
            16 to "online",
        )
    }
}