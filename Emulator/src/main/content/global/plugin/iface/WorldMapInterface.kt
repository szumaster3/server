package content.global.plugin.iface

import content.data.GameAttributes
import content.region.island.tutorial.plugin.TutorialStage
import content.region.island.tutorial.plugin.TutorialStage.TUTORIAL_STAGE
import core.api.getAttribute
import core.api.getVarbit
import core.api.setVarbit
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components

/**
 * Handles the world map interface.
 * @author Emperor, szu
 */
class WorldMapInterface : InterfaceListener {
    companion object {
        private const val RESIZABLE_WINDOW = Components.TOPLEVEL_FULLSCREEN_746
        private const val REGULAR_WINDOW = Components.TOPLEVEL_548
        private const val KEY_SORT_VARBIT = 5367
        private const val RESET_KEY_SORT = 0
        private const val MAX_KEY_SORT_VALUE = 3
    }

    override fun defineInterfaceListeners() {
        on(Components.WORLDMAP_755) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                3 -> {
                    if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                        val currentStage = getAttribute(player, TUTORIAL_STAGE, -1)
                        TutorialStage.load(player, currentStage)
                    }
                    openWorldMapWindow(player)
                }
                29 -> toggleKeySort(player)
            }
            return@on true
        }
    }

    private fun openWorldMapWindow(player: Player) {
        val windowId = if (player.interfaceManager.isResizable) RESIZABLE_WINDOW else REGULAR_WINDOW
        player.interfaceManager.openWindowsPane(Component(windowId), 2)
        player.packetDispatch.sendRunScript(1187, "ii", RESET_KEY_SORT, RESET_KEY_SORT)
        player.updateSceneGraph(true)
    }

    private fun toggleKeySort(player: Player) {
        var keySort = getVarbit(player, KEY_SORT_VARBIT)
        keySort = (keySort + 1) % MAX_KEY_SORT_VALUE
        setVarbit(player, KEY_SORT_VARBIT, keySort)
    }
}
