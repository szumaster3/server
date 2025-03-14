package content.global.skill.hunter.pitfall

import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class PitfallNPC : AbstractNPC {
    constructor() : super(NPCs.HORNED_GRAAHK_5105, null, true)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC {
        return PitfallNPC(id, location)
    }

    init {
        walkRadius = 22
    }

    override fun getIds(): IntArray {
        return Pitfall.BEAST_IDS
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        return false
    }

    override fun isIgnoreAttackRestrictions(victim: Entity): Boolean {
        return victim is Player
    }
}
