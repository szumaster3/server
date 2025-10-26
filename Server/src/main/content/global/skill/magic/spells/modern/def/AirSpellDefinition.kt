package content.global.skill.magic.spells.modern.def

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import shared.consts.Sounds
import shared.consts.Graphics as Graphic

/**
 * Represents configuration of air spells.
 */
enum class AirSpellDefinition(val type: SpellType, val button: Int, val level: Int, val xp: Double, val sound: Int, val start: Graphics, val projectile: Projectile, val end: Graphics, val runes: Array<Item>) {
    STRIKE(SpellType.STRIKE, ModernSpells.AIR_STRIKE, 1, 5.5, Sounds.WINDSTRIKE_CAST_AND_FIRE_220, Graphics(Graphic.WIND_STRIKE_CAST_90, 96), SpellProjectile.create(Graphic.WIND_STRIKE_PROJECTILE_91), Graphics(Graphic.WIND_STRIKE_IMPACT_92, 96), arrayOf(Runes.MIND_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1))),
    BOLT(SpellType.BOLT, ModernSpells.AIR_BOLT, 17, 13.5, Sounds.WINDBOLT_CAST_AND_FIRE_218, Graphics(Graphic.WIND_BOLT_CAST_117, 96), SpellProjectile.create(Graphic.WIND_BOLT_PROJECTILE_118), Graphics(Graphic.WIND_BOLT_IMPACT_119, 96), arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.AIR_RUNE.getItem(2))),
    BLAST(SpellType.BLAST, ModernSpells.AIR_BLAST, 41, 25.5, Sounds.WINDBLAST_CAST_AND_FIRE_216, Graphics(Graphic.WIND_BLAST_CAST_132, 96), SpellProjectile.create(Graphic.WIND_BLAST_PROJECTILE_133), Graphics(Graphic.WIND_BLAST_IMPACT_134, 96), arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.AIR_RUNE.getItem(3))),
    WAVE(SpellType.WAVE, ModernSpells.AIR_WAVE, 62, 36.0, Sounds.WINDWAVE_CAST_AND_FIRE_222, Graphics(Graphic.WIND_WAVE_CAST_158, 96), SpellProjectile.create(Graphic.WIND_WAVE_PROJECTILE_159), Graphics(Graphic.WIND_WAVE_IMPACT_160, 96), arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.AIR_RUNE.getItem(5)));
}