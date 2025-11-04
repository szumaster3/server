package content.global.skill.construction.decoration

import content.global.skill.construction.BuildHotspot
import content.global.skill.construction.HousingStyle
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class DoorPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (style in HousingStyle.values()) {
            SceneryDefinition.forId(style.doorId).handlers["option:open"] = this
            SceneryDefinition.forId(style.secondDoorId).handlers["option:open"] = this
        }
        for (deco in BuildHotspot.DUNGEON_DOOR_LEFT.decorations) {
            SceneryDefinition.forId(deco.objectId).handlers["option:open"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:pick-lock"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:force"] = this
        }
        for (deco in BuildHotspot.DUNGEON_DOOR_RIGHT.decorations) {
            SceneryDefinition.forId(deco.objectId).handlers["option:open"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:pick-lock"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:force"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val door = node as Scenery
        val second = DoorActionHandler.getSecondDoor(door) ?: return true

        when (option) {
            "open" -> {
                DoorActionHandler.open(
                    door, second,
                    getReplaceId(door), getReplaceId(second),
                    true, -1,  false
                )
                SceneryBuilder.remove(door)
                SceneryBuilder.remove(second)
            }
        }

        return true
    }


    private fun getReplaceId(`object`: Scenery): Int {
        for (data in REPLACEMENT) {
            if (`object`.id == data[0]) {
                return data[1]
            }
        }
        return `object`.id + 6
    }



    companion object {
        val REPLACEMENT = arrayOf(
            intArrayOf(13100, 13102),
            intArrayOf(13101, 13103),
            intArrayOf(13006, 13008),
            intArrayOf(13007, 13008),
            intArrayOf(13015, 13017),
            intArrayOf(13016, 13018),
            intArrayOf(13094, 13095),
            intArrayOf(13096, 13097),
            intArrayOf(13109, 13110),
            intArrayOf(13107, 13108),
            intArrayOf(13118, 13120),
            intArrayOf(13119, 13121)
        )
    }
}