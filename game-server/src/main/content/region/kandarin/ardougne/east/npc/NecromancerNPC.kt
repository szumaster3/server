package content.region.kandarin.ardougne.east.npc

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Behavior class for the Necromancer NPC.
 *
 * NPC can summon up to 3 zombies.
 * @see SummonedZombieNPC
 */
class NecromancerNPC : NPCBehavior(NPCs.INVRIGAR_THE_NECROMANCER_173, NPCs.NECROMANCER_194) {

    /**
     * The currently summoned zombie.
     */
    var summonedZombie: NPC? = null

    /**
     * The count of currently active summoned zombies.
     */
    var summonedZombieCounter = 0

    /**
     * Forces a zombie spawn roll on next [afterDamageReceived] if true.
     */
    private var forceZombieRoll: Boolean = false

    /**
     *  Forces a zombie spawn roll if the incoming hit is `>= 3`.
     */
    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (state.estimatedHit >= 3) {
            this.forceZombieRoll = true
        }
    }

    /**
     * Attempts to summon a zombie.
     */
    override fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (attacker is Player) {
            val thresh = self.getSkills().maximumLifepoints * (0.3 + (self.viewport.currentPlane!!.players.size * 0.05))
            if (forceZombieRoll || self.getSkills().lifepoints < thresh) {
                rollZombieSpawn(self, this, attacker)
                forceZombieRoll = false
            }
        }
    }

    /**
     * Clears active summoned zombie and resets the zombie counter.
     */
    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (summonedZombie != null) {
            summonedZombie?.clear()
            summonedZombieCounter = 0
            summonedZombie = null
        }
    }

    /**
     * Attempts to spawn a summoned zombie near the target player.
     *
     * @param entity The [NecromancerNPC] (source of summoned zombies).
     * @param npc The behavior instance (used for tracking summoned state).
     * @param victim The player to summon the zombie near.
     */
    private fun rollZombieSpawn(entity: Entity, npc: NecromancerNPC, victim: Entity) {
        if (summonedZombieCounter >= 3) return
        if (npc.summonedZombie != null && npc.summonedZombie!!.isActive && !DeathTask.isDead(npc.summonedZombie!!)) {
            return
        }
        if (!RandomFunction.roll(8)) {
            return
        }
        val location = RegionManager.getTeleportLocation(victim.location, 3)
        npc.summonedZombie = NPC.create(NPCs.SUMMONED_ZOMBIE_77, location, entity)
        npc.summonedZombie!!.isActive = true
        npc.summonedZombie!!.isWalks = true
        npc.summonedZombie!!.isNeverWalks = false
        summonedZombieCounter++
        GameWorld.Pulser.submit(object : Pulse(2, entity) {
            override fun pulse(): Boolean {
                if (npc.summonedZombie == null) return true
                npc.summonedZombie!!.init()
                npc.summonedZombie!!.attack(victim)
                return true
            }
        })
    }
}