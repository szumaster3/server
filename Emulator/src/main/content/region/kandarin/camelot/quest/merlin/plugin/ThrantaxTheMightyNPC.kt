package content.region.kandarin.camelot.quest.merlin.plugin

import content.data.GameAttributes
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ThrantaxTheMightyNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var player: Player? = null
    private var spawnedTicks = 0

    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC = ThrantaxTheMightyNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.THRANTAX_THE_MIGHTY_238)

    override fun tick() {
        if (spawnedTicks++ == 100 || player == null) {
            clear()
        }

        if (player != null) {
            val thantraxNPC = player!!.getAttribute<NPC>(GameAttributes.TEMP_ATTR_THRANTAX, null)

            if (thantraxNPC != null && thantraxNPC.equals(this)) {
                if (!player!!.interfaceManager.hasChatbox()) {
                    thantraxNPC.locks.unlockMovement()
                    player = null
                }
            }
        }
        super.tick()
    }
}
