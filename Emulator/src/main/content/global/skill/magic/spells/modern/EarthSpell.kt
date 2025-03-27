package content.global.skill.magic.spells.modern

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * The Earth spell.
 */
@Initializable
class EarthSpell : CombatSpell {
    constructor()

    private constructor(
        type: SpellType,
        level: Int,
        baseExperience: Double,
        sound: Int,
        start: Graphics,
        projectile: Projectile,
        end: Graphics,
        vararg runes: Item,
    ) : super(
        type,
        SpellBook.MODERN,
        level,
        baseExperience,
        sound,
        sound + 1,
        ANIMATION,
        start,
        projectile,
        end,
        *runes,
    )

    override fun getMaximumImpact(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ): Int {
        return getType().getImpactAmount(entity, victim, 3)
    }

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            6,
            EarthSpell(
                SpellType.STRIKE,
                9,
                9.5,
                Sounds.EARTHSTRIKE_CAST_AND_FIRE_132,
                STRIKE_START,
                STRIKE_PROJECTILE,
                STRIKE_END,
                Runes.MIND_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(2),
                Runes.AIR_RUNE.getItem(1),
            ),
        )
        SpellBook.MODERN.register(
            17,
            EarthSpell(
                SpellType.BOLT,
                29,
                19.5,
                Sounds.EARTHBOLT_CAST_AND_FIRE_130,
                BOLT_START,
                BOLT_PROJECTILE,
                BOLT_END,
                Runes.CHAOS_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(3),
                Runes.AIR_RUNE.getItem(2),
            ),
        )
        SpellBook.MODERN.register(
            33,
            EarthSpell(
                SpellType.BLAST,
                53,
                31.5,
                Sounds.EARTHBLAST_CAST_AND_FIRE_128,
                BLAST_START,
                BLAST_PROJECTILE,
                BLAST_END,
                Runes.DEATH_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(4),
                Runes.AIR_RUNE.getItem(3),
            ),
        )
        SpellBook.MODERN.register(
            52,
            EarthSpell(
                SpellType.WAVE,
                70,
                40.0,
                Sounds.EARTHWAVE_CAST_AND_FIRE_134,
                WAVE_START,
                WAVE_PROJECTILE,
                WAVE_END,
                Runes.BLOOD_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(7),
                Runes.AIR_RUNE.getItem(5),
            ),
        )
        return this
    }

    companion object {
        private val STRIKE_START = Graphics(org.rs.consts.Graphics.EARTH_STRIKE_CAST_96, 96)
        private val STRIKE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.EARTH_STRIKE_PROJECTILE_97,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val STRIKE_END = Graphics(org.rs.consts.Graphics.EARTH_STRIKE_IMPACT_98, 96)
        private val BOLT_START = Graphics(org.rs.consts.Graphics.EARTH_BOLT_CAST_123, 96)
        private val BOLT_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.EARTH_BOLT_PROJECTILE_124,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BOLT_END = Graphics(org.rs.consts.Graphics.EARTH_BOLT_IMPACT_125, 96)
        private val BLAST_START = Graphics(org.rs.consts.Graphics.EARTH_BLAST_CAST_138, 96)
        private val BLAST_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.EARTH_BLAST_PROJECTILE_139,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BLAST_END = Graphics(org.rs.consts.Graphics.EARTH_BLAST_IMPACT_140, 96)
        private val WAVE_START = Graphics(org.rs.consts.Graphics.EARTH_WAVE_CAST_164, 96)
        private val WAVE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.EARTH_WAVE_PROJECTILE_165,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val WAVE_END = Graphics(org.rs.consts.Graphics.EARTH_WAVE_IMPACT_166, 96)
        private val ANIMATION = Animation(Animations.CAST_SPELL_711, Priority.HIGH)
    }
}
