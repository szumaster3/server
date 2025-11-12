package core.game.node.entity.player.link.request

import content.global.bots.DoublingMoney
import core.game.bots.AIRepository
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks

/**
 * Manages player-to-player request interactions, such as trade requests.
 * Handles request validation, initiation, and acceptance logic.
 *
 * @property player The player sending the request.
 */
class RequestManager(
    val player: Player,
) {
    /**
     * The target player receiving the request, if any.
     */
    var target: Player? = null
        private set

    /**
     * Sends a request to another player if conditions allow it.
     *
     * @param target The player receiving the request.
     * @param type The type of request being sent.
     * @return `true` if the request was sent successfully, `false` otherwise.
     */
    fun request(
        target: Player,
        type: RequestType,
    ): Boolean {
        if (!canRequest(type, target)) {
            return false
        }
        if (acceptExisting(target, type)) {
            return true
        }
        player.packetDispatch.sendMessage(type.message)
        target.packetDispatch.sendMessage(type.getRequestMessage(player))
        player.setAttribute("lastRequest", type)
        this.target = target

        // If the target is an AI bot, delegate handling to its bot script.
        if (target.isArtificial) {
            val bot = AIRepository.PulseRepository[target.username.lowercase()]
            if (bot != null && bot.botScript is DoublingMoney) {
                bot.botScript.tradeReceived(player)
            }
        }
        return true
    }

    /**
     * Checks if the request can be sent based on various conditions.
     *
     * @param type The type of request being sent.
     * @param target The player receiving the request.
     * @return `true` if the request is allowed, `false` otherwise.
     */
    private fun canRequest(
        type: RequestType,
        target: Player,
    ): Boolean {
        if (target === player) {
            return false
        }
        if (!target.location.withinDistance(player.location, 15)) {
            player.packetDispatch.sendMessage("Unable to find " + target.username + ".")
            return false
        }
        if (!target.isActive || target.interfaceManager.isOpened()) {
            player.packetDispatch.sendMessage("Other player is busy at the moment.")
            return false
        }
        if (target.getAttribute("busy", 0) > ticks || player.getAttribute("busy", 0) > ticks) {
            player.packetDispatch.sendMessage("Other player is busy at the moment.")
            return false
        }
        return if (!player.zoneMonitor.canRequest(type, target)) {
            false
        } else {
            type.canRequest(player, target)
        }
    }

    /**
     * Checks if a request of the same type already exists between the two players.
     * If it does, it finalizes the request process immediately.
     *
     * @param target The player receiving the request.
     * @param type The type of request being sent.
     * @return `true` if the existing request was accepted, `false` otherwise.
     */
    private fun acceptExisting(
        target: Player,
        type: RequestType,
    ): Boolean {
        val lastType = target.getAttribute<RequestType>("lastRequest", null)
        if (lastType === type && player === target.requestManager.target) {
            close(player)
            clear()
            target.requestManager.clear()
            player.setAttribute("busy", ticks + 2)
            target.setAttribute("busy", ticks + 2)
            type.module.open(player, target)
            return true
        }
        close(player)
        return false
    }

    /**
     * Closes any open dialogues or interfaces related to the request.
     *
     * @param player The player whose interfaces should be closed.
     */
    private fun close(player: Player) {
        player.dialogueInterpreter.close()
        player.interfaceManager.close()
        player.interfaceManager.closeChatbox()
    }

    /**
     * Clears the current request target, resetting the manager state.
     */
    fun clear() {
        target = null
    }
}