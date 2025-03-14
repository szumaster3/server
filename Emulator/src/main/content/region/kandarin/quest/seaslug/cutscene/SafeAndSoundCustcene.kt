package content.region.kandarin.quest.seaslug.cutscene

import core.api.animate
import core.api.interaction.transformNpc
import core.api.quest.setQuestStage
import core.api.sendDialogue
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.NPCs
import org.rs.consts.Quests

class SafeAndSoundCustcene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(11059)
        addNPC(NPCs.KENNITH_4864, 16, 25, Direction.EAST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                fadeFromBlack()
                teleport(player, 20, 27, 1)
                teleport(getNPC(NPCs.KENNITH_4864)!!, 14, 24, 1)
                timedUpdate(3)
            }

            2 -> {
                move(getNPC(NPCs.KENNITH_4864)!!, 14, 25)
                move(getNPC(NPCs.KENNITH_4864)!!, 15, 25)
                timedUpdate(2)
            }

            3 -> {
                animate(player, 4795)
                sendDialogue(player, "Kennith scrambles through the broken wall...")
                teleport(getNPC(NPCs.KENNITH_4864)!!, 16, 25, 1)
                transformNpc(getNPC(NPCs.KENNITH_4864)!!, NPCs.KENNITH_6373, 3)
                timedUpdate(5)
            }

            4 -> {
                move(getNPC(NPCs.KENNITH_4864)!!, 17, 25)
                move(getNPC(NPCs.KENNITH_4864)!!, 17, 24)
                timedUpdate(4)
            }

            5 -> {
                end().also {
                    setQuestStage(player, Quests.SEA_SLUG, 50)
                }
            }
        }
    }
}
