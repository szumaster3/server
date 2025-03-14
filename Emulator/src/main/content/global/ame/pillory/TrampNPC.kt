package content.global.ame.pillory

import content.data.GameAttributes
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.NPCs

@Initializable
class TrampNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return TrampNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRAMP_2794, NPCs.TRAMP_2793, NPCs.TRAMP_2792)
    }

    override fun handleTickActions() {
        super.handleTickActions()

        var players = RegionManager.getLocalPlayers(this.asNpc(), 8)
        for (player in players) {
            if (!player.getAttribute(GameAttributes.RE_PILLORY_TARGET, false)) return
            if (RandomFunction.random(100) == 5) {
                stopWalk(this)
                faceLocation(this, player.location)
                queueScript(this, 2, QueueStrength.SOFT) {
                    animate(this, Animations.THROW_385)
                    sendChat(this, "Take that, you thief!")
                    spawnProjectile(this, player, Graphics.ROTTEN_TOMATOE_PROJECTILE_29)
                    return@queueScript stopExecuting(this)
                }
            }
        }
    }
}
