package content.region.fremennik.quest.horror.handlers

import core.api.clearHintIcon
import core.api.interaction.transformNpc
import core.api.registerHintIcon
import core.api.runTask
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.NPCs

class DagannothCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(JOSSIK, 22, 25, Direction.NORTH_WEST)
        addNPC(SMALL_DAGANNOTH, 15, 28, Direction.SOUTH_EAST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                registerHintIcon(player, getNPC(SMALL_DAGANNOTH)!!)
                moveCamera(25, 23, 500)
                rotateCamera(21, 26)
                timedUpdate(1)
            }

            1 -> {
                transformNpc(getNPC(SMALL_DAGANNOTH)!!, LARGE_DAGANNOTH, 30)
                timedUpdate(1)
            }

            2 -> {
                move(getNPC(SMALL_DAGANNOTH)!!, 19, 27)
                timedUpdate(3)
            }

            3 -> {
                end()
                resetCamera()
                runTask(player, 12) {
                    clearHintIcon(player)
                    DagonnothBabyNPC.spawnDagannothBaby(player)
                }
            }
        }
    }

    companion object {
        private const val SMALL_DAGANNOTH = NPCs.DAGANNOTH_1338
        private const val LARGE_DAGANNOTH = NPCs.DAGANNOTH_1342
        private const val JOSSIK = NPCs.JOSSIK_1335
    }
}
