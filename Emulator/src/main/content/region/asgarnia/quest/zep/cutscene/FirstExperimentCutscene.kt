package content.region.asgarnia.quest.zep.cutscene

import core.api.face
import core.api.location
import core.api.quest.setQuestStage
import core.api.visualize
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FirstExperimentCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(location(2808, 3355, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(11060)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            2 -> {
                fadeFromBlack()
                teleport(player, 56, 27)
                rotateCamera(55, 25)
                player.faceLocation(location(184, 26, 0))
                addNPC(NPCs.AUGUSTE_5049, 57, 27, Direction.SOUTH_WEST)
                timedUpdate(3)
            }

            3 -> {
                visualize(player, Animations.BALLOON_FLY_5142, Graphics.BALLOON_FLY_UPWARDS_880)
                timedUpdate(2)
            }

            4 -> {
                Projectile
                    .create(player, null, Graphics.BALLOON_FLOATING_881, 45, 45, 1, 70, 0)
                    .transform(
                        player,
                        player.location.transform(Direction.SOUTH, player.direction.ordinal + 1),
                        false,
                        70,
                        140,
                    ).send()
                timedUpdate(3)
            }

            5 -> {
                moveCamera(55, 22)
                rotateCamera(54, 27)
                timedUpdate(4)
            }

            6 -> {
                face(player, AUGUSTE, 2)
                timedUpdate(1)
            }

            7 -> {
                face(AUGUSTE, player, 2)
                timedUpdate(1)
            }

            8 -> {
                dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "That was perfect. My hypothesis was right!")
            }

            9 -> {
                playerDialogueUpdate(FaceAnim.SCARED, "Did you not see the burning?")
            }

            10 -> {
                dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "One more test. Then we shall proceed.")
            }

            11 -> {
                playerDialogueUpdate(FaceAnim.SCARED, "Burning? Fire? Hello?")
            }

            12 -> {
                dialogueUpdate(
                    NPCs.AUGUSTE_5049,
                    FaceAnim.HAPPY,
                    "We shall meekly go! No...no...it needs to sound grander. We shall cautiously go...",
                )
            }

            13 -> {
                playerDialogueUpdate(FaceAnim.SCARED, "We're doomed.")
            }

            14 -> {
                end()
                setQuestStage(player, Quests.ENLIGHTENED_JOURNEY, 4)
            }
        }
    }

    companion object {
        private val AUGUSTE = NPC(NPCs.AUGUSTE_5049)
    }
}
