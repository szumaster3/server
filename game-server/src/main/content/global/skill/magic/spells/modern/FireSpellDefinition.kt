package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Sounds
import org.rs.consts.Graphics as Graphic

/**
 * Represents configuration of fire spells.
 */
enum class FireSpellDefinition(val type: SpellType, val buttonId: Int, val level: Int, val xp: Double, val sound: Int, val start: Graphics, val projectile: Projectile, val end: Graphics, val runes: Array<Item>) {
    STRIKE(SpellType.STRIKE, ModernSpells.FIRE_STRIKE, 13, 11.5, Sounds.FIRESTRIKE_CAST_AND_FIRE_160, Graphics(Graphic.FIRE_STRIKE_CAST_99, 96), SpellProjectile.create(Graphic.FIRE_STRIKE_PROJECTILE_100), Graphics(Graphic.FIRE_STRIKE_IMPACT_101, 96), arrayOf(Runes.MIND_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2))),
    BOLT(SpellType.BOLT, ModernSpells.FIRE_BOLT, 35, 22.5, Sounds.FIREBOLT_CAST_AND_FIRE_157, Graphics(Graphic.FIRE_BOLT_CAST_126, 96), SpellProjectile.create(Graphic.FIRE_BOLT_PROJECTILE_127), Graphics(Graphic.FIRE_BOLT_IMPACT_128, 96), arrayOf(Runes.CHAOS_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3))),
    BLAST(SpellType.BLAST, ModernSpells.FIRE_BLAST, 59, 34.5, Sounds.FIREBLAST_CAST_AND_FIRE_155, Graphics(Graphic.FIRE_BLAST_CAST_129, 96), SpellProjectile.create(Graphic.FIRE_BLAST_PROJECTILE_130), Graphics(Graphic.FIRE_BLAST_IMPACT_131, 96), arrayOf(Runes.DEATH_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(5), Runes.AIR_RUNE.getItem(4))),
    WAVE(SpellType.WAVE, ModernSpells.FIRE_WAVE, 75, 42.5, Sounds.FIREWAVE_CAST_AND_FIRE_162, Graphics(Graphic.FIRE_WAVE_CAST_155, 96), SpellProjectile.create(Graphic.FIRE_WAVE_PROJECTILE_156), Graphics(Graphic.FIRE_WAVE_IMPACT_157, 96), arrayOf(Runes.BLOOD_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
}