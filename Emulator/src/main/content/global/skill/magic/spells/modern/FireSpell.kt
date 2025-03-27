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
 * The Fire spell.
 */
@Initializable
class FireSpell : CombatSpell {
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
        return getType().getImpactAmount(entity, victim, 4)
    }

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            8,
            FireSpell(
                SpellType.STRIKE,
                13,
                11.5,
                Sounds.FIRESTRIKE_CAST_AND_FIRE_160,
                STRIKE_START,
                STRIKE_PROJECTILE,
                STRIKE_END,
                Runes.MIND_RUNE.getItem(1),
                Runes.FIRE_RUNE.getItem(3),
                Runes.AIR_RUNE.getItem(2),
            ),
        )
        SpellBook.MODERN.register(
            20,
            FireSpell(
                SpellType.BOLT,
                35,
                22.5,
                Sounds.FIREBOLT_CAST_AND_FIRE_157,
                BOLT_START,
                BOLT_PROJECTILE,
                BOLT_END,
                Runes.CHAOS_RUNE.getItem(1),
                Runes.FIRE_RUNE.getItem(4),
                Runes.AIR_RUNE.getItem(3),
            ),
        )
        SpellBook.MODERN.register(
            38,
            FireSpell(
                SpellType.BLAST,
                59,
                34.5,
                Sounds.FIREBLAST_CAST_AND_FIRE_155,
                BLAST_START,
                BLAST_PROJECTILE,
                BLAST_END,
                Runes.DEATH_RUNE.getItem(1),
                Runes.FIRE_RUNE.getItem(5),
                Runes.AIR_RUNE.getItem(4),
            ),
        )
        SpellBook.MODERN.register(
            55,
            FireSpell(
                SpellType.WAVE,
                75,
                42.5,
                Sounds.FIREWAVE_CAST_AND_FIRE_162,
                WAVE_START,
                WAVE_PROJECTILE,
                WAVE_END,
                Runes.BLOOD_RUNE.getItem(1),
                Runes.FIRE_RUNE.getItem(7),
                Runes.AIR_RUNE.getItem(5),
            ),
        )
        return this
    }

    companion object {
        private val STRIKE_START = Graphics(org.rs.consts.Graphics.FIRE_STRIKE_CAST_99, 96)
        private val STRIKE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.FIRE_STRIKE_PROJECTILE_100,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val STRIKE_END = Graphics(org.rs.consts.Graphics.FIRE_STRIKE_IMPACT_101, 96)
        private val BOLT_START = Graphics(org.rs.consts.Graphics.FIRE_BOLT_CAST_126, 96)
        private val BOLT_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.FIRE_BOLT_PROJECTILE_127,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BOLT_END = Graphics(org.rs.consts.Graphics.FIRE_BOLT_IMPACT_128, 96)
        private val BLAST_START = Graphics(org.rs.consts.Graphics.FIRE_BLAST_CAST_129, 96)
        private val BLAST_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.FIRE_BLAST_PROJECTILE_130,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val BLAST_END = Graphics(org.rs.consts.Graphics.FIRE_BLAST_IMPACT_131, 96)
        private val WAVE_START = Graphics(org.rs.consts.Graphics.FIRE_WAVE_CAST_155, 96)
        private val WAVE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.FIRE_WAVE_PROJECTILE_156,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val WAVE_END = Graphics(org.rs.consts.Graphics.FIRE_WAVE_IMPACT_157, 96)
        private val ANIMATION = Animation(Animations.CAST_SPELL_711, Priority.HIGH)
    }
}
