package content.global.skill.agility.shortcuts

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.GameWorld
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class DesertRockShortcut : InteractionListener {
    private val ROCK = Scenery.ROCK_28487
    private val ROPE = Scenery.ROPE_28490
    private val TIE_ROPE = Animations.HUMAN_TIE_ROPE_9086

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.ROPE_954, ROCK) { player, _, _ ->
            animate(player, TIE_ROPE)
            setVarbit(player, 4231, 1)
            return@onUseWith true
        }

        on(ROCK, IntType.SCENERY, "climb down") { player, _ ->
            lock(player, 1000)
            lockInteractions(player, 1000)
            GameWorld.Pulser.submit(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            1 -> teleport(player, location(3382, 2825, 1))
                            2 -> {
                                player.faceLocation(location(3382, 2823, 1))
                                animate(player, 1772)
                            }

                            5 -> {
                                teleport(player, location(3382, 2825, 0))
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(ROPE, IntType.SCENERY, "climb up") { player, _ ->
            teleport(player, location(3382, 2825, 0))
            return@on true
        }
    }
}
