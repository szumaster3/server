package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Sounds
import org.rs.consts.Graphics as Graphic

/**
 * Represents configuration of air spells.
 */
enum class AirSpellDefinition(
    val type: SpellType,
    val level: Int,
    val xp: Double,
    val sound: Int,
    val start: Graphics,
    val projectile: Projectile,
    val end: Graphics,
    val runes: Array<Item>
) {
    STRIKE(
        type = SpellType.STRIKE,
        1, 5.5,
        sound = Sounds.WINDSTRIKE_CAST_AND_FIRE_220,
        start = Graphics(Graphic.WIND_STRIKE_CAST_90, 96),
        projectile = SpellProjectile.create(Graphic.WIND_STRIKE_PROJECTILE_91),
        end = Graphics(Graphic.WIND_STRIKE_IMPACT_92, 96),
        runes = arrayOf(Runes.MIND_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1))
    ),
    BOLT(
        SpellType.BOLT,
        17, 13.5,
        sound = Sounds.WINDBOLT_CAST_AND_FIRE_218,
        start = Graphics(Graphic.WIND_BOLT_CAST_117, 96),
        projectile = SpellProjectile.create(Graphic.WIND_BOLT_PROJECTILE_118),
        end = Graphics(Graphic.WIND_BOLT_IMPACT_119, 96),
        runes = arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.AIR_RUNE.getItem(2))
    ),
    BLAST(
        type = SpellType.BLAST,
        41, 25.5,
        sound = Sounds.WINDBLAST_CAST_AND_FIRE_216,
        start = Graphics(Graphic.WIND_BLAST_CAST_132, 96),
        projectile = SpellProjectile.create(Graphic.WIND_BLAST_PROJECTILE_133),
        end = Graphics(Graphic.WIND_BLAST_IMPACT_134, 96),
        runes = arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.AIR_RUNE.getItem(3))
    ),
    WAVE(
        type = SpellType.WAVE,
        62, 36.0,
        sound = Sounds.WINDWAVE_CAST_AND_FIRE_222,
        start = Graphics(Graphic.WIND_WAVE_CAST_158, 96),
        projectile = SpellProjectile.create(Graphic.WIND_WAVE_PROJECTILE_159),
        end = Graphics(Graphic.WIND_WAVE_IMPACT_160, 96),
        runes = arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.AIR_RUNE.getItem(5))
    );
}