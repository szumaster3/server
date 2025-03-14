package content.global.ame.strangeplant

import content.data.GameAttributes
import core.api.addItemOrDrop
import core.api.getAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.Items
import org.rs.consts.NPCs

class StrangePlantListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.STRANGE_PLANT_407, IntType.NPC, "pick") { player, node ->
            if (AntiMacro.getEventNpc(player) != node) {
                sendMessage(player, "This isn't your strange plant.")
                return@on true
            }

            if (!getAttribute(node.asNpc(), GameAttributes.PLANT_NPC, false)) {
                sendMessage(player, "The fruit isn't ready to be picked.")
                return@on true
            }

            if (!getAttribute(node.asNpc(), GameAttributes.PLANT_NPC_VALUE, false)) {
                setAttribute(node.asNpc(), GameAttributes.PLANT_NPC_VALUE, true)
                addItemOrDrop(player, Items.STRANGE_FRUIT_464)
            }
            return@on true
        }
    }
}
