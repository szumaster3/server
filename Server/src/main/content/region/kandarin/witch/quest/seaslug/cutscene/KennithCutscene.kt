package content.region.kandarin.witch.quest.seaslug.cutscene

import core.api.*
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

/**
 * Represents saving Kennith cutscene.
 *
 * # Relations
 * - [Sea Slug quest][content.region.kandarin.witch.quest.seaslug.SeaSlug]
 */
class KennithCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(11059)
        addNPC(NPCs.KENNITH_4864, 16, 25, Direction.EAST, 1)
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
                transformNpc(getNPC(NPCs.KENNITH_4864)!!, NPCs.KENNITH_6373, 3)
                sendPlainDialogue(player, true, "Kennith scrambles through the broken wall...")
                moveCamera(18, 32, 700)
                rotateCamera(17, 24)
                timedUpdate(6)
            }

            2 -> {
                moveCamera(15, 25, 1100, 5)
                move(getNPC(NPCs.KENNITH_4864)!!, 17, 25)
                timedUpdate(3)
            }

            3 -> {
                getNPC(NPCs.KENNITH_4864)!!.clear()
                animate(player, Animations.SEA_SLUG_USE_CRANE_4795)
                replaceScenery(getObject(18, 23, 1)!!.asScenery(), Scenery.CRANE_18326, -1)
                timedUpdate(4)
            }

            4 -> {
                end()
                sendDialogueLines(player, "Down below, you see Holgart collect the boy from the crane and", "lead him away to safety.")
                setQuestStage(player, Quests.SEA_SLUG, 50)
            }
        }
    }
}
