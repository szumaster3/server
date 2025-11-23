package content.global.skill.magic.spells.ancient

import content.global.skill.magic.spells.AncientSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.Entity
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
 * Configuration of ancient spells.
 */
enum class AncientSpellDefinition(val type: SpellType, val button: Int, val level: Int, val xp: Double, val castSound: Int, val impactSound: Int, val anim: Animation, val start: Graphics?, val projectile: Projectile?, val end: Graphics, val runes: Array<Item>) {
    SMOKE_RUSH(SpellType.RUSH, AncientSpells.SMOKE_RUSH, 50, 30.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_RUSH_IMPACT_185,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(384), Graphics(385, 92),
        arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1))),
    SMOKE_BURST(SpellType.BURST, AncientSpells.SMOKE_BURST, 62, 36.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_BURST_IMPACT_182,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, SpellProjectile.create(386), Graphics(387, 0),
        arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    SMOKE_BLITZ(SpellType.BLITZ, AncientSpells.SMOKE_BLITZ, 74, 42.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_BLITZ_IMPACT_181,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(389), Graphics(388, 92),
        arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    SMOKE_BARRAGE(SpellType.BARRAGE, AncientSpells.SMOKE_BARRAGE, 86, 48.0, Sounds.SMOKE_CAST_183, Sounds.SMOKE_BARRAGE_IMPACT_180,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, SpellProjectile.create(391), Graphics(390, 0),
        arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(4))),

    SHADOW_RUSH(SpellType.RUSH, AncientSpells.SHADOW_RUSH, 52, 31.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_RUSH_IMPACT_179,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(378), Graphics(379, 92),
        arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.AIR_RUNE.getItem(1))),
    SHADOW_BURST(SpellType.BURST, AncientSpells.SHADOW_BURST, 64, 37.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_BURST_IMPACT_177,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, SpellProjectile.create(380), Graphics(381, 0),
        arrayOf(Runes.SOUL_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.AIR_RUNE.getItem(1))),
    SHADOW_BLITZ(SpellType.BLITZ, AncientSpells.SHADOW_BLITZ, 76, 43.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_BLITZ_IMPACT_176,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, null, Graphics(382, 92),
        arrayOf(Runes.SOUL_RUNE.getItem(2), Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2))),
    SHADOW_BARRAGE(SpellType.BARRAGE, AncientSpells.SHADOW_BARRAGE, 88, 48.0, Sounds.SHADOW_CAST_178, Sounds.SHADOW_BARRAGE_IMPACT_175,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, null, Graphics(383, 0),
        arrayOf(Runes.SOUL_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.AIR_RUNE.getItem(4))),

    MIASMIC_RUSH(SpellType.RUSH, AncientSpells.MIASMIC_RUSH, 61, 36.0, 5368, 5365,
        Animation(10513, Animator.Priority.HIGH), Graphics(Graphic.MIASMIC_SPELL_1845, 0, 15), null, Graphics(Graphic.PURPLE_RED_AND_MORE_AROUND_YOU_1847, 40),
        arrayOf(Runes.EARTH_RUNE.getItem(1), Runes.SOUL_RUNE.getItem(1), Runes.CHAOS_RUNE.getItem(2))),
    MIASMIC_BURST(SpellType.BURST, AncientSpells.MIASMIC_BURST, 73, 42.0, 5366, 5372,
        Animation(10516, Animator.Priority.HIGH), Graphics(Graphic.RED_CLOUD_1848, 0), null, Graphics(Graphic.RED_CLOUD_1849, 20, 30),
        arrayOf(Runes.EARTH_RUNE.getItem(3), Runes.SOUL_RUNE.getItem(3), Runes.CHAOS_RUNE.getItem(4))),
    MIASMIC_BLITZ(SpellType.BLITZ, AncientSpells.MIASMIC_BLITZ, 85, 48.0, 5370, 5367,
        Animation(10524, Animator.Priority.HIGH), Graphics(1850, 15), null, Graphics(1851, 0),
        arrayOf(Runes.EARTH_RUNE.getItem(3), Runes.SOUL_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(2))),
    MIASMIC_BARRAGE(SpellType.BARRAGE, AncientSpells.MIASMIC_BARRAGE, 97, 54.0, 5371, 5369,
        Animation(10518, Animator.Priority.HIGH), Graphics(1853, 0), null, Graphics(1854, 0, 30),
        arrayOf(Runes.EARTH_RUNE.getItem(4), Runes.SOUL_RUNE.getItem(4), Runes.BLOOD_RUNE.getItem(4))),

    ICE_RUSH(SpellType.RUSH, AncientSpells.ICE_RUSH, 58, 34.0, Sounds.ICE_CAST_171, Sounds.ICE_RUSH_IMPACT_173,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(360), Graphics(361, 92),
        arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.WATER_RUNE.getItem(2))),
    ICE_BURST(SpellType.BURST, AncientSpells.ICE_BURST, 70, 40.0, Sounds.ICE_CAST_171, Sounds.ICE_BURST_IMPACT_170,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, SpellProjectile.create(362), Graphics(363, 0),
        arrayOf(Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4))),
    ICE_BLITZ(SpellType.BLITZ, AncientSpells.ICE_BLITZ, 82, 46.0, Sounds.ICE_CAST_171, Sounds.ICE_BLITZ_IMPACT_169,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), Graphics(366, 92), null, Graphics(367, 92),
        arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3))),
    ICE_BARRAGE(SpellType.BARRAGE, AncientSpells.ICE_BARRAGE, 94, 52.0, Sounds.ICE_CAST_171, Sounds.ICE_BARRAGE_IMPACT_168,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, Projectile.create(null as Entity?, null as Entity?, 368, 0, 0, 52, 75, 15, 11), Graphics(369, 0),
        arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(6))),

    BLOOD_RUSH(SpellType.RUSH, AncientSpells.BLOOD_RUSH, 56, 33.0, Sounds.BLOOD_RUSH_CASTING_108, Sounds.BLOOD_RUSH_IMPACT_110,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(372), Graphics(Graphic.ANCIENTS_BLOOD_SPELLS_373, 92),
        arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2))),
    BLOOD_BURST(SpellType.BURST, AncientSpells.BLOOD_BURST, 68, 39.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BURST_IMPACT_105,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, null, Graphics(376, 0),
        arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4))),
    BLOOD_BLITZ(SpellType.BLITZ, AncientSpells.BLOOD_BLITZ, 80, 45.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BLITZ_IMPACT_104,
        Animation(Animations.CAST_SPELL_1978, Animator.Priority.HIGH), null, SpellProjectile.create(374), Graphics(375, 92),
        arrayOf(Runes.BLOOD_RUNE.getItem(4), Runes.DEATH_RUNE.getItem(2))),
    BLOOD_BARRAGE(SpellType.BARRAGE, AncientSpells.BLOOD_BARRAGE, 92, 51.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BARRAGE_IMPACT_102,
        Animation(Animations.CAST_SPELL_1979, Animator.Priority.HIGH), null, null, Graphics(377, 0),
        arrayOf(Runes.SOUL_RUNE.getItem(1), Runes.BLOOD_RUNE.getItem(4), Runes.DEATH_RUNE.getItem(4)))
}
