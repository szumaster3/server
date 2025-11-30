package content.global.skill.construction.decoration.bedroom

import core.api.playAudio
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery
import java.text.SimpleDateFormat
import java.util.*

class ClockPlugin : InteractionListener {

    override fun defineListeners() {
        on(CLOCK_FURNITURE, IntType.SCENERY, "read") { player, _ ->
            val format = SimpleDateFormat("mm")
            val minuteDisplay = format.format(Calendar.getInstance().time).toInt()
            val message = buildString {
                append("It's ")
                when (minuteDisplay) {
                    0 -> append("Rune o'clock.")
                    15 -> append("a quarter past Rune.")
                    in 1..29 -> append("$minuteDisplay past Rune.")
                    45 -> append("a quarter till Rune.")
                    else -> append("${60 - minuteDisplay} till Rune.")
                }
            }
            playAudio(player,941)
            sendMessage(player, message)
            return@on true
        }
    }

    companion object {
        private val CLOCK_FURNITURE = intArrayOf(
            Scenery.CLOCK_13169,
            Scenery.CLOCK_13170,
            Scenery.CLOCK_13171
        )
    }
}