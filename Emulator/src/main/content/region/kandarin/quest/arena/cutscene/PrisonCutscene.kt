package content.region.kandarin.quest.arena.cutscene

import content.region.kandarin.quest.arena.dialogue.HengradDialogue
import core.api.face
import core.api.location
import core.api.openDialogue
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.NPCs

class PrisonCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(location(2600, 3142, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10289)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                addNPC(KHAZARD_GUARD, 48, 5, Direction.WEST)
                teleport(player, 46, 5)
                timedUpdate(1)
            }

            1 -> {
                moveCamera(43, 3)
                rotateCamera(20, 20)
                timedUpdate(1)
            }

            2 -> {
                move(player, 45, 5)
                timedUpdate(1)
            }

            3 -> {
                move(player, 40, 5)
                move(getNPC(KHAZARD_GUARD)!!, 41, 5)
                timedUpdate(6)
            }

            4 -> {
                DoorActionHandler.handleAutowalkDoor(player, getObject(40, 5)!!)
                timedUpdate(5)
            }

            5 -> {
                move(player, 40, 6)
                move(getNPC(KHAZARD_GUARD)!!, 40, 5)
                timedUpdate(2)
            }

            6 -> {
                face(getNPC(KHAZARD_GUARD)!!, player)
                dialogueUpdate(
                    KHAZARD_GUARD,
                    FaceAnim.FRIENDLY,
                    "The General seems to have taken a liking to you. He'd normally kill imposters like you without a second thought.",
                )
            }

            7 -> {
                end { openDialogue(player, HengradDialogue()) }
            }
        }
    }

    companion object {
        private const val KHAZARD_GUARD = NPCs.KHAZARD_GUARD_257
    }
}
