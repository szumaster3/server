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
 * The Water spell.
 */
@Initializable
class WaterSpell : CombatSpell {
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
        return getType().getImpactAmount(entity, victim, 2)
    }

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            4,
            WaterSpell(
                SpellType.STRIKE,
                5,
                7.5,
                Sounds.WATERSTRIKE_CAST_AND_FIRE_211,
                STRIKE_START,
                STRIKE_PROJECTILE,
                STRIKE_END,
                Runes.MIND_RUNE.getItem(1),
                Runes.WATER_RUNE.getItem(1),
                Runes.AIR_RUNE.getItem(1),
            ),
        )
        SpellBook.MODERN.register(
            14,
            WaterSpell(
                SpellType.BOLT,
                23,
                16.5,
                Sounds.WATERBOLT_CAST_AND_FIRE_209,
                BOLT_START,
                BOLT_PROJECTILE,
                BOLT_END,
                Runes.CHAOS_RUNE.getItem(1),
                Runes.WATER_RUNE.getItem(2),
                Runes.AIR_RUNE.getItem(2),
            ),
        )
        SpellBook.MODERN.register(
            27,
            WaterSpell(
                SpellType.BLAST,
                47,
                28.5,
                Sounds.WATERBLAST_CAST_AND_FIRE_207,
                BLAST_START,
                BLAST_PROJECTILE,
                BLAST_END,
                Runes.DEATH_RUNE.getItem(1),
                Runes.WATER_RUNE.getItem(3),
                Runes.AIR_RUNE.getItem(3),
            ),
        )
        SpellBook.MODERN.register(
            48,
            WaterSpell(
                SpellType.WAVE,
                65,
                37.5,
                Sounds.WATERWAVE_CAST_AND_FIRE_213,
                WAVE_START,
                WAVE_PROJECTILE,
                WAVE_END,
                Runes.BLOOD_RUNE.getItem(1),
                Runes.WATER_RUNE.getItem(7),
                Runes.AIR_RUNE.getItem(5),
            ),
        )
        return this
    }

    companion object {
        private val STRIKE_START = Graphics(org.rs.consts.Graphics.WATER_STRIKE_CAST_93, 96)
        private val STRIKE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WATER_STRIKE_PROJECTILE_94,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val STRIKE_END = Graphics(org.rs.consts.Graphics.WATER_STRIKE_IMPACT_95, 96)
        private val BOLT_START = Graphics(org.rs.consts.Graphics.WATER_BOLT_CAST_120, 96)
        private val BOLT_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WATER_BOLT_PROJECTILE_121,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BOLT_END = Graphics(org.rs.consts.Graphics.WATER_BOLT_IMPACT_122, 96)
        private val BLAST_START = Graphics(org.rs.consts.Graphics.WATER_BLAST_CAST_135, 96)
        private val BLAST_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WATER_BLAST_PROJECTILE_136,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BLAST_END = Graphics(org.rs.consts.Graphics.WATER_BLAST_IMPACT_137, 96)
        private val WAVE_START = Graphics(org.rs.consts.Graphics.WATER_WAVE_CAST_161, 96)
        private val WAVE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WATER_WAVE_PROJECTILE_162,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val WAVE_END = Graphics(org.rs.consts.Graphics.WATER_WAVE_IMPACT_163, 96)
        private val ANIMATION = Animation(Animations.CAST_SPELL_711, Priority.HIGH)
    }
}
