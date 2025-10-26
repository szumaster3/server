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
 * Represents configuration of water spells.
 */
enum class WaterSpellDefinition(val type: SpellType, val buttonId : Int, val level: Int, val xp: Double, val sound: Int, val startGraphics: Graphics, val projectile: Projectile, val endGraphics: Graphics, val runes: Array<Item>) {
    STRIKE(SpellType.STRIKE, ModernSpells.WATER_STRIKE, 5, 7.5, Sounds.WATERSTRIKE_CAST_AND_FIRE_211, Graphics(Graphic.WATER_STRIKE_CAST_93, 96), SpellProjectile.create(Graphic.WATER_STRIKE_PROJECTILE_94), Graphics(Graphic.WATER_STRIKE_IMPACT_95, 96), arrayOf(Runes.MIND_RUNE.getItem(1), Runes.WATER_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1))),
    BOLT(SpellType.BOLT, ModernSpells.WATER_BOLT, 23, 16.5, Sounds.WATERBOLT_CAST_AND_FIRE_209, Graphics(Graphic.WATER_BOLT_CAST_120, 96), SpellProjectile.create(Graphic.WATER_BOLT_PROJECTILE_121), Graphics(Graphic.WATER_BOLT_IMPACT_122, 96), arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.WATER_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    BLAST(SpellType.BLAST, ModernSpells.WATER_BLAST, 47, 28.5, Sounds.WATERBLAST_CAST_AND_FIRE_207, Graphics(Graphic.WATER_BLAST_CAST_135, 96), SpellProjectile.create(Graphic.WATER_BLAST_PROJECTILE_136), Graphics(Graphic.WATER_BLAST_IMPACT_137, 96), arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.WATER_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3))),
    WAVE(SpellType.WAVE, ModernSpells.WATER_WAVE, 65, 37.5, Sounds.WATERWAVE_CAST_AND_FIRE_213, Graphics(Graphic.WATER_WAVE_CAST_161, 96), SpellProjectile.create(Graphic.WATER_WAVE_PROJECTILE_162), Graphics(Graphic.WATER_WAVE_IMPACT_163, 96), arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.WATER_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
}