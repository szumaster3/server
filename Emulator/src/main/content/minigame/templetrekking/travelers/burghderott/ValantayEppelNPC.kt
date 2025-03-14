package content.minigame.templetrekking.travelers.burghderott

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ValantayEppelNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return ValantayEppelNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VALANTAY_EPPEL_3631)
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
