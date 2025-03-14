package content.global.skill.gathering

import content.global.skill.gathering.fishing.FishingPulse
import content.global.skill.gathering.fishing.FishingSpot
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

class GatheringListener : InteractionListener {
    override fun defineListeners() {
    }

    fun fish(
        player: Player,
        node: Node,
        opt: String,
    ): Boolean {
        val npc = node as NPC
        val spot = FishingSpot.forId(npc.id) ?: return false
        val op = spot.getOptionByName(opt) ?: return false
        player.pulseManager.run(FishingPulse(player, npc, op))

        return true
    }
}
