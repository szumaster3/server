package content.global.handlers.item

import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class GodswordDismantleHandler : OptionHandler() {
    private val bladeId = Item(Items.GODSWORD_BLADE_11690)

    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.ARMADYL_GODSWORD_11694).handlers["option:dismantle"] = this
        ItemDefinition.forId(Items.BANDOS_GODSWORD_11696).handlers["option:dismantle"] = this
        ItemDefinition.forId(Items.SARADOMIN_GODSWORD_11698).handlers["option:dismantle"] = this
        ItemDefinition.forId(Items.ZAMORAK_GODSWORD_11700).handlers["option:dismantle"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val item = node as Item
        if (item.slot < 0 || player.inventory.getNew(item.slot).id != item.id) {
            return true
        }
        val freeSlot = player.inventory.freeSlot()
        if (freeSlot == -1) {
            sendMessage(player, "Not enough space in your inventory.")
            return true
        }
        sendMessage(player, "You detach the hilt from the blade.")
        player.inventory.replace(null, item.slot, false)
        player.inventory.add(bladeId, Item(11702 + (item.id - 11694)))
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }
}
