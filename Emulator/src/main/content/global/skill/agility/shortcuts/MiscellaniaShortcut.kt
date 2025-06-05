package content.global.skill.agility.shortcuts

import content.global.handlers.item.seaBoots
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Scenery

@Initializable
class MiscellaniaShortcut : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.BROKEN_PIER_41531).handlers["option:step"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (!player.location.withinDistance(node.location, 2)) {
            sendMessage(player, "You cannot do that from here.")
            return true
        }

        if (!anyInEquipment(player, *seaBoots)) {
            sendMessage(player, "You need to claim the reward for the medium Fremennik diary to use this shortcut.")
            return true
        }

        forceMove(
            player,
            player.location,
            Location.create(2573, 3862, 0),
            0,
            animationCycles(Animations.JUMP_BRIDGE_769),
            null,
            Animations.JUMP_BRIDGE_769
        )

        forceMove(
            player,
            Location.create(2573, 3862, 0),
            Location.create(2576, 3862, 0),
            animationCycles(Animations.JUMP_BRIDGE_769),
            animationCycles(Animations.JUMP_OVER_OBSTACLE_6132),
            null,
            Animations.JUMP_OVER_OBSTACLE_6132
        )
        return true
    }

    override fun isWalk(): Boolean = false
}