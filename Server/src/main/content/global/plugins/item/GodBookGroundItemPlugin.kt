package content.global.plugins.item

import content.data.GodBook
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.ItemPlugin
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class GodBookGroundItemPlugin : ItemPlugin() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (book in GodBook.values()) {
            register(book.damagedBookId)
        }
        return this
    }

    override fun canPickUp(player: Player, item: GroundItem, type: Int): Boolean {
        if (player.hasItem(item.asItem())) {
            sendMessage(player, "You do not need more than one incomplete book.")
            return false
        }
        return true
    }
}