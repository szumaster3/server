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

@Initializable
class SteppingStoneShortcut : OptionHandler() {
    private val stones = mutableMapOf<Location, SteppingStoneInstance>()

    internal data class SteppingStoneInstance(
        val pointA: Location,
        val pointB: Location,
        val option: String,
        val levelReq: Int,
    )

    override fun handle(
        player: Player?,
        node: Node?,
        option: String?,
    ): Boolean {
        player ?: return false
        val stone = stones[player.location] ?: return false

        if (player.skills.getLevel(Skills.AGILITY) < stone.levelReq) {
            sendMessage(player, "You need an agility level of ${stone.levelReq} for this shortcut.")
            return true
        }

        val finalDest = if (player.location == stone.pointA) stone.pointB else stone.pointA
        val offset = getOffset(player, finalDest)
        player.debug("Offset: ${offset.first},${offset.second}")

        lock(player, 3)
        player.locks.lockTeleport(3)
        queueScript(player, 2, QueueStrength.SOFT) {
            if (player.location != finalDest) {
                lock(player, 3)
                player.locks.lockTeleport(3)
                ForceMovement.run(
                    player,
                    player.location,
                    player.location.transform(offset.first, offset.second, 0),
                    ANIMATION,
                    10,
                )
                delayScript(player, 2)
            } else {
                stopExecuting(player)
            }
        }
        return true
    }

    private fun getOffset(
        player: Player,
        location: Location,
    ): Pair<Int, Int> {
        val diffX = (location.x - player.location.x).coerceIn(-1, 1)
        val diffY = (location.y - player.location.y).coerceIn(-1, 1)
        return Pair(diffX, diffY)
    }

    fun configure(
        objects: IntArray,
        pointA: Location,
        pointB: Location,
        option: String,
        levelReq: Int,
    ) {
        val instance = SteppingStoneInstance(pointA, pointB, option, levelReq)
        objects.forEach {
            SceneryDefinition.forId(it).handlers["option:$option"] = this
        }
        stones[pointA] = instance
        stones[pointB] = instance
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(intArrayOf(2335, 2333), Location.create(2925, 2947, 0), Location.create(2925, 2951, 0), "cross", 30)
        configure(intArrayOf(9315), Location.create(3149, 3363, 0), Location.create(3154, 3363, 0), "jump-onto", 31)
        return this
    }

    companion object {
        private val ANIMATION = Animation(Animations.HUMAN_JUMP_SHORT_GAP_741)
    }
}
