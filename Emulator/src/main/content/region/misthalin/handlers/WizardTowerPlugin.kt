package content.region.misthalin.handlers

import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class WizardTowerPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(org.rs.consts.Scenery.DOOR_11993).handlers["option:open"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (option) {
            "open" -> {
                if (node.location == Location(3107, 3162, 0)) {
                    DoorActionHandler.handleAutowalkDoor(
                        player,
                        node as Scenery,
                        if (player.location.x >= 3107) {
                            Location.create(3106, 3161, 0)
                        } else {
                            Location.create(
                                3108,
                                3163,
                                0,
                            )
                        },
                    )
                } else {
                    DoorActionHandler.handleDoor(player, node as Scenery)
                }
            }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n is Scenery) {
            val `object` = n
            if (`object`.id == org.rs.consts.Scenery.DOOR_11993 && `object`.location == Location(3107, 3162, 0)) {
                return if (node.location.x >= 3107) Location.create(3108, 3163, 0) else Location.create(3106, 3161, 0)
            }
        }
        return null
    }
}
