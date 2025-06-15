package content.global.plugin.item.boltpouch

import core.api.addItem
import core.api.getItemName
import core.api.sendMessage
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs.consts.Items

class BoltPouchManager(val player: Player) {

    companion object {
        const val MAX_BOLTS_PER_SLOT = 255
        const val MAX_SLOTS = 4
    }

    val boltPouchContainers = mutableMapOf<Int, Container>()

    fun addBolts(pouchItemId: Int, boltItemId: Int, amount: Int): Int {
        val container = boltPouchContainers.getOrPut(pouchItemId) { Container(MAX_SLOTS) }

        if (!isAllowedBolt(boltItemId)) {
            sendMessage(player, "You can't add that type of bolt to the pouch.")
            return 0
        }

        val existingSlotIndex = container.toArray().indexOfFirst { it?.id == boltItemId }

        return if (existingSlotIndex != -1) {
            val currentAmount = container.getAmount(boltItemId)
            val toAdd = (MAX_BOLTS_PER_SLOT - currentAmount).coerceAtMost(amount)
            if (toAdd <= 0) {
                sendMessage(player, "That slot in the pouch is full.")
                0
            } else {
                container.add(Item(boltItemId, toAdd))
                sendMessage(player, "Added $toAdd bolts to the pouch.")
                toAdd
            }
        } else {
            if (container.freeSlots() <= 0) {
                sendMessage(player, "Your bolt pouch is full.")
                0
            } else {
                val toAdd = amount.coerceAtMost(MAX_BOLTS_PER_SLOT)
                container.add(Item(boltItemId, toAdd))
                sendMessage(player, "Added $toAdd bolts to the pouch.")
                toAdd
            }
        }
    }

    fun withdrawBolts(pouchItemId: Int, boltItemId: Int, amount: Int) {
        val container = boltPouchContainers[pouchItemId] ?: return

        val currentAmount = container.getAmount(boltItemId)
        if (currentAmount < amount) {
            sendMessage(player, "There are not enough bolts in the pouch.")
            return
        }

        if (!player.inventory.hasSpaceFor(Item(boltItemId, amount))) {
            sendMessage(player, "Not enough inventory space.")
            return
        }

        container.remove(Item(boltItemId, amount))
        container.shift()
        addItem(player, boltItemId, amount)
        sendMessage(player, "You withdraw $amount bolts from the pouch.")
    }

    private fun isAllowedBolt(boltId: Int): Boolean {
        val allowedBolts = setOf(
            Items.ADAMANT_BOLTSP_PLUS_9297,
            Items.ADAMANT_BOLTSP_PLUS_PLUS_9304,
            Items.ADAMANT_BOLTS_9143,
            Items.ADAMANT_BOLTS_P_9290,
            Items.BLACK_BOLTSP_13084,
            Items.BLACK_BOLTSP_PLUS_13085,
            Items.BLACK_BOLTSP_PLUS_PLUS_13086,
            Items.BLACK_BOLTS_13083,
            Items.BLURITE_BOLTSP_9286,
            Items.BLURITE_BOLTSP_PLUS_9293,
            Items.BLURITE_BOLTSP_PLUS_PLUS_9300,
            Items.BLURITE_BOLTS_9139,
            Items.BROAD_TIPPED_BOLTS_13280,
            Items.BRONZE_BOLTSP_878,
            Items.BRONZE_BOLTSP_PLUS_6061,
            Items.BRONZE_BOLTSP_PLUS_PLUS_6062,
            Items.BRONZE_BOLTS_877,
            Items.DIAMOND_BOLTS_9340,
            Items.DIAMOND_BOLTS_E_9243,
            Items.DRAGON_BOLTS_9341,
            Items.DRAGON_BOLTS_E_9244,
            Items.EMERALD_BOLTS_9338,
            Items.EMERALD_BOLTS_E_9241,
            Items.IRON_BOLTSP_PLUS_9294,
            Items.IRON_BOLTSP_PLUS_PLUS_9301,
            Items.IRON_BOLTS_9140,
            Items.IRON_BOLTS_P_9287,
            Items.JADE_BOLTS_9335,
            Items.JADE_BOLTS_E_9237,
            Items.MITHRIL_BOLTSP_PLUS_9296,
            Items.MITHRIL_BOLTSP_PLUS_PLUS_9303,
            Items.MITHRIL_BOLTS_9142,
            Items.MITHRIL_BOLTS_P_9289,
            Items.ONYX_BOLTS_9342,
            Items.ONYX_BOLTS_E_9245,
            Items.OPAL_BOLTS_879,
            Items.OPAL_BOLTS_E_9236,
            Items.PEARL_BOLTS_880,
            Items.PEARL_BOLTS_E_9238,
            Items.RUBY_BOLTS_9339,
            Items.RUBY_BOLTS_E_9242,
            Items.RUNE_BOLTS_9144,
            Items.RUNITE_BOLTSP_PLUS_9298,
            Items.RUNITE_BOLTSP_PLUS_PLUS_9305,
            Items.RUNITE_BOLTS_P_9291,
            Items.SAPPHIRE_BOLTS_9337,
            Items.SAPPHIRE_BOLTS_E_9240,
            Items.SILVER_BOLTSP_PLUS_9299,
            Items.SILVER_BOLTSP_PLUS_PLUS_9306,
            Items.SILVER_BOLTS_9145,
            Items.SILVER_BOLTS_P_9292,
            Items.STEEL_BOLTSP_PLUS_9295,
            Items.STEEL_BOLTSP_PLUS_PLUS_9302,
            Items.STEEL_BOLTS_9141,
            Items.STEEL_BOLTS_P_9288,
            Items.TOPAZ_BOLTS_9336,
            Items.TOPAZ_BOLTS_E_9239
        )
        return boltId in allowedBolts
    }

