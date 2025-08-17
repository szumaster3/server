package content.global.skill.construction.decoration.costumeroom

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class StorageContainer {
    private val stored: MutableMap<StorableType, MutableList<Int>> = mutableMapOf()
    private val currentPage: MutableMap<StorableType, Int> = mutableMapOf()

    val storedItems: List<Int>
        get() = stored.values.flatten()

    fun addItem(type: StorableType, itemId: Int) {
        stored.getOrPut(type) { mutableListOf() }.add(itemId)
    }

    fun withdraw(type: StorableType, item: Storable) {
        val id = item.takeIds.firstOrNull() ?: item.displayId
        stored[type]?.remove(id)
    }

    fun contains(type: StorableType, itemId: Int): Boolean {
        return stored[type]?.contains(itemId) == true
    }

    fun getItems(type: StorableType): List<Int> {
        return stored[type]?.toList() ?: emptyList()
    }

    fun getPageIndex(type: StorableType): Int = currentPage.getOrDefault(type, 0)

    fun nextPage(type: StorableType, totalItems: Int, pageSize: Int) {
        val page = currentPage.getOrDefault(type, 0)
        if ((page + 1) * pageSize < totalItems) {
            currentPage[type] = page + 1
        }
    }

    fun prevPage(type: StorableType) {
        val page = currentPage.getOrDefault(type, 0)
        if (page > 0) {
            currentPage[type] = page - 1
        }
    }

    fun resetPage(type: StorableType) {
        currentPage[type] = 0
    }

    fun hasNextPage(type: StorableType, pageSize: Int): Boolean {
        val total = getItems(type).size
        return (getPageIndex(type) + 1) * pageSize < total
    }

    fun hasPrevPage(type: StorableType): Boolean {
        return getPageIndex(type) > 0
    }

    fun store(type: StorableType, storable: Storable) {
        val id = storable.takeIds.firstOrNull() ?: storable.displayId
        stored.getOrPut(type) { mutableListOf() }.add(id)
    }

    fun toJson(): JsonObject {
        val obj = JsonObject()
        for ((type, list) in stored) {
            val jsonArray = JsonArray()
            for (item in list) {
                jsonArray.add(item)
            }
            obj.add(type.name.lowercase(), jsonArray)
        }
        return obj
    }

    companion object {
        fun fromJson(json: JsonObject): StorageContainer {
            val container = StorageContainer()
            for (key in json.keySet()) {
                val type = StorableType.valueOf(key.uppercase())
                val jsonArray = json.getAsJsonArray(key)
                val list = jsonArray.map { it.asLong.toInt() }.toMutableList()
                container.stored[type] = list
            }
            return container
        }
    }
}
