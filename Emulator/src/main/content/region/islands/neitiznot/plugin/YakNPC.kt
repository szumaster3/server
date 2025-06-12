package content.region.islands.neitiznot.plugin

import core.api.sendChat
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class YakNPC : NPCBehavior(NPCs.YAK_5529), InteractionListener {

    private var nextChat = 0L

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()

        if (now >= nextChat && RandomFunction.random(20) == 0) {
            sendChat(self, "Moo")
            nextChat = now + 15000L
        }

        return super.tick(self)
    }

    override fun defineListeners() {
        onUseWith(IntType.NPC, 0, NPCs.YAK_5529) { player, used, with ->
            if (with.id == NPCs.YAK_5529 && used.id != 0) {
                sendMessage(player, "The cow doesn't want that.")
            }
            return@onUseWith true
        }
    }
}