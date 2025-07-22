package content.region.kandarin.feldip.jiggig.quest.zogre.npc

import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import core.tools.minutesToTicks
import org.rs.consts.NPCs

class ZogreNPC : NPCBehavior(*IDS) {
    override fun onCreation(self: NPC) {
        self.isWalks = true
        self.isNeverWalks = false
        self.isAggressive = true
        self.task.undead = true
    }

    companion object {
        private val IDS =
            intArrayOf(
                NPCs.ZOGRE_2044,
                NPCs.ZOGRE_2045,
                NPCs.ZOGRE_2046,
                NPCs.ZOGRE_2047,
                NPCs.ZOGRE_2048,
                NPCs.ZOGRE_2049,
                NPCs.ZOGRE_2051,
                NPCs.ZOGRE_2052,
                NPCs.ZOGRE_2053,
                NPCs.ZOGRE_2054,
                NPCs.ZOGRE_2055,

                NPCs.SKOGRE_2050,
                NPCs.SKOGRE_2056,
                NPCs.SKOGRE_2057,
            )
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