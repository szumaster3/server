package content.region.kandarin.pisc.quest.phoenix.custcene

import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.NPCs

class GetLostCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(3566, 5224, 0))
        loadRegion(14161)
        addNPC(LARGE_EGG, 47, 46, Direction.SOUTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> playerDialogueUpdate(FaceAnim.THINKING, "Ack! This isn't the way out. I'm lost! Hmm... what's that?")
            1 -> {
                teleport(getNPC(LARGE_EGG)!!, 47,46)
                teleport(player, 46, 40)
                timedUpdate(4)
            }
            2 -> {
                rotateCamera(47,46)
                moveCamera(47,40, 300,5)
                timedUpdate(12)
            }
            3 -> endWithoutFade()
        }
    }

    companion object {
        private const val LARGE_EGG = NPCs.LARGE_EGG_8552
    }
}
