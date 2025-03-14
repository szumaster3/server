package content.global.handlers.item.withplayer

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FieldRationUsageHandler : UseWithHandler() {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        addHandler(7934, PLAYER_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val item = event.used as Item
        val target = event.usedWith as Player
        if (target == null || !target.isActive || target.locks.isInteractionLocked) {
            event.player.packetDispatch.sendMessage("The other player is currently busy.")
            return true
        }
        if (event.player.inventory.remove(item)) {
            target.getSkills().heal(12)
            target.packetDispatch.sendMessage("You have been healed by a field ration.")
            event.player.packetDispatch.sendMessage("You use the field ration to heal the other player.")
            return true
        }
        return false
    }
}
