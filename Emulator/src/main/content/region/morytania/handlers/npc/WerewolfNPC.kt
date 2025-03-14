package content.region.morytania.handlers.npc

import core.api.*
import core.api.combat.delayAttack
import core.api.interaction.transformNpc
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import org.rs.consts.Items

class WerewolfNPC : NPCBehavior(*HUMAN_NPCS) {
    override fun afterDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        if (DeathTask.isDead(self)) {
            return
        }

        if (attacker is Player) {
            if (!inEquipment(attacker, Items.WOLFBANE_2952, 1) && self.id in HUMAN_NPCS) {
                delayAttack(self, 3)
                delayAttack(attacker, 3)
                lock(self, 3)

                queueScript(self, 0, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            visualize(self, HUMAN_OUT_ANIMATION, WEREWOLF_IN_GFXS[self.id - 6026])
                            return@queueScript delayScript(self, WEREWOLF_IN_ANIMATION.duration)
                        }

                        1 -> {
                            transformNpc(self, WEREWOLF_NPCS[self.id - 6026], 200)
                            return@queueScript delayScript(self, 1)
                        }

                        2 -> {
                            self.properties.combatPulse.attack(attacker)
                            return@queueScript stopExecuting(self)
                        }

                        else -> return@queueScript stopExecuting(self)
                    }
                }
            }
        }
    }

    override fun onRespawn(self: NPC) {
        if (self.id in WEREWOLF_NPCS) {
            self.reTransform()
        }
        super.onRespawn(self)
    }

    companion object {
        private val HUMAN_NPCS = (6026..6045).toIntArray()

        private val WEREWOLF_NPCS = (6006..6025).toIntArray()

        private val HUMAN_OUT_ANIMATION = Animation(6554)

        private val WEREWOLF_IN_ANIMATION = Animation(6543)

        private val WEREWOLF_IN_GFXS = (1079..1098).toIntArray()
    }
}
