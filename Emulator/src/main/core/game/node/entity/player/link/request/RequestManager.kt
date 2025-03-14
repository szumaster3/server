package core.game.node.entity.player.link.request

import core.game.bots.AIRepository
import core.game.bots.impl.DoublingMoney
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks

class RequestManager(
    val player: Player,
) {
    var target: Player? = null
        private set

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

        // If the target is a bot, get their bot manager to handle the request.
        if (target.isArtificial) {
            val bot = AIRepository.PulseRepository[target.username.lowercase()]
            if (bot != null && bot.botScript is DoublingMoney) {
                bot.botScript.tradeReceived(player)
            }
        }
        return true
    }

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
        if (!target.isActive || target.interfaceManager.isOpened) {
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

    private fun close(player: Player) {
        player.dialogueInterpreter.close()
        player.interfaceManager.close()
        player.interfaceManager.closeChatbox()
    }

    fun clear() {
        target = null
    }
}
