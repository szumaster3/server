package content.global.activity.oldman

import content.global.handlers.iface.ScrollInterface
import core.api.getAttribute
import core.api.openInterface
import core.api.sendNPCDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

class WomTaskListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.NPC, 0, WISE_OLD_MAN) { player, used, _ ->
            if (used.id != 0) {
                sendNPCDialogue(
                    player,
                    WISE_OLD_MAN,
                    "Humph! You could at least say hello before waving your items in my face.",
                    FaceAnim.HALF_GUILTY,
                )
            }
            return@onUseWith true
        }

        on(OLD_MAN_MESSAGE, IntType.ITEM, "read") { player, node ->
            val letterContent = getAttribute(player, WomDeliveryLetter.LETTER_DELIVERY_ATTRIBUTE, -1)

            openInterface(player, Components.MESSAGESCROLL_220).also {
                ScrollInterface.scrollSetup(
                    player,
                    Components.MESSAGESCROLL_220,
                    WomDeliveryLetter.THE_ORACLE_LETTER_CONTENT,
                )
            }

            return@on true
        }
    }

    companion object {
        const val WISE_OLD_MAN = NPCs.WISE_OLD_MAN_2253
        const val OLD_MAN_MESSAGE = Items.OLD_MANS_MESSAGE_5506
    }
}
