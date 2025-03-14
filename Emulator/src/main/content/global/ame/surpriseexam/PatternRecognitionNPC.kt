package content.global.ame.surpriseexam

import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.ServerConstants
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import org.rs.consts.Music
import org.rs.consts.NPCs

class PatternRecognitionNPC(
    var type: String = "",
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    override fun init() {
        super.init()
        sendChat(
            "Surprise exam, ${player.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}!",
        )
        queueScript(player, 3, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    setAttribute(player, RandomEvent.save(), player.location)
                    registerLogoutListener(player, SurpriseExamUtils.SE_LOGOUT_KEY) { p ->
                        p.location = getAttribute(p, RandomEvent.save(), ServerConstants.HOME_LOCATION)
                    }

                    if (!player.musicPlayer.hasUnlocked(Music.SCHOOLS_OUT_371)) {
                        player.musicPlayer.unlock(Music.SCHOOLS_OUT_371)
                    }
                    teleport(player, Location(1886, 5025, 0), TeleportManager.TeleportType.NORMAL)
                    sendMessageWithDelay(
                        player,
                        "Answer three out of six questions correctly to be teleported back where you",
                        3,
                    )
                    sendMessageWithDelay(player, "came from.", 3)
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript delayScript(player, getAnimation(8939).duration + getAnimation(8941).duration)
                }

                1 -> {
                    openDialogue(player, MordautDialogue(false))
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "You can't do that right now.")
    }
}
