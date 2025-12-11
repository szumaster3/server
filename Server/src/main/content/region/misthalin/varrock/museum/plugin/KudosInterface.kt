package content.region.misthalin.varrock.museum.plugin

import core.api.getVarbit
import core.api.sendString
import core.game.interaction.InterfaceListener
import core.tools.colorize
import shared.consts.Components
import shared.consts.Vars

class KudosInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        /*
         * Handles updates of kudos at Varrock museum.
         */

        onOpen(Components.VM_KUDOS_532) { player, _ ->
            val kudosAmount = getVarbit(player, Vars.VARBIT_KUDOS_VALUE_3637)
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
            // playAudio(player, Sounds.VM_GAIN_KUDOS_3653)
            return@onOpen true
        }
    }
}
