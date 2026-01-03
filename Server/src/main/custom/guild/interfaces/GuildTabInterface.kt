package custom.guild.interfaces

import core.api.getAttribute
import core.api.repositionChild
import core.api.sendString
import core.game.component.Component
import core.game.interaction.InterfaceListener
import custom.guild.GuildGlobalAttributes
import shared.consts.Components

class GuildTabInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        onOpen(Components.GUILD_V1_834) { player, _ ->
            repositionChild(player, Components.GUILD_V1_834, 24, 16, 35)
            sendString(player, "Guild level: ${getAttribute(player, GuildGlobalAttributes.GUILD_LEVEL, 0)}", Components.GUILD_V1_834, 24)

            repositionChild(player, Components.GUILD_V1_834, 25, 16, 59)
            sendString(player, "Contribution: ${getAttribute(player, GuildGlobalAttributes.GUILD_CONTRIBUTION, 0)}", Components.GUILD_V1_834, 25)

            repositionChild(player, Components.GUILD_V1_834, 26, 16, 83)
            sendString(player, "Enemy kills: ${getAttribute(player, GuildGlobalAttributes.GUILD_ENEMY_KILLS, 0)}", Components.GUILD_V1_834, 26)

            repositionChild(player, Components.GUILD_V1_834, 27, 16, 107)
            sendString(player, "Active bonuses: ${getAttribute(player, GuildGlobalAttributes.GUILD_ACTIVE_BONUS, 0)}", Components.GUILD_V1_834, 27)

            repositionChild(player, Components.GUILD_V1_834, 28, 16, 131)
            sendString(player, "Active Tasks", Components.GUILD_V1_834, 28)

            repositionChild(player, Components.GUILD_V1_834, 29, 16, 155)
            sendString(player, "Talents", Components.GUILD_V1_834, 29)

            repositionChild(player, Components.GUILD_V1_834, 30, 16, 179)
            sendString(player, "Read guide", Components.GUILD_V1_834, 30)

            repositionChild(player, Components.GUILD_V1_834, 31, 16, 203)
            sendString(player, "Guild rank: ${getAttribute(player, GuildGlobalAttributes.GUILD_PLAYER_RANK, 0)}", Components.GUILD_V1_834, 31)

            repositionChild(player, Components.GUILD_V1_834, 32, 16, 227)
            sendString(player, "Siege victory: ${getAttribute(player, GuildGlobalAttributes.GUILD_SIEGE, 0)}", Components.GUILD_V1_834, 32)

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