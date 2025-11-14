package content.global.random.event.pinball

import content.data.GameAttributes
import content.data.RandomEvent
import content.global.random.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.NPCs

/**
 * Handles pinball npc.
 */
class PinballNPC(override var loot: WeightBasedTable? = null): RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {

    override fun init() {
        super.init()
        lock(player, 10)
        sendChat("Good day, ${player.name}, care for a quick game?")
        setAttribute(player, RandomEvent.save(), player.location)
        registerLogoutListener(player, RandomEvent.logout()) { p ->
            p.location = getAttribute(p, RandomEvent.save(), player.location)
        }
        queueScript(player, 1, QueueStrength.SOFT)
        { stage: Int ->
            when(stage) {
                0 -> {
                    PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.init()
                    PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.isWalks = false
                    PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.direction = Direction.EAST
                    teleport(player, Location.create(1972, 5046, 0), TeleportManager.TeleportType.NORMAL)
                    return@queueScript delayScript(player, 5)
                }
                1 -> {
                    setMinimapState(player, 2)
                    openOverlay(player, Components.PINBALL_INTERFACE_263)
                    setVarbit(player, PinballUtils.VARBIT_PINBALL_SCORE, 0)
                    setAttribute(player, GameAttributes.RE_PINBALL_OBJ, 0)
                    removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14)
                    face(player, findNPC(NPCs.MYSTERIOUS_OLD_MAN_410)!!)
                    openDialogue(player, PinballDialogue())
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC)
    {
        if (!inBorders(player, PinballUtils.PINBALL_EVENT_ZONE_BORDERS)) {
            sendMessage(player, "He's busy right now.")
        } else {
            openDialogue(player, PinballDialogue(), npc)
        }
    }

}
