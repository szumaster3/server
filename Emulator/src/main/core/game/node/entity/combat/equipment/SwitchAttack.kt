package core.game.node.entity.combat.equipment

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.impl.Projectile
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics

open class SwitchAttack
    @JvmOverloads
    constructor(
        handler: CombatSwingHandler?,
        animation: Animation?,
        startGraphics: Graphics? = null,
        endGraphics: Graphics? = null,
        projectile: Projectile? = null,
    ) {
        var handler: CombatSwingHandler? = null

        val startGraphics: Graphics?

        val animation: Animation?

        val endGraphics: Graphics?

        val projectile: Projectile?

        @JvmField var isUseHandler: Boolean = false

        @JvmField var maximumHit: Int = -1

        constructor(handler: CombatSwingHandler?, animation: Animation?, projectile: Projectile?) : this(
            handler = handler,
            animation = animation,
            startGraphics = null,
            endGraphics = null,
            projectile = projectile,
        )

        constructor(style: CombatStyle) : this(
            handler = style.swingHandler,
            animation = null,
            startGraphics = null,
            endGraphics = null,
        )

        init {
            this.handler = handler
            this.animation = animation
            this.startGraphics = startGraphics
            this.endGraphics = endGraphics
            this.projectile = projectile
        }

        open fun canSelect(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ): Boolean {
            return true
        }

        val style: CombatStyle? get() = handler!!.type

        fun setUseHandler(useHandler: Boolean): SwitchAttack {
            this.isUseHandler = useHandler
            return this
        }

        fun setMaximumHit(maximumHit: Int): SwitchAttack {
            this.maximumHit = maximumHit
            return this
        }
    }
