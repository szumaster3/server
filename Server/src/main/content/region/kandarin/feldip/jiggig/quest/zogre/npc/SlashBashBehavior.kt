package content.region.kandarin.feldip.jiggig.quest.zogre.npc

import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import core.tools.minutesToTicks
import shared.consts.NPCs

class SlashBashBehavior : NPCBehavior(NPCs.SLASH_BASH_2060) {

    override fun onCreation(self: NPC) {
        self.task.undead = true
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        super.beforeAttackFinalized(self, victim, state)
        if (RandomFunction.roll(10)) {
            registerTimer(victim, spawnTimer("disease", minutesToTicks(22.5)))
        }
    }
}