package core.auth

import core.ServerConstants
import core.game.node.entity.player.Player
import core.storage.AccountStorageProvider

class DevelopmentAuthenticator : AuthProvider<AccountStorageProvider>() {
    override fun configureFor(provider: AccountStorageProvider) {
        storageProvider = provider
    }

    override fun checkLogin(
        username: String,
        password: String,
    ): Pair<AuthResponse, UserAccountInfo?> {
        val info: UserAccountInfo
        if (!storageProvider.checkUsernameTaken(username.lowercase())) {
            info = UserAccountInfo.createDefault()
            info.username = username
            createAccountWith(info)
        } else {
            info = storageProvider.getAccountInfo(username.lowercase())
        }
        return Pair(AuthResponse.Success, storageProvider.getAccountInfo(username))
    }

    override fun createAccountWith(info: UserAccountInfo): Boolean {
        info.username = info.username.lowercase()
        if (ServerConstants.NOAUTH_DEFAULT_ADMIN) info.rights = 2
        storageProvider.store(info)
        return true
    }

    override fun checkPassword(
        player: Player,
        password: String,
    ): Boolean {
        return password == player.details.password
    }

    override fun updatePassword(
        username: String,
        newPassword: String,
    ) {
        val info = storageProvider.getAccountInfo(username)
        info.password = newPassword
        storageProvider.update(info)
    }
}
