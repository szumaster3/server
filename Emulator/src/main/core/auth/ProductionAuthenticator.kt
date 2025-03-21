package core.auth

import core.ServerConstants
import core.game.node.entity.player.Player
import core.game.system.SystemManager
import core.storage.AccountStorageProvider
import core.storage.SQLStorageProvider
import java.sql.SQLDataException
import java.sql.Timestamp

/**
 * The [ProductionAuthenticator] is an implementation of [AuthProvider] that handles production-level authentication.
 * It interacts with a storage provider (typically an [SQLStorageProvider]) to manage account information and perform
 * actions like creating accounts, checking login credentials, and updating passwords.
 *
 * This class is designed for a live or production environment where security and persistence of user data are essential.
 */
class ProductionAuthenticator : AuthProvider<AccountStorageProvider>() {

    /**
     * Configures the authenticator to use the specified [AccountStorageProvider] and sets up the connection for
     * the database if the provider is an instance of [SQLStorageProvider].
     *
     * @param provider The storage provider to configure the authenticator with.
     */
    override fun configureFor(provider: AccountStorageProvider) {
        storageProvider = provider
        // If the provider is a SQLStorageProvider, configure the database connection.
        if (provider is SQLStorageProvider) {
            provider.configure(
                ServerConstants.DATABASE_ADDRESS!!,
                ServerConstants.DATABASE_NAME!!,
                ServerConstants.DATABASE_USER!!,
                ServerConstants.DATABASE_PASS!!,
            )
        }
    }

    /**
     * Creates a new user account with the provided [UserAccountInfo].
     * The password is encrypted before storing the account information in the storage provider.
     * The account's join date is set to the current timestamp.
     *
     * @param info The information for the account to be created, including the username and password.
     * @return `true` if the account was successfully created and stored, `false` otherwise.
     */
    override fun createAccountWith(info: UserAccountInfo): Boolean {
        try {
            // Encrypt the user's password before storing.
            info.password = SystemManager.encryption.hashPassword(info.password)
            // Set the account's join date to the current time.
            info.joinDate = Timestamp(System.currentTimeMillis())
            // Store the account in the provider's storage.
            storageProvider.store(info)
        } catch (e: SQLDataException) {
            // Handle SQL-specific exceptions if the account creation fails.
            return false
        } catch (e: Exception) {
            // Log any other exceptions and return false.
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * Checks the login credentials (username and password) against the stored user data.
     * The username is converted to lowercase, and the password is checked using encryption.
     * If the account is disabled or already logged in, the login attempt will fail.
     *
     * @param username The username to check.
     * @param password The password to validate.
     * @return A pair containing an [AuthResponse] status and the associated [UserAccountInfo] if the login is successful.
     */
    override fun checkLogin(
        username: String,
        password: String,
    ): Pair<AuthResponse, UserAccountInfo?> {
        val info: UserAccountInfo
        try {
            // Check if the username exists in the storage provider.
            if (!storageProvider.checkUsernameTaken(username.lowercase())) {
                return Pair(AuthResponse.InvalidCredentials, null)
            }
            // Retrieve account information for the given username.
            info = storageProvider.getAccountInfo(username.lowercase())
            // Verify the password using encryption.
            val passCorrect = SystemManager.encryption.checkPassword(password, info.password)
            if (!passCorrect || info.password.isEmpty()) return Pair(AuthResponse.InvalidCredentials, null)
            // Check if the account is disabled or banned.
            if (info.banEndTime > System.currentTimeMillis()) return Pair(AuthResponse.AccountDisabled, null)
            // Check if the user is already online.
            if (info.online) return Pair(AuthResponse.AlreadyOnline, null)
        } catch (e: Exception) {
            // Handle any exceptions during the login check process.
            e.printStackTrace()
            return Pair(AuthResponse.CouldNotLogin, null)
        }
        // Return success and the user account information if the login is valid.
        return Pair(AuthResponse.Success, info)
    }

    /**
     * Validates the password for the given player using encrypted password checking.
     *
     * @param player The player whose password is to be checked.
     * @param password The password to validate.
     * @return `true` if the password matches the encrypted password, `false` otherwise.
     */
    override fun checkPassword(
        player: Player,
        password: String,
    ): Boolean = SystemManager.encryption.checkPassword(password, player.details.password)

    /**
     * Updates the password for the specified username.
     * The new password is encrypted before being stored.
     *
     * @param username The username for which the password should be updated.
     * @param newPassword The new password to set for the account.
     */
    override fun updatePassword(
        username: String,
        newPassword: String,
    ) {
        // Retrieve the user's account info and update the password with the encrypted version.
        val info = storageProvider.getAccountInfo(username)
        info.password = SystemManager.encryption.hashPassword(newPassword)
        // Update the account in the storage provider.
        storageProvider.update(info)
    }
}
