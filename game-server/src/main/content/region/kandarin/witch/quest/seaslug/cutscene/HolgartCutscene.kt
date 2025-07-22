package content.region.kandarin.witch.quest.seaslug.cutscene

import core.api.animate
import core.api.face
import core.api.setQuestStage
import core.api.sendChat
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents repair boat cutscene.
 *
 * # Relations
 * - [Sea Slug quest][content.region.kandarin.witch.quest.seaslug.SeaSlug]
 */
class HolgartCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(2716, 3300, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10803)
        addNPC(NPCs.HOLGART_698, 29, 36, Direction.EAST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                teleport(player, 28, 36)
                face(player, Location(2720, 3300, 0))
                fadeFromBlack()
                timedUpdate(3)
            }

            2 -> {
                sendChat(getNPC(NPCs.HOLGART_698)!!, "Right me hearty. Let's get this boat fixed.")
                move(getNPC(NPCs.HOLGART_698)!!, 30, 36)
                timedUpdate(3)
            }

            3 -> {
                teleport(getNPC(NPCs.HOLGART_698)!!, 30, 36, 1)
                addNPC(NPCs.HOLGART_4866, 30, 36, Direction.EAST)
                animate(getNPC(NPCs.HOLGART_4866)!!, Animations.SEA_SLUG_HOGART_FIX_BOAT_4786)
                timedUpdate(8)
            }

            4 -> {
                sendChat(getNPC(NPCs.HOLGART_4866)!!, "Huzzah! We've fixed the leaky old tub.")
                timedUpdate(3)
            }

            5 -> {
                end()
                setQuestStage(player, Quests.SEA_SLUG, 5)
            }
        }
    }
}
