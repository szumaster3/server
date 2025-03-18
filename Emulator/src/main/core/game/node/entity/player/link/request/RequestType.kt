package core.game.node.entity.player.link.request

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.assist.AssistSession
import core.game.node.entity.player.link.request.trade.TradeModule

open class RequestType(
    val message: String,
    val requestMessage: String,
    val module: RequestModule,
) {
    open fun canRequest(
        player: Player?,
        target: Player?,
    ): Boolean = true

    fun getRequestMessage(target: Player): String = target.username + requestMessage

    companion object {
        val TRADE = RequestType("Sending a trade offer...", ":tradereq:", TradeModule(null, null))

        val ASSIST = RequestType("Sending assistance request...", ":assistreq:", AssistSession())
    }
}
