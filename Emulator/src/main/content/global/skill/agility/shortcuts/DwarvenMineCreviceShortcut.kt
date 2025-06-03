package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class DwarvenMineCreviceShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CREVICE_30868, IntType.SCENERY, "squeeze-through") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 42)) {
                sendDialogue(player, "You need an agility level of at least 42 to do this.")
                return@on true
            }

            val destination = if (player.location == Location(3035, 9806, 0)) Location(3028, 9806, 0) else Location(3035, 9806, 0)

            faceLocation(player, destination)
            animate(player, Animations.DUCK_UNDER_2240, true)
            playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310)

            AgilityHandler.forceWalk(player, -1, player.location, destination, Animation.create(Animations.HUMAN_TURNS_INVISIBLE_2590), 10, 0.0, null, 1)
                .endAnimation = Animation(Animations.DUCK_UNDER_2240)

            return@on true
        }
    }
}
