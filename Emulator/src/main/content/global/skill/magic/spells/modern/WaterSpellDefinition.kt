package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Sounds
import org.rs.consts.Graphics as Graphic

/**
 * Represents configuration of water spells.
 */
enum class WaterSpellDefinition(
    val type: SpellType,
    val buttonId : Int,
    val level: Int,
    val baseExperience: Double,
    val sound: Int,
    val startGraphics: Graphics,
    val projectile: Projectile,
    val endGraphics: Graphics,
    val runes: Array<Item>
) {
    STRIKE(
        type = SpellType.STRIKE,
        4, 5, 7.5,
        sound = Sounds.WATERSTRIKE_CAST_AND_FIRE_211,
        startGraphics = Graphics(Graphic.WATER_STRIKE_CAST_93, 96),
        projectile = SpellProjectile.create(Graphic.WATER_STRIKE_PROJECTILE_94),
        endGraphics = Graphics(Graphic.WATER_STRIKE_IMPACT_95, 96),
        runes = arrayOf(Runes.MIND_RUNE.getItem(1), Runes.WATER_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1))
    ),
    BOLT(
        type = SpellType.BOLT,
        14,23, 16.5,
        sound = Sounds.WATERBOLT_CAST_AND_FIRE_209,
        startGraphics = Graphics(Graphic.WATER_BOLT_CAST_120, 96),
        projectile = SpellProjectile.create(Graphic.WATER_BOLT_PROJECTILE_121),
        endGraphics = Graphics(Graphic.WATER_BOLT_IMPACT_122, 96),
        runes = arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.WATER_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))
    ),
    BLAST(
        type = SpellType.BLAST,
        27, 47, 28.5,
        sound = Sounds.WATERBLAST_CAST_AND_FIRE_207,
        startGraphics = Graphics(Graphic.WATER_BLAST_CAST_135, 96),
        projectile = SpellProjectile.create(Graphic.WATER_BLAST_PROJECTILE_136),
        endGraphics = Graphics(Graphic.WATER_BLAST_IMPACT_137, 96),
        runes = arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.WATER_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3))
    ),
    WAVE(
        type = SpellType.WAVE,
        48, 65, 37.5,
        sound = Sounds.WATERWAVE_CAST_AND_FIRE_213,
        startGraphics = Graphics(Graphic.WATER_WAVE_CAST_161, 96),
        projectile = SpellProjectile.create(Graphic.WATER_WAVE_PROJECTILE_162),
        endGraphics = Graphics(Graphic.WATER_WAVE_IMPACT_163, 96),
        runes = arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.WATER_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5))
    );
}