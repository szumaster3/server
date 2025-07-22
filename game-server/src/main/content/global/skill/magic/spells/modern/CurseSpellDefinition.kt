package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Sounds
import org.rs.consts.Graphics as Gfx

/**
 * Represents configuration of curse spells.
 */
enum class CurseSpellDefinition(
    val type: SpellType,
    val buttonId: Int,
    val level: Int,
    val xp: Double,
    val castSound: Int,
    val impactSound: Int,
    val start: Graphics,
    val projectile: Projectile,
    val end: Graphics,
    val runes: Array<Item>
) {
    CONFUSE(type = SpellType.CONFUSE, buttonId = ModernSpells.CONFUSE, level = 3, xp = 13.0, castSound = Sounds.CONFUSE_CAST_AND_FIRE_119, impactSound = Sounds.CONFUSE_HIT_121, start = Graphics(Gfx.CONFUSE_CAST_102, 96), projectile = SpellProjectile.create(Gfx.CONFUSE_PROJECTILE_103), end = Graphics(Gfx.CONFUSE_IMPACT_104, 96), runes = arrayOf(Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3))),
    WEAKEN(type = SpellType.WEAKEN, buttonId = ModernSpells.WEAKEN, level = 11, xp = 21.0, castSound = Sounds.WEAKEN_CAST_AND_FIRE_3011, impactSound = Sounds.WEAKEN_HIT_3010, start = Graphics(Gfx.WEAKEN_CAST_105, 96), projectile = SpellProjectile.create(Gfx.WEAKEN_PROJECTILE_106), end = Graphics(Gfx.WEAKEN_IMPACT_107, 96), runes = arrayOf(Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3))),
    CURSE(type = SpellType.CURSE, buttonId = ModernSpells.CURSE, level = 19, xp = 29.0, castSound = Sounds.CURSE_CAST_AND_FIRE_127, impactSound = Sounds.CURSE_HIT_126, start = Graphics(Gfx.CURSE_CAST_108, 96), projectile = SpellProjectile.create(Gfx.CURSE_PROJECTILE_109), end = Graphics(Gfx.CURSE_IMPACT_110, 96), runes = arrayOf(Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(2))),
    VULNERABILITY(type = SpellType.VULNERABILITY, buttonId = ModernSpells.VULNERABILITY, level = 66, xp = 76.0, castSound = Sounds.VULNERABILITY_CAST_AND_FIRE_3009, impactSound = Sounds.VULNERABILITY_IMPACT_3008, start = Graphics(Gfx.VULNERABILITY_CAST_167, 96), projectile = SpellProjectile.create(Gfx.VULNERABILITY_PROJECTILE_168), end = Graphics(Gfx.VULNERABILITY_IMPACT_169, 96, 1), runes = arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5))),
    ENFEEBLE(type = SpellType.ENFEEBLE, buttonId = ModernSpells.ENFEEBLE, level = 73, xp = 83.0, castSound = Sounds.ENFEEBLE_CAST_AND_FIRE_148, impactSound = Sounds.ENFEEBLE_HIT_150, start = Graphics(Gfx.ENFEEBLE_CAST_170, 96), projectile = SpellProjectile.create(Gfx.ENFEEBLE_PROJECTILE_171), end = Graphics(Gfx.ENFEEBLE_IMPACT_172, 96), runes = arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(8), Runes.WATER_RUNE.getItem(8))),
    STUN(type = SpellType.STUN, buttonId = ModernSpells.STUN, level = 80, xp = 90.0, castSound = Sounds.STUN_CAST_AND_FIRE_3004, impactSound = Sounds.STUN_IMPACT_3005, start = Graphics(Gfx.STUN_CAST_173, 96), projectile = SpellProjectile.create(Gfx.STUN_PROJECTILE_174), end = Graphics(Gfx.WEAKEN_IMPACT_107, 96), runes = arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(12), Runes.WATER_RUNE.getItem(12)))
}
