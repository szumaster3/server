package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import content.global.skill.agility.AgilityHandler
import core.api.animationCycles
import core.api.face
import core.api.lock
import core.api.playAudio
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Sounds

class LumberyardFenceShortcut : AgilityShortcut(intArrayOf(31149), 0, 0.0, "squeeze-under") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val anim = Animations.LUMBER_YARD_ENTER_9221
        val destination = Location(if (player.location.x > 3295) 3296 else 3295, 3498, 0)
        lock(player, animationCycles(anim) + 2)
        face(player, scenery)
        playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310)
        AgilityHandler.forceWalk(player, -1, player.location, destination, Animation(anim), animationCycles(anim), 0.0, null, 0).endAnimation = Animation.RESET
    }
}
