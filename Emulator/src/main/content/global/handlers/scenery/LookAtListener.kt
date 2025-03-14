package content.global.handlers.scenery

import core.api.interaction.getSceneryName
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener

class LookAtListener : InteractionListener {
    override fun defineListeners() {
        for (i in 18877..18900) {
            on(i, IntType.SCENERY, "look at") { player, _ ->
                sendMessage(player, "The ${getSceneryName(i).lowercase()} seem to be going south-west.")
                return@on true
            }
        }
    }
}
