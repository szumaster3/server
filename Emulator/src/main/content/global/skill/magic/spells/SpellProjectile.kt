package content.global.skill.magic.spells

import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations

/**
 * Helper object responsible for creating projectiles used in spell casting.
 */
object SpellProjectile {

    /**
     * Default spell animation.
     */
    val ANIMATION = Animation(Animations.CAST_SPELL_711, Priority.HIGH)

    /**
     * Creates a new projectile.
     */
    fun create(graphicId: Int): Projectile = Projectile.create(
        null as Entity?,
        null as Entity?,
        graphicId,
        40, 36, 52,
        75, 15, 11
    )
}