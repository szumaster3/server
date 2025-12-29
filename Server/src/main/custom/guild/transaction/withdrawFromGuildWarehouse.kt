package custom.guild.transaction

import core.api.addItem
import core.game.node.entity.player.Player

fun withdrawFromGuildWarehouse(player: Player, itemId: Int, amount: Int) {
    val guild = player.guild ?: return
    val rank = guild.members[player.index] ?: return

    val item = guild.warehouse.withdraw(player, itemId, amount) ?: return
    addItem(player, item.id, item.amount)
}