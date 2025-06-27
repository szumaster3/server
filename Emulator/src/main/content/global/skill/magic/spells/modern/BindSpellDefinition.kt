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
 * Represents configuration of bind spells.
 */
enum class BindSpellDefinition(
    val type: SpellType,
    val level: Int,
    val xp: Double,
    val castSound: Int,
    val impactSound: Int,
    val start: Graphics,
    val projectile: Projectile,
    val end: Graphics,
    val runes: Array<Item>
) {
    BIND(
        type = SpellType.BIND,
        20, 30.0,
        castSound = Sounds.BIND_CAST_101,
        impactSound = Sounds.BIND_IMPACT_99,
        start = Graphics(Graphic.BIND_CAST_177, 96),
        projectile = SpellProjectile.create(Graphic.BIND_PROJECTILE_178),
        end = Graphics(Graphic.BIND_IMPACT_179, 96),
        runes = arrayOf(Runes.NATURE_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(3))
    ),
    SNARE(
        type = SpellType.SNARE,
        50, 60.0,
        castSound = Sounds.SNARE_CAST_AND_FIRE_3003,
        impactSound = Sounds.SNARE_IMPACT_3002,
        start = Graphics(Graphic.BIND_CAST_177, 96),
        projectile = SpellProjectile.create(Graphic.BIND_PROJECTILE_178),
        end = Graphics(Graphic.SNARE_IMPACT_180, 96),
        runes = arrayOf(Runes.NATURE_RUNE.getItem(3), Runes.EARTH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4))
    ),
    ENTANGLE(
        type = SpellType.ENTANGLE,
        79, 89.0,
        castSound = Sounds.ENTANGLE_CAST_AND_FIRE_151,
        impactSound = Sounds.ENTANGLE_HIT_153,
        start = Graphics(Graphic.BIND_CAST_177, 96),
        projectile = SpellProjectile.create(Graphic.BIND_PROJECTILE_178),
        end = Graphics(Graphic.ENTANGLE_IMPACT_181, 96),
        runes = arrayOf(Runes.NATURE_RUNE.getItem(4), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5))
    )
}