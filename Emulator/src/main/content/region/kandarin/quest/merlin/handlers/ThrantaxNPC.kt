package content.region.kandarin.quest.merlin.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ThrantaxNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var player: Player? = null
    var spawnedTicks = 0

    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC {
        return ThrantaxNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THRANTAX_THE_MIGHTY_238)
    }

    override fun tick() {
        if (spawnedTicks++ == 100 || player == null) {
            clear()
        }

        if (player != null) {
            val plrThrantax = player!!.getAttribute<NPC>(MerlinUtils.TEMP_ATTR_THRANTAX, null)

            if (plrThrantax != null && plrThrantax.equals(this)) {
                if (!player!!.interfaceManager.hasChatbox()) {
                    plrThrantax.locks.unlockMovement()
                    player = null
                }
            }
        }
        super.tick()
    }
}
