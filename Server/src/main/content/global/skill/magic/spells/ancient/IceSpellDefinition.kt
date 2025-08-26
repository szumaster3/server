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
import shared.consts.Sounds

/**
 * Configuration of Ice spells.
 */
enum class IceSpellDefinition(val type: SpellType, val button: Int, val level: Int, val xp: Double, val impactSound: Int, val anim: Animation, val start: Graphics?, val projectile: Projectile?, val end: Graphics, val runes: Array<Item>) {
    RUSH(SpellType.RUSH, AncientSpells.ICE_RUSH, 58, 34.0, Sounds.ICE_RUSH_IMPACT_173, Animation(1978, Animator.Priority.HIGH), null, SpellProjectile.create(360), Graphics(361, 96), arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.WATER_RUNE.getItem(2))),
    BURST(SpellType.BURST, AncientSpells.ICE_BURST, 70, 40.0, Sounds.ICE_BURST_IMPACT_170, Animation(1979, Animator.Priority.HIGH), null, SpellProjectile.create(362), Graphics(363, 0), arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4))),
    BLITZ(SpellType.BLITZ, AncientSpells.ICE_BLITZ, 82, 46.0, Sounds.ICE_BLITZ_IMPACT_169, Animation(1978, Animator.Priority.HIGH), Graphics(366, 96), null, Graphics(367, 96), arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3))),
    BARRAGE(SpellType.BARRAGE, AncientSpells.ICE_BARRAGE, 94, 52.0, Sounds.ICE_BARRAGE_IMPACT_168, Animation(1979, Animator.Priority.HIGH), null, SpellProjectile.create(368), Graphics(369, 0), arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(6)));
}