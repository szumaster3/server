package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.update.flag.context.Animation
import org.rs.consts.Scenery

class CombatTrainingCampShortcut : InteractionListener {

    override fun defineListeners() {
        on(Scenery.LOOSE_RAILING_19171, IntType.SCENERY, "squeeze-through") { player, node ->
            val start = if (player.location.x <= 2522) node.location else node.location.transform(1, 0, 0)
            player.lock(1)
            AgilityHandler.forceWalk(
                player,
                -1,
                start,
                start.transform(if (player.location.x <= 2522) 1 else -1, 0, 0),
                Animation.create(2240),
                5,
                1.0,
                null,
                0
            )
            return@on true
        }
    }
}