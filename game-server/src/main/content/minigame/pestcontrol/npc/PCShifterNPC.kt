package content.minigame.pestcontrol.npc

import content.minigame.pestcontrol.plugin.PestControlSession
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.isTeleportPermitted
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.NPCs
import java.util.*

class PCShifterNPC : AbstractNPC {
    private var session: PestControlSession? = null

    constructor() : super(NPCs.SHIFTER_3732, null)

    constructor(id: Int, location: Location?) : super(id, location)

    override fun init() {
        super.setAggressive(true)
        super.init()
        super.getDefinition().combatDistance = 1
        super.walkRadius = 64
        properties.combatPulse.style = CombatStyle.MELEE
        session = getExtension(PestControlSession::class.java)
    }

    override fun tick() {
        super.tick()
        val pulse = properties.combatPulse
        if (session != null && !inCombat() && !pulse.isAttacking && RandomFunction.RANDOM.nextInt(50) < 2) {
            pulse.attack(session!!.squire)
        }
        if (pulse.isAttacking && !getLocation().withinDistance(pulse.victim!!.location, 5)) {
            if (session == null || session!!.isActive) {
                teleport(session, this, getDestination(pulse.victim))
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

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = SWING_HANDLER

    private fun getDestination(victim: Entity?): Location? {
        val locations: MutableList<Location?> = ArrayList(20)
        val radius = 2
        for (x in -radius until radius + 1) {
            for (y in -radius until radius + 1) {
                if (x != 0 && y != 0) {
                    locations.add(victim!!.location.transform(x, y, 0))
                }
            }
        }
        Collections.shuffle(locations, RandomFunction.RANDOM)
        for (l in locations) {
            if (isTeleportPermitted(l!!)) {
                return l
            }
        }
        return null
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = PCShifterNPC(id, location)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.SHIFTER_3732,
            NPCs.SHIFTER_3733,
            NPCs.SHIFTER_3734,
            NPCs.SHIFTER_3735,
            NPCs.SHIFTER_3736,
            NPCs.SHIFTER_3737,
            NPCs.SHIFTER_3738,
            NPCs.SHIFTER_3739,
            NPCs.SHIFTER_3740,
            NPCs.SHIFTER_3741,
        )

    companion object {
        private val SWING_HANDLER: CombatSwingHandler =
            object : MeleeSwingHandler() {
                override fun canSwing(
                    entity: Entity,
                    victim: Entity,
                ): InteractionType? = CombatStyle.RANGE.swingHandler.canSwing(entity, victim)
            }

        fun teleport(
            session: PestControlSession?,
            entity: Entity,
            destination: Location?,
        ) {
            if (destination == null || session != null && destination.getRegionId() != session.region.id) {
                return
            }
            Graphics.send(Graphics.create(org.rs.consts.Graphics.PEST_CONTROL_SPAWN_654), entity.location)
            entity.properties.teleportLocation = destination
            entity.walkingQueue.reset()
            entity.locks.lockMovement(2)
            entity.lock(3)
            Pulser.submit(
                object : Pulse(1, entity) {
                    override fun pulse(): Boolean {
                        entity.animate(Animation.create(3904))
                        Graphics.send(Graphics.create(org.rs.consts.Graphics.PEST_CONTROL_SPAWN_654), destination)
                        return true
                    }
                },
            )
        }
    }
}
