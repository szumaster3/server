package content.region.morytania.handlers.tarnslair.traps

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import kotlin.random.Random

@Initializable
class SpikeWallHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(20920).handlers["option:search"] = this
        return this
    }

    override fun handle(
        player: Player?,
        node: Node?,
        option: String?,
    ): Boolean {
        if (player == null || node !is Scenery || option != "search") return false
        val animId = Animations.PICK_POCKET_881
        val animDuration = animationDuration(Animation(animId))

        val floorTrap = FloorTrap.getFromCoords(player.location.x, player.location.y)
        if (floorTrap == null) {
            sendMessage(player, "You find nothing of interest.")
            return false
        }

        animate(player, Animation(animId))
        sendMessage(player, "You try to disarm the trap...")

        player.walkingQueue.reset()
        submitIndividualPulse(
            player,
            object : Pulse(animDuration) {
                override fun pulse(): Boolean {
                    if (Random.nextInt(100) < 20) {
                        sendMessage(player, "...and succeed! You quickly walk past.")
                        return true
                    } else if (Random.nextInt(100) < 5) {
                        player.moveStep()
                        animateScenery(node, 5631)
                        animate(player, Animations.STARTLED_BACK_1441)
                        impact(player, Random.nextInt(1, 5))
                        sendMessage(player, "...but unfortunately you fail.")
                        sendChat(player, "Ouch!")
                        return true
                    }
                    animate(player, Animation(animId))
                    return false
                }
            },
        )

        return true
    }
}
