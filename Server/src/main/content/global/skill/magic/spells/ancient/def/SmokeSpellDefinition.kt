package content.global.skill.magic.spells.ancient.def

import content.global.skill.magic.spells.AncientSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Sounds

/**
 * Configuration of Smoke spells.
 */
enum class SmokeSpellDefinition(val type: SpellType, val button: Int, val level: Int, val xp: Double, val castSound: Int, val impactSound: Int, val anim: Animation, val start: Graphics?, val projectile: Projectile?, val end: Graphics, val runes: Array<Item>) {
    RUSH(SpellType.RUSH, AncientSpells.SMOKE_RUSH, 50, 30.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_RUSH_IMPACT_185, Animation(1978, Animator.Priority.HIGH), null, SpellProjectile.create(384), Graphics(385, 96), arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1))),
    BURST(SpellType.BURST, AncientSpells.SMOKE_BURST, 62, 36.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_BURST_IMPACT_182, Animation(1979, Animator.Priority.HIGH), null, SpellProjectile.create(386), Graphics(387, 0), arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    BLITZ(SpellType.BLITZ, AncientSpells.SMOKE_BLITZ, 74, 42.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_BLITZ_IMPACT_181, Animation(1978, Animator.Priority.HIGH), null, SpellProjectile.create(389), Graphics(388, 96), arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    BARRAGE(SpellType.BARRAGE, AncientSpells.SMOKE_BARRAGE, 86, 48.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_BARRAGE_IMPACT_180, Animation(1979, Animator.Priority.HIGH), null, SpellProjectile.create(391), Graphics(390, 0), arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(4)))
}
