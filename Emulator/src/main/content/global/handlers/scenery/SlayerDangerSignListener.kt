package content.global.handlers.scenery

import core.api.sendDialogueLines
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class SlayerDangerSignListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.DANGER_SIGN_5127, IntType.SCENERY, "read") { player, _ ->
            sendDialogueLines(
                player,
                "<col=FFF0000>WARNING!</col>",
                "This area contains very dangerous creatures!",
                "Do not pass unless properly prepared!",
            )
            return@on true
        }
    }
}
