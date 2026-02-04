package content.global.plugins.iface.tab

import core.api.getAttribute
import core.api.repositionChild
import core.api.sendString
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.tools.DARK_RED
import core.tools.RED
import shared.consts.Components

class GuildTabInterface : InterfaceListener {

    val GUILD_LEVEL = "guild:guild-tier"
    val GUILD_CONTRIBUTION = "guild:contribution"
    val GUILD_ACTIVE_BONUS = "guild:active-bonus"

    override fun defineInterfaceListeners() {

        onOpen(Components.GUILD_V1_834) { player, _ ->
            sendString(player, "", Components.GUILD_V1_834, 24)
            sendString(player, "", Components.GUILD_V1_834, 25)
            sendString(player, "", Components.GUILD_V1_834, 26)
            sendString(player, "", Components.GUILD_V1_834, 27)
            sendString(player, "", Components.GUILD_V1_834, 28)
            sendString(player, "", Components.GUILD_V1_834, 29)
            sendString(player, "", Components.GUILD_V1_834, 30)
            sendString(player, "", Components.GUILD_V1_834, 31)
            sendString(player, "", Components.GUILD_V1_834, 32)
            return@onOpen true
        }

        on(Components.GUILD_V1_834) { player, _, _, buttonID, _, _ ->
            if(buttonID == 1) {
                player.interfaceManager.openTab(2, Component(Components.QUESTJOURNAL_V2_274))
            } else if(buttonID == 2) {
                player.interfaceManager.openTab(2, Component(Components.AREA_TASK_259))
            }
            return@on true
        }
    }
}