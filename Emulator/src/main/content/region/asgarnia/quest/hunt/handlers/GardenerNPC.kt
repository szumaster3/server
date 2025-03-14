package content.region.asgarnia.quest.hunt.handlers

import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class GardenerNPC : AbstractNPC {
    constructor() : super(0, null, true)

    private constructor(id: Int, location: Location) : super(id, location, true)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return GardenerNPC(id, location)
    }

    override fun tick() {
        super.tick()
        if (getAttribute<Any?>("target", null) != null) {
            val target = getAttribute<Player>("target", null)
            if (properties.combatPulse.isRunning) {
                properties.combatPulse.attack(target)
            }
            if (!target.isActive || target.location.getDistance(getLocation()) > 16) {
                target.getSavedData().questData.isGardenerAttack = true
                Pulser.submit(
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            clear()
                            return true
                        }
                    },
                )
            }
        }
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        val target = getAttribute<Player>("target", null)
        if (target !== entity) {
            return false
        }
        return super.isAttackable(entity, style, message)
    }

    override fun finalizeDeath(killer: Entity) {
        val target = getAttribute<Player>("target", null)
        if (target != null && target == killer) {
            target.getSavedData().questData.isGardenerAttack = true
        }
        clear()
        super.finalizeDeath(killer)
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        private val ID = intArrayOf(NPCs.GARDENER_1217)
    }
}
