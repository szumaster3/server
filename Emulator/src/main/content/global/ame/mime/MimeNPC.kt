package content.global.ame.mime

import content.data.GameAttributes
import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import core.tools.RandomFunction
import org.rs.consts.Music
import org.rs.consts.NPCs

class MimeNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    override fun init() {
        super.init()
        sendChat(
            "Aha, you're required, " +
                player.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } +
                "!",
        )
        queueScript(player, 3, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 10)
                    lockInteractions(player, 10)
                    setAttribute(player, RandomEvent.save(), player.location)
                    registerLogoutListener(player, RandomEvent.logout()) { p ->
                        p.location = getAttribute(p, RandomEvent.save(), p.location)
                    }

                    if (!player.musicPlayer.hasUnlocked(Music.ARTISTRY_247)) {
                        player.musicPlayer.unlock(Music.ARTISTRY_247)
                    }
                    removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14)
                    teleport(player, MimeUtils.MIME_EVENT_LOCATION, TeleportManager.TeleportType.NORMAL)
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript delayScript(player, 8)
                }

                1 -> {
                    sendMessage(
                        player,
                        "You need to copy the mime's performance, then you'll be returned to where you were.",
                    )

                    forceMove(player, MimeUtils.MIME_EVENT_LOCATION, MimeUtils.MIME_LOCATION, 20, 80)
                    return@queueScript delayScript(player, 6)
                }

                2 -> {
                    setAttribute(player, GameAttributes.RE_MIME_INDEX, 0)
                    setAttribute(player, GameAttributes.RE_MIME_EMOTE, RandomFunction.random(2, 9))
                    MimeUtils.getEmote(player)
                    return@queueScript stopExecuting(player)
                }
            }
            return@queueScript stopExecuting(player)
        }
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "You can't do that right now.")
    }
}
