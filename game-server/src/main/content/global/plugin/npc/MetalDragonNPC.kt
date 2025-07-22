package content.global.plugin.npc

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
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.NPCs

@Initializable
class MetalDragonNPC(id: Int = 0, location: Location? = null, ) : AbstractNPC(id, location) {

    private val combatAction: CombatSwingHandler =
        MultiSwingHandler(true, SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(80, Priority.HIGH)), SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(91, Priority.HIGH)), DRAGONFIRE)

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = combatAction

    override fun getDragonfireProtection(fire: Boolean): Int = 0x2 or 0x4 or 0x8

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MetalDragonNPC(id, location)

    override fun getIds(): IntArray =
        intArrayOf(NPCs.BRONZE_DRAGON_1590, NPCs.IRON_DRAGON_1591, NPCs.STEEL_DRAGON_1592, NPCs.STEEL_DRAGON_3590)

    companion object {
        private val DRAGONFIRE: SwitchAttack = DragonfireSwingHandler.get(
            false,
            52,
            Animation(Animations.DRAGON_BREATH_81, Priority.HIGH),
            null,
            null,
            Projectile.create(null as Entity?, null, Graphics.GIANT_ORANGE_BLAST_54, 40, 36, 41, 46, 20, 255)
        )
    }
}
