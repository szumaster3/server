package content.global.handlers.item.boltpouch

import core.api.*
import core.api.ui.restoreTabs
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class BoltPouchInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        on(Components.XBOWS_POUCH_433) { player, _, _, _, slot, bolts ->
            val boltItemSlots = intArrayOf(2, 6, 10, 14)
            val wieldBoltSlots = intArrayOf(3, 7, 11, 15)
            val removeBoltSlots = intArrayOf(4, 8, 12, 16)
            val unwieldBoltItemSlot = 18
            val unwieldBoltSlot = 19

            for (i in boltItemSlots) {
                sendItemOnInterface(player, Components.XBOWS_POUCH_433, i, item = bolts)
            }

            val boltAmountSlots = intArrayOf(24, 25, 26, 27, 28)
            val boltNameSlots = intArrayOf(21, 22, 23, 28, 29)

            val pouch = player.getAttribute("bolt_pouch", BoltPouch)
            for (i in boltAmountSlots.indices) {
                val amount = pouch.getAmount(i)
                sendItemOnInterface(player, Components.XBOWS_POUCH_433, boltAmountSlots[i], item = bolts, amount)
            }

            for (i in boltNameSlots.indices) {
                val boltId = pouch.getBolt(i)
                val boltName = getItemName(boltId).capitalize()
                sendString(player, boltName, Components.XBOWS_POUCH_433, i)
            }

            when (slot) {
                in wieldBoltSlots -> {
                    val pouchSlot = wieldBoltSlots.indexOf(slot)
                    if (!pouch.hasBolts(pouchSlot)) {
                        sendMessage(player, "There's nothing in that slot to wield.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    pouch.wield(player, pouchSlot)
                    sendMessage(player, "You wield some bolts from your bolt pouch.")
                }

                in removeBoltSlots -> {
                    val pouchSlot = removeBoltSlots.indexOf(slot)
                    if (!pouch.hasBolts(pouchSlot)) {
                        sendMessage(player, "There's nothing to remove from that slot.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    pouch.removeToInventory(player, pouchSlot)
                    val ordinal = arrayOf("first", "second", "third", "fourth")[pouchSlot]
                    sendMessage(player, "You remove all the bolts from the $ordinal slot of your bolt pouch.")
                }

                unwieldBoltSlot -> {
                    if (player.equipment.getNew(EquipmentSlot.AMMO.ordinal) == null) {
                        sendMessage(player, "You're not wielding anything.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    BoltPouch.unwield(player)
                    sendMessage(player, "You place the items you were wielding into your pack.")
                }
            }

            return@on true
        }

        onClose(Components.XBOWS_POUCH_433) { player, _ ->
            restoreTabs(player)
            return@onClose true
        }
    }
}