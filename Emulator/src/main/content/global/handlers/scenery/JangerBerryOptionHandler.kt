package content.global.handlers.scenery

import content.global.skill.agility.AgilityHandler.forceWalk
import core.api.animateScenery
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

@Initializable
class JangerBerryOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        UseWithHandler.addHandler(
            Scenery.BRANCH_2326,
            UseWithHandler.OBJECT_TYPE,
            object : UseWithHandler(Items.ROPE_954) {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    return this
                }

                override fun handle(event: NodeUsageEvent): Boolean {
                    val `object` = event.usedWith.asScenery()
                    if (`object`.isActive) SceneryBuilder.replace(`object`, `object`.transform(2325))
                    event.player.inventory.remove(event.usedItem)
                    sendMessage(event.player, "You tie the rope to the tree...")
                    return true
                }
            },
        )
        SceneryDefinition.forId(Scenery.ROPESWING_2325).handlers["option:swing-on"] = this
        SceneryDefinition.forId(Scenery.ROPESWING_2324).handlers["option:swing-on"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (!player.location.withinDistance(node.location, 2)) {
            player.sendMessage("You cannot do that from here.")
            return true
        }
        val end = if (node.id == 2325) Location(2505, 3087, 0) else Location(2511, 3096, 0)
        animateScenery(player, node.asScenery(), 497, true)
        forceWalk(
            player,
            0,
            player.location,
            end,
            Animation.create(Animations.ROPE_SWING_751),
            20,
            22.0,
            "You skillfully swing across.",
            1,
        )
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location {
        return if (n.id == Scenery.ROPESWING_2324) Location(2511, 3092, 0) else Location(2501, 3087, 0)
    }
}
