package content.global.plugins.item.equipment.bolt_pouch

import core.api.freeSlots
import core.api.openInterface
import core.api.sendMessage
import core.api.sendString
import core.game.component.Component
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import shared.consts.Components
import shared.consts.Items

private val BOLT_POUCH = Items.BOLT_POUCH_9433
private val EQUIP_SLOTS = listOf(3, 7, 11, 15)
private val UNEQUIP_SLOTS = 19
private val REMOVE_SLOTS = listOf(4, 8, 12, 16)

class BoltPouchPlugin : InteractionListener , InterfaceListener {

    override fun defineListeners() {
        on(Items.BOLT_POUCH_9433, IntType.ITEM, "open") { player, _ ->
            openInterface(player, Components.XBOWS_POUCH_433)
            return@on true
        }

        onUseWith(IntType.ITEM, BoltPouchManager.ALLOWED_BOLT_IDS, Items.BOLT_POUCH_9433) { player, item, otherItem ->
            val pouchItem = if (item.id == Items.BOLT_POUCH_9433) item else otherItem
            val boltItem = if (pouchItem == item) otherItem else item

            val addedAmount = player.boltPouchManager.addBolts(pouchItem.id, boltItem.id, boltItem.asItem().amount)
            if (addedAmount <= 0) {
                sendMessage(player, "You can't add that type of bolt or your pouch is full.")
            } else {
                sendMessage(player, "Added $addedAmount bolts to the pouch.")
                player.inventory.remove(boltItem.asItem())
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.XBOWS_POUCH_433) { player, _, _, _, slot, _ ->
            val pouch = player.boltPouchManager ?: run {
                sendMessage(player, "Bolt pouch data not found.")
                return@on true
            }

            when (slot) {
                in EQUIP_SLOTS -> {
                    val pouchSlot = EQUIP_SLOTS.indexOf(slot)
                    if (!pouch.hasBoltsInSlot(BOLT_POUCH, pouchSlot)) {
                        sendMessage(player, "There's nothing in that slot to wield.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    if (pouch.wieldFromSlot(BOLT_POUCH, pouchSlot, player)) {
                        sendMessage(player, "You wield some bolts from your bolt pouch.")
                    }
                }

                in REMOVE_SLOTS -> {
                    val pouchSlot = REMOVE_SLOTS.indexOf(slot)
                    if (!pouch.hasBoltsInSlot(BOLT_POUCH, pouchSlot)) {
                        sendMessage(player, "There's nothing to remove from that slot.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    if (pouch.removeSlotToInventory(BOLT_POUCH, pouchSlot, player)) {
                        val ordinal = arrayOf("first", "second", "third", "fourth")[pouchSlot]
                        sendMessage(player, "You remove all the bolts from the $ordinal slot of your bolt pouch.")
                    }
                }

                UNEQUIP_SLOTS -> {
                    if (pouch.unwield(player)) {
                        sendMessage(player, "You place the items you were wielding into your pack.")
                    } else {
                        sendMessage(player, "You have no bolts wielded or your pouch is full.")
                    }
                }

                else -> return@on false
            }

            updateBoltPouch(player)
            return@on true
        }
    }

    fun open(player: Player) {
        player.interfaceManager.open(Component(Components.XBOWS_POUCH_433))
        updateBoltPouch(player)
    }

    private fun updateBoltPouch(player: Player) {
        val pouch = player.boltPouchManager ?: return
        val container = pouch.boltPouchContainers[BOLT_POUCH] ?: return

        container.toArray().forEachIndexed { index, _ ->
            val componentId = when(index) {
                0 -> 3
                1 -> 7
                2 -> 11
                3 -> 15
                else -> -1
            }
            if (componentId != -1) {
                val text = pouch.getSlotDisplayText(BOLT_POUCH, index)
                sendString(player, text, Components.XBOWS_POUCH_433, componentId)
            }
        }
    }
}
