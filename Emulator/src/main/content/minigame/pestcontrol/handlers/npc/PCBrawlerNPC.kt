package content.minigame.pestcontrol.handlers.npc

import content.minigame.pestcontrol.handlers.PestControlSession
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs

class PCBrawlerNPC : AbstractNPC {
    private var session: PestControlSession? = null

    constructor() : super(NPCs.BRAWLER_3772, null)

    constructor(id: Int, location: Location?) : super(id, location)

    override fun init() {
        super.setAggressive(true)
        super.init()
        super.getDefinition().combatDistance = 1
        super.walkRadius = 64
        properties.combatPulse.style = CombatStyle.MELEE
        session = getExtension(PestControlSession::class.java)
    }

    override fun shouldPreventStacking(mover: Entity): Boolean {
        return true
    }

    override fun onImpact(
        entity: Entity,
        state: BattleState,
    ) {
        super.onImpact(entity, state)
        if (session != null && state != null && entity is Player) {
            var total = 0
            if (state.estimatedHit > 0) {
                total += state.estimatedHit
            }
            if (state.secondaryHit > 0) {
                total += state.secondaryHit
            }
            session!!.addZealGained(entity, total)
        }
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return PCBrawlerNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BRAWLER_3772, NPCs.BRAWLER_3773, NPCs.BRAWLER_3774, NPCs.BRAWLER_3775, NPCs.BRAWLER_3776)
    }
}
