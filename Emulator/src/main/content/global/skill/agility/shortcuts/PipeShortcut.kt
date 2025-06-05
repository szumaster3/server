package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.ticksToCycles
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Represents the various Pipe shortcuts.
 */
@Initializable
class PipeShortcut : AgilityShortcut {

    constructor() : super(intArrayOf(), 0, 0.0, "")

    constructor(ids: IntArray?, level: Int, experience: Double, vararg options: String?) : super(ids, level, experience, *options,)

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(PipeShortcut(intArrayOf(YANILLE_DUNGEON), 49, 0.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(BRIMHAVEN_DRAGONS), 22, 8.5, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(BRIMHAVEN_GIANTS), 34, 10.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(TAVERLEY_DUNGEON), 70, 10.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(BARBARIAN_OUTPOST), 35, 10.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(EDGEVILLE_DUNGEON), 51, 10.0, "squeeze-through"))
        return this
    }

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean, ) {
        val lockTime: Int
        val interactionTime: Int
        val soundDelay: Int

        when (obj.id) {
            YANILLE_DUNGEON, TAVERLEY_DUNGEON -> {
                lockTime = 6
                interactionTime = 6
                soundDelay = 5
            }
            BRIMHAVEN_DRAGONS, BRIMHAVEN_GIANTS -> {
                lockTime = 7
                interactionTime = 7
                soundDelay = 6
            }
            BARBARIAN_OUTPOST -> {
                if (player.location.x != 2552) {
                    sendMessage(player, "I can't get into this pipe at that angle.")
                    return
                }
                lockTime = 3
                interactionTime = 0
                soundDelay = 2
            }
            EDGEVILLE_DUNGEON -> {
                if (player.location.y != 9906) {
                    sendMessage(player, "I can't get into this pipe at that angle.")
                    return
                }
                lockTime = 6
                interactionTime = 6
                soundDelay = 5
            }
            else -> return
        }

        lock(player, lockTime)
        queueScript(player, 0, QueueStrength.SOFT) {
            forceMove(
                player,
                player.location,
                pipeDestination(player, obj, interactionTime),
                0,
                ticksToCycles(lockTime),
                null,
                if (obj.id == BARBARIAN_OUTPOST) Animations.CLIMB_THROUGH_OBSTACLE_10580 else Animations.CLIMB_INTO_OBSTACLE_10578
            )
            if (interactionTime > 0) {
                player.animate(Animation(Animations.HUMAN_TURNS_INVISIBLE_2590), 2)
                player.animate(Animation(Animations.CLIMB_OUT_OF_OBSTACLE_10579), interactionTime)
            }
            playAudio(player, Sounds.SQUEEZE_OUT_2490, soundDelay)
            return@queueScript stopExecuting(player)
        }
    }

    companion object {
        private const val YANILLE_DUNGEON = 2290
        private const val BRIMHAVEN_DRAGONS = 5099
        private const val BRIMHAVEN_GIANTS = 5100
        private const val TAVERLEY_DUNGEON = 9293
        private const val BARBARIAN_OUTPOST = 20210
        private const val EDGEVILLE_DUNGEON = 29370
    }
}
