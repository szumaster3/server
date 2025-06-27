package core.game.node.entity.combat.equipment

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.impl.Projectile
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics

/**
 * Represents a possible attack the entity can switch to.
 *
 * @author Emperor
 */
open class SwitchAttack @JvmOverloads constructor(handler: CombatSwingHandler?, animation: Animation?, startGraphic: Graphics? = null, endGraphic: Graphics? = null, projectile: Projectile? = null) {

    /**
     * The combat swing handler.
     */
    var handler: CombatSwingHandler? = null

    /**
     * The start graphic.
     */
    val startGraphic: Graphics?

    /**
     * The animation.
     */
    val animation: Animation?

    /**
     * The end graphic.
     */
    val endGraphic: Graphics?

    /**
     * The projectile.
     */
    val projectile: Projectile?

    /**
     * If the handler should be used for visualizing/handling combat.
     */
    var isUseHandler: Boolean = false
        private set

    /**
     * The maximum hit of this attack.
     */
    var maximumHit: Int = -1
        private set

    /**
     * Constructs a new `SwitchAttack` `Object`
     *
     * @param handler the handler.
     * @param projectile the projectile.
     */
    constructor(handler: CombatSwingHandler?, animation: Animation?, projectile: Projectile?) : this(
        handler, animation, null, null, projectile
    )

    /**
     * Constructs a new `SwitchAttack` `Object`
     *
     * @param style the style.
     */
    constructor(style: CombatStyle) : this(style.swingHandler, null, null, null)

    init {
        this.handler = handler
        this.animation = animation
        this.startGraphic = startGraphic
        this.endGraphic = endGraphic
        this.projectile = projectile
    }

    /**
     * Checks if this attack can be selected.
     *
     * @param entity the entity.
     * @param victim the victim.
     * @param state the state.
     * @return `True` if selected.
     */
    open fun canSelect(entity: Entity?, victim: Entity?, state: BattleState?): Boolean {
        return true
    }

    val style: CombatStyle?
        /**
         * Gets the style.
         *
         * @return The style.
         */
        get() = handler!!.type

    /**
     * Sets the useHandler.
     *
     * @param useHandler The useHandler to set.
     * @return This instance, for chaining.
     */
    fun setUseHandler(useHandler: Boolean): SwitchAttack {
        this.isUseHandler = useHandler
        return this
    }

    /**
     * Sets the maximumHit value.
     *
     * @param maximumHit The maximumHit to set.
     */
    fun setMaximumHit(maximumHit: Int): SwitchAttack {
        this.maximumHit = maximumHit
        return this
    }
}