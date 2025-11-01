package content.global.skill.runecrafting

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.api.*
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class PouchManager(val player: Player) {
    val pouches = mapOf(
        Items.SMALL_POUCH_5509 to Pouches(3, 3, 1),
        Items.MEDIUM_POUCH_5510 to Pouches(6, 264, 25),
        Items.LARGE_POUCH_5512 to Pouches(9, 186, 50),
        Items.GIANT_POUCH_5514 to Pouches(12, 140, 75),
    )

    fun addToPouch(itemId: Int, amount: Int, essence: Int) {
        val pouchId = if (isDecayedPouch(itemId)) itemId - 1 else itemId
        if (!checkRequirement(pouchId)) {
            sendMessage(player, "You lack the required level to use this pouch.")
            return
        }
        var amt = amount
        val pouch = pouches[pouchId]
        val otherEssence = when (essence) {
            Items.RUNE_ESSENCE_1436 -> Items.PURE_ESSENCE_7936
            Items.PURE_ESSENCE_7936 -> Items.RUNE_ESSENCE_1436
            else -> 0
        }
        pouch ?: return
        if (amount > pouch.container.freeSlots()) {
            amt = pouch.container.freeSlots()
        }
        if (amt == pouch.container.freeSlots()) {
            sendMessage(player, "Your pouch is full.")
        }
        if (pouch.container.contains(otherEssence, 1)) {
            sendMessage(player, "You can only store one type of essence in each pouch.")
            return
        }

        var disappeared = false
        if (itemId != Items.SMALL_POUCH_5509) {
            pouch.charges -= amt
        }
        if (pouch.charges <= 0) {
            pouch.currentCap -= when (pouchId) {
                Items.MEDIUM_POUCH_5510 -> 1
                Items.LARGE_POUCH_5512 -> 2
                Items.GIANT_POUCH_5514 -> 3
                else -> 0
            }
            if (pouch.currentCap <= 0) {
                if (removeItem(player, itemId)) {
                    disappeared = true
                    sendMessage(player, "Your pouch has degraded completely.")

                    pouch.currentCap = pouch.capacity
                    pouch.charges = pouch.maxCharges
                    pouch.remakeContainer()
                }
            } else {
                if (!isDecayedPouch(itemId)) {
                    val slot = player.inventory.getSlot(Item(itemId))
                    replaceSlot(player, slot, Item(itemId + 1))
                }
                sendMessage(
                    player,
                    "Your pouch has decayed through use.",
                )
                pouch.charges = 9 * pouch.currentCap
                pouch.remakeContainer()
                if (amt > pouch.currentCap) {
                    amt = pouch.currentCap
                }
            }
        }
        val essItem = Item(essence, amt)
        if (!disappeared && removeItem(player, essItem)) {
            pouch.container.add(essItem)
        }
    }

    fun withdrawFromPouch(itemId: Int) {
        val pouchId = if (isDecayedPouch(itemId)) itemId - 1 else itemId
        val pouch = pouches[pouchId]
        pouch ?: return
        val playerFree = freeSlots(player)
        var amount = pouch.currentCap - pouch.container.freeSlots()
        if (amount > playerFree) {
            amount = playerFree
        } else {
            sendMessage(player, "Your pouch has no essence left in it.")
            if (amount == 0) {
                return
            }
        }
        val essence = Item(pouch.container.get(0).id, amount)
        pouch.container.remove(essence)
        pouch.container.shift()
        addItem(player, essence.id, essence.amount)
    }

    fun save(root: JsonObject) {
        val pouches = JsonArray()

        for ((id, pouch) in this.pouches) {
            val pouchJson = JsonObject()
            pouchJson.addProperty("id", id.toString())

            val items = JsonArray()
            for (item in pouch.container.toArray()) {
                if (item == null) continue
                val it = JsonObject()
                it.addProperty("itemId", item.id.toString())
                it.addProperty("amount", item.amount.toString())
                items.add(it)
            }
            pouchJson.add("container", items)
            pouchJson.addProperty("charges", pouch.charges.toString())
            pouchJson.addProperty("currentCap", pouch.currentCap.toString())

            pouches.add(pouchJson)
        }
        root.add("pouches", pouches)
    }

    fun parse(data: JsonArray) {
        for (element in data) {
            val pouchJson = element.asJsonObject
            val id = pouchJson.get("id").asInt
            val p = pouches[id] ?: continue
            val charges = pouchJson.get("charges").asInt
            val currentCap = pouchJson.get("currentCap").asInt

            p.charges = charges
            p.currentCap = currentCap
            p.remakeContainer()

            val containerArray = pouchJson.getAsJsonArray("container")
            for (itemElement in containerArray) {
                val it = itemElement.asJsonObject
                if (!it.has("itemId")) continue
                val itemId = it.get("itemId").asInt
                val amount = it.get("amount").asInt
                p.container.add(Item(itemId, amount))
            }
        }
    }

    private fun checkRequirement(pouchId: Int): Boolean {
        val p = pouches[pouchId]
        p ?: return false
        return player.skills.getLevel(Skills.RUNECRAFTING) >= p.levelRequirement
    }

    fun checkAmount(itemId: Int) {
        val pouchId = if (isDecayedPouch(itemId)) itemId - 1 else itemId
        val p = pouches[pouchId]
        p ?: return
        player.sendMessage("This pouch has space for ${p.container.freeSlots()} more essence.")
    }

    private fun isDecayedPouch(pouchId: Int): Boolean {
        if (pouchId == Items.MEDIUM_POUCH_5510) return false
        return pouches[pouchId - 1] != null
    }

    class Pouches(val capacity: Int, val maxCharges: Int, val levelRequirement: Int) {
        var container = Container(capacity)
        var currentCap = capacity
        var charges = maxCharges

        fun remakeContainer() {
            this.container = Container(currentCap)
        }
    }
}
