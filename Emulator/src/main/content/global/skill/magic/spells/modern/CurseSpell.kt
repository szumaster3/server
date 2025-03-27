package content.global.skill.magic.spells.modern

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * The Curse spell.
 */
@Initializable
class CurseSpell : CombatSpell {
    constructor()

    private constructor(
        type: SpellType,
        level: Int,
        baseExperience: Double,
        sound: Int,
        impactAudio: Int,
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
        impactAudio,
        if (type.ordinal <= SpellType.CURSE.ordinal) LOW_ANIMATION else HIGH_ANIMATION,
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
        return 1
    }

    override fun fireEffect(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ) {
        if (state.estimatedHit == -1) {
            return
        }
        state.estimatedHit = -2
        when (getType()) {
            SpellType.CONFUSE -> victim.getSkills().drainLevel(Skills.ATTACK, 0.05, 0.05)
            SpellType.WEAKEN -> victim.getSkills().drainLevel(Skills.STRENGTH, 0.05, 0.05)
            SpellType.CURSE -> victim.getSkills().drainLevel(Skills.DEFENCE, 0.05, 0.05)
            SpellType.VULNERABILITY -> victim.getSkills().drainLevel(Skills.DEFENCE, 0.10, 0.10)
            SpellType.ENFEEBLE -> victim.getSkills().drainLevel(Skills.STRENGTH, 0.10, 0.10)
            SpellType.STUN -> victim.getSkills().drainLevel(Skills.ATTACK, 0.10, 0.10)
            else -> {}
        }
    }

    override fun addExperience(
        entity: Entity,
        hit: Int,
    ) {
        entity.getSkills().addExperience(Skills.MAGIC, experience)
    }

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            2,
            CurseSpell(
                SpellType.CONFUSE,
                3,
                13.0,
                Sounds.CONFUSE_CAST_AND_FIRE_119,
                Sounds.CONFUSE_HIT_121,
                CONFUSE_START,
                CONFUSE_PROJECTILE,
                CONFUSE_END,
                Runes.BODY_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(2),
                Runes.WATER_RUNE.getItem(3),
            ),
        )
        SpellBook.MODERN.register(
            7,
            CurseSpell(
                SpellType.WEAKEN,
                11,
                21.0,
                Sounds.WEAKEN_CAST_AND_FIRE_3011,
                Sounds.WEAKEN_HIT_3010,
                WEAKEN_START,
                WEAKEN_PROJECTILE,
                WEAKEN_END,
                Runes.BODY_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(2),
                Runes.WATER_RUNE.getItem(3),
            ),
        )
        SpellBook.MODERN.register(
            11,
            CurseSpell(
                SpellType.CURSE,
                19,
                29.0,
                Sounds.CURSE_CAST_AND_FIRE_127,
                Sounds.CURSE_HIT_126,
                CURSE_START,
                CURSE_PROJECTILE,
                CURSE_END,
                Runes.BODY_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(3),
                Runes.WATER_RUNE.getItem(2),
            ),
        )
        SpellBook.MODERN.register(
            50,
            CurseSpell(
                SpellType.VULNERABILITY,
                66,
                76.0,
                Sounds.VULNERABILITY_CAST_AND_FIRE_3009,
                Sounds.VULNERABILITY_IMPACT_3008,
                VULNER_START,
                VULNER_PROJECTILE,
                VULNER_END,
                Runes.SOUL_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(5),
                Runes.WATER_RUNE.getItem(5),
            ),
        )
        SpellBook.MODERN.register(
            53,
            CurseSpell(
                SpellType.ENFEEBLE,
                73,
                83.0,
                Sounds.ENFEEBLE_CAST_AND_FIRE_148,
                Sounds.ENFEEBLE_HIT_150,
                ENFEEBLE_START,
                ENFEEBLE_PROJECTILE,
                ENFEEBLE_END,
                Runes.SOUL_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(8),
                Runes.WATER_RUNE.getItem(8),
            ),
        )
        SpellBook.MODERN.register(
            57,
            CurseSpell(
                SpellType.STUN,
                80,
                90.0,
                Sounds.STUN_CAST_AND_FIRE_3004,
                Sounds.STUN_IMPACT_3005,
                STUN_START,
                STUN_PROJECTILE,
                STUN_END,
                Runes.SOUL_RUNE.getItem(1),
                Runes.EARTH_RUNE.getItem(12),
                Runes.WATER_RUNE.getItem(12),
            ),
        )
        return this
    }

    companion object {
        private val CONFUSE_START = Graphics(org.rs.consts.Graphics.CONFUSE_CAST_102, 96)
        private val CONFUSE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.CONFUSE_PROJECTILE_103,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val CONFUSE_END = Graphics(org.rs.consts.Graphics.CONFUSE_IMPACT_104, 96)
        private val WEAKEN_START = Graphics(org.rs.consts.Graphics.WEAKEN_CAST_105, 96)
        private val WEAKEN_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.WEAKEN_PROJECTILE_106,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val WEAKEN_END = Graphics(org.rs.consts.Graphics.WEAKEN_IMPACT_107, 96)
        private val CURSE_START = Graphics(org.rs.consts.Graphics.CURSE_CAST_108, 96)
        private val CURSE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.CURSE_PROJECTILE_109,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val CURSE_END = Graphics(org.rs.consts.Graphics.CURSE_IMPACT_110, 96)
        private val VULNER_START = Graphics(org.rs.consts.Graphics.VULNERABILITY_CAST_167, 96)
        private val VULNER_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.VULNERABILITY_PROJECTILE_168,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val VULNER_END = Graphics(org.rs.consts.Graphics.VULNERABILITY_IMPACT_169, 96, 1)
        private val ENFEEBLE_START = Graphics(org.rs.consts.Graphics.ENFEEBLE_CAST_170, 96)
        private val ENFEEBLE_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.ENFEEBLE_PROJECTILE_171,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val ENFEEBLE_END = Graphics(org.rs.consts.Graphics.ENFEEBLE_IMPACT_172, 96)
        private val STUN_START = Graphics(org.rs.consts.Graphics.STUN_CAST_173, 96)
        private val STUN_PROJECTILE: Projectile =
            Projectile.create(
                null as Entity?,
                null,
                org.rs.consts.Graphics.STUN_PROJECTILE_174,
                40,
                36,
                52,
                75,
                15,
                11,
            )
        private val STUN_END = Graphics(org.rs.consts.Graphics.WEAKEN_IMPACT_107, 96)
        private val LOW_ANIMATION = Animation(716, Priority.HIGH)
        private val HIGH_ANIMATION = Animation(Animations.CAST_SPELL_B_729, Priority.HIGH)
    }
}
