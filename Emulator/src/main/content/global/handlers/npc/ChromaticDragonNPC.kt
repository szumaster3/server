package content.global.handlers.npc

import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.equipment.special.DragonfireSwingHandler
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ChromaticDragonNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return ChromaticDragonNPC(id, location)
    }

    private val combatAction: CombatSwingHandler =
        MultiSwingHandler(
            false,
            SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(80, Priority.HIGH)),
            SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(91, Priority.HIGH)),
            DRAGONFIRE,
        )

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return combatAction
    }

    override fun getDragonfireProtection(fire: Boolean): Int {
        return 0x2 or 0x4 or 0x8
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.GREEN_DRAGON_941,
            NPCs.GREEN_DRAGON_4677,
            NPCs.GREEN_DRAGON_4678,
            NPCs.GREEN_DRAGON_4679,
            NPCs.GREEN_DRAGON_4680,
            NPCs.RED_DRAGON_53,
            NPCs.RED_DRAGON_4669,
            NPCs.RED_DRAGON_4670,
            NPCs.RED_DRAGON_4671,
            NPCs.RED_DRAGON_4672,
            NPCs.BLACK_DRAGON_54,
            NPCs.BLACK_DRAGON_4673,
            NPCs.BLACK_DRAGON_4674,
            NPCs.BLACK_DRAGON_4675,
            NPCs.BLACK_DRAGON_4676,
            NPCs.BLUE_DRAGON_55,
            NPCs.BLUE_DRAGON_4681,
            NPCs.BLUE_DRAGON_4682,
            NPCs.BLUE_DRAGON_4683,
            NPCs.BLUE_DRAGON_4684,
        )
    }

    companion object {
        private val DRAGONFIRE: SwitchAttack =
            DragonfireSwingHandler.get(
                true,
                52,
                Animation(81, Priority.HIGH),
                Graphics(1, 64),
                null,
                null,
            )
    }
}
