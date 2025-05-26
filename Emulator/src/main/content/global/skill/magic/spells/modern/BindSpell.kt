package content.global.skill.magic.spells.modern

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.item.Item
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * The Bind spell.
 */
@Initializable
class BindSpell : CombatSpell {
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
        ANIMATION,
        start,
        projectile,
        end,
        *runes,
    )

    override fun fireEffect(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ) {
        if (victim is NPC) {
            if (victim.name.contains("impling")) {
                state.estimatedHit = -2
            }
        }
        if (state.estimatedHit == -1) {
            return
        }
        var tick = 9
        if (getType() === SpellType.BIND) {
            state.estimatedHit = -2
        }
        if (state.spell.spellId == 30) {
            tick = 17
        } else if (state.spell.spellId == 56) {
            tick = 25
        }
        if (!victim.locks.isMovementLocked() && victim is Player) {
            victim.packetDispatch.sendMessage("A magical force stops you from moving!")
        }
        victim.walkingQueue.reset()
        victim.locks.lockMovement(tick)
        entity.setAttribute("entangleDelay", ticks + tick + 2)
    }

    override fun getMaximumImpact(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ): Int = if (getType() === SpellType.ENTANGLE) 5 else 3

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            12,
            BindSpell(
                SpellType.BIND,
                20,
                30.0,
                Sounds.BIND_CAST_101,
                Sounds.BIND_IMPACT_99,
                BIND_START,
                BIND_PROJECTILE,
                BIND_END,
                Runes.NATURE_RUNE.getItem(2),
                Runes.EARTH_RUNE.getItem(3),
                Runes.WATER_RUNE.getItem(3),
            ),
        )
        SpellBook.MODERN.register(
            30,
            BindSpell(
                SpellType.SNARE,
                50,
                60.0,
                Sounds.SNARE_CAST_AND_FIRE_3003,
                Sounds.SNARE_IMPACT_3002,
                SNARE_START,
                SNARE_PROJECTILE,
                SNARE_END,
                Runes.NATURE_RUNE.getItem(3),
                Runes.EARTH_RUNE.getItem(4),
                Runes.WATER_RUNE.getItem(4),
            ),
        )
        SpellBook.MODERN.register(
            56,
            BindSpell(
                SpellType.ENTANGLE,
                79,
                89.0,
                Sounds.ENTANGLE_CAST_AND_FIRE_151,
                Sounds.ENTANGLE_HIT_153,
                ENTANGLE_START,
                ENTANGLE_PROJECTILE,
                ENTANGLE_END,
                Runes.NATURE_RUNE.getItem(4),
                Runes.EARTH_RUNE.getItem(5),
                Runes.WATER_RUNE.getItem(5),
            ),
        )
        return this
    }

    companion object {
        private val BIND_START = Graphics(org.rs.consts.Graphics.BIND_CAST_177, 96)
        private val BIND_PROJECTILE: Projectile =
            Projectile.create(null as Entity?, null, org.rs.consts.Graphics.BIND_PROJECTILE_178, 40, 36, 52, 75, 15, 11)
        private val BIND_END = Graphics(org.rs.consts.Graphics.BIND_IMPACT_179, 96)
        private val SNARE_START = Graphics(org.rs.consts.Graphics.BIND_CAST_177, 96)
        private val SNARE_PROJECTILE: Projectile =
            Projectile.create(null as Entity?, null, org.rs.consts.Graphics.BIND_PROJECTILE_178, 40, 36, 52, 75, 15, 11)
        private val SNARE_END = Graphics(org.rs.consts.Graphics.SNARE_IMPACT_180, 96)
        private val ENTANGLE_START = Graphics(org.rs.consts.Graphics.BIND_CAST_177, 96)
        private val ENTANGLE_PROJECTILE: Projectile =
            Projectile.create(null as Entity?, null, org.rs.consts.Graphics.BIND_PROJECTILE_178, 40, 36, 52, 75, 15, 11)
        private val ENTANGLE_END = Graphics(org.rs.consts.Graphics.ENTANGLE_IMPACT_181, 96)
        private val ANIMATION = Animation(Animations.CAST_SPELL_WISE_OLD_710, Priority.HIGH)
    }
}
