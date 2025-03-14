package content.region.asgarnia.handlers.npc.trollheim

import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.agg.AggressiveBehavior
import core.game.node.entity.npc.agg.AggressiveHandler
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ThrowerTrollNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return ThrowerTrollNPC(id, location)
    }

    override fun setDefaultBehavior() {
        super.setAggressive(true)
        super.setAggressiveHandler(
            AggressiveHandler(
                this,
                object : AggressiveBehavior() {
                    override fun canSelectTarget(
                        entity: Entity,
                        target: Entity,
                    ): Boolean {
                        if (!target.isActive || DeathTask.isDead(target)) {
                            return false
                        }
                        if (!target.properties.isMultiZone && target.inCombat()) {
                            return false
                        }
                        return true
                    }
                },
            ),
        )
        getAggressiveHandler().chanceRatio = 8
        getAggressiveHandler().radius = 7
        definition.combatDistance = 7
        super.setWalks(false)
        super.getLocks().lockMovement(1 shl 25)
    }

    override fun init() {
        super.init()
        properties.combatPulse.style = CombatStyle.RANGE
    }

    override fun getIds(): IntArray {
        return ID
    }


    companion object {
        val ID = intArrayOf(
            NPCs.THROWER_TROLL_1101,
            NPCs.THROWER_TROLL_1102,
            NPCs.THROWER_TROLL_1103,
            NPCs.THROWER_TROLL_1104,
            NPCs.THROWER_TROLL_1105,
            NPCs.THROWER_TROLL_1130,
            NPCs.THROWER_TROLL_1131,
            NPCs.THROWER_TROLL_1132,
            NPCs.THROWER_TROLL_1133,
            NPCs.THROWER_TROLL_1134,
        )
    }
}
