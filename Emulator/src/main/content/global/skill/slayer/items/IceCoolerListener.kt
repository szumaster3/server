package content.global.skill.slayer.items

import content.global.skill.slayer.Tasks
import core.api.getStatLevel
import core.api.impact
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

class IceCoolerListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.ICE_COOLER_6696, *Tasks.DESERT_LIZARDS.npcs) { player, used, with ->
            if (getStatLevel(player, Skills.SLAYER) < 22) {
                sendMessage(player, "You need a Slayer level of at least 22 to do this.")
                return@onUseWith true
            }
            val npc = with.asNpc() as NPC
            if (npc.getSkills().lifepoints > 2) {
                sendMessage(player, "The lizard isn't weak enough to be affected by the icy water.")
                return@onUseWith true
            }
            if (removeItem(player, used.asItem())) {
                impact(player, npc.getSkills().lifepoints, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "The lizard shudders and collapses from the freezing water.")
            }
            return@onUseWith true
        }
    }
}
