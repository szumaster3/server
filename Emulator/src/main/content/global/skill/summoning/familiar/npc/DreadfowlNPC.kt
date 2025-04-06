package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import kotlin.math.floor

/**
 * The type Dreadfowl npc.
 */
@Initializable
class DreadfowlNPC @JvmOverloads constructor(owner: Player? = null, id: Int = 6825) :
    Familiar(owner, id, 400, 12043, 3, WeaponInterface.STYLE_CAST) {
    private var specialMove = false

    /**
     * Instantiates a new Dreadfowl npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Dreadfowl npc.
     */
    init {
        super.setCombatHandler(COMBAT_HANDLER)
        boosts.add(SkillBonus(Skills.FARMING, 1.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return DreadfowlNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.node as Entity
        if (!canAttack(target)) {
            return false
        }
        if (!owner.properties.combatPulse.isAttacking && !owner.inCombat()) {
            owner.packetDispatch.sendMessage("Your familiar can only attack when you're in combat.")
            return false
        }
        if (properties.combatPulse.getNextAttack() > GameWorld.ticks || CombatStyle.MAGIC.swingHandler.canSwing(
                this,
                target
            ) == InteractionType.NO_INTERACT
        ) {
            specialMove = true
            properties.combatPulse.attack(target)
            return true
        }
        visualize(Animation(5387, Priority.HIGH), Graphics.create(1523))
        Projectile.magic(this, target, 1318, 40, 36, 51, 10).send()
        val ticks = 2 + floor(getLocation().getDistance(target.location) * 0.5).toInt()
        properties.combatPulse.setNextAttack(4)
        Pulser.submit(object : Pulse(ticks, this, target) {
            override fun pulse(): Boolean {
                val state = BattleState(this@DreadfowlNPC, target)
                var hit = 0
                if (CombatStyle.MAGIC.swingHandler.isAccurateImpact(this@DreadfowlNPC, target)) {
                    hit = RandomFunction.randomize(3)
                }
                state.estimatedHit = hit
                target.impactHandler.handleImpact(owner, hit, CombatStyle.MAGIC, state)
                return true
            }
        })
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(6825, 6826)
    }

    companion object {
        private val COMBAT_HANDLER: CombatSwingHandler = object : MeleeSwingHandler() {
            override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
                if ((entity as DreadfowlNPC).specialMove) {
                    return CombatStyle.MAGIC.swingHandler.canSwing(entity, victim)
                }
                return super.canSwing(entity, victim)
            }

            override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
                val npc = entity as DreadfowlNPC?
                if (npc!!.specialMove) {
                    npc.specialMove(FamiliarSpecial(victim))
                    npc.specialMove = false
                    return -1
                }
                npc.specialMove = RandomFunction.randomize(10) == 0
                return super.swing(entity, victim, state)
            }
        }
    }
}
