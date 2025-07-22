package content.global.skill.slayer.npc

import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.equipment.special.DragonfireSwingHandler
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * The type Brutal dragon npc.
 */
@Initializable
class BrutalDragonNPC : AbstractNPC {
    /**
     * Instantiates a new Brutal dragon npc.
     */
    constructor() : super(0, null)

    private val combatAction: CombatSwingHandler = MultiSwingHandler(
        true,
        SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(80, Priority.HIGH)),
        SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(80, Priority.HIGH)),
        SwitchAttack(
            CombatStyle.MAGIC.swingHandler,
            Animation(81, Priority.HIGH),
            null,
            null,
            Projectile.create(null as Entity?, null, 500, 20, 20, 41, 40, 18, 255)
        ),
        DRAGONFIRE
    )

    /**
     * Instantiates a new Brutal dragon npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BrutalDragonNPC(id, location)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return combatAction
    }

    override fun getDragonfireProtection(fire: Boolean): Int {
        return 0x2 or 0x4 or 0x8
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BRUTAL_GREEN_DRAGON_5362)
    }

    companion object {
        private val DRAGONFIRE: SwitchAttack = DragonfireSwingHandler.get(
            false, 52, Animation(81, Priority.HIGH), Graphics.create(1), null, null
        )
    }
}
