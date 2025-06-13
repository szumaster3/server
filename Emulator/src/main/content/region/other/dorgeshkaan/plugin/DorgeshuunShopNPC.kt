package content.region.other.dorgeshkaan.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class DorgeshuunShopNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val chats = mapOf(
        NPCs.ZENKOG_5797 to listOf(
            "Fingers!",
            "Wall beast fingers!",
            "Fresh wall beast fingers!",
            "Lovely wall beast fingers!",
            "Tasty wall beast fingers!"
        ),
        NPCs.GUNDIK_5796 to listOf(
            "Spicy kebabs!",
            "Bat kebabs!",
            "Spicy bat kebabs!",
            "Bat shish kebabs!",
            "Kebabs!"
        ),
        NPCs.MILTOG_5781 to listOf(
            "Lamps!",
            "Lanterns!",
            "Tinderboxes!",
            "Torches!",
            "Lamp oil!"
        ),
        NPCs.TINDAR_5795 to listOf(
            "Crispy!",
            "Creeeespy frogs' legs!",
            "Get your crispy frogs' legs!",
            "Frogs' legs!",
            "Crispy frogs' legs!"
        )
    )

    private var chatDelay = 0

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        DorgeshuunShopNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.ZENKOG_5797,
        NPCs.GUNDIK_5796,
        NPCs.MILTOG_5781,
        NPCs.TINDAR_5795
    )

    override fun handleTickActions() {
        if (chatDelay > 0) {
            chatDelay--
            return
        }

        if (!isPlayerNearby(15)) return

        if (RandomFunction.roll(8)) {
            chats[id]?.random()?.let { sendChat(this, it) }
            chatDelay = 100
        }
    }
}
