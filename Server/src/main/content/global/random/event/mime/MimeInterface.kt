package content.global.random.event.mime

import content.data.GameAttributes
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.emote.Emotes
import shared.consts.Components

/**
 * Represents the Mime interface.
 * @author szu
 */
class MimeInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.MACRO_MIME_EMOTES_188) { player, _, _, buttonID, _, _ ->

            when (buttonID) {
                2 -> animate(player, Emotes.THINK.animation)
                3 -> animate(player, Emotes.CRY.animation)
                4 -> animate(player, Emotes.LAUGH.animation)
                5 -> animate(player, Emotes.DANCE.animation)
                6 -> animate(player, Emotes.CLIMB_ROPE.animation)
                7 -> animate(player, Emotes.LEAN_ON_AIR.animation)
                8 -> animate(player, Emotes.GLASS_BOX.animation)
                9 -> animate(player, Emotes.GLASS_WALL.animation)
            }

            for (i in (2..9)) {
                if (i == buttonID && getAttribute(player, GameAttributes.RE_MIME_INDEX, -1) == i) {
                    player.incrementAttribute(GameAttributes.RE_MIME_CORRECT)
                } else {
                    setAttribute(player, GameAttributes.RE_MIME_WRONG, 1)
                }
            }

            runTask(player, 6) {
                var correct = getAttribute(player, GameAttributes.RE_MIME_CORRECT, -1)

                if (correct in 1..3) {
                    sendUnclosablePlainDialogue(player, true, "", "Correct!")
                    animate(player, Emotes.CHEER.animation)
                } else {
                    sendUnclosablePlainDialogue(player, true, "", "Wrong!")
                    animate(player, Emotes.CRY.animation)
                }

                removeAttribute(player, GameAttributes.RE_MIME_EMOTE)
                MimeUtils.getContinue(player)
            }

            return@on true
        }
    }
}
