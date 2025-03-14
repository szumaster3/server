package core.game.global.report

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor.log
import core.game.system.command.CommandMapping.get

class AbuseReport(
    private val reporter: String,
    private val victim: String,
    private val rule: Rule,
) {
    var messages: String? = null

    fun construct(
        player: Player,
        mute: Boolean,
    ) {
        if (mute) {
            get("mute")?.attemptHandling(player, arrayOf("mute", victim, "48h"))
        }
        sendMessage(player, "Thank you, your abuse report has been received.")
        log(player, LogType.COMMAND, "$reporter-$victim-Abuse Report - ${rule.name}")
    }
}
