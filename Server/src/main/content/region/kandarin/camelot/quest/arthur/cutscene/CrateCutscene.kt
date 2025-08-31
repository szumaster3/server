package content.region.kandarin.camelot.quest.arthur.cutscene

import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Location

class CrateCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(2778, 3401, 0))
        loadRegion(11161)
        player.teleport(base.transform(26, 47, 0))
    }

    override fun runStage(stage: Int) {
        when (stage) {
            100 -> end { player.teleport(Location.create(2778, 3401, 0)) }
        }
    }
}
