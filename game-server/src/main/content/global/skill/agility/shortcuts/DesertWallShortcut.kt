package content.global.skill.agility.shortcuts

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations

/**
 * Represents the climb through desert wall shortcut.
 */
@Initializable
class DesertWallShortcut : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any>? {
        SceneryDefinition.forId(SCENERY_ID).handlers["option:climb-through"] = this
        return null
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (getStatLevel(player, Skills.AGILITY) < REQUIRED_AGILITY_LEVEL) {
            sendMessage(player, "You need an agility level of at least $REQUIRED_AGILITY_LEVEL to do this.")
            return true
        }

        player.lock(4)
        val scenery = node as Scenery

        if (scenery.id == SCENERY_ID) {
            ForceMovement.run(
                player,
                START_LOCATION,
                scenery.location,
                CLIMB_DOWN,
                Animation(Animations.HUMAN_TURNS_INVISIBLE_2590),
                node.direction
            )
            submitIndividualPulse(
                player,
                object : Pulse(1, player) {
                    var counter = 0
                    override fun pulse(): Boolean =
                        when (++counter) {
                            2 -> {
                                animate(player, CRAWL_THROUGH)
                                teleport(player, END_LOCATION)
                                false
                            }

                            3 -> {
                                ForceMovement.run(player, END_LOCATION, END_LOCATION, CLIMB_UP)
                                true
                            }

                            else -> false
                        }
                },
            )
        }
        return true
    }

    companion object {
        const val REQUIRED_AGILITY_LEVEL = 21
        const val SCENERY_ID = org.rs.consts.Scenery.HOLE_6620
        val START_LOCATION: Location = Location.create(3320, 2796, 0)
        val END_LOCATION: Location = Location.create(3324, 2796, 0)
        val CLIMB_DOWN: Animation = Animation.create(Animations.CRAWL_UNDER_WALL_A_2589)
        val CRAWL_THROUGH: Animation = Animation.create(Animations.HUMAN_TURNS_INVISIBLE_2590)
        val CLIMB_UP: Animation = Animation.create(Animations.CRAWL_UNDER_WALL_C_2591)
    }
}
