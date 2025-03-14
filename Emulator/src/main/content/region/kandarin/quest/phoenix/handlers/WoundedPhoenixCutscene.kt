package content.region.kandarin.quest.phoenix.handlers

import core.api.setVarbit
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Vars

class WoundedPhoenixCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(3533, 5204, 0))
        loadRegion(REGION)
        addNPC(NPCs.WOUNDED_PHOENIX_8547, 14, 12, Direction.NORTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                teleport(player, 13, 20)
                moveCamera(11, 16, 400)
                rotateCamera(20, 9, 300)
                timedUpdate(2)
            }

            2 -> {
                fadeFromBlack()
                setVarbit(player, 5774, 2, true)
                timedUpdate(2)
            }

            3 -> {
                move(getNPC(PHOENIX)!!, 14, 15)
                rotateCamera(20, 20, 300, 2)
                timedUpdate(5)
            }

            4 -> {
                teleport(getNPC(PHOENIX)!!, 14, 19) // Inside
                // Outside
                PhoenixLairListener.woundedPhoenix.teleporter.send(
                    Location.create(3534, 5203, 0),
                    TeleportManager.TeleportType.INSTANT,
                )
                rotateCamera(20, 25, 300, 2)
                timedUpdate(3)
            }

            5 -> {
                endWithoutFade {
                    resetCamera()
                    setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 6, true)
                }
            }
        }
    }

    companion object {
        private const val PHOENIX = NPCs.WOUNDED_PHOENIX_8547
        private const val REGION = 14161
    }
}
