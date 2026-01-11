package content.global.skill.slayer.dungeon.waterbirth_dungeon

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import shared.consts.NPCs

class SpinolypBehavior : NPCBehavior(NPCs.SPINOLYP_2894, NPCs.SPINOLYP_2896) {

    override fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (self.skills.lifepoints <= 15) {
            val loc = self.location
            self.clear()

            val water = NPC(NPCs.SUSPICIOUS_WATER_2895, loc)
            water.init()
        }
    }
}