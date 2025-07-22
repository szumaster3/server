package content.global.skill.construction.decoration.costumeroom

import org.json.simple.JSONObject

class StorageContainer {
    private val stored: MutableMap<Storable.Type, MutableList<Int>> = mutableMapOf()
    private val currentPage: MutableMap<Storable.Type, Int> = mutableMapOf()

    val storedItems: List<Int>
        get() = stored.values.flatten()

    fun addItem(type: Storable.Type, itemId: Int) {
        stored.getOrPut(type) { mutableListOf() }.add(itemId)
    }

    fun withdraw(type: Storable.Type, item: Storable) {
        val id = item.takeIds.firstOrNull() ?: item.displayId
        stored[type]?.remove(id)
    }

    fun contains(type: Storable.Type, itemId: Int): Boolean {
        return stored[type]?.contains(itemId) == true
    }

    fun getItems(type: Storable.Type): List<Int> {
        return stored[type]?.toList() ?: emptyList()
    }

    fun getPageIndex(type: Storable.Type): Int = currentPage.getOrDefault(type, 0)

    fun nextPage(type: Storable.Type, totalItems: Int, pageSize: Int) {
        val page = currentPage.getOrDefault(type, 0)
        if ((page + 1) * pageSize < totalItems) {
            currentPage[type] = page + 1
        }
    }

    fun prevPage(type: Storable.Type) {
        val page = currentPage.getOrDefault(type, 0)
        if (page > 0) {
            currentPage[type] = page - 1
        }
    }

    fun resetPage(type: Storable.Type) {
        currentPage[type] = 0
    }

    fun hasNextPage(type: Storable.Type, pageSize: Int): Boolean {
        val total = getItems(type).size
        return (getPageIndex(type) + 1) * pageSize < total
    }

    fun hasPrevPage(type: Storable.Type): Boolean {
        return getPageIndex(type) > 0
    }

    fun store(type: Storable.Type, storable: Storable) {
        val id = storable.takeIds.firstOrNull() ?: storable.displayId
        stored.getOrPut(type) { mutableListOf() }.add(id)
    }

    fun toJson(): JSONObject {
        val obj = JSONObject()
        for ((type, list) in stored) {
            obj[type.name.lowercase()] = list
        }
        return obj
    }

    companion object {
        fun fromJson(json: JSONObject): StorageContainer {
            val container = StorageContainer()
            for (key in json.keys) {
                val type = Storable.Type.valueOf((key as String).uppercase())
                val list = json[key] as List<Long>
                container.stored[type] = list.map { it.toInt() }.toMutableList()
            }
            return container
        }
    }
}
