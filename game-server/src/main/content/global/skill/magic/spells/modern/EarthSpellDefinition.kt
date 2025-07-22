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
 * Represents configuration of earth spells.
 */
enum class EarthSpellDefinition(val type: SpellType, val buttonId: Int, val level: Int, val xp: Double, val castSound: Int, val start: Graphics, val projectile: Projectile, val end: Graphics, val runes: Array<Item>) {
    STRIKE(SpellType.STRIKE, ModernSpells.EARTH_STRIKE, 9, 9.5, Sounds.EARTHSTRIKE_CAST_AND_FIRE_132, Graphics(Graphic.EARTH_STRIKE_CAST_96, 96), SpellProjectile.create(Graphic.EARTH_STRIKE_PROJECTILE_97), Graphics(Graphic.EARTH_STRIKE_IMPACT_98, 96), arrayOf(Runes.MIND_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.AIR_RUNE.getItem(1))),
    BOLT(SpellType.BOLT, ModernSpells.EARTH_BOLT, 29, 19.5, Sounds.EARTHBOLT_CAST_AND_FIRE_130, Graphics(Graphic.EARTH_BOLT_CAST_123, 96), SpellProjectile.create(Graphic.EARTH_BOLT_PROJECTILE_124), Graphics(Graphic.EARTH_BOLT_IMPACT_125, 96), arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2))),
    BLAST(SpellType.BLAST, ModernSpells.EARTH_BLAST, 53, 31.5, Sounds.EARTHBLAST_CAST_AND_FIRE_128, Graphics(Graphic.EARTH_BLAST_CAST_138, 96), SpellProjectile.create(Graphic.EARTH_BLAST_PROJECTILE_139), Graphics(Graphic.EARTH_BLAST_IMPACT_140, 96), arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3))),
    WAVE(SpellType.WAVE, ModernSpells.EARTH_WAVE, 70, 40.0, Sounds.EARTHWAVE_CAST_AND_FIRE_134, Graphics(Graphic.EARTH_WAVE_CAST_164, 96), SpellProjectile.create(Graphic.EARTH_WAVE_PROJECTILE_165), Graphics(Graphic.EARTH_WAVE_IMPACT_166, 96), arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
}
