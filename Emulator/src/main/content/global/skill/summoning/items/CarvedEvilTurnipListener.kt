package content.global.skill.summoning.items

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class CarvedEvilTurnipListener : InteractionListener {
    val knife = Items.KNIFE_946
    val evilTurnip = Items.EVIL_TURNIP_12134
    val carvedEvilTurnip = Items.CARVED_EVIL_TURNIP_12153

    override fun defineListeners() {
        onUseWith(IntType.ITEM, evilTurnip, knife) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendMessage(player, "You carve a scary face into the evil turnip.")
                sendMessage(player, "Wooo! It's enough to give you nightmares.")
                return@onUseWith addItem(player, carvedEvilTurnip)
            }
            return@onUseWith false
        }
    }
}
