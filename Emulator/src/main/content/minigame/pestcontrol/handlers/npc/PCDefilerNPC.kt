package content.minigame.pestcontrol.handlers.npc

import content.minigame.pestcontrol.handlers.PestControlSession
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.MapDistance
import org.rs.consts.NPCs

class PCDefilerNPC : AbstractNPC {
    private var session: PestControlSession? = null

    constructor() : super(NPCs.DEFILER_3762, null)

    constructor(id: Int, location: Location?) : super(id, location)

    override fun init() {
        super.setAggressive(true)
        super.init()
        super.getDefinition().combatDistance = 10
        super.walkRadius = 64
        properties.combatPulse.style = CombatStyle.RANGE
        session = getExtension(PestControlSession::class.java)
    }

    override fun tick() {
        super.tick()
        if (session != null && !inCombat() && !properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(session!!.squire)
        }
    }

    override fun shouldPreventStacking(mover: Entity): Boolean = mover is NPC

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

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = SWING_HANDLER

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = PCDefilerNPC(id, location)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.DEFILER_3762,
            NPCs.DEFILER_3763,
            NPCs.DEFILER_3764,
            NPCs.DEFILER_3765,
            NPCs.DEFILER_3766,
            NPCs.DEFILER_3767,
            NPCs.DEFILER_3768,
            NPCs.DEFILER_3769,
            NPCs.DEFILER_3770,
            NPCs.DEFILER_3771,
        )

    companion object {
        private val SWING_HANDLER: CombatSwingHandler =
            object : RangeSwingHandler() {
                override fun canSwing(
                    entity: Entity,
                    victim: Entity,
                ): InteractionType {
                    if (!isProjectileClipped(entity, victim, false)) {
                        return InteractionType.NO_INTERACT
                    }
                    if (victim.centerLocation.withinDistance(entity.centerLocation, 8) &&
                        isAttackable(
                            entity,
                            victim,
                        ) !== InteractionType.NO_INTERACT
                    ) {
                        if (victim.location.withinDistance(entity.location, MapDistance.RENDERING.distance / 2)) {
                            entity.walkingQueue.reset()
                        }
                        return InteractionType.STILL_INTERACT
                    }
                    return InteractionType.NO_INTERACT
                }
            }
    }
}
