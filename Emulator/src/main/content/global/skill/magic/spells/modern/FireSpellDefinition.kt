package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Graphics as Gfx
import org.rs.consts.Sounds

/**
 * Represents configuration of fire spells.
 */
enum class FireSpellDefinition(
    val type: SpellType,
    val buttonId: Int,
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
        8, 13, 11.5,
        sound = Sounds.FIRESTRIKE_CAST_AND_FIRE_160,
        start = Graphics(Gfx.FIRE_STRIKE_CAST_99, 96),
        projectile = SpellProjectile.create(Gfx.FIRE_STRIKE_PROJECTILE_100),
        end = Graphics(Gfx.FIRE_STRIKE_IMPACT_101, 96),
        runes = arrayOf(Runes.MIND_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2))
    ),
    BOLT(
        type = SpellType.BOLT,
        20, 35, 22.5,
        sound = Sounds.FIREBOLT_CAST_AND_FIRE_157,
        start = Graphics(Gfx.FIRE_BOLT_CAST_126, 96),
        projectile = SpellProjectile.create(Gfx.FIRE_BOLT_PROJECTILE_127),
        end = Graphics(Gfx.FIRE_BOLT_IMPACT_128, 96),
        runes = arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3))
    ),
    BLAST(
        type = SpellType.BLAST,
        38, 59, 34.5,
        sound = Sounds.FIREBLAST_CAST_AND_FIRE_155,
        start = Graphics(Gfx.FIRE_BLAST_CAST_129, 96),
        projectile = SpellProjectile.create(Gfx.FIRE_BLAST_PROJECTILE_130),
        end = Graphics(Gfx.FIRE_BLAST_IMPACT_131, 96),
        runes = arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(5), Runes.AIR_RUNE.getItem(4))
    ),
    WAVE(
        type = SpellType.WAVE,
        55, 75, 42.5,
        sound = Sounds.FIREWAVE_CAST_AND_FIRE_162,
        start = Graphics(Gfx.FIRE_WAVE_CAST_155, 96),
        projectile = SpellProjectile.create(Gfx.FIRE_WAVE_PROJECTILE_156),
        end = Graphics(Gfx.FIRE_WAVE_IMPACT_157, 96),
        runes = arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5))
    );
}
