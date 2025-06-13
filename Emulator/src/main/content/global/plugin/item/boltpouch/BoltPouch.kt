package content.global.plugin.item.boltpouch

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.colorize
import org.rs.consts.Components

/**
 * Object representing the Bolt Pouch.
 */
object BoltPouch {
    const val MAX_PER_SLOT = 255

    /**
     * Sets the bolt data (ID and amount) for a specific slot in the bolt pouch.
     *
     * @param player The player whose bolt pouch data is being modified.
     * @param slot The slot to modify (0 to 3).
     * @param boltId The ID of the bolt to store.
     * @param amount The amount of bolts to store in the slot.
     */
    private fun setBolt(player: Player, slot: Int, boltId: Int, amount: Int) {
        player.setAttribute("/save:bolt_pouch_id_$slot", boltId)
        player.setAttribute("/save:bolt_pouch_amount_$slot", amount)
    }

    /**
     * Clears the bolt data (sets ID to -1 and amount to 0) for a specific slot in the bolt pouch.
     *
     * @param player The player whose bolt pouch data is being modified.
     * @param slot The slot to clear (0 to 3).
     */
    private fun clearBolt(player: Player, slot: Int) {
        setBolt(player, slot, -1, 0)
    }

    /**
     * Checks if the player has any bolts in a specific slot.
     *
     * @param player The player whose bolt pouch is being checked.
     * @param slot The slot to check (0 to 3).
     * @return True if there are bolts in the slot, false otherwise.
     */
    fun hasBolts(player: Player, slot: Int): Boolean {
        return getAmount(player, slot) > 0
    }

    /**
     * Gets the ID of the bolt stored in a specific slot of the player's bolt pouch.
     *
     * @param player The player whose bolt pouch data is being accessed.
     * @param slot The slot to retrieve the bolt ID from (0 to 3).
     * @return The bolt ID, or -1 if no bolt is stored in the slot.
     */
    private fun getBolt(player: Player, slot: Int): Int {
        return player.getAttribute("bolt_pouch_id_$slot") ?: -1
    }

    /**
     * Gets the amount of bolts stored in a specific slot of the player's bolt pouch.
     *
     * @param player The player whose bolt pouch data is being accessed.
     * @param slot The slot to retrieve the bolt amount from (0 to 3).
     * @return The amount of bolts stored in the slot.
     */
    private fun getAmount(player: Player, slot: Int): Int {
        return player.getAttribute("bolt_pouch_amount_$slot") ?: 0
    }

    /**
     * Removes a specific bolt from the player's bolt pouch and adds it to the player's inventory.
     *
     * @param player The player whose bolt pouch is being modified.
     * @param slot The slot from which the bolt will be removed (0 to 3).
     * @return True if the bolt was successfully removed and added to the inventory, false otherwise.
     */
    fun removeToInventory(player: Player, slot: Int): Boolean {
        val id = getBolt(player, slot)
        val amt = getAmount(player, slot)

        if (amt <= 0) return false
        if (!player.inventory.add(Item(id, amt))) {
            sendMessage(player, "You don't have enough space in your inventory to do that.")
            return false
        }

        clearBolt(player, slot)
        return true
    }

    /**
     * Wields a specific bolt from the player's bolt pouch, placing it into the equipped ammo slot.
     *
     * @param player The player wielding the bolt.
     * @param slot The slot from which the bolt will be wielded (0 to 3).
     * @return True if the bolt was successfully wielded, false otherwise.
     */
    fun wield(player: Player, slot: Int): Boolean {
        val id = getBolt(player, slot)
        val amt = getAmount(player, slot)

        if (amt <= 0) {
            sendMessage(player, "There's nothing in that slot to wield.")
            return false
        }

        val AMMO_SLOT = EquipmentSlot.AMMO.ordinal
        val currentlyEquipped = player.equipment[AMMO_SLOT]
        if (currentlyEquipped != null && !player.inventory.add(currentlyEquipped)) {
            sendMessage(player, "You don't have enough space in your inventory to do that.")
            return false
        }

        player.equipment.add(Item(id, amt))
        clearBolt(player, slot)
        sendMessage(player, "You wield some bolts from your bolt pouch.")
        return true
    }

