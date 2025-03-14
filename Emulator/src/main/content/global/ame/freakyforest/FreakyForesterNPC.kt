package content.global.ame.freakyforest

import content.data.RandomEvent
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

class FreakyForesterNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.FREAKY_FORESTER_2458) {
    override fun init() {
        super.init()
        sendChat("Ah, ${player.username}, just the person I need!")
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    visualize(player, 714, Graphics(308, 85, 50))
                    playAudio(player, Sounds.TP_ALL_200)
                    return@queueScript delayScript(player, 3)
                }

                1 -> {
                    setAttribute(player, RandomEvent.save(), player.location)
                    teleport(player, Location.create(2599, 4777, 0))
                    FreakyForesterUtils.giveFreakTask(player)
                    AntiMacro.terminateEventNpc(player)
                    openDialogue(player, FreakyForesterDialogue(), FreakyForesterUtils.FREAK_NPC)
                    resetAnimator(player)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(FreakyForesterDialogue(), npc)
    }
}
