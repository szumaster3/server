package content.region.kandarin.handlers.guilds.ranging

import core.api.setAttribute
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.DoorActionHandler
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Ranging guild plugin.
 */
@Initializable
class RangingGuildPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(2514).handlers["option:open"] = this
        SceneryDefinition.forId(2511).handlers["option:climb-up"] = this
        SceneryDefinition.forId(2512).handlers["option:climb-down"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val id = if (node is Scenery) node.getId() else 0
        when (option) {
            "open" ->
                when (id) {
                    2514 -> {
                        if (player.location.y >= 3438) {
                            if (player.getSkills().getStaticLevel(Skills.RANGE) < 40) {
                                player.dialogueInterpreter.sendDialogue("You need a Ranging level of 40 to enter here.")
                                return true
                            }
                        }
                        handleAutowalkDoor(
                            player,
                            (node as Scenery),
                            if (player.location.y >= 3438) {
                                Location.create(2659, 3437, 0)
                            } else {
                                Location.create(
                                    2657,
                                    3439,
                                    0,
                                )
                            },
                        )
                    }
                }

            "climb-up" ->
                when (id) {
                    2511 -> {
                        setAttribute(player, "ladder", node)
                        player.interfaceManager.open(Component(564))
                    }
                }

            "climb-down" ->
                when (id) {
                    2512 -> climb(player, null, Location.create(2668, 3427, 0))
                }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n is Scenery) {
            if (n.definition.hasAction("open")) {
                if (n.getId() == 2514) {
                    return if (node.location.y >= 3438) {
                        Location.create(2657, 3439, 0)
                    } else {
                        Location.create(2659, 3437, 0)
                    }
                }
                return DoorActionHandler.getDestination((node as Player), n)
            }
            if (n.getId() == 2513) return Location.create(2673, 3420, 0)
        }
        return null
    }
}
