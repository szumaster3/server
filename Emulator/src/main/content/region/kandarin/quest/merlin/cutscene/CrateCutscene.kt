package content.region.kandarin.quest.merlin.cutscene

import core.api.addScenery
import core.api.ui.setMinimapState
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location

class CrateCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(2778, 3401, 0))
        loadRegion(12609)
        player.teleport(base.transform(18, 25, 0))
        addScenery(Scenery(65, base.transform(18, 25, 0), 0, 0))
        addScenery(Scenery(65, base.transform(19, 25, 0), 0, 4))
        addScenery(Scenery(65, base.transform(18, 24, 0), 0, 1))
        addScenery(Scenery(65, base.transform(18, 26, 0), 0, 3))
    }

    override fun runStage(stage: Int) {
        when (stage) {
            100 -> {
                player.teleport(Location.create(2778, 3401, 0))
                setMinimapState(player, 0)
                player.interfaceManager.restoreTabs()
                player.unlock()
                player.logoutListeners.remove("cutscene")
                AntiMacro.unpause(player)
                ended = true
            }
        }
    }
}
