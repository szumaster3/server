package core.auth

import core.game.node.entity.player.Player
import core.storage.AccountStorageProvider

/**
 * Abstract class for authenticators that manage account-related operations.
 *
 * This class provides a base for various types of authenticators that can work with
 * different account storage providers. It defines common methods for creating accounts,
 * checking logins, validating passwords, and updating passwords, while allowing specific
 * behavior to be defined by subclasses.
 *
 * @param T The type of [AccountStorageProvider] that this authenticator will use for
 * storage operations (e.g., SQL or in-memory storage).
 */
abstract class AuthProvider<T : AccountStorageProvider> {
    /**
     * The storage provider used for managing account data.
     * This is typically initialized by calling [configureFor].
     */
    protected lateinit var storageProvider: T

    /**
     * Configures the authenticator to use a specific [AccountStorageProvider].
     * This method is abstract and must be implemented by subclasses to set up the
     * storage provider.
     *
     * @param provider The storage provider to configure the authenticator with.
     */
    abstract fun configureFor(provider: T)

    /**
     * Checks whether an account can be created with the given account information.
     * This is done by checking whether the username is already taken.
     *
     * @param info The information for the account to be created, including the username.
     * @return `true` if the username is not taken and the account can be created, `false` otherwise.
     */
    fun canCreateAccountWith(info: UserAccountInfo): Boolean = !storageProvider.checkUsernameTaken(info.username)

    /**
     * Creates a new account with the provided user account information.
     * This method is abstract and must be implemented by subclasses to define
     * how the account is created based on the provided information.
     *
     * @param info The information for the account to be created.
     * @return `true` if the account was successfully created, `false` otherwise.
     */
    abstract fun createAccountWith(info: UserAccountInfo): Boolean

    /**
     * Checks if the provided username and password are valid for logging in.
     * This method is abstract and must be implemented by subclasses to define
     * how login verification is done.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return A pair where the first value is the [AuthResponse] indicating the result of the login attempt,
     *         and the second value is a [UserAccountInfo] object containing the account information if the login is successful.
     */
    abstract fun checkLogin(
        username: String,
        password: String,
    ): Pair<AuthResponse, UserAccountInfo?>

    /**
     * Validates the password for the given player.
     * This method is abstract and must be implemented by subclasses to define how password verification is done.
     *
     * @param player The player whose password is to be checked.
     * @param password The password to validate.
     * @return `true` if the password is correct, `false` otherwise.
     */
    abstract fun checkPassword(
        player: Player,
        password: String,
    ): Boolean

    /**
     * Updates the password for the specified username.
     * This method is abstract and must be implemented by subclasses to define
     * how the password update process is handled.
     *
     * @param username The username for which the password should be updated.
     * @param newPassword The new password to set for the account.
     */
    abstract fun updatePassword(
        username: String,
        newPassword: String,
    )
}
