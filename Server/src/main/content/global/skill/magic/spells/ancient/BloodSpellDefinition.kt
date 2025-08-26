package content.global.skill.magic.spells.ancient

import content.global.skill.magic.spells.AncientSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Sounds
import shared.consts.Graphics as Graphic

/**
 * Represents the configuration of Blood spells.
 */
enum class BloodSpellDefinition(
    val type: SpellType,
    val button: Int,
    val level: Int,
    val xp: Double,
    val sound: Int,
    val impactSound: Int,
    val anim: Animation,
    val start: Graphics?,
    val projectile: Projectile?,
    val end: Graphics,
    val runes: Array<Item>
) {
    RUSH(SpellType.RUSH, AncientSpells.BLOOD_RUSH, 56, 33.0, Sounds.BLOOD_RUSH_CASTING_108, Sounds.BLOOD_RUSH_IMPACT_110, Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(372), Graphics(Graphic.ANCIENTS_BLOOD_SPELLS_373, 96), arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2))),
    BURST(SpellType.BURST, AncientSpells.BLOOD_BURST, 68, 39.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BURST_IMPACT_105, Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, null, Graphics(376, 0), arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4))),
    BLITZ(SpellType.BLITZ, AncientSpells.BLOOD_BLITZ, 80, 45.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BLITZ_IMPACT_104, Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(374), Graphics(375, 96), arrayOf(Runes.BLOOD_RUNE.getItem(4), Runes.DEATH_RUNE.getItem(2))),
    BARRAGE(SpellType.BARRAGE, AncientSpells.BLOOD_BARRAGE, 92, 51.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BARRAGE_IMPACT_102, Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, null, Graphics(377, 0), arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.BLOOD_RUNE.getItem(4), Runes.DEATH_RUNE.getItem(4)));
}