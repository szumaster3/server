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
 * The Air spell.
 */
@Initializable
class AirSpell : CombatSpell {
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
        return getType().getImpactAmount(entity, victim, 1)
    }

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            1,
            AirSpell(
                SpellType.STRIKE,
                1,
                5.5,
                Sounds.WINDSTRIKE_CAST_AND_FIRE_220,
                STRIKE_START,
                STRIKE_PROJECTILE,
                STRIKE_END,
                Runes.MIND_RUNE.getItem(1),
                Runes.AIR_RUNE.getItem(1),
            ),
        )
        SpellBook.MODERN.register(
            10,
            AirSpell(
                SpellType.BOLT,
                17,
                13.5,
                Sounds.WINDBOLT_CAST_AND_FIRE_218,
                BOLT_START,
                BOLT_PROJECTILE,
                BOLT_END,
                Runes.CHAOS_RUNE.getItem(1),
                Runes.AIR_RUNE.getItem(2),
            ),
        )
        SpellBook.MODERN.register(
            24,
            AirSpell(
                SpellType.BLAST,
                41,
                25.5,
                Sounds.WINDBLAST_CAST_AND_FIRE_216,
                BLAST_START,
                BLAST_PROJECTILE,
                BLAST_END,
                Runes.DEATH_RUNE.getItem(1),
                Runes.AIR_RUNE.getItem(3),
            ),
        )
        SpellBook.MODERN.register(
            45,
            AirSpell(
                SpellType.WAVE,
                62,
                36.0,
                Sounds.WINDWAVE_CAST_AND_FIRE_222,
                WAVE_START,
                WAVE_PROJECTILE,
                WAVE_END,
                Runes.BLOOD_RUNE.getItem(1),
                Runes.AIR_RUNE.getItem(5),
            ),
        )
        return this
    }

    companion object {
        private val STRIKE_START = Graphics(org.rs.consts.Graphics.WIND_STRIKE_CAST_90, 96)
        private val STRIKE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WIND_STRIKE_PROJECTILE_91,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val STRIKE_END = Graphics(org.rs.consts.Graphics.WIND_STRIKE_IMPACT_92, 96)
        private val BOLT_START = Graphics(org.rs.consts.Graphics.WIND_BOLT_CAST_117, 96)
        private val BOLT_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WIND_BOLT_PROJECTILE_118,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BOLT_END = Graphics(org.rs.consts.Graphics.WIND_BOLT_IMPACT_119, 96)
        private val BLAST_START = Graphics(org.rs.consts.Graphics.WIND_BLAST_CAST_132, 96)
        private val BLAST_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WIND_BLAST_PROJECTILE_133,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BLAST_END = Graphics(org.rs.consts.Graphics.WIND_BLAST_IMPACT_134, 96)
        private val WAVE_START = Graphics(org.rs.consts.Graphics.WIND_WAVE_CAST_158, 96)
        private val WAVE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WIND_WAVE_PROJECTILE_159,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val WAVE_END = Graphics(org.rs.consts.Graphics.WIND_WAVE_IMPACT_160, 96)
        private val ANIMATION = Animation(Animations.CAST_SPELL_711, Priority.HIGH)
    }
}
