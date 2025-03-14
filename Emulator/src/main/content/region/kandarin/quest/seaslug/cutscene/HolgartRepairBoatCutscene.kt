package content.region.kandarin.quest.seaslug.cutscene

import core.api.*
import core.api.quest.setQuestStage
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

class HolgartRepairBoatCutscene(
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
                face(player, location(2720, 3300, 0))
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
                animate(getNPC(NPCs.HOLGART_4866)!!, 4786)
                timedUpdate(8)
            }

            4 -> {
                sendChat(getNPC(NPCs.HOLGART_4866)!!, "Huzzah! We've fixed the leaky old tub.")
                timedUpdate(2)
            }

            5 -> {
                end()
                setQuestStage(player, Quests.SEA_SLUG, 5)
            }
        }
    }
}
