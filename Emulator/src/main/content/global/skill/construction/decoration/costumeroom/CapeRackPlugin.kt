package content.global.skill.construction.decoration.costumeroom

import content.global.skill.construction.decoration.costumeroom.data.Cape
import core.api.*
import core.api.ui.sendInterfaceConfig
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContainerPacket
import org.rs.consts.Scenery

class CapeRackPlugin : InteractionListener {
    private val CAPE_RACK = intArrayOf(Scenery.OAK_CAPE_RACK_18766, Scenery.TEAK_CAPE_RACK_18767, Scenery.MAHOGANY_CAPE_RACK_18768, Scenery.GILDED_CAPE_RACK_18769, Scenery.MARBLE_CAPE_RACK_18770, Scenery.MAGIC_CAPE_RACK_18771)
    private val INTERFACE = 467

    override fun defineListeners() {

        /*
         * Handles interaction with cape rack.
         */

        on(CAPE_RACK, IntType.SCENERY, "search") { player, _ ->
            setAttribute(player, "con:cape-rack", true)
            openInterface(player, INTERFACE).also {
                sendString(player, "Cape rack", INTERFACE, 225)
                val storedItems = Cape.values().map { Item(it.displayId) }.toTypedArray()
                PacketRepository.send(
                    ContainerPacket::class.java,
                    OutgoingContext.Container(player, INTERFACE, 164, 30, storedItems, false)
                )
                Cape.values().forEachIndexed { index, item ->
                    val itemName = getItemName(item.displayId)
                    val key = "cape:$index"
                    val hidden = getAttribute(player, key, false)
                    sendString(player, itemName, INTERFACE, 55 + index * 2)
                    sendInterfaceConfig(player, INTERFACE, item.labelId, hidden)
                    sendInterfaceConfig(player, INTERFACE, item.iconId + 1, hidden)
                }
            }
            return@on true
        }
    }
}