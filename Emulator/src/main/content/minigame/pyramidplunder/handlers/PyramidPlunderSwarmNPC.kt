package content.minigame.pyramidplunder.handlers

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

class PyramidPlunderSwarmNPC(
    location: Location?,
    player: Player?,
) : PyramidPlunderNPC(IDS[0], location, player!!) {
    override fun init() {
        super.init()
        isRespawn = false
        properties.combatPulse.attack(player)
        sendChat("bzzzzz")
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (!properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(player)
        }
        if (properties.combatPulse.isAttacking) {
            if (RandomFunction.random(40) < 2) {
                sendChat("bzzzzz")
            }
        }
    }

    override fun isIgnoreMultiBoundaries(victim: Entity): Boolean = victim === player

    override fun getIds(): IntArray = IDS

    companion object {
        private val IDS = intArrayOf(NPCs.SCARAB_SWARM_2001)
    }
}
