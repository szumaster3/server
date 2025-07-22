package content.global.skill.agility.shortcuts

import core.api.animationCycles
import core.api.anyInEquipment
import core.api.forceMove
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

/**
 * Handles the broken pier shortcut.
 */
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

        if (!anyInEquipment(player, Items.FREMENNIK_SEA_BOOTS_1_14571, Items.FREMENNIK_SEA_BOOTS_2_14572, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
            sendMessage(player, "You need to claim the reward for the medium Fremennik diary to use this shortcut.")
            return true
        }

        forceMove(player, player.location, Location.create(2573, 3862, 0), 0, 60, null, Animations.JUMP_BRIDGE_769)
        forceMove(player, Location.create(2573, 3862, 0), Location.create(2576, 3862, 0), 90, animationCycles(Animations.JUMP_OVER_OBSTACLE_6132), null, Animations.JUMP_OVER_OBSTACLE_6132)
        return true
    }

    override fun isWalk(): Boolean = false
}