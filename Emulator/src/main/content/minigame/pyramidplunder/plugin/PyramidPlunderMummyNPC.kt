package content.minigame.pyramidplunder.plugin

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

class PyramidPlunderMummyNPC(
    location: Location?,
    player: Player?,
) : PyramidPlunderNPC(IDS[0], location, player!!) {
    override fun init() {
        super.init()
        isRespawn = false
        properties.combatPulse.attack(player)
        sendChat("How dare you disturb my rest!")
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (!properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(player)
        }
        if (properties.combatPulse.isAttacking) {
            if (RandomFunction.random(40) < 2) {
                sendChat("Leave this place!")
            }
        }
    }

    override fun isIgnoreMultiBoundaries(victim: Entity): Boolean = victim === player

    override fun getIds(): IntArray = IDS

    companion object {
        private val IDS = intArrayOf(NPCs.MUMMY_1958)
    }
}
