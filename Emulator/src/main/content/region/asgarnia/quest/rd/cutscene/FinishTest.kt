package content.region.asgarnia.quest.rd.cutscene

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.ui.closeDialogue
import core.api.ui.setMinimapState
import core.game.activity.Cutscene
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FinishTest(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location(2996, 3375))
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                if (getQuestStage(player, Quests.RECRUITMENT_DRIVE) == 2) {
                    setQuestStage(player, Quests.RECRUITMENT_DRIVE, 3)
                }
                player.lock()
                closeDialogue(player)
                fadeToBlack()
                setMinimapState(player, 2)
                timedUpdate(6)
            }

            1 -> {
                clearInventory(player)
                queueScript(player, 1, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            fadeFromBlack()
                            return@queueScript delayScript(player, 2)
                        }

                        1 -> {
                            openDialogue(player, SirTiffyCashienDialogueFile(), NPC(NPCs.SIR_TIFFY_CASHIEN_2290))
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }

                endWithoutFade {
                    face(player, findLocalNPC(player, NPCs.SIR_TIFFY_CASHIEN_2290)!!)
                    player.interfaceManager.restoreTabs()
                    player.unlock()
                }
            }
        }
    }
}
