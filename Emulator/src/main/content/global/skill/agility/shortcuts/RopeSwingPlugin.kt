package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

@Initializable
class RopeSwingPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        UseWithHandler.addHandler(2326, UseWithHandler.OBJECT_TYPE, object : UseWithHandler(Items.ROPE_954) {
            override fun newInstance(arg: Any?) = this

            override fun handle(event: NodeUsageEvent): Boolean {
                val player = event.player
                val obj = event.usedWith.asScenery()
                val rope = event.usedItem

                if (!withinDistance(player, obj.location, 2)) {
                    sendMessage(player, "You cannot do that from here.")
                    return true
                }

                if (obj.isActive && player.inventory.remove(rope)) {
                    SceneryBuilder.replace(obj, obj.transform(2325))
                    playAudio(player, Sounds.TIGHTROPE_2495)
                    animate(player, Animations.SUMMON_ROPE_SWING_775)
                    sendMessage(player, "You tie the rope to the tree...")

                    val end = Location(2505, 3087, 0)
                    val swingAnimation = Animation.create(Animations.ROPE_SWING_751)

                    ropeDelay = GameWorld.ticks + animationDuration(ROPE_ANIM)

                    AgilityHandler.forceWalk(
                        player,
                        0,
                        player.location,
                        end,
                        swingAnimation,
                        50,
                        22.0,
                        "You skillfully swing across."
                    )
                }

                return true
            }
        })

        SceneryDefinition.forId(2325).handlers["option:swing-on"] = this
        SceneryDefinition.forId(2324).handlers["option:swing-on"] = this

        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (!player.location.withinDistance(node.location, 2)) {
            sendMessage(player, "You cannot do that from here.")
            return true
        }

        if (ropeDelay > GameWorld.ticks) {
            sendMessage(player, "The rope is being used.")
            return true
        }

        val end = if (node.id == 2325) Location(2505, 3087, 0) else Location(2511, 3096, 0)
        val swingAnimation = Animation.create(Animations.ROPE_SWING_751)

        player.faceLocation(end)
        playAudio(player, Sounds.SWING_ACROSS_2494)
        animateScenery(player, node.asScenery(), ROPE_ANIM.id, true)

        ropeDelay = GameWorld.ticks + animationDuration(ROPE_ANIM)

        AgilityHandler.forceWalk(
            player,
            0,
            player.location,
            end,
            swingAnimation,
            50,
            22.0,
            "You skillfully swing across."
        )
        return true
    }

    override fun getDestination(node: Node, n: Node): Location {
        return if (n.id == 2324) Location(2511, 3092, 0) else Location(2501, 3087, 0)
    }

    companion object {
        /**
         * Rope usage delay to prevent overlapping animations.
         */
        private var ropeDelay: Int = 0

        /**
         * Rope swing animation.
         */
        private val ROPE_ANIM = Animation.create(497)
    }
}
