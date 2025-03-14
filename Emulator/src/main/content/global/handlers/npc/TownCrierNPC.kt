package content.global.handlers.npc

import core.api.animate
import core.api.sendChat
import core.api.stopWalk
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.NPCs

private val IDS =
    intArrayOf(
        NPCs.TOWN_CRIER_6135,
        NPCs.TOWN_CRIER_6136,
        NPCs.TOWN_CRIER_6137,
        NPCs.TOWN_CRIER_6138,
        NPCs.TOWN_CRIER_6139,
    )

class TownCrierNPC : NPCBehavior(*IDS) {
    override fun tick(self: NPC): Boolean {
        when {
            RandomFunction.random(100) < 5 -> {
                stopWalk(self)
                animate(self, Animations.TOWN_CRIER_RING_BELL_6865)
                sendChat(self, "The Duke of Lumbridge needs a hand.")
            }
            RandomFunction.random(100) < 3 -> {
                stopWalk(self)
                animate(self, Animations.TOWN_CRIER_SCRATCHES_HEAD_6863)
                sendChat(self, "The squirrels! The squirrels are coming! Noooo, get them out of my head!")
            }
        }
        return super.tick(self)
    }
}
