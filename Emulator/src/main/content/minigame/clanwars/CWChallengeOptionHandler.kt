package content.minigame.clanwars

import core.game.activity.ActivityManager
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestModule
import core.game.node.entity.player.link.request.RequestType
import core.game.system.communication.ClanRank
import core.plugin.Plugin

class CWChallengeOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        OPTION.setHandler(this)
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        player.requestManager.request((node as Player), REQUEST_TYPE)
        return true
    }

    companion object {
        val OPTION = Option("Challenge", 0)
        private val REQUEST_TYPE: RequestType =
            object : RequestType("Sending challenge request...", ":clanreq:", CWChallengeOptionHandler.module) {
                override fun canRequest(
                    player: Player?,
                    target: Player?,
                ): Boolean {
                    if (player!!.communication.clan == null) {
                        player.packetDispatch.sendMessage("You have to be in a clan to challenge players.")
                        return false
                    }
                    if (player.communication.clan
                            .getRank(player)
                            .ordinal < ClanRank.CAPTAIN.ordinal
                    ) {
                        player.packetDispatch.sendMessage("Your clan rank is not high enough to challenge other clans.")
                        return false
                    }
                    if (player.communication.clan.clanWar != null) {
                        player.packetDispatch.sendMessage("Your clan is already in a war.")
                        return false
                    }
                    if (target!!.communication.clan == null) {
                        player.packetDispatch.sendMessage("This player is not in a clan.")
                        return false
                    }
                    if (target.communication.clan
                            .getRank(target)
                            .ordinal < ClanRank.CAPTAIN.ordinal
                    ) {
                        player.packetDispatch.sendMessage(
                            "This player's clan rank is not high enough to accept challenges.",
                        )
                        return false
                    }
                    if (target.communication.clan.clanWar != null) {
                        player.packetDispatch.sendMessage("This player's clan is already in a war.")
                        return false
                    }
                    if (target.communication.clan == player.communication.clan) {
                        player.packetDispatch.sendMessage("You can't challenge someone from your own clan.")
                        return false
                    }
                    return true
                }
            }
        private val module: RequestModule
            private get() =
                object : RequestModule {
                    override fun open(
                        player: Player?,
                        target: Player?,
                    ) {
                        ActivityManager.start(player, "Clan wars", false, target)
                    }
                }
    }
}