    /**
     * Unwields the current bolts from the player's ammo slot and places them back into the inventory.
     *
     * @param player The player unwielding the bolts.
     * @return True if the bolts were successfully unwielded, false otherwise.
     */
    fun unwield(player: Player): Boolean {
        val AMMO_SLOT = EquipmentSlot.AMMO.ordinal
        val ammo = player.equipment[AMMO_SLOT] ?: run {
            sendMessage(player, "You're not wielding anything.")
            return false
        }

        if (!player.inventory.add(ammo)) {
            sendMessage(player, "You don't have enough space in your inventory to do that.")
            return false
        }

        player.equipment.remove(ammo)
        sendMessage(player, "You place the items you were wielding into your pack.")
        return true
    }

    /**
     * Stores a bolt in the player's bolt pouch if space is available and if the bolt is allowed.
     *
     * @param player The player storing the bolt.
     * @param item The bolt item to store.
     * @return True if the bolt was successfully stored, false otherwise.
     */
    fun storeBolt(player: Player, item: Item): Boolean {
        if (!isAllowedBolt(item.id)) {
            sendMessage(player, "You may only put crossbow bolts in the bolt pouch.")
            return false
        }

        for (i in 0..3) {
            val id = getBolt(player, i)
            val amount = getAmount(player, i)

            if (id == item.id || id == -1) {
                val canStore = (MAX_PER_SLOT - amount).coerceAtMost(item.amount)
                if (canStore <= 0) return false

                if (!player.inventory.remove(Item(item.id, canStore))) return false

                val newAmount = amount + canStore
                val boltId = if (id == -1) item.id else id

                setBolt(player, i, boltId, newAmount)

                val boltName = getItemName(item.id).lowercase()
                if (newAmount == MAX_PER_SLOT) {
                    sendMessage(player, "You completely fill one of the slots in the bolt pouch with $boltName.")
                } else {
                    sendMessage(player, "You store some $boltName in the bolt pouch.")
                }
                return true
            }
        }

        sendMessage(player, "You don't have space to store that type of bolt.")
        return false
    }

    /**
     * Updates the display of the bolt pouch showing amounts, names, and sprites.
     *
     * @param player The player whose bolt pouch is being updated.
     */
    fun updateBoltPouchDisplay(player: Player) {
        val boltAmount = intArrayOf(20, 21, 22, 23, 24)
        val boltName = intArrayOf(25, 26, 27, 28)
        val itemSprite = intArrayOf(2, 6, 10, 14)

        // Current ammo slot:
        val current = player.equipment.getNew(13).copy()
        if(current != null) {
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, 18, current.id, current.amount)
        } else {
            sendModelOnInterface(player, Components.XBOWS_POUCH_433, 18, -1)
        }

        sendString(player, if (current.id == -1) "Nothing" else current.name, Components.XBOWS_POUCH_433, 29)
        sendString(player, if (current.amount > 0) colorize("%G${current.amount}") else "0", Components.XBOWS_POUCH_433, 24)

        // Pouch slots:
        for (i in 0 until 4) {
            val boltId = getBolt(player, i)
            val amount = getAmount(player, i)
            sendString(player, if (amount > 0) colorize("%G$amount") else "0", Components.XBOWS_POUCH_433, boltAmount[i])
            sendString(player, if (boltId != -1) getItemName(boltId) else "Nothing", Components.XBOWS_POUCH_433, boltName[i])
            val def = if (boltId != -1) boltId else null
            if (def != null) {
                sendItemOnInterface(player, Components.XBOWS_POUCH_433, itemSprite[i], boltId, amount)
            } else {
                sendModelOnInterface(player, Components.XBOWS_POUCH_433, itemSprite[i], -1)
            }
        }
    }

    /**
     * Checks if the provided item ID corresponds to a valid bolt type.
     *
     * @param id The ID of the item to check.
     * @return True if the item is a bolt, false otherwise.
     */
    private fun isAllowedBolt(id: Int): Boolean {
        val name = Item(id).name.lowercase()
        return name.contains("bolt", true)
    }
}
