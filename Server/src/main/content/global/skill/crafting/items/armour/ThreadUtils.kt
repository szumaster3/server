package content.global.skill.crafting.items.armour

import core.api.removeItem
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

object ThreadUtils {

    /**
     * Checks if the player has a thread charged at or above 1004.
     */
    @JvmStatic
    fun isLastThread(player: Player): Boolean = getThread(player)?.charge?.let { it >= 1004 } ?: false

    /**
     * Increases the thread’s charge by 1 if present.
     */
    @JvmStatic
    fun decayThread(player: Player) {
        getThread(player)?.let { thread ->
            thread.charge += 1
        }
    }

    /**
     * Removes one thread item from player inventory and sets charge to 1000.
     */
    @JvmStatic
    fun removeThread(player: Player) {
        if (removeItem(player, Item(Items.THREAD_1734, 1))) {
            sendMessage(player, "You use up one of your reels of thread.")
            getThread(player)?.charge = 1000
        }
    }

    /**
     * Gets the thread item from player inventory, or null if not found.
     */
    @JvmStatic
    fun getThread(player: Player): Item? = player.inventory[player.inventory.getSlot(Item(Items.THREAD_1734, 1))]
}