package content.global.skill.construction.decoration

import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * The type Fireplace plugin.
 */
@Initializable
class FireplacePlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(13609).handlers["option:light"] = this
        SceneryDefinition.forId(13611).handlers["option:light"] = this
        SceneryDefinition.forId(13613).handlers["option:light"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (!player.inventory.contains(Items.LOGS_1511, 1) || !player.inventory.contains(Items.TINDERBOX_590, 1)) {
            player.sendMessage("You need some logs and a tinderbox in order to light the fireplace.")
            return true
        }
        val obj = node.asScenery()
        player.lock(2)
        player.animate(ANIMATION)
        Pulser.submit(object : Pulse(2, player) {
            override fun pulse(): Boolean {
                if (!obj.isActive) {
                    return true
                }
                player.inventory.remove(Item(Items.LOGS_1511))
                player.getSkills().addExperience(Skills.FIREMAKING, 80.0)
                SceneryBuilder.replace(
                    Scenery(obj.id, obj.location),
                    Scenery(obj.id + 1, obj.location, obj.rotation),
                    1000
                )
                player.sendMessage("You light the fireplace.")
                return true
            }
        })
        return true
    }

    companion object {
        private val ANIMATION: Animation = Animation.create(Animations.TINDERBOX_3658)
    }
}