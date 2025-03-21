package core.auth

import core.ServerConstants
import core.storage.AccountStorageProvider
import core.storage.InMemoryStorageProvider
import core.storage.SQLStorageProvider

/**
 * The Auth object handles the configuration of the authentication system and the storage provider.
 * It initializes the appropriate storage provider and authenticator based on the configuration
 * constants defined in [ServerConstants].
 */
object Auth {
    /**
     * The authenticator that is responsible for authenticating users.
     * The type of authenticator is determined by the configuration in [ServerConstants].
     */
    lateinit var authenticator: AuthProvider<*>

    /**
     * The storage provider used to manage account data.
     * This will either be a persistent storage solution (SQL) or an in-memory solution,
     * based on the configuration in [ServerConstants].
     */
    lateinit var storageProvider: AccountStorageProvider

    /**
     * Configures the authentication system based on the current server constants.
     * The method sets up the storage provider and authenticator based on whether account persistence
     * is enabled or not, and whether authentication is required.
     *
     * - If [ServerConstants.PERSIST_ACCOUNTS] is true, an [SQLStorageProvider] is used.
     * - If [ServerConstants.PERSIST_ACCOUNTS] is false, an [InMemoryStorageProvider] is used.
     * - If [ServerConstants.USE_AUTH] is true, a [ProductionAuthenticator] is used.
     * - If [ServerConstants.USE_AUTH] is false, a [DevelopmentAuthenticator] is used.
     */
    fun configure() {
        storageProvider =
            if (ServerConstants.PERSIST_ACCOUNTS) {
                // Use SQL storage provider for persistent account data.
                SQLStorageProvider()
            } else {
                // Use in-memory storage if persistence is not required.
                InMemoryStorageProvider()
            }

        // Initialize the authenticator based on the authentication configuration.
        authenticator =
            if (ServerConstants.USE_AUTH) {
                // Use the production authenticator when authentication is enabled.
                ProductionAuthenticator().also { it.configureFor(storageProvider) }
            } else {
                // Use the development authenticator when authentication is not required.
                DevelopmentAuthenticator().also { it.configureFor(storageProvider) }
            }
    }
}
