package content.global.plugins.iface.tab

import core.api.getAttribute
import core.api.repositionChild
import core.api.sendString
import core.game.component.Component
import core.game.interaction.InterfaceListener
import shared.consts.Components

class GuildTabInterface : InterfaceListener {
    val GUILD_LEVEL = "guild:guild-level"
    val GUILD_CONTRIBUTION = "guild:contribution"
    val GUILD_ENEMY_KILLS = "guild:enemy-kills"
    val GUILD_ACTIVE_BONUS = "guild:active-bonus"
    val GUILD_PLAYER_RANK = "guild:player-rank"
    val GUILD_SIEGE = "guild:siege-victories"

    override fun defineInterfaceListeners() {

        onOpen(Components.GUILD_V1_834) { player, _ ->
            sendString(player, "Guild level: ${getAttribute(player, GUILD_LEVEL, 0)}", Components.GUILD_V1_834, 24)
            sendString(player, "Contribution: ${getAttribute(player, GUILD_CONTRIBUTION, 0)}", Components.GUILD_V1_834, 25)
            sendString(player, "Enemy kills: ${getAttribute(player, GUILD_ENEMY_KILLS, 0)}", Components.GUILD_V1_834, 26)
            sendString(player, "Active bonuses: ${getAttribute(player, GUILD_ACTIVE_BONUS, 0)}", Components.GUILD_V1_834, 27)
            sendString(player, "Active Tasks", Components.GUILD_V1_834, 28)
            sendString(player, "Talents", Components.GUILD_V1_834, 29)
            sendString(player, "Read guide", Components.GUILD_V1_834, 30)
            sendString(player, "Guild rank: ${getAttribute(player, GUILD_PLAYER_RANK, 0)}", Components.GUILD_V1_834, 31)
            sendString(player, "Siege victory: ${getAttribute(player, GUILD_SIEGE, 0)}", Components.GUILD_V1_834, 32)
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