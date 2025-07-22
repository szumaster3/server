package content.region.kandarin.ardougne.east.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * The Summoned zombie NPC, which is summoned by a Necromancer NPC.
 */
@Initializable
class SummonedZombieNPC @JvmOverloads constructor(id: Int = NPCs.SUMMONED_ZOMBIE_77, location: Location? = null) : AbstractNPC(id, location, true) {

    /**
     * The owner NPC that summoned this zombie.
     */
    private var owner: NPC? = null

    /**
     * Counter for tracking ticks.
     */
    private var ticks = 0

    /**
     * Counter for failed attempts to summon the zombie near its victim.
     */
    private var fails = 0

    /**
     * Constructs the [SummonedZombieNPC].
     */
    override fun construct(id: Int, location: Location, vararg objects: Any): SummonedZombieNPC {
        val npc = SummonedZombieNPC(id, location)
        if (objects.isNotEmpty()) {
            npc.owner = objects[0] as NPC
        }
        npc.isRespawn = false
        npc.isAggressive = true
        return npc
    }

    override fun canStartCombat(victim: Entity): Boolean = true

    override fun handleTickActions() {
        if (isInvisible) return

        ticks++

        if (ticks < 100) return

        if (ticks % 3 != 0) return

        val victim = owner?.properties?.combatPulse?.victim ?: return

        if (victim.viewport.currentPlane != viewport.currentPlane) return

        val path = Pathfinder.find(location, victim.location, 1)

        if (path.isSuccessful || !path.isMoveNear) {
            summon(victim.location)
            fails = 0
        } else {
            fails++
            if (fails >= 3) {
                fails = 0
            }
        }
    }

    /**
     * Teleports the [SummonedZombieNPC] to the victim.
     *
     * @param location The target location to summon the zombie.
     */
    private fun summon(location: Location) {
        isInvisible = true
        GameWorld.Pulser.submit(object : Pulse(2, this) {
            override fun pulse(): Boolean {
                properties.teleportLocation = location
                isInvisible = false
                return true
            }
        })
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        (owner?.behavior as? NecromancerNPC)?.let { behavior ->
            behavior.summonedZombie = null
            behavior.summonedZombieCounter = maxOf(0, behavior.summonedZombieCounter - 1)
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SUMMONED_ZOMBIE_77)
}