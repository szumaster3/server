package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityCourse.Companion.getHitAmount
import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.impact
import core.api.sendMessage
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations

/**
 * Handles dungeon pipes shortcuts include one pipe from barbarian outpost.
 */
@Initializable
class PipeShortcut : AgilityShortcut {

    constructor() : super(intArrayOf(), 0, 0.0, "")
    constructor(id: Int, level: Int, experience: Double) : super(intArrayOf(id), level, experience, "squeeze-through")

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(PipeShortcut(YANILLE_DUNGEON, 49, 7.5))
        configure(PipeShortcut(BRIMHAVEN_DRAGONS, 22, 8.5))
        configure(PipeShortcut(BRIMHAVEN_GIANTS, 34, 10.0))
        configure(PipeShortcut(TAVERLEY_DUNGEON, 70, 10.0))
        configure(PipeShortcut(BARBARIAN_OUTPOST, 35, 10.0))
        configure(PipeShortcut(EDGEVILLE_DUNGEON, 51, 10.0))
        return this
    }

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean) {
        val pipe = obj.id
        val roll = getHitAmount(player)
        val fail = AgilityHandler.hasFailed(player, 1, 0.015)

        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                when (pipe) {
                    YANILLE_DUNGEON, TAVERLEY_DUNGEON -> {
                        player.lock(7)
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            player.location,
                            pipeDestination(player, obj, 6),
                            CLIMB_THROUGH,
                            10,
                            0.0,
                            null
                        )
                        player.animate(CRAWL, 4)
                        player.animate(CLIMB_OUT, 5)
                        if (fail) impact(player, amount = roll, ImpactHandler.HitsplatType.NORMAL, 7)
                    }

                    BRIMHAVEN_GIANTS, BRIMHAVEN_DRAGONS -> {
                        player.lock(5)
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            player.location,
                            pipeDestination(player, obj, 7),
                            CLIMB_THROUGH,
                            10,
                            0.0,
                            null
                        )
                        player.animate(CRAWL, 5)
                        player.animate(CLIMB_OUT, 6)
                        if (fail) impact(player, amount = roll, ImpactHandler.HitsplatType.NORMAL, 5)
                    }

                    BARBARIAN_OUTPOST -> {
                        if (player.location.x != 2552) {
                            sendMessage(player, "I can't get into this pipe at that angle.")
                            return true
                        }
                        player.lock(3)
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            player.location,
                            pipeDestination(player, obj, 3),
                            Animation.create(10580),
                            15,
                            0.0,
                            null
                        )
                        return true
                    }

                    EDGEVILLE_DUNGEON -> {
                        if (player.location.y != 9906) {
                            sendMessage(player, "I can't get into this pipe at that angle.")
                            return true
                        }
                        player.lock(7)
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            player.location,
                            pipeDestination(player, obj, 6),
                            CLIMB_THROUGH,
                            10,
                            0.0,
                            null
                        )
                        player.animate(CRAWL, 4)
                        player.animate(CLIMB_OUT, 5)
                        if (fail) impact(player, amount = roll, ImpactHandler.HitsplatType.NORMAL, 7)
                        return true
                    }
                }
                return false
            }
        })
    }

    companion object {
        private const val YANILLE_DUNGEON = 2290
        private const val BRIMHAVEN_DRAGONS = 5099
        private const val BRIMHAVEN_GIANTS = 5100
        private const val TAVERLEY_DUNGEON = 9293
        private const val BARBARIAN_OUTPOST = 20210
        private const val EDGEVILLE_DUNGEON = 29370

        private val CRAWL = Animation(Animations.HUMAN_CRAWLS_844)
        private val CLIMB_THROUGH = Animation(Animations.CLIMB_THROUGH_OBSTACLE_10580)
        private val CLIMB_OUT = Animation(Animations.CLIMB_OUT_OF_OBSTACLE_10579)
    }
}
