package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

@Initializable
class FenceJumpShortcut : AgilityShortcut(intArrayOf(9300), 13, 0.0, "jump-over") {
    private val runningAnim = Animation(Animations.RUNNING_OSRS_STYLE_1995)
    private val jumpAnim = Animation(Animations.JUMP_WEREWOLF_1603)
    private val locations =
        arrayOf(
            Location(3240, 3331, 0),
            Location(3240, 3338, 0),
            Location(3240, 3334, 0),
            Location(3240, 3335, 0),
        )

    override fun run(
        player: Player,
        scenery: Scenery,
        option: String?,
        failed: Boolean,
    ) {
        player.faceLocation(scenery.location)
        Pulser.submit(
            object : Pulse(2, player) {
                override fun pulse(): Boolean {
                    player.animate(jumpAnim)
                    player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, 5)
                    return true
                }
            },
        )
        ForceMovement.run(
            player,
            if (player.location.y >= 3335) locations[1] else locations[0],
            if (player.location.y >= 3335) locations[2] else locations[3],
            runningAnim,
            18,
        )
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location {
        val player = node.asPlayer()
        return if (player.location.y >= 3335) locations[1] else locations[0]
    }
}
