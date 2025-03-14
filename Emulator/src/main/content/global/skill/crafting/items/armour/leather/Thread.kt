package content.global.skill.crafting.items.armour.leather

import core.api.removeItem
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

object Thread {
    @JvmStatic
    fun isLastThread(player: Player): Boolean {
        return getThread(player)?.charge?.let { it >= 1004 } ?: false
    }

    @JvmStatic
    fun decayThread(player: Player) {
        getThread(player)?.let { thread ->
            thread.charge += 1
        }
    }

    @JvmStatic
    fun removeThread(player: Player) {
        if (removeItem(player, Item(Items.THREAD_1734, 1))) {
            sendMessage(player, "You use up one of your reels of thread.")
            getThread(player)?.charge = 1000
        }
    }

    @JvmStatic
    fun getThread(player: Player): Item? {
        return player.inventory[player.inventory.getSlot(Item(Items.THREAD_1734, 1))]
    }
}
