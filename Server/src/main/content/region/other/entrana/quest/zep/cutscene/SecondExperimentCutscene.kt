package content.region.other.entrana.quest.zep.cutscene

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import shared.consts.*

class SecondExperimentCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(location(2808, 3355, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(Regions.ENTRANA_11060)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            1 -> {
                teleport(player, 56, 27,0)
                faceLocation(player, player.location.transform(Direction.SOUTH))
                moveCamera(58,30, 350)
                rotateCamera(54, 18, 400)
                addNPC(NPCs.AUGUSTE_5049, 57, 27, Direction.SOUTH_WEST)
                fadeFromBlack()
                timedUpdate(6)
            }
            2 -> {
                visualize(player, Animations.BALLOON_FLY_5142, Graphics.BALLOON_FLY_UPWARDS_880)
                timedUpdate(2)
            }
            3 -> {
                Projectile
                    .create(player, null, Graphics.BALLOON_PROJECTILE_882, 45, 45, 1, 70, 0)
                    .transform(player, player.location.transform(Direction.SOUTH, player.direction.ordinal + 1), false, 70, 140).send()
                //playAudio(player, Sounds.ZEP_BALLOON_FLOAT_3245)
                timedUpdate(3)
            }

            4 -> {
                addNPC(NPCs.BOB_5058,58, 23, Direction.WEST)
                addNPC(NPCs.CURLY_5059,59, 24, Direction.WEST)
                addNPC(NPCs.MOE_5060,60, 23, Direction.WEST)
                addNPC(NPCs.LARRY_5061,59, 22, Direction.WEST)
                moveCamera(63, 23, 600)
                rotateCamera(63, 23, 600, 2)
                timedUpdate(4)
            }

            5 -> {
                move(getNPC(NPCs.BOB_5058)!!, 53, 23)
                move(getNPC(NPCs.CURLY_5059)!!, 53, 24)
                move(getNPC(NPCs.MOE_5060)!!, 54, 23)
                move(getNPC(NPCs.LARRY_5061)!!, 53, 22)
                sendChat(getNPC(NPCs.BOB_5058)!!, "Destroy the foating thing!")
                timedUpdate(5)
            }

            6 -> {
                rotateCamera(56,22, 600) // 1:1
                moveCamera(56,22, 600) // 1:1
                timedUpdate(1)
            }
            7 -> {
                animate(player, Animations.HUMAN_SHRUG_2113)
                playerDialogueUpdate(FaceAnim.FRIENDLY, "Well, that went down like a lead balloon.")
            }
            8 -> end {

            }
        }
    }
}
