package core.auth

import core.ServerConstants
import core.game.node.entity.player.Player
import core.storage.AccountStorageProvider

/**
 * The [DevelopmentAuthenticator] is an implementation of [AuthProvider] that simulates the authentication process
 * for development or testing purposes. It provides basic functionality like checking login credentials, creating accounts,
 * and updating passwords without any advanced authentication mechanisms.
 *
 * This class is used when authentication is not required (e.g., in a development environment or when testing).
 */
class DevelopmentAuthenticator : AuthProvider<AccountStorageProvider>() {
    /**
     * Configures the authenticator to use the specified [AccountStorageProvider].
     *
     * This method sets the [storageProvider] to the given provider, which is used for accessing and manipulating account data.
     *
     * @param provider The storage provider to configure the authenticator with.
     */
    override fun configureFor(provider: AccountStorageProvider) {
        storageProvider = provider
    }

    /**
     * Checks if the provided username and password are valid for logging in.
     * If the username is not taken, a new account is created with default information.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return A pair where the first value is the [AuthResponse.Success] indicating a successful login,
     *         and the second value is the [UserAccountInfo] of the user associated with the given username.
     */
    override fun checkLogin(
        username: String,
        password: String,
    ): Pair<AuthResponse, UserAccountInfo?> {
        val info: UserAccountInfo
        // If the username is not already taken, create a new account with default information.
        if (!storageProvider.checkUsernameTaken(username.lowercase())) {
            info = UserAccountInfo.createDefault()
            info.username = username
            createAccountWith(info)
        } else {
            // If the username exists, retrieve the account information.
            info = storageProvider.getAccountInfo(username.lowercase())
        }
        // Return successful authentication along with the user's account info.
        return Pair(AuthResponse.Success, storageProvider.getAccountInfo(username))
    }

    /**
     * Creates a new account with the provided [UserAccountInfo].
     * If the account is being created for an admin (as specified by [ServerConstants.NOAUTH_DEFAULT_ADMIN]),
     * the account will be given admin rights.
     *
     * @param info The information for the account to be created, including the username and other details.
     * @return `true` if the account was successfully created and stored, `false` otherwise.
     */
    override fun createAccountWith(info: UserAccountInfo): Boolean {
        info.username = info.username.lowercase() // Normalize the username to lowercase.
        // If the default admin setting is enabled, assign admin rights to the user.
        if (ServerConstants.NOAUTH_DEFAULT_ADMIN) info.rights = 2
        // Store the created account in the storage provider.
        storageProvider.store(info)
        return true
    }

    /**
     * Checks if the provided password matches the password stored for the given player.
     *
     * @param player The player whose password is to be checked.
     * @param password The password to validate.
     * @return `true` if the password matches, `false` otherwise.
     */
    override fun checkPassword(
        player: Player,
        password: String,
    ): Boolean = password == player.details.password

    /**
     * Updates the password for the specified username.
     *
     * @param username The username for which the password should be updated.
     * @param newPassword The new password to set for the account.
     */
    override fun updatePassword(
        username: String,
        newPassword: String,
    ) {
        // Retrieve the user's account info and update the password.
        val info = storageProvider.getAccountInfo(username)
        info.password = newPassword
        storageProvider.update(info)
    }
}
