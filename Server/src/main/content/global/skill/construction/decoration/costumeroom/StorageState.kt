package content.global.skill.construction.decoration.costumeroom

import com.google.gson.JsonObject
import core.game.node.entity.player.Player

class StorageState(val player: Player) {

    enum class ContainerGroup {
        BOOK, CAPE, FANCY, TOY, TRAILS, ARMOUR, ARMOUR_CASE;

        companion object {
            fun fromType(type: StorableType): ContainerGroup = when (type) {
                StorableType.BOOK -> BOOK
                StorableType.CAPE -> CAPE
                StorableType.FANCY -> FANCY
                StorableType.TOY -> TOY

                StorableType.LOW_LEVEL_TRAILS,
                StorableType.MED_LEVEL_TRAILS,
                StorableType.HIGH_LEVEL_TRAILS -> TRAILS

                StorableType.ONE_SET_OF_ARMOUR,
                StorableType.TWO_SETS_OF_ARMOUR,
                StorableType.THREE_SETS_OF_ARMOUR,
                StorableType.FOUR_SETS_OF_ARMOUR,
                StorableType.FIVE_SETS_OF_ARMOUR,
                StorableType.SIX_SETS_OF_ARMOUR,
                StorableType.ALL_SETS_OF_ARMOUR -> ARMOUR

                StorableType.TWO_SETS_ARMOUR_CASE,
                StorableType.FOUR_SETS_ARMOUR_CASE,
                StorableType.ALL_SETS_ARMOUR_CASE -> ARMOUR_CASE
            }
        }
    }

    val containers: MutableMap<ContainerGroup, StorageContainer> = mutableMapOf()

    init {
        for (group in ContainerGroup.values()) {
            containers[group] = StorageContainer()
        }
    }

    fun getContainer(type: StorableType): StorageContainer {
        return containers[ContainerGroup.fromType(type)]!!
    }

    fun toJson(): JsonObject {
        val save = JsonObject()
        val containersJson = JsonObject()
        containers.forEach { (group, container) ->
            containersJson.add(group.name.lowercase(), container.toJson())
        }
        save.add("containers", containersJson)
        return save
    }

    fun readJson(data: JsonObject) {
        val containersJson = data.getAsJsonObject("containers") ?: return
        for (group in ContainerGroup.values()) {
            if (containersJson.has(group.name.lowercase())) {
                containers[group] = StorageContainer.fromJson(
                    containersJson.getAsJsonObject(group.name.lowercase())
                )
            }
        }
    }

}