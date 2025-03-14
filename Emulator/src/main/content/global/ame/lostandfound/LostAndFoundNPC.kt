package content.global.ame.lostandfound

import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.ui.setMinimapState
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components

class LostAndFoundNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(-1) {
    override fun init() {
        super.init()

        val runes = getAttribute(player, "teleport:items", emptyArray<Item>())

        lock(player, 7)
        sendChat(player, "Uh? Help!")
        if (inBorders(player, ZoneBorders.forRegion(11595))) {
            setAttribute(player, LostAndFoundUtils.essenceMineKey, true)
        }
        queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    if (runes.isNotEmpty()) {
                        setAttribute(player, "teleport:items", runes)
                    }
                    setMinimapState(player, 2)
                    openInterface(player, Components.FADE_TO_BLACK_115)
                    LostAndFoundUtils.setRandomAppendage(player)
                    setAttribute(player, RandomEvent.save(), player.location)
                    teleport(
                        player,
                        LostAndFoundUtils.eventLocation,
                        type = TeleportManager.TeleportType.RANDOM_EVENT_OLD,
                    )
                    sendMessage(player, "You have slipped through to the Abyssal plane!")
                    return@queueScript delayScript(player, 6)
                }

                1 -> {
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                    sendUnclosablePlainDialogue(
                        player,
                        true,
                        "There has been a fault in the teleportation matrix. Please operate the",
                        "odd appendage out to be forwarded to your destination.",
                    )
                    lockTeleport(player, 100)
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
    }
}
