package content.region.desert.handlers

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items

@Initializable
class KalphiteEntranceHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        val handler =
            object : UseWithHandler(Items.ROPE_954) {
                override fun handle(event: NodeUsageEvent): Boolean {
                    val node = event.usedWith as Scenery
                    if (node.id == 3827 || node.id == 23609) {
                        if (removeItem(event.player, event.usedItem)) {
                            replaceScenery(node, node.id + 1, 500)
                            return true
                        }
                    }
                    return false
                }

                override fun newInstance(arg: Any?): Plugin<Any> {
                    return this
                }
            }

        UseWithHandler.addHandler(3827, UseWithHandler.OBJECT_TYPE, handler)
        UseWithHandler.addHandler(23609, UseWithHandler.OBJECT_TYPE, handler)

        SceneryDefinition.forId(3828).handlers["option:climb-down"] = this
        SceneryDefinition.forId(3829).handlers["option:climb-up"] = this
        SceneryDefinition.forId(23610).handlers["option:climb-down"] = this
        SceneryDefinition.forId(3832).handlers["option:climb-up"] = this

        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val node = node as Scenery
        var destination: Location? = null
        when (node.id) {
            23610 -> destination = Location.create(3508, 9493, 0)
            3832 -> destination = Location.create(3509, 9496, 2)
        }
        val dest = destination
        lock(player, 2)
        animate(player, Animations.USE_LADDER_828)
        queueScript(player, 1, QueueStrength.WEAK) {
            player.properties.teleportLocation = dest
            return@queueScript stopExecuting(player)
        }
        return true
    }
}
