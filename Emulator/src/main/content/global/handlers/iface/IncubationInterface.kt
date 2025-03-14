package content.global.handlers.iface

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.plugin.Plugin

private const val GAUGE_UPDATE_SCRIPT = 894
private const val COLD_OFFSET = 22
private const val HOT_OFFSET = 18
private const val GAUGE_OFFSET = 15
private const val GAUGE_VARP = 1205
private const val METER_VARP = 1201
private const val BUTTON_ADD_COLD = 16
private const val BUTTON_ADD_HOT = 18

class IncubationInterface : ComponentPlugin() {
    override fun handle(
        player: Player?,
        component: Component?,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(717, this)
        return this
    }
}
