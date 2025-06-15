package content.global.plugin.item.boltpouch

import core.api.freeSlots
import core.api.getItemName
import core.api.sendMessage
import core.api.sendString
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.Items

class BoltPouchInterface : InterfaceListener {

    private val pouchItemId = Items.BOLT_POUCH_9433
    private val wieldSlots = listOf(3, 7, 11, 15)
    private val removeSlots = listOf(4, 8, 12, 16)
    private val unwieldSlot = 19

    override fun defineInterfaceListeners() {
        on(Components.XBOWS_POUCH_433) { player, _, _, _, slot, _ ->
            val pouch = player.boltPouchManager ?: run {
                sendMessage(player, "Bolt pouch data not found.")
                return@on true
            }

            when (slot) {
                in wieldSlots -> {
                    val pouchSlot = wieldSlots.indexOf(slot)
                    if (!pouch.hasBoltsInSlot(pouchItemId, pouchSlot)) {
                        sendMessage(player, "There's nothing in that slot to wield.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    if (pouch.wieldFromSlot(pouchItemId, pouchSlot, player)) {
                        sendMessage(player, "You wield some bolts from your bolt pouch.")
                    }
                }

                in removeSlots -> {
                    val pouchSlot = removeSlots.indexOf(slot)
                    if (!pouch.hasBoltsInSlot(pouchItemId, pouchSlot)) {
                        sendMessage(player, "There's nothing to remove from that slot.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    if (pouch.removeSlotToInventory(pouchItemId, pouchSlot, player)) {
                        val ordinal = arrayOf("first", "second", "third", "fourth")[pouchSlot]
                        sendMessage(player, "You remove all the bolts from the $ordinal slot of your bolt pouch.")
                    }
                }

                unwieldSlot -> {
                    if (pouch.unwield(player)) {
                        sendMessage(player, "You place the items you were wielding into your pack.")
                    } else {
                        sendMessage(player, "You have no bolts wielded or your pouch is full.")
                    }
                }

                else -> return@on false
            }

            updateBoltPouchDisplay(player)
            true
        }
    }

    fun open(player: Player) {
        player.interfaceManager.open(Component(Components.XBOWS_POUCH_433))
        updateBoltPouchDisplay(player)
    }

    private fun updateBoltPouchDisplay(player: Player) {
        val pouch = player.boltPouchManager ?: return
        val container = pouch.boltPouchContainers[pouchItemId] ?: return

        container.toArray().forEachIndexed { index, _ ->
            val componentId = when(index) {
                0 -> 3
                1 -> 7
                2 -> 11
                3 -> 15
                else -> -1
            }
            if (componentId != -1) {
                val text = pouch.getSlotDisplayText(pouchItemId, index)
                sendString(player, text, Components.XBOWS_POUCH_433, componentId)
            }
        }
    }
}
