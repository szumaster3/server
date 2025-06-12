package content.region.misthalin.lumbridge.quest.restless.plugin

import core.api.*
import core.api.quest.finishQuest
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests

class RestlessGhostCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(12849)
        addNPC(GHOST, 48, 57, Direction.SOUTH)
    }

    override fun runStage(stage: Int) {
        val npc = getNPC(NPCs.RESTLESS_GHOST_457)!!.location
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                fadeFromBlack()
                teleport(player, 48, 56)
                face(player, getNPC(GHOST)!!)
                moveCamera(52, 57)
                rotateCamera(46, 57)
                timedUpdate(1)
            }

            2 -> {
                sendChat(getNPC(GHOST)!!, "Release! Thank you")
                sendNPCDialogueLines(
                    player,
                    NPCs.RESTLESS_GHOST_457,
                    FaceAnim.HAPPY,
                    true,
                    "Release! Thank you stranger..",
                )
                timedUpdate(2)
            }

            3 -> {
                sendChat(getNPC(GHOST)!!, "stranger..")
                timedUpdate(2)
            }

            4 -> {
                visualize(
                    getNPC(NPCs.RESTLESS_GHOST_457)!!,
                    Animations.RESTLESS_GHOST_ASCENDS_4018,
                    Graphics(org.rs.consts.Graphics.GREY_SWIVELS_604, 30),
                )
                timedUpdate(2)
            }

            5 -> {
                moveCamera(39, 58, 700)
                rotateCamera(44, 55, 100)
                timedUpdate(-1)
            }

            6 -> {
                spawnProjectile(
                    source = npc.transform(-4, 1, 0),
                    dest = npc.transform(-4, -4, 0),
                    projectile = org.rs.consts.Graphics.SWIRLEY_GREY_SMOKE_668,
                    startHeight = 42,
                    endHeight = 30,
                    delay = 0,
                    speed = 90,
                    angle = 0,
                )
                rotateCamera(44, 54, 300, 1)
                timedUpdate(3)
            }

            7 -> {
                spawnProjectile(
                    source = npc.transform(-4, -3, 0),
                    dest = npc.transform(0, -7, 0),
                    projectile = org.rs.consts.Graphics.SWIRLEY_GREY_SMOKE_668,
                    startHeight = 30,
                    endHeight = 100,
                    delay = 0,
                    speed = 60,
                    angle = 10,
                )
                rotateCameraBy(4, -4, 400, 1)
                timedUpdate(2)
            }

            8 -> {
                end()
                runTask(player, 16) {
                    finishQuest(player, Quests.THE_RESTLESS_GHOST)
                }
            }
        }
    }

    companion object {
        private const val GHOST = NPCs.RESTLESS_GHOST_457
    }
}
