package content.region.kandarin.quest.zogre.handlers

import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import core.tools.minutesToTicks
import org.rs.consts.NPCs

class ZogreNPC : NPCBehavior(*ZOGRES_NPC + SKOGRE_NPC) {
    override fun onCreation(self: NPC) {
        self.isWalks = true
        self.isNeverWalks = false
        self.isAggressive = true
    }

    companion object {
        private val ZOGRES_NPC =
            intArrayOf(
                NPCs.ZOGRE_2044,
                NPCs.ZOGRE_2045,
                NPCs.ZOGRE_2046,
                NPCs.ZOGRE_2047,
                NPCs.ZOGRE_2048,
                NPCs.ZOGRE_2049,
                NPCs.SKOGRE_2050,
                NPCs.ZOGRE_2051,
                NPCs.ZOGRE_2052,
                NPCs.ZOGRE_2053,
                NPCs.ZOGRE_2054,
                NPCs.ZOGRE_2055,
            )
        private val SKOGRE_NPC = intArrayOf(NPCs.SKOGRE_2056, NPCs.SKOGRE_2057)
    }

    override fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
        super.beforeAttackFinalized(self, victim, state)
        if (RandomFunction.roll(10)) {
            registerTimer(victim, spawnTimer("disease", minutesToTicks(15)))
        }
    }
}
