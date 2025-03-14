package content.region.desert.quest.deserttreasure.custscene

import core.api.closeInterface
import core.api.openInterface
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import org.rs.consts.Components

class MirrorLookCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13642)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }

            1 -> {
                openInterface(player, Components.LEGENDS_MIRROR_155)
                rotateCamera(29, 30, 300, 100)
                moveCamera(29, 38, 600)
                timedUpdate(2)
            }

            2 -> {
                timedUpdate(2)
                fadeFromBlack()
            }

            3 -> {
                timedUpdate(4)
            }

            4 -> {
                closeInterface(player)
                end()
            }
        }
    }
}
