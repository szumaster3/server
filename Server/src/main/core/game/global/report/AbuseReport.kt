package core.game.global.report

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor.log
import core.game.system.command.CommandMapping.get

/**
 * Represents a report of player abuse submitted in-game.
 *
 * @property reporter The username of the player submitting the report.
 * @property victim The username of the reported player.
 * @property rule The specific rule that was violated.
 */
class AbuseReport(
    private val reporter: String,
    private val victim: String,
    private val rule: Rule
) {

    /**
     * Optional messages provided by the reporter describing the abuse.
     */
    var messages: String? = null

    /**
     * Constructs and processes the abuse report.
     *
     * This will optionally mute the reported player for 48 hours if [mute] is true,
     * sends a confirmation message to the reporting player, and logs the report.
     *
     * @param player The player who submitted the report (used to send confirmation and logging).
     * @param mute If true, attempts to mute the reported player for 48 hours.
     */
    fun construct(player: Player, mute: Boolean) {
        if (mute) {
            get("mute")?.attemptHandling(player, arrayOf("mute", victim, "48h"))
        }
        sendMessage(player, "Thank you, your abuse report has been received.")
        log(player, LogType.COMMAND, "$reporter-$victim-Abuse Report - ${rule.name}")
    }
}