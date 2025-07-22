package content.global.skill.magic.spells.modern

import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Items
import org.rs.consts.Sounds
import core.game.node.entity.skill.Skills as Skill
import org.rs.consts.Graphics as Graphic

/**
 * Represents configuration of gods spells.
 */
enum class GodSpellDefinition(val staffId: Int, val requiredCasts: Int = 100, val impactAudio: Int, val failAudio: Int, val endGraphics: Graphics, val projectile: Projectile?, val runes: Array<Item>, val effect: (Entity, Entity) -> Unit) {
    SARADOMIN(
        staffId = Items.SARADOMIN_STAFF_2415,
        impactAudio = Sounds.SARADOMIN_STRIKE_1659,
        failAudio = Sounds.SARADOMIN_STRIKE_FAIL_1656,
        endGraphics = Graphics.create(Graphic.SARADOMIN_STRIKE_76),
        projectile = null,
        runes = arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(4)),
        effect = { _, victim -> victim.getSkills().decrementPrayerPoints(1.0) }
    ),
    GUTHIX(
        staffId = Items.GUTHIX_STAFF_2416,
        impactAudio = Sounds.CLAWS_OF_GUTHIX_1653,
        failAudio = Sounds.CLAWS_OF_GUTHIX_FAIL_1652,
        endGraphics = Graphics.create(Graphic.CLAWS_OF_GUTHIX_77),
        projectile = null,
        runes = arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(1), Runes.AIR_RUNE.getItem(4)),
        effect = { _, victim -> victim.getSkills().drainLevel(Skill.DEFENCE, 0.05, 0.05) }
    ),
    ZAMORAK(
        staffId = Items.ZAMORAK_STAFF_2417,
        impactAudio = Sounds.FLAMES_OF_ZAMORAK_1655,
        failAudio = Sounds.FLAMES_OF_ZAMORAK_FAIL_1654,
        endGraphics = Graphics.create(Graphic.FLAMES_OF_ZAMORAK_78),
        projectile = null,
        runes = arrayOf(Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(1)),
        effect = { _, victim -> victim.getSkills().drainLevel(Skill.MAGIC, 0.05, 0.05) }
    );

    companion object {
        private val values = enumValues<GodSpellDefinition>()

        fun fromRunes(runes: Array<Item>): GodSpellDefinition? {
            val inputSet = runes.map { it.id to it.amount }.toSet()
            for (spell in values) {
                val spellSet = spell.runes.map { it.id to it.amount }.toSet()
                if (spellSet == inputSet) {
                    return spell
                }
            }
            return null
        }

        fun fromIndex(index: Int): GodSpellDefinition? =
            if (index in values.indices) values[index] else null
    }
}