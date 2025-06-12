package content.region.morytania.plugin.tarnslair.plugin.traps

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations

@Initializable
class TrapHandler : OptionHandler() {
    private val buttonID = org.rs.consts.Scenery.FLOOR_20966

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(buttonID).handlers["option:search"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String, ): Boolean {
        val target = node as? Scenery ?: return false
        val chance = RandomFunction.getSkillSuccessChance(50.0, 100.0, getStatLevel(player, Skills.THIEVING))
        val roll = RandomFunction.random(0.0, 100.0)

        sendMessage(player, "You try to disarm the trap...")

        lock(player, 1)
        faceLocation(player, target.location)
        animate(player, Animations.ARMS_OUT_TOGETHER_3071)

        queueScript(player, 1, QueueStrength.SOFT) {
            if (roll < chance) {
                sendMessage(player, "...you hear a click.")
            } else {
                impact(player, RandomFunction.random(1, 6))
                sendMessage(player, "...but unfortunately you fail.")
            }
            return@queueScript stopExecuting(player)
        }
        return true
    }
}
