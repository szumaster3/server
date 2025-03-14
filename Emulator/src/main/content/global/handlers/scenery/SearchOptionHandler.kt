package content.global.handlers.scenery

import core.api.addItem
import core.api.inInventory
import core.api.sendMessage
import core.api.sendMessageWithDelay
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.Scenery

@Initializable
class SearchOptionHandler : OptionHandler() {
    enum class Search(
        val scenery: Int,
        val item: Item,
    ) {
        DEFAULT(-1, Item(Items.LEATHER_GLOVES_1059, 1)),
        ;

        companion object {
            fun forId(id: Int): Search? {
                for (search in values()) {
                    if (search.scenery == id) {
                        return search
                    }
                }
                return null
            }
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("search", this)
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (node.name == "Bookcase") {
            sendMessage(player, "You search the books...")
            sendMessageWithDelay(player, "You find nothing of interest to you.", 1)
            return true
        }
        if (node.id == Scenery.SACK_14743 && !inInventory(player, Items.KNIFE_946) && !player.inCombat()) {
            sendMessage(player, "You mindlessly reach into the sack labeled 'knives'...")
            sendMessageWithDelay(player, "Against all odds you pull out a knife without hurting yourself.", 2)
            addItem(player, Items.KNIFE_946)
            return true
        }
        sendMessage(player, "You search the " + node.name.lowercase() + " but find nothing.")
        return true
    }
}
