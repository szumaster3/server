package content.region.asgarnia.falador.quest.rd.cutscene

import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import content.region.asgarnia.falador.quest.rd.plugin.RecruitmentDrivePlugin
import content.region.asgarnia.falador.quest.rd.plugin.RecruitmentDrivePlugin.Companion.initRoomStage
import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import shared.consts.NPCs

class EnterTestingRoomCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        val currentStage = getAttribute(player, RecruitmentDrive.stageArray[0], 0)
        setExit(
            RecruitmentDrivePlugin.Companion.Rooms.index[currentStage]!!.location,
        )
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                dialogueLinesUpdate(
                    NPCs.SIR_TIFFY_CASHIEN_2290, FaceAnim.HAPPY, "Here we go!", "Mind your head!"
                )
                timedUpdate(3)
            }

            2 -> {
                dialogueLinesUpdate(
                    NPCs.SIR_TIFFY_CASHIEN_2290, FaceAnim.HAPPY, "Oops. Ignore the smell!", "Nearly there!"
                )
                timedUpdate(3)
            }

            3 -> {
                dialogueLinesUpdate(
                    NPCs.SIR_TIFFY_CASHIEN_2290, FaceAnim.HAPPY, "And...", "Here we are!", "Best of luck!"
                )
                timedUpdate(3)
            }

            4 -> {
                clearInventory(player)
                endWithoutFade {
                    val currentStage = getAttribute(player, RecruitmentDrive.stageArray[0], 0)
                    val firstStage = RecruitmentDrivePlugin.Companion.Rooms.index[currentStage]!!
                    queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                fadeFromBlack()
                                return@queueScript delayScript(player, 3)
                            }

                            1 -> {
                                forceWalk(player, firstStage.destination, "DUMB")
                                return@queueScript delayScript(player, 2)
                            }

                            2 -> {
                                unlock(player)
                                closeDialogue(player)
                                initRoomStage(player, firstStage.npc)
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
            }
        }
    }
}
