package content.global.handlers.item

import core.api.sendString
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items

@Initializable
class GnomecopterTicketOption : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.GNOMECOPTER_TICKET_12843).handlers["option:read"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        player.interfaceManager.open(Component(Components.CARPET_TICKET_729))
        var info = "Gnomecopter ticket:"
        info += "<br>" + "Castle Wars"
        info += "<br>" + "Ref. #000"
        for (i in 3 until 8) {
            info += RandomFunction.randomize(10)
        }
        sendString(player, info, Components.CARPET_TICKET_729, 2)
        return true
    }
}
