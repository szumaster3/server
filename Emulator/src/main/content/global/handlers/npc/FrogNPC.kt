package content.global.handlers.npc

import content.global.skill.slayer.Tasks
import core.api.event.applyPoison
import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.system.timer.impl.PoisonImmunity
import core.tools.RandomFunction
import org.rs.consts.NPCs

class FrogNPC : NPCBehavior(*Tasks.FROGS.npcs) {
    override fun onCreation(self: NPC) {
        registerTimer(self, spawnTimer<PoisonImmunity>())
    }

    override fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
        super.beforeAttackFinalized(self, victim, state)
        if (self.id == NPCs.FROG_3783) {
            if (RandomFunction.roll(10)) {
                applyPoison(victim, self, 21)
            }
        }
    }
}
