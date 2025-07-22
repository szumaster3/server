package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.equipment.special.DragonfireSwingHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items

class SkeletalWyvernNPC : NPCBehavior(*Tasks.SKELETAL_WYVERN.ids) {

    private val COMBAT_HANDLER = MultiSwingHandler(SwitchAttack(
        CombatStyle.MELEE.swingHandler,
        Animation(Animations.STAND_2985)
    ),
        SwitchAttack(
            CombatStyle.RANGE.swingHandler,
            Animation(2989),
            Graphics(499)
        ),
        DragonfireSwingHandler.get(
            false,
            54,
            Animation(2988),
            Graphics(501),
            null,
            null,
            false
        )
    )
    private val COMBAT_HANDLER_FAR = MultiSwingHandler(SwitchAttack(
        CombatStyle.RANGE.swingHandler,
        Animation(2989),
        Graphics(499)
    ))

    private val SHIELDS = intArrayOf(
        Items.DRAGONFIRE_SHIELD_11283,
        Items.DRAGONFIRE_SHIELD_11285,
        Items.ELEMENTAL_SHIELD_2890,
        Items.MIND_SHIELD_9731
    )

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        val victim = self.properties.combatPulse.victim ?: return original
        if (victim !is Player) return original

        return if (victim.location.getDistance(self.location) >= 5)
            COMBAT_HANDLER_FAR
        else
            COMBAT_HANDLER
    }
}
