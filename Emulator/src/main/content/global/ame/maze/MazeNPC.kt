package content.global.ame.maze

import content.data.GameAttributes
import content.global.ame.RandomEventNPC
import content.global.ame.RandomEvents
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import org.rs.consts.Components
import org.rs.consts.NPCs

class MazeNPC(
    var type: String = "",
    override var loot: WeightBasedTable? = null,

) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {

    override fun init() {
        super.init()
        face(player)
        sendChat("Aha, you'll do ${player.username}!")
        runTask(player, 3) {
            setAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 300)
            setVarp(player, MazeInterface.MAZE_TIMER_VARP, (getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 0) / 3), false)
            openOverlay(player, Components.MAZETIMER_209)
            sendMessage(player, "You need to reach the maze center, then you'll be returned to where you were.")
            sendNPCDialogue(player, NPCs.MYSTERIOUS_OLD_MAN_410, "You need to reach the maze center, then you'll be returned to where you were.")
            teleport(player, MazeInterface.STARTING_POINTS.random(), TeleportManager.TeleportType.NORMAL)
            RandomEvents.MAZE.npc.follow()
        }
    }

    override fun talkTo(npc: NPC) {
    }
}
