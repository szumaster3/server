package core.storage

import core.auth.UserAccountInfo

/**
 * Interface for user account storage.
 */
interface AccountStorageProvider {

    /** Returns true if the username exists. */
    fun checkUsernameTaken(username: String): Boolean

    /** Returns account info for the given username. */
    fun getAccountInfo(username: String): UserAccountInfo

    /** Returns usernames associated with the given IP. */
    fun getUsernamesWithIP(ip: String): List<String>

    /** Stores account info. */
    fun store(info: UserAccountInfo)

    /** Updates account info. */
    fun update(info: UserAccountInfo)

    /** Removes account info. */
    fun remove(info: UserAccountInfo)

    /** Returns a list of online friends. */
    fun getOnlineFriends(username: String): List<String>
}
