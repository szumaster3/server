package content.global.plugins.npc

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import content.global.skill.magic.spells.modern.def.BindSpellDefinition
import content.global.skill.magic.spells.modern.def.CurseSpellDefinition
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.NPCs

@Initializable
class ChaosDruidNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    init {
        isAggressive = true
        isWalks = true
        isNeverWalks = false
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): ChaosDruidNPC {
        return ChaosDruidNPC(id, location)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = SWING_HANDLER
    override fun getIds(): IntArray = ID

    private val SWING_HANDLER = object : MultiSwingHandler(
        SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(Animations.PUNCH_422)),
        SwitchAttack(CombatStyle.MAGIC.swingHandler, null)
    ) {

        override fun canSwing(entity: Entity, victim: Entity): InteractionType {
            val type = super.canSwing(entity, victim)
            return if (type == InteractionType.MOVE_INTERACT) InteractionType.NO_INTERACT else type ?: InteractionType.NO_INTERACT
        }

        override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
            val swing = super.swing(entity, victim, state)
            if (entity == null || victim == null || state == null) return swing

            val distance = entity.location.getDistance(victim.location)

            if (distance > 8.0) {
                entity.properties.combatPulse.style = CombatStyle.MELEE
            }

            if (RandomFunction.random(4) == 0) {
                entity.properties.combatPulse.style = CombatStyle.MAGIC

                val spellId = if (RandomFunction.random(2) == 0)
                    ModernSpells.BIND
                else
                    ModernSpells.CONFUSE

                val spell = SpellBookManager.SpellBook.MODERN.getSpell(spellId) as? CombatSpell
                if (spell != null) {
                    state.spell = spell
                    entity.properties.spell = spell
                    entity.properties.autocastSpell = spell

                    when (spellId) {
                        ModernSpells.BIND -> {
                            entity.animator.forceAnimation(Animation(Animations.CAST_SPELL_707))
                            entity.graphics(BindSpellDefinition.BIND.start)
                            SpellProjectile.create(BindSpellDefinition.BIND.projectile.projectileId)
                        }
                        ModernSpells.CONFUSE -> {
                            entity.animator.forceAnimation(CurseSpellDefinition.CONFUSE.animation)
                            entity.graphics(CurseSpellDefinition.CONFUSE.start)
                            SpellProjectile.create(CurseSpellDefinition.CONFUSE.projectile.projectileId)
                        }
                    }
                }
            } else {
                entity.properties.combatPulse.style = CombatStyle.MELEE
            }

            return swing
        }
    }

    companion object {
        private val ID = intArrayOf(
            NPCs.CHAOS_DRUID_181,
            NPCs.CHAOS_DRUID_2547,
            NPCs.CHAOS_DRUID_7105,
            NPCs.CHAOS_DRUID_7966
        )
    }
}
