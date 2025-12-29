package custom.guild.warehouse

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import custom.guild.Guild
import custom.guild.warehouse.slot.WarehouseSlot
import custom.guild.warehouse.slot.WarehouseSlotPermission

class GuildWarehouse(
    private val guild: Guild
) {

    private var slots = Array(maxCapacity()) { WarehouseSlot() }

    private fun maxCapacity(): Int = GuildWarehouseLevel.capacityForLevel(guild.level)

    fun deposit(item: Item): Int {
        var remaining = item.amount

        for (slot in slots) {//s
            val it = slot.item ?: continue
            if (slot.locked) continue
            if (it.id == item.id && it.definition.isStackable) {
                val toAdd = remaining.coerceAtMost(Int.MAX_VALUE - it.amount)
                it.amount += toAdd
                remaining -= toAdd
                if (remaining == 0) return 0
            }
        }

        for (slot in slots) {//f
            if (slot.item == null && !slot.locked) {
                slot.item = Item(item.id, remaining)
                return 0
            }
        }

        return remaining
    }

    fun withdraw(p: Player, itemId: Int, amount: Int): Item? {
        val rank = guild.members[p.index] ?: return null

        var remaining = amount
        var taken = 0

        for (slot in slots) {
            val item = slot.item ?: continue
            if (slot.locked) continue
            if (item.id != itemId) continue

            if (!slot.permission.canWithdraw(rank)) continue

            val toTake = remaining.coerceAtMost(item.amount)
            item.amount -= toTake
            remaining -= toTake
            taken += toTake

            if (item.amount <= 0) slot.item = null
            if (remaining == 0) break
        }

        return if (taken > 0) Item(itemId, taken) else null
    }

    fun setSlotPermission(slotIndex: Int, permission: WarehouseSlotPermission): Boolean {
        val slot = slots.getOrNull(slotIndex) ?: return false
        if (guild.gold < permission.changeCost) return false

        guild.gold -= permission.changeCost
        slot.permission = permission
        return true
    }

    fun setSlotLocked(slotIndex: Int, locked: Boolean) {
        slots.getOrNull(slotIndex)?.locked = locked
    }

    fun expand() {
        val newCapacity = maxCapacity()
        if (slots.size >= newCapacity) return

        val old = slots.size
        val newSlots = Array(newCapacity) { i -> if (i < old) slots[i] else WarehouseSlot() }
        slots = newSlots
    }

    fun freeSlots(): Int = slots.count { it.item == null && !it.locked }

    fun allItems(): List<Item> = slots.mapNotNull { it.item }

    fun toJson(): JsonObject {
        val root = JsonObject()
        val arr = JsonArray()

        for (slot in slots) {
            val obj = JsonObject()
            slot.item?.let {
                obj.addProperty("id", it.id)
                obj.addProperty("amount", it.amount)
            }
            obj.addProperty("perm", slot.permission.name)
            obj.addProperty("locked", slot.locked)
            arr.add(obj)
        }

        root.add("slots", arr)
        return root
    }

    companion object {
        fun fromJson(guild: Guild, json: JsonObject): GuildWarehouse {
            val wh = GuildWarehouse(guild)
            val arr = json.getAsJsonArray("slots")

            for (i in 0 until arr.size()) {
                val obj = arr[i].asJsonObject
                val slot = wh.slots[i]

                if (obj.has("id")) {
                    slot.item = Item(
                        obj.get("id").asInt, obj.get("amount").asInt
                    )
                }

                slot.permission = WarehouseSlotPermission.valueOf(obj.get("perm").asString)
                slot.locked = obj.get("locked").asBoolean
            }

            return wh
        }
    }
}
