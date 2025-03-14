package content.global.skill.herblore

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
class SwampToadHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.setOptionHandler("remove-legs", this)
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (player.inventory.replace(Item(Items.TOADS_LEGS_2152), (node as Item).slot) != null) {
            sendMessage(player, "You pull the legs off the toad. Poor toad. At least they'll grow back.")
        }
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }
}
