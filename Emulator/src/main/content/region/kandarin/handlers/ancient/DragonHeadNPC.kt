package content.region.kandarin.handlers.ancient

import core.api.interaction.transformNpc
import core.api.location
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import org.rs.consts.NPCs

class DragonHeadNPC : NPCBehavior(*DRAGON_HEAD) {
    override fun canBeAttackedBy(
        self: NPC,
        attacker: Entity,
        style: CombatStyle,
        shouldSendMessage: Boolean,
    ): Boolean {
        if (attacker !is Player) return false

        if (style != CombatStyle.MAGIC) {
            if (shouldSendMessage) sendMessage(attacker, "You can't do that.")
            return false
        }

        if (attacker.properties.spell.spellId != 55) {
            if (shouldSendMessage) sendMessage(attacker, "You can't do that.")
            return false
        }
        self.impactHandler.disabledTicks = 10
        return true
    }

    override fun afterDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        transformNpc(self, NPCs.DRAGON_HEAD_8426, 100)
    }

    override fun onDeathStarted(
        self: NPC,
        killer: Entity,
    ) {
        self.configureMovementPath(location(1820, 5279, 0))
    }

    companion object {
        private val DRAGON_HEAD = intArrayOf(NPCs.DRAGON_HEAD_8425, NPCs.DRAGON_HEAD_8426, NPCs.DRAGON_HEAD_8427)
    }
}
