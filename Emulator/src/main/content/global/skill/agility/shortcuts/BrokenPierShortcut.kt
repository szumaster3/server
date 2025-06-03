package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.animationCycles
import core.api.anyInEquipment
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class BrokenPierShortcut : InteractionListener {

    override fun defineListeners() {
        on(Scenery.BROKEN_PIER_41531, IntType.SCENERY, "step") { player, node ->
            if(player.location.withinDistance(node.location, 2)) {
                sendMessage(player, "You cannot do that from here.")
                return@on true
            }

            if (!anyInEquipment(player, Items.FREMENNIK_SEA_BOOTS_2_14572, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
                sendMessage(player, "You need to claim the reward for the medium Fremennik diary to use this shortcut.")
                return@on true
            }

            val duration = Animation(Animations.JUMP_BRIDGE_769).duration
            AgilityHandler.forceWalk(player, -1, Location(2572, 3862, 0), Location(2573, 3862, 0), Animation(Animations.JUMP_BRIDGE_769), animationCycles(Animations.JUMP_BRIDGE_769), 0.0, null)
            AgilityHandler.forceWalk(player, -1, Location.create(2573, 3862, 0), Location(2576, 3862, 0), Animation(Animations.JUMP_OVER_OBSTACLE_6132), animationCycles(Animations.JUMP_OVER_OBSTACLE_6132), 0.0, null, duration)
            return@on true
        }
    }
}
