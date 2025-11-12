package content.global.plugins.iface

import core.api.getVarbit
import core.api.setVarbit
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Components
import shared.consts.Vars

/**
 * Handles the world map interface.
 * @author Emperor
 */
@Initializable
class WorldMapInterface : ComponentPlugin() {

    companion object {
        private const val KEY_SORT_VARBIT = Vars.VARBIT_INTERFACE_WORLD_MAP_KEY_SORT_5367
        private const val MAX_KEY_SORT_VALUE = 3
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.WORLDMAP_755, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        when (button) {
            3 -> {
                player.interfaceManager.openWindowsPane(
                    Component(if (player.interfaceManager.isResizable) 746 else 548),
                    2
                )
                player.packetDispatch.sendRunScript(1187, "ii", 0, 0)
                player.updateSceneGraph(true)
                return true
            }
            29 -> {
                var keySort = getVarbit(player, KEY_SORT_VARBIT)
                keySort = (keySort + 1) % MAX_KEY_SORT_VALUE
                setVarbit(player, KEY_SORT_VARBIT, keySort)
                return true
            }
            else -> return true
        }
    }
}
