package content.region.kandarin.handlers.npc.yanille

import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SalarinTwistedNPC : AbstractNPC {
    val SPELL_IDS = intArrayOf(1, 4, 6, 8)

    constructor() : super(-1, null)

    constructor(id: Int, location: Location) : super(id, location) {
        super.setAggressive(true)
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC {
        return SalarinTwistedNPC(id, location)
    }

    override fun checkImpact(state: BattleState) {
        if (state.style != CombatStyle.MAGIC) {
            state.neutralizeHits()
            return
        }
        if (state.spell == null) {
            state.neutralizeHits()
            return
        }
        val spell = state.spell
        for (id in SPELL_IDS) {
            if (id == spell.spellId) {
                state.estimatedHit = state.maximumHit
                return
            }
        }
        state.neutralizeHits()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SALARIN_THE_TWISTED_205)
    }
}
