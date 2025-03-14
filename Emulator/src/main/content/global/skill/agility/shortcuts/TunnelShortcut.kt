package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.quest.hasRequirement
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Quests

@Initializable
class TunnelShortcut : AgilityShortcut {
    private var offset: Int = 0

    constructor() : super(intArrayOf(), 0, 0.0)

    constructor(ids: IntArray, level: Int, experience: Double, offset: Int, vararg options: String) : super(
        ids,
        level,
        experience,
        *options,
    ) {
        this.offset = offset
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        configure(TunnelShortcut(intArrayOf(9309, 9310), 26, 0.0, 0, "climb-into"))
        configure(TunnelShortcut(intArrayOf(9302, 9301), 16, 0.0, 1, "climb-into", "climb-under"))
        configure(TunnelShortcut(intArrayOf(14922), 1, 0.0, 1, "enter"))
        return this
    }

    override fun run(
        player: Player,
        obj: Scenery,
        option: String,
        failed: Boolean,
    ) {
        if (obj.id == 14922 && !hasRequirement(player, Quests.SWAN_SONG)) {
            return
        }

        val objectLocation = obj.location
        val start = player.location
        val direction = Direction.getLogicalDirection(start, objectLocation)
        offset = if (objectLocation.x == 2575) 1 else 0

        ForceMovement.run(player, start, objectLocation, Animation.create(Animations.CRAWL_UNDER_WALL_A_2589), 8)

        GameWorld.Pulser.submit(
            object : Pulse(1, player) {
                private var count = 0

                override fun pulse(): Boolean {
                    return when (++count) {
                        1 -> {
                            player.lock(6)
                            player.locks.lockMovement(6)
                            false
                        }

                        2 -> {
                            player.animate(Animation.create(Animations.CRAWL_UNDER_WALL_B_2590))
                            player.properties.teleportLocation = start.transform(direction, 2 + offset)
                            false
                        }

                        3 -> {
                            ForceMovement.run(
                                player,
                                player.location,
                                start.transform(direction, 4 + offset),
                                Animation.create(Animations.CRAWL_UNDER_WALL_C_2591),
                                19,
                            )
                            false
                        }

                        4 -> {
                            player.animate(ForceMovement.WALK_ANIMATION)
                            if ((obj.id == 9309 || obj.id == 9310) &&
                                !player.achievementDiaryManager
                                    .getDiary(DiaryType.FALADOR)!!
                                    .isComplete(1, 1)
                            ) {
                                player.achievementDiaryManager
                                    .getDiary(DiaryType.FALADOR)!!
                                    .updateTask(player, 1, 1, true)
                            }
                            true
                        }

                        else -> false
                    }
                }
            },
        )
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location {
        return if (n.id == 14922) {
            n.location.transform(getObjectDirection(n.asScenery().direction), 1)
        } else {
            getStart(n.location, n.direction)
        }
    }

    private fun getStart(
        location: Location,
        dir: Direction,
    ): Location {
        return when (dir) {
            Direction.NORTH, Direction.SOUTH -> location
            Direction.EAST -> location.transform(0, if (location.y == 3111) 1 else -1, 0)
            Direction.WEST -> location.transform(0, 1, 0)
            else -> location
        }
    }
}
