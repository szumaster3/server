package content.global.handlers.iface

import core.api.inEquipment
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.emote.Emotes
import org.rs.consts.Components
import org.rs.consts.Items

class EmoteTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.EMOTES_464) { player, _, _, buttonID, _, _ ->
            if (inEquipment(player, Items.SLED_4084)) {
                if (!sledEmoteIDs.contains(buttonID)) {
                    sendMessage(player, "You can't do that on a sled.")
                    return@on true
                }
            }
            Emotes.handle(player, buttonID)
            return@on true
        }
    }

    companion object {
        private val sledEmoteIDs = intArrayOf(22, 10, 9, 7)
    }
}
