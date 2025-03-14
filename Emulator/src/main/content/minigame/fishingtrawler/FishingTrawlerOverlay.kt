package content.minigame.fishingtrawler

import core.api.sendString
import core.api.setVarp
import core.game.node.entity.player.Player

private const val configIndex = 391
private const val interfaceID = 366
private const val netOkayChild = 27
private const val netRippedChild = 28
private const val fishChild = 29
private const val timeChild = 30

object FishingTrawlerOverlay {
    @JvmStatic
    fun sendUpdate(
        player: Player,
        waterPercent: Int,
        netRipped: Boolean,
        fishCaught: Int,
        timeLeft: Int,
    ) {
        setVarp(player, configIndex, waterPercent)
        player.packetDispatch.sendInterfaceConfig(interfaceID, if (netRipped) netRippedChild else netOkayChild, false)
        player.packetDispatch.sendInterfaceConfig(interfaceID, if (netRipped) netOkayChild else netRippedChild, true)
        sendString(player, "${if (fishCaught > 0) fishCaught else "Nothing"}", interfaceID, fishChild)
        sendString(player, "$timeLeft Minutes", interfaceID, timeChild)
    }
}
