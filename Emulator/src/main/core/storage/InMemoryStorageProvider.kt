package core.storage

import core.auth.UserAccountInfo

/**
 * In-memory implementation of [AccountStorageProvider].
 * Stores data temporarily using a HashMap.
 */
class InMemoryStorageProvider : AccountStorageProvider {
    private val storage = HashMap<String, UserAccountInfo>()

    /** Returns true if the username exists. */
    override fun checkUsernameTaken(username: String): Boolean = storage[username] != null

    /** Gets or creates default account info for the username. */
    override fun getAccountInfo(username: String): UserAccountInfo =
        storage[username] ?: UserAccountInfo
            .createDefault()
            .also {
                it.uid = username.hashCode()
                storage[username] = it
            }

    /** Stores account info and assigns UID. */
    override fun store(info: UserAccountInfo) {
        info.uid = info.username.hashCode()
        storage[info.username] = info
    }

    /** Updates existing account info. */
    override fun update(info: UserAccountInfo) {
        storage[info.username] = info
    }

    /** Removes the given account. */
    override fun remove(info: UserAccountInfo) {
        storage.remove(info.username)
    }

    /** Returns an empty list of online friends. */
    override fun getOnlineFriends(username: String): List<String> = ArrayList()

    /** Returns an empty list of usernames for the given IP. */
    override fun getUsernamesWithIP(ip: String): List<String> = ArrayList()
}