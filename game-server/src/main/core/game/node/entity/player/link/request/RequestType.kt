package core.game.node.entity.player.link.request

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.assist.AssistSessionPulse
import core.game.node.entity.player.link.request.trade.TradeModule

/**
 * Represents a type of request that can be made between players.
 *
 * @property message The message displayed when sending the request.
 * @property requestMessage The request command identifier.
 * @property module The request module handling this type of request.
 */
open class RequestType(
    val message: String,
    val requestMessage: String,
    val module: RequestModule,
) {
    /**
     * Determines whether a player can send a request to a target player.
     *
     * @param player The player initiating the request.
     * @param target The target player receiving the request.
     * @return `true` if the request can be sent, otherwise `false`.
     */
    open fun canRequest(
        player: Player?,
        target: Player?,
    ): Boolean = true

    /**
     * Constructs the request message including the target player's username.
     *
     * @param target The target player.
     * @return The formatted request message.
     */
    fun getRequestMessage(target: Player): String = target.username + requestMessage

    companion object {
        /**
         * Trade request type, allowing players to send trade offers.
         */
        val TRADE = RequestType("Sending a trade offer...", ":tradereq:", TradeModule(null, null))

        /**
         * Assist request type, allowing players to request assistance.
         */
        val ASSIST = RequestType("Sending assistance request...", ":assistreq:", AssistSessionPulse())
    }
}