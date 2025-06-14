package content.minigame.pestcontrol.plugin.npc

import content.minigame.pestcontrol.plugin.PestControlSession
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.NPCs

class PCSplatterNPC : AbstractNPC {
    private var session: PestControlSession? = null
    private var exploding = false

    constructor() : super(NPCs.SPLATTER_3727, null)

    constructor(id: Int, location: Location?) : super(id, location)

    override fun init() {
        super.init()
        super.walkRadius = 64
        session = getExtension(PestControlSession::class.java)
    }

    override fun tick() {
        super.tick()
        if (exploding || session == null) {
            return
        }
        if (properties.combatPulse.isAttacking) {
            return
        }
        for (o in session!!.barricades) {
            if (o.location.isNextTo(this)) {
                commenceDeath(this)
                break
            }
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

    override fun commenceDeath(killer: Entity) {
        exploding = true
        visualize(
            Animation(3888, Priority.VERY_HIGH),
            Graphics.create(
                org.rs.consts.Graphics.SPLATTER_DAMAGE_WHITE_649 + (id - NPCs.SPLATTER_3727),
            ),
        )
        Pulser.submit(
            object : Pulse(1, this) {
                override fun pulse(): Boolean {
                    explode()
                    return true
                }
            },
        )
    }

    override fun finalizeDeath(killer: Entity) {}

    private fun explode() {
        val maximum = properties.currentCombatLevel / 3
        val minimum = maximum / 2
        if (session != null) {
            for (o in ArrayList(session!!.barricades)) {
                if (o.location.isNextTo(this)) {
                    val newId = o.id + if (o.id < 14233) 3 else 4
                    val destroyed = !isTarget(newId)
                    val newTarget = o.transform(newId, o.rotation, if (destroyed) 22 else o.type)
                    if (session!!.barricades.remove(o)) {
                        session!!.barricades.add(newTarget)
                        SceneryBuilder.replace(o, newTarget)
                    }
                }
            }
        }
        for (p in getLocalPlayers(this, 1)) {
            p.impactHandler.manualHit(this, RandomFunction.random(minimum, maximum), null)
        }
        for (npc in getLocalNpcs(this, 1)) {
            if (npc !== this) {
                npc.impactHandler.manualHit(this, RandomFunction.random(minimum, maximum), null)
            }
        }
        clear()
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = PCSplatterNPC(id, location)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.SPLATTER_3727,
            NPCs.SPLATTER_3728,
            NPCs.SPLATTER_3729,
            NPCs.SPLATTER_3730,
            NPCs.SPLATTER_3731,
        )

    companion object {
        private fun isTarget(id: Int): Boolean {
            for (`object` in PestControlSession.INVALID_OBJECT_IDS) {
                if (id == `object`) {
                    return false
                }
            }
            return true
        }
    }
}
