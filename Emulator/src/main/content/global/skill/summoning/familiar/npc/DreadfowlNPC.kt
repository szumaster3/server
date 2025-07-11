package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.MeleeSwingHandler
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
import org.rs.consts.Items
import org.rs.consts.NPCs
import kotlin.math.floor

@Initializable
class DreadfowlNPC @JvmOverloads constructor(
    owner: Player? = null,
    id: Int = NPCs.DREADFOWL_6825
) : Familiar(owner, id, 400, Items.DREADFOWL_POUCH_12043, 3, WeaponInterface.STYLE_CAST) {

    init {
        super.setCombatHandler(DreadfowlCombatHandler)
        boosts.add(SkillBonus(Skills.FARMING, 1.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return DreadfowlNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.node as? Entity ?: return false

        if (!canAttack(target)) return false

        if (!owner.inCombat()) {
            sendMessage(owner, "Your familiar can only attack when you're in combat.")
            return false
        }

        if (!canSwingAt(target)) {
            DreadfowlCombatHandler.queueMagicAttack(this, target)
            return true
        }

        performMagicAttack(target)
        return true
    }

    private fun canSwingAt(target: Entity): Boolean {
        return properties.combatPulse.getNextAttack() <= GameWorld.ticks &&
                CombatStyle.MAGIC.swingHandler.canSwing(this, target) != InteractionType.NO_INTERACT
    }

    fun performMagicAttack(target: Entity) {
        visualize(Animation(5387, Priority.HIGH), Graphics.create(1523))
        Projectile.magic(this, target, 1318, 40, 36, 51, 10).send()

        val ticks = 2 + floor(location.getDistance(target.location) * 0.5).toInt()
        properties.combatPulse.setNextAttack(4)

        Pulser.submit(object : Pulse(ticks, this, target) {
            override fun pulse(): Boolean {
                val state = BattleState(this@DreadfowlNPC, target)
                val hit = if (CombatStyle.MAGIC.swingHandler.isAccurateImpact(this@DreadfowlNPC, target))
                    RandomFunction.randomize(3)
                else 0

                state.estimatedHit = hit
                target.impactHandler.handleImpact(owner, hit, CombatStyle.MAGIC, state)
                return true
            }
        })
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DREADFOWL_6825, NPCs.DREADFOWL_6826)

    companion object {
        private object DreadfowlCombatHandler : MeleeSwingHandler() {
            private val specialQueue = mutableSetOf<Int>()

            fun queueMagicAttack(npc: DreadfowlNPC, target: Entity) {
                specialQueue.add(npc.index)
                npc.properties.combatPulse.attack(target)
            }

            override fun canSwing(entity: Entity, victim: Entity): InteractionType {
                val npc = entity as DreadfowlNPC
                val interaction = if (specialQueue.contains(npc.index)) {
                    CombatStyle.MAGIC.swingHandler.canSwing(npc, victim)
                } else {
                    super.canSwing(npc, victim)
                }
                return interaction ?: InteractionType.NO_INTERACT
            }

            override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
                val npc = entity as? DreadfowlNPC ?: return -1
                val target = victim ?: return -1

                if (specialQueue.remove(npc.index)) {
                    npc.performMagicAttack(target)
                    return -1 // Skip Melee attack.
                }

                // Optional: 10% chance to trigger magic auto-attack.
                if (RandomFunction.randomize(10) == 0) {
                    queueMagicAttack(npc, target)
                    return -1
                }

                return super.swing(npc, target, state)
            }
        }
    }
}
