package content.global.handlers.item.boltpouch

import core.api.EquipmentSlot
import core.api.getItemName
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * Object representing the Bolt Pouch.
 */
object BoltPouch {

    private const val ATTR_BOLT_IDS = "/save:bolt_pouch_ids"
    private const val ATTR_BOLT_AMOUNTS = "/save:bolt_pouch_amount"
    const val MAX_PER_SLOT = 255

    /**
     * Retrieves the bolt IDs stored in the player's bolt pouch.
     *
     * @param player The player whose bolt pouch data is being accessed.
     * @return An array of integers representing the bolt IDs stored in the pouch.
     */
    private fun getBoltIds(player: Player): IntArray {
        return player.getAttribute(ATTR_BOLT_IDS) ?: IntArray(4) { -1 }
    }

    /**
     * Retrieves the amounts of each bolt stored in the player's bolt pouch.
     *
     * @param player The player whose bolt pouch data is being accessed.
     * @return An array of integers representing the amounts of each bolt type.
     */
    private fun getBoltAmounts(player: Player): IntArray {
        return player.getAttribute(ATTR_BOLT_AMOUNTS) ?: IntArray(4)
    }

    /**
     * Sets the bolt IDs and amounts for the player's bolt pouch.
     *
     * @param player The player whose bolt pouch data is being set.
     * @param ids An array of bolt IDs to store in the pouch.
     * @param amounts An array of amounts corresponding to each bolt ID.
     */
    private fun setBoltData(player: Player, ids: IntArray, amounts: IntArray) {
        player.setAttribute(ATTR_BOLT_IDS, ids)
        player.setAttribute(ATTR_BOLT_AMOUNTS, amounts)
    }

    /**
     * Checks if the player has any bolts in the specified slot of their bolt pouch.
     *
     * @param player The player whose bolt pouch is being checked.
     * @param slot The slot to check for bolts.
     * @return True if the slot contains bolts, false otherwise.
     */
    fun hasBolts(player: Player, slot: Int): Boolean {
        val amounts = getBoltAmounts(player)
        return amounts.getOrNull(slot) ?: 0 > 0
    }

    /**
     * Retrieves the ID of the bolt in the specified slot.
     *
     * @param slot The slot number to retrieve the bolt ID from.
     * @return The ID of the bolt in the given slot.
     */
    fun getBolt(player: Player, slot: Int): Int {
        return getBoltIds(player).getOrNull(slot) ?: -1
    }

    /**
     * Retrieves the amount of bolts in the specified slot.
     *
     * @param slot The slot number to retrieve the bolt amount from.
     * @return The amount of bolts in the given slot.
     */
    fun getAmount(player: Player, slot: Int): Int {
        return getBoltAmounts(player).getOrNull(slot) ?: 0
    }

    /**
     * Removes bolts from the specified slot and adds them to the player's inventory.
     *
     * @param player The player performing the action.
     * @param slot The slot from which to remove the bolts.
     * @return True if the removal was successful, false if there were no bolts to remove or if the inventory is full.
     */
    fun removeToInventory(player: Player, slot: Int): Boolean {
        val ids = getBoltIds(player)
        val amounts = getBoltAmounts(player)
        val id = ids[slot]
        val amt = amounts[slot]

        if (amt <= 0) return false
        if (!player.inventory.add(Item(id, amt))) {
            sendMessage(player, "You don't have enough space in your inventory to do that.")
            return false
        }

        ids[slot] = -1
        amounts[slot] = 0
        setBoltData(player, ids, amounts)
        return true
    }

    /**
     * Wields bolts from the specified slot into the player's equipment.
     *
     * @param player The player performing the action.
     * @param slot The slot containing the bolts to wield.
     * @return True if the wielding was successful, false if the slot is empty or if the inventory is full.
     */
    fun wield(player: Player, slot: Int): Boolean {
        val ids = getBoltIds(player)
        val amounts = getBoltAmounts(player)
        val id = ids[slot]
        val amt = amounts[slot]

        if (amt <= 0) {
            sendMessage(player, "There's nothing in that slot to wield.")
            return false
        }

        val AMMO_SLOT = EquipmentSlot.AMMO.ordinal
        val currentlyEquipped = player.equipment[AMMO_SLOT]
        if (currentlyEquipped != null) {
            if (!player.inventory.add(currentlyEquipped)) {
                sendMessage(player, "You don't have enough space in your inventory to do that.")
                return false
            }
        }

        player.equipment.add(Item(id, amt))
        ids[slot] = -1
        amounts[slot] = 0
        setBoltData(player, ids, amounts)
        sendMessage(player, "You wield some bolts from your bolt pouch.")
        return true
    }

    /**
     * Unwields the currently equipped bolts and places them back in the player's inventory.
     *
     * @param player The player performing the action.
     * @return True if the unwielding was successful, false if no bolts are currently equipped.
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
     * Stores bolts in the bolt pouch if there is space.
     *
     * @param player The player performing the action.
     * @param item The bolt item to store in the pouch.
     * @return True if the bolt was stored successfully, false if there is no space in the pouch.
     */
    fun storeBolt(player: Player, item: Item): Boolean {
        if (!isAllowedBolt(item.id)) {
            sendMessage(player, "You may only put crossbow bolts in the bolt pouch.")
            return false
        }

        val ids = getBoltIds(player)
        val amounts = getBoltAmounts(player)

        for (i in ids.indices) {
            if (ids[i] == item.id || ids[i] == -1) {
                val current = amounts[i]
                val canStore = (MAX_PER_SLOT - current).coerceAtMost(item.amount)
                if (canStore <= 0) return false

                if (!player.inventory.remove(Item(item.id, canStore))) return false

                if (ids[i] == -1) ids[i] = item.id
                amounts[i] += canStore

                setBoltData(player, ids, amounts)

                val boltName = getItemName(item.id).lowercase()
                if (amounts[i] == MAX_PER_SLOT) {
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
     * Checks if the provided item ID corresponds to a valid bolt type.
     *
     * @param id The ID of the item to check.
     * @return True if the item is a bolt, false otherwise.
     */
    private fun isAllowedBolt(id: Int): Boolean {
        val name = Item(id).name.lowercase()
        return name.contains("bolt")
    }
}
