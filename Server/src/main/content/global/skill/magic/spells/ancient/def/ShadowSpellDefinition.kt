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
 * Configuration of Shadow spells.
 */
enum class ShadowSpellDefinition(val type: SpellType, val button: Int, val level: Int, val xp: Double, val castSound: Int, val impactSound: Int, val anim: Animation, val start: Graphics?, val projectile: Projectile?, val end: Graphics, val runes: Array<Item>) {
    RUSH(SpellType.RUSH, AncientSpells.SHADOW_RUSH, 52, 31.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_RUSH_IMPACT_179, Animation(1978, Animator.Priority.HIGH), null, SpellProjectile.create(378), Graphics(379, 96), arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.AIR_RUNE.getItem(1))),
    BURST(SpellType.BURST, AncientSpells.SHADOW_BURST, 64, 37.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_BURST_IMPACT_177, Animation(1979, Animator.Priority.HIGH), null, SpellProjectile.create(380), Graphics(381, 0), arrayOf(Runes.SOUL_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.AIR_RUNE.getItem(1))),
    BLITZ(SpellType.BLITZ, AncientSpells.SHADOW_BLITZ, 76, 43.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_BLITZ_IMPACT_176, Animation(1978, Animator.Priority.HIGH), null, null, Graphics(382, 96), arrayOf(Runes.SOUL_RUNE.getItem(2), Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    BARRAGE(SpellType.BARRAGE, AncientSpells.SHADOW_BARRAGE, 88, 48.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_BARRAGE_IMPACT_175, Animation(1979, Animator.Priority.HIGH), null, null, Graphics(383, 0), arrayOf(Runes.SOUL_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.AIR_RUNE.getItem(4)))
}
