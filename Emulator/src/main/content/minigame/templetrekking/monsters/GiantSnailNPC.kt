package content.minigame.templetrekking.monsters

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs

class GiantSnailNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return GiantSnailNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GIANT_SNAIL_3612, NPCs.GIANT_SNAIL_3613, NPCs.GIANT_SNAIL_3614)
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            clear()
            super.finalizeDeath(killer)
        }
    }
}
