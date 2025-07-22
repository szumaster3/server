package content.global.ame.maze

import content.data.GameAttributes
import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.Components
import org.rs.consts.NPCs

/**
 * Represents the Mysterious Old Man NPC for Maze random event.
 * @author ovenbreado
 */
class MazeNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {

    override fun init() {
        super.init()
        val npc = AntiMacro.getEventNpc(player)
        face(player)
        lock(player, 6)
        sendChat("Aha, you'll do ${player.username}!")
        setAttribute(player, RandomEvent.save(), player.location)
        registerLogoutListener(player, RandomEvent.logout()) { p ->
            p.location = getAttribute(p, RandomEvent.save(), player.location)
        }
        teleport(player, Maze.STARTING_POINTS.random(), TeleportManager.TeleportType.NORMAL)
        queueScript(player, 6, QueueStrength.SOFT) {
            teleport(npc!!.asNpc(), player.location, TeleportManager.TeleportType.INSTANT)
            openOverlay(player, Components.MAZETIMER_209)
            setAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 300)
            setVarp(player, Maze.MAZE_TIMER_VARP, (getAttribute(player, GameAttributes.MAZE_ATTRIBUTE_TICKS_LEFT, 0) / 3), false)
            sendNPCDialogue(player, NPCs.MYSTERIOUS_OLD_MAN_410, "You need to reach the maze center, then you'll be returned to where you were.")
            sendMessage(player, "Head for the center of the maze.")
            return@queueScript stopExecuting(player)
        }
    }

    override fun talkTo(npc: NPC) {
        if (npc.viewport.region!!.id == 11591) {
            openDialogue(player, MazeDialogue())
        }
    }

}
