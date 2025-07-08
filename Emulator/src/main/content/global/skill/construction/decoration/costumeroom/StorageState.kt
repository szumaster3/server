package content.global.skill.construction.decoration.costumeroom

import core.game.node.entity.player.Player
import org.json.simple.JSONObject

class StorageState(val player: Player) {

    enum class ContainerGroup {
        BOOK, CAPE, FANCY, TOY, TRAILS, ARMOUR, ARMOUR_CASE;

        companion object {
            fun fromType(type: Storable.Type): ContainerGroup = when (type) {
                Storable.Type.BOOK -> BOOK
                Storable.Type.CAPE -> CAPE
                Storable.Type.FANCY -> FANCY
                Storable.Type.TOY -> TOY

                Storable.Type.LOW_LEVEL_TRAILS,
                Storable.Type.MED_LEVEL_TRAILS,
                Storable.Type.HIGH_LEVEL_TRAILS -> TRAILS

                Storable.Type.ONE_SET_OF_ARMOUR,
                Storable.Type.TWO_SETS_OF_ARMOUR,
                Storable.Type.THREE_SETS_OF_ARMOUR,
                Storable.Type.FOUR_SETS_OF_ARMOUR,
                Storable.Type.FIVE_SETS_OF_ARMOUR,
                Storable.Type.SIX_SETS_OF_ARMOUR,
                Storable.Type.ALL_SETS_OF_ARMOUR -> ARMOUR

                Storable.Type.TWO_SETS_ARMOUR_CASE,
                Storable.Type.FOUR_SETS_ARMOUR_CASE,
                Storable.Type.ALL_SETS_ARMOUR_CASE -> ARMOUR_CASE
            }
        }
    }

    val containers: MutableMap<ContainerGroup, StorageContainer> = mutableMapOf()

    init {
        for (group in ContainerGroup.values()) {
            containers[group] = StorageContainer()
        }
    }

    fun getContainer(type: Storable.Type): StorageContainer {
        return containers[ContainerGroup.fromType(type)]!!
    }

    fun toJson(): JSONObject {
        val save = JSONObject()
        val containersJson = JSONObject()
        containers.forEach { (group, container) ->
            containersJson[group.name.lowercase()] = container.toJson()
        }
        save["containers"] = containersJson
        return save
    }

    fun readJson(data: JSONObject) {
        val containersJson = data["containers"] as? JSONObject ?: return
        for (group in ContainerGroup.values()) {
            if (containersJson.containsKey(group.name.lowercase())) {
                containers[group] = StorageContainer.fromJson(
                    containersJson[group.name.lowercase()] as JSONObject
                )
            }
        }
    }
}