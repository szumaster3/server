package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Quests

@Initializable
class BarSqueezeShortcut : AgilityShortcut {
    constructor() : super(intArrayOf(9334, 9337), 66, 1.0, "squeeze-through")

    constructor(ids: IntArray, level: Int, exp: Double, option: String) : super(ids, level, exp, option)

    override fun newInstance(arg: Any?): Plugin<Any> {
        return super.newInstance(arg).apply {
            configure(BarSqueezeShortcut(intArrayOf(2186), 1, 0.0, "squeeze-through"))
        }
    }

    override fun run(
        player: Player,
        obj: Scenery,
        option: String,
        failed: Boolean,
    ) {
        val logicalDirection = Direction.getLogicalDirection(player.location, obj.location)
        val (dir, start) =
            when {
                obj.id == 9334 && logicalDirection == Direction.NORTH ->
                    Direction.WEST to
                        Location.create(3424, 3476, 0)
                obj.id == 9337 && logicalDirection == Direction.NORTH ->
                    if (player.location.y <
                        obj.location.y
                    ) {
                        Direction.NORTH to player.location
                    } else {
                        Direction.SOUTH to player.location
                    }
                obj.id == 2186 && player.location.y >= 3161 -> Direction.SOUTH to player.location
                else -> logicalDirection to player.location
            }
        AgilityHandler.forceWalk(
            player,
            -1,
            start,
            player.location.transform(dir, 1),
            Animation.create(2240),
            10,
            0.0,
            null,
        )
    }

    override fun checkRequirements(player: Player): Boolean {
        return if (!isQuestComplete(player, Quests.PRIEST_IN_PERIL) && player.location.y !in 3159..3161) {
            sendDialogue(player, "You need to have completed Priest in Peril in order to do this.")
            false
        } else {
            super.checkRequirements(player)
        }
    }
}
