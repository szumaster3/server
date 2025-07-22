package content.global.ame.mime

import content.data.GameAttributes
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.emote.Emotes
import org.rs.consts.Components

/**
 * Represents the Mime interface.
 * @author szu
 */
class MimeInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.MACRO_MIME_EMOTES_188) { player, _, _, buttonID, _, _ ->

            when (buttonID) {
                2 -> Emotes.THINK
                3 -> Emotes.CRY
                4 -> Emotes.LAUGH
                5 -> Emotes.DANCE
                6 -> Emotes.CLIMB_ROPE
                7 -> Emotes.LEAN_ON_AIR
                8 -> Emotes.GLASS_BOX
                9 -> Emotes.GLASS_WALL
            }

            for (i in (2..9)) {
                if (i == buttonID && getAttribute(player, GameAttributes.RE_MIME_INDEX, -1) == i) {
                    player.incrementAttribute(GameAttributes.RE_MIME_CORRECT)
                } else {
                    setAttribute(player, GameAttributes.RE_MIME_WRONG, 1)
                }
            }

            runTask(player, 5) {
                var correct = getAttribute(player, GameAttributes.RE_MIME_CORRECT, -1)

                if (correct in 1..3) {
                    sendUnclosablePlainDialogue(player, true, "", "Correct!")
                    Emotes.CHEER
                } else {
                    sendUnclosablePlainDialogue(player, true, "", "Wrong!")
                    Emotes.CRY
                }

                removeAttribute(player, GameAttributes.RE_MIME_EMOTE)
                MimeUtils.getContinue(player)
            }

            return@on true
        }
    }
}
