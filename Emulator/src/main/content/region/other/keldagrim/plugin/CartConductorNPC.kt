package content.region.other.keldagrim.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class CartConductorNPC : NPCBehavior(NPCs.CART_CONDUCTOR_2182, NPCs.CART_CONDUCTOR_2183) {

    private val forceChat =
        arrayOf(
            "Grand Exchange carts departing from all tracks!",
            "Ice Mountain carts departing from all tracks!",
            "White Wolf Mountain carts departing from all tracks!",
            "Ice Mountain carts departing from all tracks!",
            "Grand Exchange carts departing from all tracks!",
            "Mind the cart!",
            "Careful!",
        )

    private val secondChat = arrayOf(
        "Tickets! Tickets!",
        "Buy your tickets here!",
        "Selling tickets!"
    )

    private var tickDelay = 100

    override fun tick(self: NPC): Boolean {
        if (!isPlayerNearby(self)) return true

        if (--tickDelay > 0) return true
        tickDelay = 100

        if (RandomFunction.random(2) == 0) {
            when (self.id) {
                NPCs.CART_CONDUCTOR_2182 -> sendChat(self, forceChat.random())
                NPCs.CART_CONDUCTOR_2183 -> sendChat(self, secondChat.random())
            }
        }

        return true
    }
}