package content.global.handlers.iface

import core.api.closeInterface
import core.api.runTask
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.info.login.LoginConfiguration
import org.rs.consts.Components

class LoginInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.WELCOME_SCREEN_378) { player, _, _, buttonID, _, _ ->
            val playButton = 140
            val creditButton = 145
            val messageCentre = 204

            when (buttonID) {
                playButton -> {
                    player.locks.lock("login", 2)
                    closeInterface(player)
                    runTask(player, 1, 0) {
                        LoginConfiguration.configureGameWorld(player)
                    }
                }

                creditButton -> return@on true
                messageCentre -> return@on true
            }
            return@on true
        }

        onClose(Components.WELCOME_SCREEN_378) { player, _ ->
            return@onClose player.locks.isLocked("login")
        }
    }
}
