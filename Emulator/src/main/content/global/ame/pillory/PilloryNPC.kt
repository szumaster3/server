package content.global.ame.pillory

import content.data.GameAttributes
import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.NPCs

class PilloryNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.PILLORY_GUARD_2791) {
    override fun init() {
        super.init()
        lock(player, 4)
        sendChat("${player.username}, you're under arrest!")
        submitIndividualPulse(
            player,
            object : Pulse(1) {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            face(player)
                            sendChat("${player.username}, you're under arrest!")
                            registerLogoutListener(player, RandomEvent.logout()) { p ->
                                p.location = getAttribute(p, RandomEvent.save(), player.location)
                            }
                        }

                        1 -> {
                            teleport(player, PilloryUtils.LOCATIONS.random(), TeleportManager.TeleportType.NORMAL)
                            setAttribute(player, GameAttributes.RE_PILLORY_CORRECT, 3)
                            setAttribute(player, GameAttributes.RE_PILLORY_SCORE, 0)
                            setAttribute(player, GameAttributes.RE_PILLORY_TARGET, true)
                            removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 12)
                            sendPlainDialogue(
                                player,
                                true,
                                "",
                                "Solve the pillory puzzle to be returned to where you came from.",
                            )
                            AntiMacro.terminateEventNpc(player)
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "He look a bit busy at the moment.")
    }
}
