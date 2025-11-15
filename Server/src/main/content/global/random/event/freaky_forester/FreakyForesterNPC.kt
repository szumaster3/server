package content.global.random.event.freaky_forester

import content.data.RandomEvent
import content.global.random.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Represents the Freaky forester npc.
 */
class FreakyForesterNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.FREAKY_FORESTER_2458) {

    override fun init() {
        super.init()
        sendChat("Ah, ${player.username}, just the person I need!")
        player.lock(6)
        setAttribute(player, RandomEvent.save(), player.location)
        teleport(player, Location.create(2599, 4777, 0), TeleportManager.TeleportType.RANDOM_EVENT_OLD)
        queueScript(player, 5, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    openDialogue(player, FreakyForesterDialogue(), FreakyForesterUtils.FREAK_NPC)
                    FreakyForesterUtils.giveFreakTask(player)
                    resetAnimator(player)
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, FreakyForesterDialogue(), npc)
    }
}
