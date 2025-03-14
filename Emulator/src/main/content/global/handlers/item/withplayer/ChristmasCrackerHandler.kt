package content.global.handlers.item.withplayer

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

@Initializable
class ChristmasCrackerHandler : UseWithHandler() {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        addHandler(962, PLAYER_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val target = event.usedWith as Player
        if (target == null ||
            !target.isActive ||
            target.locks.isInteractionLocked ||
            target.interfaceManager.opened != null
        ) {
            event.player.packetDispatch.sendMessage("The other player is currently busy.")
            return true
        }
        if (target.inventory.freeSlots() == 0) {
            event.player.sendMessage("The other player doesn't have enough inventory space.")
            return true
        }
        val player = event.player
        if (player.inventory.remove(event.usedItem)) {
            player.faceTemporary(target, 2)
            player.lock(3)
            player.animate(Animation.create(451))
            player.graphics(Graphics.create(176))
            player.sendMessage("You pull a Christmas cracker...")
            val winner = RandomFunction.random(2) == 1
            player.sendMessage(
                if (winner) "You get the prize from the cracker." else "The person you pull the cracker with gets the prize.",
            )
            if (winner) {
                player.sendChat("Hey! I got the cracker!")
            } else {
                target.sendChat("Hey! I got the cracker!")
            }
            val hat = RandomFunction.getRandomElement(HATS)
            if (winner) {
                player.inventory.add(hat, player)
            } else {
                target.inventory.add(hat, target)
            }
        }
        return true
    }

    companion object {
        private val HATS = arrayOf(Item(1038), Item(1040), Item(1042), Item(1044), Item(1046), Item(1048))
    }
}
