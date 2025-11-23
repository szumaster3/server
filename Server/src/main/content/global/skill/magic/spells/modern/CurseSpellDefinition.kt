package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Sounds
import shared.consts.Graphics as Gfx

/**
 * Represents configuration of curse spells.
 */
enum class CurseSpellDefinition(val type: SpellType, val buttonId: Int, val level: Int, val xp: Double, val castSound: Int, val impactSound: Int, val animation : Animation, val start: Graphics, val projectile: Projectile, val end: Graphics, val runes: Array<Item>) {
    CONFUSE(SpellType.CONFUSE, ModernSpells.CONFUSE, 3, 13.0, Sounds.CONFUSE_CAST_AND_FIRE_119, Sounds.CONFUSE_HIT_121, Animation(Animations.CAST_CONFUSE_1163), Graphics(Gfx.CONFUSE_CAST_102, 92), SpellProjectile.create(Gfx.CONFUSE_PROJECTILE_103), Graphics(Gfx.CONFUSE_IMPACT_104, 92), arrayOf(Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3))),
    WEAKEN(SpellType.WEAKEN, ModernSpells.WEAKEN, 11, 21.0, Sounds.WEAKEN_CAST_AND_FIRE_3011, Sounds.WEAKEN_HIT_3010, Animation(Animations.CAST_WEAKEN_1164), Graphics(Gfx.WEAKEN_CAST_105, 92), SpellProjectile.create(Gfx.WEAKEN_PROJECTILE_106), Graphics(Gfx.WEAKEN_IMPACT_107, 92), arrayOf(Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3))),
    CURSE(SpellType.CURSE, ModernSpells.CURSE, 19, 29.0, Sounds.CURSE_CAST_AND_FIRE_127, Sounds.CURSE_HIT_126, Animation(Animations.CAST_CURSE_1165), Graphics(Gfx.CURSE_CAST_108, 92), SpellProjectile.create(Gfx.CURSE_PROJECTILE_109), Graphics(Gfx.CURSE_IMPACT_110, 92), arrayOf(Runes.BODY_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(2))),
    VULNERABILITY(SpellType.VULNERABILITY, ModernSpells.VULNERABILITY, 66, 76.0, Sounds.VULNERABILITY_CAST_AND_FIRE_3009, Sounds.VULNERABILITY_IMPACT_3008, Animation(Animations.CAST_SPELL_1167), Graphics(Gfx.VULNERABILITY_CAST_167, 92), SpellProjectile.create(Gfx.VULNERABILITY_PROJECTILE_168), Graphics(Gfx.VULNERABILITY_IMPACT_169, 92, 1), arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5))),
    ENFEEBLE(SpellType.ENFEEBLE, ModernSpells.ENFEEBLE, 73, 83.0, Sounds.ENFEEBLE_CAST_AND_FIRE_148, Sounds.ENFEEBLE_HIT_150, Animation(Animations.CAST_ENFEEBLE_1168),Graphics(Gfx.ENFEEBLE_CAST_170, 92), SpellProjectile.create(Gfx.ENFEEBLE_PROJECTILE_171), Graphics(Gfx.ENFEEBLE_IMPACT_172, 92), arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(8), Runes.WATER_RUNE.getItem(8))),
    STUN(SpellType.STUN, ModernSpells.STUN, 80, 90.0, Sounds.STUN_CAST_AND_FIRE_3004, Sounds.STUN_IMPACT_3005, Animation(Animations.CAST_STUN_1166), Graphics(Gfx.STUN_CAST_173, 92), SpellProjectile.create(Gfx.STUN_PROJECTILE_174), Graphics(Gfx.WEAKEN_IMPACT_107, 92), arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(12), Runes.WATER_RUNE.getItem(12)))
}
