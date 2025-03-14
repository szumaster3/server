package content.global.handlers.iface

import content.data.GameAttributes
import content.region.kandarin.miniquest.knightwave.KnightWaveAttributes
import core.api.getAttribute
import core.api.sendDialogue
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.tools.DARK_BLUE
import org.rs.consts.Components

class PrayerTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.PRAYER_271) { player, _, _, buttonID, _, _ ->
            val type = PrayerType.get(buttonID) ?: return@on true

            if (!additionalPrayers(player, type)) {
                return@on true
            }

            if (isPrayerLocked(player)) {
                type.toggle(player, false)
                return@on true
            }

            player.prayer.toggle(type)
            return@on true
        }
    }

    private fun isPrayerLocked(player: Player): Boolean {
        if (getAttribute(player, GameAttributes.PRAYER_LOCK, false)) {
            sendMessage(player, "You can't use it right now.")
            return true
        }
        return false
    }

    private fun additionalPrayers(
        player: Player,
        type: PrayerType,
    ): Boolean {
        val hasRequirements = getAttribute(player, KnightWaveAttributes.KW_COMPLETE, false)
        val hasPrayerPoints = player.skills.prayerPoints >= 1

        if (!hasPrayerPoints) return false

        return when (type) {
            PrayerType.CHIVALRY -> {
                if (!hasRequirements) {
                    sendDialogue(
                        player,
                        "You need a$DARK_BLUE Prayer level of 60</col>, a$DARK_BLUE Defence level of ${PrayerType.CHIVALRY.defenceReq}</col> and have completed the King's Ransom quest's Knight Wave</col> reward$DARK_BLUE to use Chivalry</col>.",
                    )
                    false
                } else {
                    true
                }
            }

            PrayerType.PIETY -> {
                if (!hasRequirements) {
                    sendDialogue(
                        player,
                        "You need a$DARK_BLUE Prayer level of 70</col>, a$DARK_BLUE Defence level of ${PrayerType.PIETY.defenceReq}</col> and to have completed the King's Ransom quest's Knight Wave</col> reward$DARK_BLUE to use Piety</col>.",
                    )
                    false
                } else {
                    true
                }
            }

            else -> true
        }
    }
}
