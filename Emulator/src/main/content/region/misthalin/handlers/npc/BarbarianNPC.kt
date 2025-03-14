package content.region.misthalin.handlers.npc

import core.api.sendChat
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class BarbarianNPC : NPCBehavior(*ID) {
    override fun afterDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        if (RandomFunction.random(8) == 1) {
            sendChat(self, "YEEEEEEEEAARRRGGGGHHHHHHHH")
        }
    }

    companion object {
        val ID =
            intArrayOf(
                NPCs.BARBARIAN_12,
                NPCs.BARBARIAN_3246,
                NPCs.BARBARIAN_3247,
                NPCs.BARBARIAN_3248,
                NPCs.BARBARIAN_3249,
                NPCs.BARBARIAN_3250,
                NPCs.BARBARIAN_3251,
                NPCs.BARBARIAN_3252,
                NPCs.BARBARIAN_3253,
                NPCs.BARBARIAN_3255,
                NPCs.BARBARIAN_3256,
                NPCs.BARBARIAN_3257,
                NPCs.BARBARIAN_3258,
                NPCs.BARBARIAN_3259,
                NPCs.BARBARIAN_3260,
                NPCs.BARBARIAN_3261,
                NPCs.BARBARIAN_3262,
                NPCs.BARBARIAN_3263,
                NPCs.BARBARIAN_5909,
            )
    }
}
