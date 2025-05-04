package content.global.skill.construction.storage

import content.global.skill.construction.storage.box.CapeRackItem
import core.api.*
import core.api.ui.sendInterfaceConfig
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs.consts.Scenery

class CapeRackListener : InteractionListener {
    private val CAPE_RACK =
        intArrayOf(
            Scenery.OAK_CAPE_RACK_18766,
            Scenery.TEAK_CAPE_RACK_18767,
            Scenery.MAHOGANY_CAPE_RACK_18768,
            Scenery.GILDED_CAPE_RACK_18769,
            Scenery.MARBLE_CAPE_RACK_18770,
            Scenery.MAGIC_CAPE_RACK_18771
        )
    private val INTERFACE = 467

    override fun defineListeners() {

        /*
         * Handles interaction with cape rack.
         */

        on(CAPE_RACK, IntType.SCENERY, "search") { player, _ ->
            setAttribute(player, "con:cape-rack", true)
            openInterface(player, INTERFACE).also {
                sendString(player, "Cape rack", INTERFACE, 225)
                val storedItems = CapeRackItem.values().map { Item(it.displayId) }.toTypedArray()
                PacketRepository.send(
                    ContainerPacket::class.java,
                    ContainerContext(player, INTERFACE, 164, 30, storedItems, false)
                )
                CapeRackItem.values().forEachIndexed { index, item ->
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
