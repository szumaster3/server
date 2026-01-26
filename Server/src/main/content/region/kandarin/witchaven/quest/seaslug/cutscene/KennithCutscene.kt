package content.region.kandarin.witchaven.quest.seaslug.cutscene

import core.api.*
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import shared.consts.*

/**
 * Represents saving Kennith cutscene.
 *
 * # Relations
 * - [Sea Slug quest][content.region.kandarin.witchaven.quest.seaslug.SeaSlugQuest]
 */
class KennithCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(Regions.FISHING_PLATFORM_11059)
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
                faceLocation(player, base.transform(20, 5, 1))
                moveCamera(18, 32, 700)
                rotateCamera(17, 24)
                // playAudio(player, 3021)
                val animDelay = animationDuration(Animation(4805))
                animate(getNPC(NPCs.KENNITH_4864)!!, 4805)
                animate(player, Animations.SEA_SLUG_USE_CRANE_4795)
                timedUpdate(animDelay)
            }
            2 -> {
                sendPlainDialogue(player, true, "Kennith scrambles through the broken wall...")
                sendMessage(player, "You rotate the crane around.")
                moveCamera(15, 25, 1100, 5)
                move(getNPC(NPCs.KENNITH_4864)!!, 17, 25)
                timedUpdate(2)
            }
            3 -> {
                animate(getNPC(NPCs.KENNITH_4864)!!, 4789)
                sendMessage(player, "You rotate the crane around.")
                timedUpdate(1)
            }
            4 -> {
                getNPC(NPCs.KENNITH_4864)!!.clear()
                playAudio(player, 3020)
                sendMessage(player, "You rotate the crane around.")
                replaceScenery(getObject(18, 23, 1)!!.asScenery(), Scenery.CRANE_18326, -1)
                timedUpdate(3)
            }
            5 -> end{
                sendDialogueLines(player, "Down below, you see Holgart collect the boy from the crane and", "lead him away to safety.")
            }
        }
    }
}
