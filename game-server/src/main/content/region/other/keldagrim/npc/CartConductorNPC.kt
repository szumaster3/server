package content.region.other.keldagrim.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the CartConductorNPC.
 */
class CartConductorNPC : NPCBehavior(NPCs.CART_CONDUCTOR_2182, NPCs.CART_CONDUCTOR_2183) {

    private val forceChat = listOf(
        "Grand Exchange carts departing from all tracks!",
        "Ice Mountain carts departing from all tracks!",
        "White Wolf Mountain carts departing from all tracks!",
        "Ice Mountain carts departing from all tracks!",
        "Grand Exchange carts departing from all tracks!",
        "Mind the cart!",
        "Careful!"
    )

    private val secondChat = listOf(
        "Tickets! Tickets!",
        "Buy your tickets here!",
        "Selling tickets!"
    )

    private var ticks = 0
    private var cachedChatList: List<String>? = null
    private var cachedId: Int = -1

    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 20) {
            return true
        }
        ticks = 0

        if (self.id != cachedId) {
            cachedId = self.id
            cachedChatList = when (self.id) {
                NPCs.CART_CONDUCTOR_2182 -> forceChat
                NPCs.CART_CONDUCTOR_2183 -> secondChat
                else -> null
            }
        }

        cachedChatList?.takeIf { RandomFunction.roll(15) }?.let {
            sendChat(self, it.random())
        }

        return true
    }
}