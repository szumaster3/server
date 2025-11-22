package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class ModernElementalSpell private constructor(val def: ElementalSpellDefinition) :
    CombatSpell(
        def.type,
        SpellBook.MODERN,
        def.level,
        def.xp,
        def.sound,
        def.sound + 1,
        SpellProjectile.ANIMATION,
        def.start,
        def.projectile,
        def.end,
        *def.runes
    ) {
    constructor() : this(ElementalSpellDefinition.AIR_STRIKE)

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        when (def.element) {
            ElementalSpellDefinition.Element.AIR -> def.type.getImpactAmount(entity, victim, 1)
            ElementalSpellDefinition.Element.WATER -> def.type.getImpactAmount(entity, victim, 2)
            ElementalSpellDefinition.Element.EARTH -> def.type.getImpactAmount(entity, victim, 3)
            ElementalSpellDefinition.Element.FIRE -> def.type.getImpactAmount(entity, victim, 4)
        }

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        ElementalSpellDefinition.values().forEach {
            SpellBook.MODERN.register(it.button, ModernElementalSpell(it))
        }
        return this
    }
}
