package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.quest.hasRequirement
import core.api.queueScript
import core.api.sendDialogue
import core.api.stopExecuting
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Quests
import org.rs.consts.Scenery

class BarrowsShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.BROKEN_FENCE_18411, IntType.SCENERY, "climb-over") { player, _ ->
            if (!hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE)) {
                sendDialogue(player, "Um... those vampyres don't look very nice. I'm not going through here.")
                return@on true
            }
            handleShortcut(player)
            return@on true
        }
    }

    private fun handleShortcut(player: Player) {
        queueScript(player, 1, QueueStrength.SOFT) {
            val direction = if (player.location.y < 3264) Direction.NORTH else Direction.SOUTH
            AgilityHandler.forceWalk(
                player,
                -1,
                player.location,
                player.location.transform(direction, 1),
                Animation(Animations.WALK_OVER_STILE_10980),
                10,
                0.0,
                null,
            )
            return@queueScript stopExecuting(player)
        }
    }
}
