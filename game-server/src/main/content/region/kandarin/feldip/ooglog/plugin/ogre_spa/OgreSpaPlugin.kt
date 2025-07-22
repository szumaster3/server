package content.region.kandarin.feldip.ooglog.plugin.ogre_spa

import core.api.refreshAppearance
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles ogre spa interactions.
 */
@Initializable
class OgreSpaPlugin : OptionHandler() {

    private val spaPools = listOf(
        ZoneBorders(2522, 2842, 2527, 2848, 0),// BANDOS_POOL
        ZoneBorders(2533, 2853, 2542, 2856, 0),// SUPPHUR_POOL
        ZoneBorders(2555, 2861, 2559, 2866, 0),// SALT_POOL
        ZoneBorders(2572, 2863, 2577, 2866, 0),// THEMRAL_POOL
        ZoneBorders(2592, 2859, 2600, 2862, 0) // MUD_POOL
    )

    private fun inside(player: Player) = spaPools.any { it.insideBorder(player) }

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (i in intArrayOf(29057, 29058, 29018, 29031, 29032, 29044, 29045, 29004, 29005)) {
            SceneryDefinition.forId(i).handlers["option:climb-over"] = this
        }
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        if (player == null || node !is Scenery) return false

        val delta = Location.getDelta(player.location, node.location)
        val destination = node.location.transform(delta)
        val direction = Direction.getDirection(player.location, node.location)

        val isInside = !inside(player)
        val anim = if (isInside) 8698 else 8694
        val animDuration = if (isInside) 13 else 8

        ForceMovement.run(
            player,
            player.location,
            destination,
            Animation(anim),
            Animation(anim),
            direction,
            animDuration
        )

        if (isInside) player.appearance.setAnimations(Animation(1139)) else player.appearance.setDefaultAnimations()
        refreshAppearance(player)
        return true
    }
}