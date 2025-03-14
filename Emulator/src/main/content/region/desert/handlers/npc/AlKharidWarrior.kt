package content.region.desert.handlers.npc

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class AlKharidWarrior(
    id: Int = NPCs.AL_KHARID_WARRIOR_18,
    location: Location? = null,
) : AbstractNPC(id, location, true) {
    private var target: Player? = null
    private val supportRange: Int = 5

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return AlKharidWarrior(id, location)
    }

    override fun tick() {
        target?.let { player ->
            if (!inCombat() && skills.lifepoints > 0) {
                attack(player)
            }
        }
        super.tick()
    }

    override fun finalizeDeath(killer: Entity?) {
        target = null
        super.finalizeDeath(killer)
    }

    override fun attack(node: Node?) {
        if (node is Player) {
            target = node
        }
        super.attack(node)
    }

    override fun onImpact(
        entity: Entity?,
        state: BattleState?,
    ) {
        if (entity is Player) {
            if (target == null) {
                target = entity
                val localNPC = RegionManager.getLocalNpcs(this, supportRange)
                localNPC.forEach { npc ->
                    if (npc.id == NPCs.AL_KHARID_WARRIOR_18 && !npc.properties.combatPulse.isAttacking && npc != this) {
                        npc.sendChat("Brother, I will help thee with this infidel!")
                        npc.attack(entity)
                    }
                }
            }
        }
        super.onImpact(entity, state)
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        private val ID = intArrayOf(NPCs.AL_KHARID_WARRIOR_18)
    }
}
