package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class FaridMorrisaneNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "I can make so much money here!",
        "My father shall be so pleased.",
        "Woo hoo! What a sale!",
        "What shall I trade next....",
        "Hmm. If I divide 20 and take off 50%...",
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        FaridMorrisaneNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.FARID_MORRISANE_ORES_6523)


    override fun handleTickActions() {
        if (!isPlayerNearby(12)) return

        if (--chatDelay <= 0) {
            if (RandomFunction.roll(8)) {
                sendChat(this, forceChat.random())
            }
            chatDelay = randomDelay()
        }
    }

    private fun randomDelay() = RandomFunction.random(10, 20)
}
