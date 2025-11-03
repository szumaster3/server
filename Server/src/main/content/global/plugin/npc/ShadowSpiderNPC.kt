package content.global.plugin.npc

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager
import shared.consts.NPCs

/**
 * Represents the shadow Spider behavior.
 */
class ShadowSpiderNPC : NPCBehavior(NPCs.SHADOW_SPIDER_58) {

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        if (victim !is Player) return

        val player = victim
        val currentPrayer = player.skills.prayerPoints
        if (currentPrayer > 1) {
            val drain = (currentPrayer / 2)
            val updatedAmount = (currentPrayer - drain).coerceAtLeast(1.0)
            player.skills.prayerPoints = updatedAmount
            player.sendMessage("The spider drains your prayer...")
        }
    }

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        return super.canBeAttackedBy(self, attacker, style, shouldSendMessage)
    }

    override fun tick(self: NPC): Boolean {
        val nearbyPlayers = RegionManager.getLocalPlayers(self, 8)
        for (player in nearbyPlayers) {
            val isInWild = self.zoneMonitor.isInZone("Wilderness") || player.zoneMonitor.isInZone("Wilderness")
            self.isAggressive = (player.properties.currentCombatLevel < 105) || isInWild
        }
        return true
    }

}
