package content.global.skill.magic.spells.ancient

import content.global.skill.magic.spells.AncientSpells
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Graphics as Graphic

/**
 * Configuration of Miasmic spells.
 */
enum class MiasmicSpellDefinition(val type: SpellType, val button: Int, val level: Int, val xp: Double, val castSound: Int, val impactSound: Int, val anim: Animation, val start: Graphics?, val projectile: Projectile?, val end: Graphics, val runes: Array<Item>) {
    RUSH(SpellType.RUSH, AncientSpells.MIASMIC_RUSH, 61, 36.0, 5368, 5365, Animation(10513, Animator.Priority.HIGH), Graphics(Graphic.MIASMIC_SPELL_1845, 0, 15), null, Graphics(Graphic.MIASMIC_SPELL_1847, 40), arrayOf(Runes.EARTH_RUNE.getItem(1), Runes.SOUL_RUNE.getItem(1), Runes.CHAOS_RUNE.getItem(2))),
    BURST(SpellType.BURST, AncientSpells.MIASMIC_BURST, 73, 42.0, 5366, 5372, Animation(10516, Animator.Priority.HIGH), Graphics(Graphic.RED_CLOUD_1848, 0), null, Graphics(Graphic.RED_CLOUD_1849, 20, 30), arrayOf(Runes.EARTH_RUNE.getItem(3), Runes.SOUL_RUNE.getItem(3), Runes.CHAOS_RUNE.getItem(4))),
    BLITZ(SpellType.BLITZ, AncientSpells.MIASMIC_BLITZ, 85, 48.0, 5370, 5367, Animation(10524, Animator.Priority.HIGH), Graphics(1850, 15), null, Graphics(1851, 0), arrayOf(Runes.EARTH_RUNE.getItem(3), Runes.SOUL_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(2))),
    BARRAGE(SpellType.BARRAGE, AncientSpells.MIASMIC_BARRAGE, 97, 54.0, 5371, 5369, Animation(10518, Animator.Priority.HIGH), Graphics(1853, 0), null, Graphics(1854, 0, 30), arrayOf(Runes.EARTH_RUNE.getItem(4), Runes.SOUL_RUNE.getItem(4), Runes.BLOOD_RUNE.getItem(4)));
}