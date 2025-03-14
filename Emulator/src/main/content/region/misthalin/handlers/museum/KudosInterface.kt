package content.region.misthalin.handlers.museum

import core.api.getVarbit
import core.api.sendString
import core.game.interaction.InterfaceListener
import core.tools.colorize
import org.rs.consts.Components

class KudosInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        /*
         * Handles updates of kudos at Varrock museum.
         */

        onOpen(Components.VM_KUDOS_532) { player, _ ->
            val kudosAmount = getVarbit(player, 3637)
            sendString(
                player,
                if (kudosAmount == 163) {
                    colorize("%G$kudosAmount/163")
                } else {
                    "$kudosAmount/163"
                },
                Components.VM_KUDOS_532,
                1,
            )
            return@onOpen true
        }
    }
}
