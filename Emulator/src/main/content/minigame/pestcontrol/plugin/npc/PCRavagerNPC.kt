package content.minigame.pestcontrol.plugin.npc

import content.minigame.pestcontrol.plugin.PestControlSession
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.MapDistance
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs.consts.NPCs

class PCRavagerNPC : AbstractNPC {
    private var session: PestControlSession? = null
    private var target: Scenery? = null

    var portalIndex = 0
    private var nextAttack = 0
    private var offLimits: ZoneBorders? = null

    constructor() : super(NPCs.RAVAGER_3742, null)

    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = PCRavagerNPC(id, location)

    override fun init() {
        super.init()
        super.walkRadius = 64
        if (getExtension<PestControlSession>(PestControlSession::class.java).also { session = it } != null) {
            val l = session!!.region.baseLocation
            offLimits = ZoneBorders(l.x + 20, l.y + 26, l.x + 44, l.y + 57)
        }
    }

    override fun tick() {
        super.tick()
        if (session != null && !properties.combatPulse.isAttacking) {
            if (target == null) {
                if (findTarget()) {
                    pulseManager.clear()
                    walk(target)
                } else if (!pulseManager.hasPulseRunning() && RandomFunction.RANDOM.nextInt(10) < 2) {
                    if (offLimits!!.insideBorder(getLocation().x, getLocation().y)) {
                        walk(session!!.portals[portalIndex])
                    } else {
                        walk(session!!.squire!!.location)
                    }
                }
            } else {
                if (!target!!.isActive || !session!!.barricades.contains(target)) {
                    attack(null)
                } else if (nextAttack < ticks && getLocation().withinDistance(target!!.location, 1)) {
                    pulseManager.clear()
                    isWalks = false
                    super.getWalkingQueue().reset()
                    super.getLocks().lockMovement(2)
                    super.faceLocation(target!!.location)
                    super.animate(properties.attackAnimation)
                    val newId = target!!.id + if (target!!.id < 14233) 3 else 4
                    val destroyed = !isTarget(newId)
                    val type = if (destroyed) 22 else target!!.type
                    val o: Scenery = target!!
                    val newTarget = o.transform(newId, o.rotation, type)
                    Pulser.submit(
                        object : Pulse(1, this, o) {
                            override fun pulse(): Boolean {
                                if (viewport.region.isActive && session!!.barricades.remove(o)) {
                                    session!!.barricades.add(newTarget)
                                    SceneryBuilder.replace(o, newTarget)
                                }
                                return true
                            }
                        },
                    )
                    target = newTarget
                    if (destroyed) {
                        attack(null)
                    }
                    nextAttack = ticks + 5
                }
            }
        } else {
            attack(null)
        }
        properties.isRetaliating = target == null
    }

    private fun attack(o: Scenery?) {
        target = o
        if (o == null) {
            isWalks = true
            super.faceLocation(null)
        }
    }

    private fun walk(destination: Node?) {
        pulseManager.run(
            object : MovementPulse(this, destination, Pathfinder.SMART) {
                override fun pulse(): Boolean = true
            },
            PulseType.STANDARD,
        )
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

    private fun findTarget(): Boolean {
        for (o in session!!.barricades) {
            if (o.location.withinDistance(getLocation(), MapDistance.RENDERING.distance / 3) && isTarget(o.id)) {
                attack(o)
                return true
            }
        }
        return false
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.RAVAGER_3742, NPCs.RAVAGER_3743, NPCs.RAVAGER_3744, NPCs.RAVAGER_3745, NPCs.RAVAGER_3746)

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
