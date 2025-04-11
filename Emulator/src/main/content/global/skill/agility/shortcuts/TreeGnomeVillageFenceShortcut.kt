package content.global.skill.agility.shortcuts

import core.api.forceMove
import core.api.sendMessageWithDelay
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Direction
import org.rs.consts.Scenery

class TreeGnomeVillageFenceShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.LOOSE_RAILING_2186, IntType.SCENERY, "squeeze-through") { player, _ ->
            val direction = if (player.location.y >= 3161) Direction.SOUTH else Direction.NORTH
            val destination = player.location.transform(direction, 1)
            forceMove(player, player.location, destination, 0, 80, anim = 3844)
            sendMessageWithDelay(player, "You squeeze through the loose railing.", 1)
            return@on true
        }
    }
}
