package content.global.ame.maze

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class MazeNPC(
    var type: String = "",
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    override fun init() {
        super.init()
        sendChat("Aha, you'll do ${player.username}!")
        face(player)
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    sendGraphics(Graphics(1576, 0, 0), player.location)
                    animate(player, 8939)
                    playAudio(player, Sounds.TP_ALL_200)
                    return@queueScript delayScript(player, 3)
                }
                1 -> {
                    if (getAttribute<Location?>(player, MazeInterface.MAZE_ATTRIBUTE_RETURN_LOC, null) == null) {
                        setAttribute(player, MazeInterface.MAZE_ATTRIBUTE_RETURN_LOC, player.location)
                    }
                    MazeInterface.initMaze(player)
                    teleport(player, MazeInterface.STARTING_POINTS.random())
                    AntiMacro.terminateEventNpc(player)
                    sendGraphics(Graphics(org.rs.consts.Graphics.NORMAL_TP_DOWNWARDS_1577, 0, 0), player.location)
                    animate(player, 8941)
                    removeAttribute(player, MazeInterface.MAZE_ATTRIBUTE_CHESTS_OPEN)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        // Do nothing.
    }
}
