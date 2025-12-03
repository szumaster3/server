package content.region.other.entrana.quest.zep.cutscene

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.*

class FirstExperimentCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2810, 3356, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(Regions.ENTRANA_11060)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                teleport(player, 56, 27,0)
                faceLocation(player, player.location.transform(Direction.SOUTH))
                moveCamera(58,30, 350) // 1:1
                rotateCamera(54, 18, 400) // 1:1
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
            //4 -> {
                // playAudio(player, Sounds.ZEP_BALLOON_MOVE_3246)
                // moveCamera(63, 27, 400)
                // rotateCamera(55,23,400)
                // timedUpdate(4)
            //}
            4 -> {
                //playAudio(player, Sounds.ZEP_BALLOON_BURNS_3243)
                moveCamera(63, 29, 600)
                rotateCamera(55, 23, 600, 2)
                timedUpdate(3)
            }
            5 -> {
                face(getNPC(AUGUSTE.id)!!, player, 2)
                face(player,getNPC(AUGUSTE.id)!!,  2)
                timedUpdate(2)
            }
            6  -> dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "That was perfect. My hypothesis was right!")
            7  -> playerDialogueUpdate(FaceAnim.SCARED, "Did you not see the burning?")
            8  -> dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "One more test. Then we shall proceed.")
            9 -> playerDialogueUpdate(FaceAnim.SCARED, "Burning? Fire? Hello?")
            10 -> dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "We shall meekly go! No...no...it needs to sound grander. We shall cautiously go...")
            11 -> playerDialogueUpdate(FaceAnim.SCARED, "We're doomed.")
            12 -> {
                end()
                setQuestStage(player, Quests.ENLIGHTENED_JOURNEY, 4)
            }
        }
    }

    companion object {
        private val AUGUSTE = NPC(NPCs.AUGUSTE_5049)
    }
}
