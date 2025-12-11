package core.game.node.entity.player.info.login

import core.api.LoginListener
import core.api.reinitVarps
import core.auth.AuthResponse
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.system.SystemManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.GameWorld.loginListeners
import core.game.world.repository.Repository
import java.util.function.Consumer

/**
 * Handles the login process for a player.
 *
 * @property details The details of the player attempting to log in.
 */
class LoginParser(val details: PlayerDetails) {
    /**
     * Initializes the login process.
     *
     * @param player The player instance being logged in.
     * @param reconnect Whether the player is reconnecting to an active session.
     */
    fun initialize(player: Player, reconnect: Boolean) {
        if (!validateRequest()) return

        lateinit var parser: PlayerSaveParser

        try {
            parser = PlayerParser.parse(player)
                ?: throw IllegalStateException("Failed parsing save for: " + player.username) // Handle parsing failure
        } catch (e: Exception) {
            e.printStackTrace()
            Repository.removePlayer(player)
            flag(AuthResponse.ErrorLoadingProfile)
        }

        GameWorld.Pulser.submit(
            object : Pulse(1) {
                override fun pulse(): Boolean {
                    try {
                        if (details.session.isActive()) {
                            player.properties.spawnLocation = player.getRespawnLocation()
                            loginListeners.forEach(Consumer { listener: LoginListener -> listener.login(player) }) // Execute login hooks
                            parser.runContentHooks()
                            player.details.session.setObject(player)

                            if (reconnect) {
                                reconnect(player)
                            } else {
                                flag(AuthResponse.Success)
                                player.init()
                                reinitVarps(player)
                            }
                        } else {
                            Repository.removePlayer(player)
                        }
                    } catch (t: Throwable) {
                        t.printStackTrace()
                        Repository.removePlayer(player)
                        flag(AuthResponse.ErrorLoadingProfile)
                    }
                    return true
                }
            },
        )
    }

    /**
     * Handles the reconnection process.
     *
     * @param player The player instance reconnecting.
     */
    private fun reconnect(player: Player) {
        Repository.disconnectionQueue.remove(details.username)
        player.initReconnect()
        player.isActive = true
        flag(AuthResponse.Success)
        player.updateSceneGraph(true)
        reinitVarps(player)
        LoginConfiguration.configureGameWorld(player)
    }

    /**
     * Validates whether the login request should proceed.
     *
     * @return `true` if the request is valid, otherwise `false`.
     */
    private fun validateRequest(): Boolean {
        if (!details.session.isActive()) {
            return false
        }
        if (SystemManager.isUpdating) {
            return flag(AuthResponse.Updating)
        }
        return if (details.isBanned) {
            flag(AuthResponse.AccountDisabled)
        } else {
            true
        }
    }

    /**
     * Sends an authentication response to the player's session.
     *
     * @param response The authentication response to be sent.
     * @return `true` if the response is `AuthResponse.Success`, otherwise `false`.
     */
    fun flag(response: AuthResponse): Boolean {
        details.session.write(response, true)
        return response == AuthResponse.Success
    }
}