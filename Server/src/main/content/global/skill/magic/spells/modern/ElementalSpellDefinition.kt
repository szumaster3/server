package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import shared.consts.Sounds
import shared.consts.Graphics as Gfx

/**
 * Configuration of modern elemental spells.
 */
enum class ElementalSpellDefinition(val element: Element, val type: SpellType, val button: Int, val level: Int, val xp: Double, val sound: Int, val start: Graphics, val projectile: Projectile, val end: Graphics, val runes: Array<Item>) {
    AIR_STRIKE   (Element.AIR,   SpellType.STRIKE, ModernSpells.AIR_STRIKE, 1, 5.5,   Sounds.WINDSTRIKE_CAST_AND_FIRE_220,     Graphics(Gfx.WIND_STRIKE_CAST_90,92),  SpellProjectile.create(Gfx.WIND_STRIKE_PROJECTILE_91),  Graphics(Gfx.WIND_STRIKE_IMPACT_92,92),  arrayOf(Runes.MIND_RUNE.getItem(1),  Runes.AIR_RUNE.getItem(1))),
    AIR_BOLT     (Element.AIR,   SpellType.BOLT,   ModernSpells.AIR_BOLT,   17, 13.5, Sounds.WINDBOLT_CAST_AND_FIRE_218,       Graphics(Gfx.WIND_BOLT_CAST_117,92),   SpellProjectile.create(Gfx.WIND_BOLT_PROJECTILE_118),   Graphics(Gfx.WIND_BOLT_IMPACT_119,92),   arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.AIR_RUNE.getItem(2))),
    AIR_BLAST    (Element.AIR,   SpellType.BLAST,  ModernSpells.AIR_BLAST,  41, 25.5, Sounds.WINDBLAST_CAST_AND_FIRE_216,      Graphics(Gfx.WIND_BLAST_CAST_132,92),  SpellProjectile.create(Gfx.WIND_BLAST_PROJECTILE_133),  Graphics(Gfx.WIND_BLAST_IMPACT_134,92),  arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.AIR_RUNE.getItem(3))),
    AIR_WAVE     (Element.AIR,   SpellType.WAVE,   ModernSpells.AIR_WAVE,   62, 36.0, Sounds.WINDWAVE_CAST_AND_FIRE_222,       Graphics(Gfx.WIND_WAVE_CAST_158,92),   SpellProjectile.create(Gfx.WIND_WAVE_PROJECTILE_159),   Graphics(Gfx.WIND_WAVE_IMPACT_160,92),   arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.AIR_RUNE.getItem(5))),

    WATER_STRIKE (Element.WATER, SpellType.STRIKE, ModernSpells.WATER_STRIKE, 5, 7.5,  Sounds.WATERSTRIKE_CAST_AND_FIRE_211, Graphics(Gfx.WATER_STRIKE_CAST_93,92), SpellProjectile.create(Gfx.WATER_STRIKE_PROJECTILE_94), Graphics(Gfx.WATER_STRIKE_IMPACT_95,92), arrayOf(Runes.MIND_RUNE.getItem(1),  Runes.WATER_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1))),
    WATER_BOLT   (Element.WATER, SpellType.BOLT,   ModernSpells.WATER_BOLT,  23, 16.5, Sounds.WATERBOLT_CAST_AND_FIRE_209,   Graphics(Gfx.WATER_BOLT_CAST_120,92),  SpellProjectile.create(Gfx.WATER_BOLT_PROJECTILE_121),  Graphics(Gfx.WATER_BOLT_IMPACT_122,92),  arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.WATER_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    WATER_BLAST  (Element.WATER, SpellType.BLAST,  ModernSpells.WATER_BLAST, 47, 28.5, Sounds.WATERBLAST_CAST_AND_FIRE_207,  Graphics(Gfx.WATER_BLAST_CAST_135,92), SpellProjectile.create(Gfx.WATER_BLAST_PROJECTILE_136), Graphics(Gfx.WATER_BLAST_IMPACT_137,92), arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.WATER_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3))),
    WATER_WAVE   (Element.WATER, SpellType.WAVE,   ModernSpells.WATER_WAVE,  65, 37.5, Sounds.WATERWAVE_CAST_AND_FIRE_213,   Graphics(Gfx.WATER_WAVE_CAST_161,92),  SpellProjectile.create(Gfx.WATER_WAVE_PROJECTILE_162),  Graphics(Gfx.WATER_WAVE_IMPACT_163,92),  arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.WATER_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5))),

    EARTH_STRIKE (Element.EARTH, SpellType.STRIKE, ModernSpells.EARTH_STRIKE,9, 9.5,   Sounds.EARTHSTRIKE_CAST_AND_FIRE_132, Graphics(Gfx.EARTH_STRIKE_CAST_96,92), SpellProjectile.create(Gfx.EARTH_STRIKE_PROJECTILE_97), Graphics(Gfx.EARTH_STRIKE_IMPACT_98,92), arrayOf(Runes.MIND_RUNE.getItem(1),  Runes.EARTH_RUNE.getItem(2), Runes.AIR_RUNE.getItem(1))),
    EARTH_BOLT   (Element.EARTH, SpellType.BOLT,   ModernSpells.EARTH_BOLT,  29, 19.5, Sounds.EARTHBOLT_CAST_AND_FIRE_130,   Graphics(Gfx.EARTH_BOLT_CAST_123,92),  SpellProjectile.create(Gfx.EARTH_BOLT_PROJECTILE_124),  Graphics(Gfx.EARTH_BOLT_IMPACT_125,92),  arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2))),
    EARTH_BLAST  (Element.EARTH, SpellType.BLAST,  ModernSpells.EARTH_BLAST, 53, 31.5, Sounds.EARTHBLAST_CAST_AND_FIRE_128,  Graphics(Gfx.EARTH_BLAST_CAST_138,92), SpellProjectile.create(Gfx.EARTH_BLAST_PROJECTILE_139), Graphics(Gfx.EARTH_BLAST_IMPACT_140,92), arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3))),
    EARTH_WAVE   (Element.EARTH, SpellType.WAVE,   ModernSpells.EARTH_WAVE,  70, 40.0, Sounds.EARTHWAVE_CAST_AND_FIRE_134,   Graphics(Gfx.EARTH_WAVE_CAST_164,92),  SpellProjectile.create(Gfx.EARTH_WAVE_PROJECTILE_165),  Graphics(Gfx.EARTH_WAVE_IMPACT_166,92),  arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5))),

    FIRE_STRIKE  (Element.FIRE,  SpellType.STRIKE, ModernSpells.FIRE_STRIKE, 13, 11.5, Sounds.FIRESTRIKE_CAST_AND_FIRE_160,   Graphics(Gfx.FIRE_STRIKE_CAST_99,92),  SpellProjectile.create(Gfx.FIRE_STRIKE_PROJECTILE_100), Graphics(Gfx.FIRE_STRIKE_IMPACT_101,92), arrayOf(Runes.MIND_RUNE.getItem(1),  Runes.FIRE_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2))),
    FIRE_BOLT    (Element.FIRE,  SpellType.BOLT,   ModernSpells.FIRE_BOLT,   35, 22.5, Sounds.FIREBOLT_CAST_AND_FIRE_157,     Graphics(Gfx.FIRE_BOLT_CAST_126,92),   SpellProjectile.create(Gfx.FIRE_BOLT_PROJECTILE_127),   Graphics(Gfx.FIRE_BOLT_IMPACT_128,92),   arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3))),
    FIRE_BLAST   (Element.FIRE,  SpellType.BLAST,  ModernSpells.FIRE_BLAST,  59, 34.5, Sounds.FIREBLAST_CAST_AND_FIRE_155,    Graphics(Gfx.FIRE_BLAST_CAST_129,92),  SpellProjectile.create(Gfx.FIRE_BLAST_PROJECTILE_130),  Graphics(Gfx.FIRE_BLAST_IMPACT_131,92),  arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(5), Runes.AIR_RUNE.getItem(4))),
    FIRE_WAVE    (Element.FIRE,  SpellType.WAVE,   ModernSpells.FIRE_WAVE,   75, 42.5, Sounds.FIREWAVE_CAST_AND_FIRE_162,     Graphics(Gfx.FIRE_WAVE_CAST_155,92),   SpellProjectile.create(Gfx.FIRE_WAVE_PROJECTILE_156),   Graphics(Gfx.FIRE_WAVE_IMPACT_157,92),   arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));

    enum class Element { AIR, WATER, EARTH, FIRE }
}
