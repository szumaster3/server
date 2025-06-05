package content.global.skill.agility.shortcuts

import core.api.getStatLevel
import core.api.sendMessage
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery

/**
 * Represents the shortcut found in the Ectofuntus basement.
 */
class WeatheredWallShortcut : InteractionListener {

    override fun defineListeners() {
        on(WEATHERED_WALL, IntType.SCENERY, "jump-up", "jump-down") { player, node ->
            if (getStatLevel(player, Skills.AGILITY) < 58) {
                sendMessage(player, "You need an Agility level of at least 58 to climb this wall.")
                return@on true
            }

            val isJumpingUp = node.id == Scenery.WEATHERED_WALL_9308
            val destination = if (isJumpingUp) Location(3671, 9888, 2) else Location(3670, 9888, 3)

            player.faceLocation(destination)
            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> player.animate(if (isJumpingUp) FIRST_ANIM else UP_ANIM)
                        1 -> {
                            if (isJumpingUp) player.animate(SECOND_ANIM)
                            player.properties.teleportLocation = destination
                            return true
                        }
                    }
                    return false
                }
            })

            return@on true
        }
    }

    companion object {
        val WEATHERED_WALL          = intArrayOf(Scenery.WEATHERED_WALL_9307, Scenery.WEATHERED_WALL_9308)
        val FIRST_ANIM: Animation   = Animation(Animations.JUMP_OFF_LEDGE_BEGIN_2586)
        val SECOND_ANIM: Animation  = Animation(Animations.JUMP_OFF_LEDGE_END_2588)
        val UP_ANIM: Animation      = Animation(Animations.CLIMB_WALL_A_737)
    }
}