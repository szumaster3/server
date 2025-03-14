package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.lock
import core.api.playAudio
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Sounds

@Initializable
class PipeShortcut : AgilityShortcut {
    constructor() : super(intArrayOf(), 0, 0.0, "")
    constructor(ids: IntArray?, level: Int, experience: Double, vararg options: String?) : super(
        ids,
        level,
        experience,
        *options,
    )

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(PipeShortcut(intArrayOf(YANILLE_DUNGEON), 49, 0.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(BRIMHAVEN_DRAGONS), 22, 8.5, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(BRIMHAVEN_GIANTS), 34, 10.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(TAVERLEY_DUNGEON), 70, 10.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(BARBARIAN_OUTPOST), 35, 10.0, "squeeze-through"))
        configure(PipeShortcut(intArrayOf(EDGEVILLE_DUNGEON), 51, 10.0, "squeeze-through"))
        return this
    }

    override fun run(
        player: Player,
        obj: Scenery,
        option: String,
        failed: Boolean,
    ) {
        GameWorld.Pulser.submit(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    when (obj.id) {
                        2290, 9293 -> {
                            player.lock(7)
                            AgilityHandler.forceWalk(
                                player,
                                -1,
                                player.location,
                                pipeDestination(player, obj, 6),
                                Animation.create(10580),
                                10,
                                0.0,
                                null,
                            )
                            player.animate(Animation(Animations.CRAWL_844), 3)
                            player.animate(Animation(Animations.CLIMB_OUT_OF_OBSTACLE_10579), 5)
                            playAudio(player, Sounds.SQUEEZE_OUT_2490, 5)
                            return true
                        }

                        5099, 5100 -> {
                            player.lock(5)
                            AgilityHandler.forceWalk(
                                player,
                                -1,
                                player.location,
                                pipeDestination(player, obj, 7),
                                Animation.create(10580),
                                10,
                                0.0,
                                null,
                            )
                            player.animate(Animation(Animations.CRAWL_844), 4)
                            player.animate(Animation(Animations.CLIMB_OUT_OF_OBSTACLE_10579), 6)
                            playAudio(player, Sounds.SQUEEZE_OUT_2490, 6)
                            return true
                        }

                        20210 -> {
                            if (player.location.x != 2552) {
                                sendMessage(player, "I can't get into this pipe at that angle.")
                                return true
                            }
                            lock(player, 3)
                            AgilityHandler.forceWalk(
                                player,
                                -1,
                                player.location,
                                pipeDestination(player, obj, 3),
                                Animation.create(10580),
                                15,
                                0.0,
                                null,
                            )
                            return true
                        }

                        29370 -> {
                            if (player.location.y != 9906) {
                                sendMessage(player, "I can't get into this pipe at that angle.")
                                return true
                            }
                            lock(player, 7)
                            AgilityHandler.forceWalk(
                                player,
                                -1,
                                player.location,
                                pipeDestination(player, obj, 6),
                                Animation.create(10580),
                                10,
                                0.0,
                                null,
                            )
                            player.animate(Animation(Animations.CRAWL_844), 3)
                            player.animate(Animation(Animations.CLIMB_OUT_OF_OBSTACLE_10579), 5)
                            playAudio(player, Sounds.SQUEEZE_OUT_2490, 5)
                            return true
                        }
                    }
                    return false
                }
            },
        )
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
