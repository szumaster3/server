package content.global.activity.phoenix

import content.data.consumables.effects.RestoreEffect
import content.data.consumables.effects.SkillEffect
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction

class PhoenixSwingHandler : CombatSwingHandler(CombatStyle.MAGIC) {

    companion object {
        private val MAGIC_ATTACK = Animation(11076, Animator.Priority.HIGH)
        private val DUST_ATTACK  = Animation(11093, Animator.Priority.HIGH)
        private const val DUST_DURATION_TICKS = 20
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int
    {
        return if (RandomFunction.randomize(10) < 2)
        {
            applyDustAttack(entity as NPC)
            state?.style = CombatStyle.MAGIC
            state?.estimatedHit = 0
            4
        }
        else
        {
            val maxHit = 25
            val hit = RandomFunction.random(maxHit)
            state?.estimatedHit = hit
            state?.maximumHit = maxHit
            state?.style = CombatStyle.MAGIC
            3
        }
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?)
    {
        if (state?.style == CombatStyle.MAGIC)
        {
            if (state.estimatedHit == 0)
            {
                entity.animate(DUST_ATTACK)
                for (target in RegionManager.getLocalPlayers(entity as NPC, 12))
                {
                    if (target is Player)
                    {
                        Projectile.magic(entity, target, 1980, 42, 36, 45, 5).send()
                    }
                }
            }
            else
            {
                entity.animate(MAGIC_ATTACK)
                for (target in RegionManager.getLocalPlayers(entity as NPC, 12))
                {
                    if (target is Player)
                    {
                        Projectile.magic(entity, target, 1976, 42, 36, 45, 5).send()
                    }
                }
            }
        }
    }

    private fun applyDustAttack(phoenix: NPC)
    {
        for (target in RegionManager.getLocalPlayers(phoenix, 5))
        {
            if (target is Player)
            {

                fun drainSkill(skill: Int)
                {
                    val drain = (3 + target.skills.getLevel(skill) / 14).coerceAtMost(10)
                    target.skills.updateLevel(
                        skill, -drain, target.skills.getStaticLevel(skill) - drain
                    )
                }

                drainSkill(Skills.ATTACK)
                drainSkill(Skills.RANGE)
                drainSkill(Skills.MAGIC)

                GameWorld.Pulser.submit(object : Pulse(DUST_DURATION_TICKS, target)
                {
                    override fun pulse(): Boolean {
                        if (target.isActive) {
                            RestoreEffect(base = 0.0, bonus = 0.0, skills = false).activate(target)
                        }
                        return true
                    }
                })
            }
        }
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?)
    {
        if (state != null && state.estimatedHit > 0)
        {
            state.style.swingHandler.impact(entity, victim, state)
        }
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?)
    {
        if (state != null &&
            state.estimatedHit > 0 &&
            victim != null)
        {
            victim.animate(victim.properties.defenceAnimation)
        }
    }

    override fun calculateAccuracy(entity: Entity?) = 0
    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double) = 0
    override fun calculateDefence(victim: Entity?, attacker: Entity?) = 0
    override fun getSetMultiplier(e: Entity?, skillId: Int) = 0.0
}
