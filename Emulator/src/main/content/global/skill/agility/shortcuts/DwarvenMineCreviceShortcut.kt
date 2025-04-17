package content.global.skill.agility.shortcuts

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery

class DwarvenMineCreviceShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CREVICE_30868, IntType.SCENERY, "squeeze-through") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 42)) {
                sendDialogue(player, "You need an agility level of at least 42 to do this.")
                return@on true
            }

            val destination =
                if (player.location == Location(3035, 9806, 0)) {
                    Location(3028, 9806, 0)
                } else {
                    Location(3035, 9806, 0)
                }

            lock(player, 8)
            face(player, node)
            animate(player, Animations.DUCK_UNDER_2240)
            forceMove(player, player.location, destination, 0, 240, null, Animations.HUMAN_TURNS_INVISIBLE_2590)
            runTask(player, 7) {
                animate(player, Animations.DUCK_UNDER_2240)
            }
            return@on true
        }
    }
}
