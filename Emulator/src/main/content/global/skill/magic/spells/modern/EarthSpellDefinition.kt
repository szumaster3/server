package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Graphics as Graphic
import org.rs.consts.Sounds

/**
 * Represents configuration of earth spells.
 */
enum class EarthSpellDefinition(val type: SpellType, val buttonId: Int, val level: Int, val xp: Double, val castSound: Int, val start: Graphics, val projectile: Projectile, val end: Graphics, val runes: Array<Item>) {
    STRIKE(type = SpellType.STRIKE, buttonId = 6, level = 9, xp = 9.5, castSound = Sounds.EARTHSTRIKE_CAST_AND_FIRE_132, start = Graphics(Graphic.EARTH_STRIKE_CAST_96, 96), projectile = SpellProjectile.create(Graphic.EARTH_STRIKE_PROJECTILE_97), end = Graphics(Graphic.EARTH_STRIKE_IMPACT_98, 96), runes = arrayOf(Runes.MIND_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.AIR_RUNE.getItem(1))),
    BOLT(type = SpellType.BOLT, buttonId = 17, level = 29, xp = 19.5, castSound = Sounds.EARTHBOLT_CAST_AND_FIRE_130, start = Graphics(Graphic.EARTH_BOLT_CAST_123, 96), projectile = SpellProjectile.create(Graphic.EARTH_BOLT_PROJECTILE_124), end = Graphics(Graphic.EARTH_BOLT_IMPACT_125, 96), runes = arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2))),
    BLAST(type = SpellType.BLAST, buttonId = 33, level = 53, xp = 31.5, castSound = Sounds.EARTHBLAST_CAST_AND_FIRE_128, start = Graphics(Graphic.EARTH_BLAST_CAST_138, 96), projectile = SpellProjectile.create(Graphic.EARTH_BLAST_PROJECTILE_139), end = Graphics(Graphic.EARTH_BLAST_IMPACT_140, 96), runes = arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3))),
    WAVE(type = SpellType.WAVE, buttonId = 52, level = 70, xp = 40.0, castSound = Sounds.EARTHWAVE_CAST_AND_FIRE_134, start = Graphics(Graphic.EARTH_WAVE_CAST_164, 96), projectile = SpellProjectile.create(Graphic.EARTH_WAVE_PROJECTILE_165), end = Graphics(Graphic.EARTH_WAVE_IMPACT_166, 96), runes = arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
}
