package content.region.fremennik.handlers.general_shadows

import content.region.fremennik.handlers.general_shadows.GhostBouncerNPC.Companion.spawnGhostBouncer
import core.api.location
import core.api.removeAttribute
import core.api.sendMessageWithDelay
import core.api.unlock
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.NPCs

class CavernCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(location(1759, 4711, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(6985)
        addNPC(NPCs.BOUNCER_5564, 31, 30, Direction.NORTH_EAST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                fadeFromBlack()
                timedUpdate(-1)
            }

            2 -> {
                teleport(player, 31, 39)
                moveCamera(34, 35, 700)
                rotateCamera(31, 39, 100)
                timedUpdate(1)
            }

            3 -> {
                playerDialogueUpdate(FaceAnim.WORRIED, "What the...! It's Bouncer, Khazard's hell hound.")
            }

            4 -> {
                moveCamera(34, 34)
                rotateCamera(31, 30)
                timedUpdate(1)
            }

            5 ->
                playerDialogueUpdate(
                    FaceAnim.ANGRY,
                    "Khazard tricked me! This is no reward, he's trying to get me killed...AGAIN!",
                )

            6 -> {
                end()
                sendMessageWithDelay(
                    player,
                    "An evil prescence in the cave prevents your prayers from being heard.",
                    10,
                )
                removeAttribute(player, GeneralShadowUtils.GS_SEVERED_LEG)
                spawnGhostBouncer(player)
                unlock(player)
                resetCamera()
            }
        }
    }
}
