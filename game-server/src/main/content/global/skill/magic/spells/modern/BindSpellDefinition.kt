package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Sounds
import org.rs.consts.Graphics as Graphic

/**
 * Represents configuration of bind spells.
 */
enum class BindSpellDefinition(val type: SpellType, val buttonId: Int, val level: Int, val xp: Double, val castSound: Int, val impactSound: Int, val start: Graphics, val projectile: Projectile, val end: Graphics, val runes: Array<Item>) {
    BIND(SpellType.BIND, ModernSpells.BIND, 20, 30.0, Sounds.BIND_CAST_101, Sounds.BIND_IMPACT_99, Graphics(Graphic.BIND_CAST_177, 96), SpellProjectile.create(Graphic.BIND_PROJECTILE_178), Graphics(Graphic.BIND_IMPACT_179, 96), arrayOf(Runes.NATURE_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(3))),
    SNARE(SpellType.SNARE, ModernSpells.SNARE, 50, 60.0, Sounds.SNARE_CAST_AND_FIRE_3003, Sounds.SNARE_IMPACT_3002, Graphics(Graphic.BIND_CAST_177, 96), SpellProjectile.create(Graphic.BIND_PROJECTILE_178), Graphics(Graphic.SNARE_IMPACT_180, 96), arrayOf(Runes.NATURE_RUNE.getItem(3), Runes.EARTH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4))),
    ENTANGLE(SpellType.ENTANGLE, ModernSpells.ENTANGLE, 79, 89.0, Sounds.ENTANGLE_CAST_AND_FIRE_151, Sounds.ENTANGLE_HIT_153, Graphics(Graphic.BIND_CAST_177, 96), SpellProjectile.create(Graphic.BIND_PROJECTILE_178), Graphics(Graphic.ENTANGLE_IMPACT_181, 96), arrayOf(Runes.NATURE_RUNE.getItem(4), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5)))
}