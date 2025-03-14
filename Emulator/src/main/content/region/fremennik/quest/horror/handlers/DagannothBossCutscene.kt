package content.region.fremennik.quest.horror.handlers

import core.api.clearHintIcon
import core.api.interaction.transformNpc
import core.api.registerHintIcon
import core.api.runTask
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Direction
import org.rs.consts.NPCs

class DagannothBossCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(JOSSIK, 22, 25, Direction.NORTH_WEST)
        addNPC(DAGANNOTH_NECK, 13, 34, Direction.EAST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                registerHintIcon(player, getNPC(DAGANNOTH_NECK)!!)
                moveCamera(22, 42, 400)
                rotateCamera(5, 30)
                timedUpdate(1)
            }

            1 -> {
                move(getNPC(DAGANNOTH_NECK)!!, 17, 37)
                transformNpc(getNPC(DAGANNOTH_NECK)!!, DAGANNOTH_BODY, 10)
                timedUpdate(1)
            }

            2 -> {
                transformNpc(getNPC(DAGANNOTH_NECK)!!, DAGANNOTH_LEGS, 10)
                timedUpdate(2)
            }

            3 -> {
                transformNpc(getNPC(DAGANNOTH_NECK)!!, DAGANNOTH_FULL, 10)
                timedUpdate(2)
            }

            4 -> {
                end()
                resetCamera()
                runTask(player, 12) {
                    clearHintIcon(player)
                    GameWorld.Pulser.submit(DagonnothSessionPulse(player))
                }
            }
        }
    }

    companion object {
        private const val DAGANNOTH_NECK = NPCs.DAGANNOTH_MOTHER_1348
        private const val DAGANNOTH_BODY = NPCs.DAGANNOTH_MOTHER_1349
        private const val DAGANNOTH_LEGS = NPCs.DAGANNOTH_MOTHER_1350
        private const val DAGANNOTH_FULL = NPCs.DAGANNOTH_MOTHER_1351
        private const val JOSSIK = NPCs.JOSSIK_1335
    }
}
