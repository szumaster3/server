package content.global.skill.agility.shortcuts

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Scenery

/**
 * Handles the stepping stone shortcuts.
 */
@Initializable
class SteppingStoneShortcut : OptionHandler() {
    private val stones = mutableMapOf<Location, SteppingStoneInstance>()

    private data class SteppingStoneInstance(
        val pointA: Location, val pointB: Location, val option: String, val requirements: Int
    )

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        val stone = stones[player.location] ?: return false
        if (getStatLevel(player, Skills.AGILITY) < stone.requirements) {
            sendMessage(player, "You need an agility level of ${stone.requirements} for this shortcut.")
            return true
        }

        val destination = if (player.location == stone.pointA) stone.pointB else stone.pointA
        val offsetX = (destination.x - player.location.x).coerceIn(-1, 1)
        val offsetY = (destination.y - player.location.y).coerceIn(-1, 1)

        lock(player, 3)
        player.locks.lockTeleport(3)
        queueScript(player, 2, QueueStrength.SOFT) {
            if (player.location != destination) {
                ForceMovement.run(
                    player, player.location, player.location.transform(offsetX, offsetY, 0), ANIMATION, 10
                )
                return@queueScript delayScript(player, 2)
            } else {
                return@queueScript stopExecuting(player)
            }
        }
        return true
    }

    fun configure(objects: IntArray, pointA: Location, pointB: Location, option: String, levelReq: Int) {
        val instance = SteppingStoneInstance(pointA, pointB, option, levelReq)
        objects.forEach { id ->
            SceneryDefinition.forId(id).handlers["option:$option"] = this
        }
        stones[pointA] = instance
        stones[pointB] = instance
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        // Stepping stones (Karamja).
        configure(
            intArrayOf(Scenery.STEPPING_STONES_2335, Scenery.STEPPING_STONES_2333),
            Location.create(2925, 2947, 0),
            Location.create(2925, 2951, 0),
            "cross",
            30
        )
        configure(
            intArrayOf(Scenery.STEPPING_STONE_9315),
            Location.create(3149, 3363, 0),
            Location.create(3154, 3363, 0),
            "jump-onto",
            31
        )
        return this
    }

    companion object {
        private val ANIMATION = Animation(Animations.HUMAN_JUMP_SHORT_GAP_741)
    }
}