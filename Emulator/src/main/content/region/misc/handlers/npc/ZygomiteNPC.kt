package content.region.misc.handlers.npc

import core.api.*
import core.api.interaction.transformNpc
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.lang.Integer.max

class ZygomiteNPC :
    NPCBehavior(NPCs.FUNGI_3344, NPCs.FUNGI_3345, NPCs.ZYGOMITE_3346, NPCs.ZYGOMITE_3347),
    InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.FUNGI_3344, NPCs.FUNGI_3345), IntType.NPC, "pick") { player, node ->
            val fungi = node as NPC
            if (getStatLevel(player, Skills.SLAYER) < 57) {
                sendMessage(player, "Zygomite is Slayer monster that require a Slayer level of 57 to kill.")
                return@on true
            }
            lock(player, 1)
            animate(player, FIRST_ANIMATION)
            submitWorldPulse(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> {
                                animate(entity = fungi, anim = SECOND_ANIMATION)
                                transformNpc(fungi, fungi.id + 2, 500)
                                fungi.impactHandler.disabledTicks = 1
                                fungi.attack(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        onUseWith(IntType.NPC, (7421..7431).toIntArray(), *ids, handler = ::handleFungicideSpray)
    }

    override fun beforeDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        val lifepoints = self.skills.lifepoints
        if (state.estimatedHit + max(state.secondaryHit, 0) > lifepoints - 1) {
            state.estimatedHit = lifepoints - 1
            state.secondaryHit = -1
            setAttribute(self, "lock-damage", true)
        }
    }

    override fun tick(self: NPC): Boolean {
        if (getAttribute(self, "lock-damage", false)) {
            self.properties.combatPulse.stop()
            removeAttribute(self, "lock-damage")
        }
        return true
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        super.onDeathFinished(self, killer)
        self.reTransform()
    }

    override fun shouldIgnoreMultiRestrictions(
        self: NPC,
        victim: Entity,
    ): Boolean {
        return true
    }

    private fun handleFungicideSpray(
        player: Player,
        used: Node,
        with: Node,
    ): Boolean {
        if (with !is NPC) return false
        if (used.id == Items.FUNGICIDE_SPRAY_0_7431) return false
        if (with.skills.lifepoints >= 3) {
            sendMessage(player, "The zygomite isn't weak enough to be affected by the fungicide.")
        } else {
            sendMessage(player, "The zygomite is covered in fungicide. It bubbles away to nothing!")
            replaceSlot(player, used.asItem().slot, Item(used.id + 1))
            with.startDeath(player)
        }
        return true
    }

    companion object {
        private const val FIRST_ANIMATION = Animations.CLOSING_CHEST_3335
        private const val SECOND_ANIMATION = 3322
    }
}
