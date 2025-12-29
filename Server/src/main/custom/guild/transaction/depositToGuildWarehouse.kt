package custom.guild.transaction

import core.api.addItem
import core.api.removeItem
import core.game.node.entity.player.Player
import core.game.node.item.Item

fun depositToGuildWarehouse(player: Player, itemId: Int, amount: Int) {
    val guild = player.guild ?: return
    val removed = removeItem(player, Item(itemId, amount))
    if (!removed) return

    val left = guild.warehouse.deposit(Item(itemId, amount))
    if (left > 0) {
        addItem(player, itemId, left)
    }
}