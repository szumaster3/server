package content.global.skill.slayer.npc

import content.global.skill.slayer.SlayerEquipmentFlags.hasFaceMask
import content.global.skill.slayer.Tasks
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * The type Dust devil npc.
 */
@Initializable
class DustDevilNPC : AbstractNPC {
    /**
     * Instantiates a new Dust devil npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location) {
        super.getProperties().combatPulse.handler = COMBAT_HANDLER
    }

    /**
     * Instantiates a new Dust devil npc.
     */
    constructor() : super(0, null)

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return COMBAT_HANDLER
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        if (state.attacker is Player) {
            val player = state.attacker as Player
            if (!hasFaceMask(player)) {
                state.neutralizeHits()
            }
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return DustDevilNPC(id, location)
    }

    override fun getIds(): IntArray {
        return Tasks.DUST_DEVILS.npcs
    }

    companion object {
        private val COMBAT_HANDLER: MeleeSwingHandler = object : MeleeSwingHandler() {
            override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
                if (victim is Player) {
                    val player = victim
                    if (!hasFaceMask(player)) {
                        for (i in SKILLS) {
                            player.getSkills().updateLevel(i, -player.getSkills().getStaticLevel(i), 0)
                        }
                        player.getSkills().decrementPrayerPoints(player.getSkills().getStaticLevel(Skills.PRAYER) / 2.0)
                        state!!.estimatedHit = 14
                    }
                }
                super.impact(entity, victim, state)
            }

            override fun isAttackable(entity: Entity, victim: Entity): InteractionType? {
                return CombatStyle.MAGIC.swingHandler.isAttackable(entity, victim)
            }
        }

        private val SKILLS = intArrayOf(Skills.ATTACK, Skills.STRENGTH, Skills.RANGE, Skills.MAGIC)
    }
}
