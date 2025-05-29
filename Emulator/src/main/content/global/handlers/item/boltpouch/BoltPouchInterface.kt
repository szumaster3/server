package content.global.handlers.item.boltpouch

import content.global.handlers.item.boltpouch.BoltPouch.updateBoltPouchDisplay
import core.api.freeSlots
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class BoltPouchInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        /*
         * Handles the visual layer of the pouch itself.
         */

        onOpen(Components.XBOWS_POUCH_433) { player, _ ->
            updateBoltPouchDisplay(player)
            return@onOpen true
        }

        /*
         * Handles interaction with pouch.
         */

        on(Components.XBOWS_POUCH_433) { player, _, _, _, slot, _ ->
            val wieldBoltSlots = listOf(3, 7, 11, 15)
            val removeBoltSlots = listOf(4, 8, 12, 16)
            val unwieldBoltSlot = 19

            when (slot) {
                in wieldBoltSlots -> {
                    val pouchSlot = wieldBoltSlots.indexOf(slot)
                    if (!BoltPouch.hasBolts(player, pouchSlot)) {
                        sendMessage(player, "There's nothing in that slot to wield.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    if (BoltPouch.wield(player, pouchSlot)) {
                        sendMessage(player, "You wield some bolts from your bolt pouch.")
                    }
                }

                in removeBoltSlots -> {
                    val pouchSlot = removeBoltSlots.indexOf(slot)
                    if (!BoltPouch.hasBolts(player, pouchSlot)) {
                        sendMessage(player, "There's nothing to remove from that slot.")
                        return@on true
                    }
                    if (freeSlots(player) == 0) {
                        sendMessage(player, "You don't have enough space in your inventory to do that.")
                        return@on true
                    }
                    if (BoltPouch.removeToInventory(player, pouchSlot)) {
                        val ordinal = arrayOf("first", "second", "third", "fourth")[pouchSlot]
                        sendMessage(player, "You remove all the bolts from the $ordinal slot of your bolt pouch.")
                    }
                }

                unwieldBoltSlot -> {
                    if (BoltPouch.unwield(player)) {
                        sendMessage(player, "You place the items you were wielding into your pack.")
                    }
                }
            }

            updateBoltPouchDisplay(player)
            return@on true
        }
    }
}