    fun hasBolts(pouchItemId: Int): Boolean {
        val container = boltPouchContainers[pouchItemId] ?: return false
        return container.toArray().any { it != null }
    }

    fun getSlotDisplayText(pouchItemId: Int, slot: Int): String {
        val container = boltPouchContainers[pouchItemId] ?: return "Empty"
        val item = container.get(slot)
        return if (item == null) "Empty" else "${item.amount} x ${getItemName(item.id)}"
    }

    fun hasBoltsInSlot(pouchItemId: Int, slot: Int): Boolean {
        val container = boltPouchContainers[pouchItemId] ?: return false
        val item = container.get(slot)
        return item != null && item.amount > 0
    }

    fun wieldFromSlot(pouchItemId: Int, slot: Int, player: Player): Boolean {
        val container = boltPouchContainers[pouchItemId] ?: return false
        val item = container.get(slot) ?: return false
        if (!player.inventory.hasSpaceFor(item)) return false
        container.remove(item)
        container.shift()
        player.inventory.add(item)
        return true
    }

    fun unwield(player: Player): Boolean {
        val pouchId = Items.BOLT_POUCH_9433
        val container = boltPouchContainers.getOrPut(pouchId) { Container(MAX_SLOTS) }

        val boltsInInventory = player.inventory.toArray().filterNotNull().filter { isAllowedBolt(it.id) }

        if (boltsInInventory.isEmpty()) {
            sendMessage(player, "You have no bolts wielded.")
            return false
        }

        val freeSlots = container.freeSlots() + container.toArray().count { it != null && it.amount < MAX_BOLTS_PER_SLOT }
        if (freeSlots == 0) {
            sendMessage(player, "Your bolt pouch is full.")
            return false
        }

        var addedAny = false
        boltsInInventory.forEach { bolt ->
            val added = addBolts(pouchId, bolt.id, bolt.amount)
            if (added > 0) {
                player.inventory.remove(Item(bolt.id, added))
                addedAny = true
            }
        }

        return addedAny
    }

    fun removeSlotToInventory(pouchItemId: Int, slot: Int, player: Player): Boolean {
        val container = boltPouchContainers[pouchItemId] ?: return false
        val item = container.get(slot) ?: return false
        if (!player.inventory.hasSpaceFor(item)) return false
        container.remove(item)
        container.shift()
        player.inventory.add(item)
        return true
    }

    fun save(root: JSONObject) {
        val arr = JSONArray()
        boltPouchContainers.forEach { (pouchId, container) ->
            val obj = JSONObject().apply {
                put("pouchId", pouchId)
                put("bolts", JSONArray().apply {
                    container.toArray().forEach { item ->
                        if (item == null) return@forEach
                        add(JSONObject().apply {
                            put("id", item.id)
                            put("amount", item.amount)
                        })
                    }
                })
            }
            arr.add(obj)
        }
        root["bolt_pouches"] = arr
    }

    fun parse(data: JSONArray) {
        boltPouchContainers.clear()
        data.forEach { e ->
            val obj = e as JSONObject
            val pouchId = obj["pouchId"].toString().toInt()
            val boltsArr = obj["bolts"] as JSONArray
            val container = Container(MAX_SLOTS)
            boltsArr.forEach { b ->
                val boltObj = b as JSONObject
                val id = boltObj["id"].toString().toInt()
                val amount = boltObj["amount"].toString().toInt()
                container.add(Item(id, amount))
            }
            boltPouchContainers[pouchId] = container
        }
    }
}