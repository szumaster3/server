package content.global.ame.pinball

import content.data.GameAttributes
import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.ui.setMinimapState
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.Components
import org.rs.consts.NPCs

class PinballNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    override fun init() {
        super.init()
        player.lock()
        submitWorldPulse(
            object : Pulse(1) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 ->
                            sendChat(
                                "Good day, ${
                                    player.username.replaceFirstChar {
                                        if (it.isLowerCase()) {
                                            it.titlecase()
                                        } else {
                                            it.toString()
                                        }
                                    }
                                }, care for a quick game?",
                            )

                        3 -> {
                            setAttribute(player, RandomEvent.save(), player.location)
                            registerLogoutListener(player, RandomEvent.logout()) { p ->
                                p.location = getAttribute(p, RandomEvent.save(), player.location)
                            }
                            PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.init()
                            PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.properties.teleportLocation =
                                PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.properties.spawnLocation
                            teleport(player, PinballUtils.PINBALL_EVENT_LOCATION, TeleportManager.TeleportType.NORMAL)
                        }

                        7 -> {
                            setMinimapState(player, 2)
                            openOverlay(player, Components.PINBALL_INTERFACE_263)
                            setVarbit(player, PinballUtils.VARBIT_PINBALL_SCORE, 0)
                            setAttribute(player, GameAttributes.RE_PINBALL_OBJ, 0)
                            removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14)
                            face(player, findNPC(NPCs.MYSTERIOUS_OLD_MAN_410)!!)
                            openDialogue(player, PinballDialogue())
                            AntiMacro.terminateEventNpc(player)
                            poofClear(findNPC(NPCs.MYSTERIOUS_OLD_MAN_410)!!)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    override fun talkTo(npc: NPC) {
        if (!inBorders(player, PinballUtils.PINBALL_EVENT_ZONE_BORDERS)) {
            sendMessage(player, "He's busy right now.")
        } else {
            openDialogue(player, PinballDialogue(), npc)
        }
    }
}
