package core.auth

import java.sql.Timestamp

/**
 * Represents a user's account information and state data.
 */
class UserAccountInfo(var username: String, var password: String, var uid: Int, var rights: Int, var credits: Int, var ip: String, var lastUsedIp: String, var muteEndTime: Long, var banEndTime: Long, var contacts: String, var blocked: String, var clanName: String, var currentClan: String, var clanReqs: String, var timePlayed: Long, var lastLogin: Long, var online: Boolean, var joinDate: Timestamp) {
    companion object {
        /**
         * The reference instance used for comparisons.
         */
        val default = createDefault()

        /**
         * Creates a new account with default values.
         *
         * @return A [UserAccountInfo] instance initialized with safe defaults.
         */
        @JvmStatic
        fun createDefault(): UserAccountInfo =
            UserAccountInfo(username = "", password = "", uid = 0, rights = 0, credits = 0, ip = "", lastUsedIp = "", muteEndTime = 0L, banEndTime = 0L, contacts = "", blocked = "", clanName = "", currentClan = "", clanReqs = "1,0,8,9", timePlayed = 0L, lastLogin = 0L, online = false, joinDate = Timestamp(System.currentTimeMillis())).also {
                it.setInitialReferenceValues()
            }
    }

    /**
     * Snapshot of field values for change detection.
     */
    lateinit var initialValues: Array<Any>

    /**
     * Stores the current field values for later comparison.
     */
    fun setInitialReferenceValues() {
        initialValues = toArray()
    }

    /**
     * Gets a list of indices for changed fields and the current field values.
     *
     * @return A pair of (changed field indices, current values).
     */
    fun getChangedFields(): Pair<ArrayList<Int>, Array<Any>> {
        val current = toArray()
        val changed = ArrayList<Int>()

        for (i in current.indices) {
            if (current[i] != initialValues[i]) changed.add(i)
        }

        return Pair(changed, current)
    }

    /**
     * Converts all user fields into an array for quick iteration and comparison.
     *
     * @return An [Array] of all stored field values.
     */
    fun toArray(): Array<Any> =
        arrayOf(username, password, uid, rights, credits, ip, lastUsedIp, muteEndTime, banEndTime, contacts, blocked, clanName, currentClan, clanReqs, timePlayed, lastLogin, online, joinDate)

    override fun toString(): String = "USER:$username,PASS:$password,UID:$uid,RIGHTS:$rights,CREDITS:$credits,IP:$ip,LASTIP:$lastUsedIp"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserAccountInfo

        if (username != other.username) return false
        if (password != other.password) return false
        if (uid != other.uid) return false
        if (rights != other.rights) return false
        if (credits != other.credits) return false
        if (ip != other.ip) return false
        if (lastUsedIp != other.lastUsedIp) return false
        if (muteEndTime != other.muteEndTime) return false
        if (banEndTime != other.banEndTime) return false
        if (contacts != other.contacts) return false
        if (blocked != other.blocked) return false
        if (clanName != other.clanName) return false
        if (currentClan != other.currentClan) return false
        if (clanReqs != other.clanReqs) return false
        if (timePlayed != other.timePlayed) return false
        if (lastLogin != other.lastLogin) return false
        if (online != other.online) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + uid
        result = 31 * result + rights
        result = 31 * result + credits
        result = 31 * result + ip.hashCode()
        result = 31 * result + lastUsedIp.hashCode()
        result = 31 * result + muteEndTime.hashCode()
        result = 31 * result + banEndTime.hashCode()
        result = 31 * result + contacts.hashCode()
        result = 31 * result + blocked.hashCode()
        result = 31 * result + clanName.hashCode()
        result = 31 * result + currentClan.hashCode()
        result = 31 * result + clanReqs.hashCode()
        result = 31 * result + timePlayed.hashCode()
        result = 31 * result + lastLogin.hashCode()
        result = 31 * result + online.hashCode()
        return result
    }

    /**
     * Checks whether this account matches the default (uninitialized) state.
     *
     * @return `true` if all fields are equal to the default instance.
     */
    fun isDefault(): Boolean = this == default
}
