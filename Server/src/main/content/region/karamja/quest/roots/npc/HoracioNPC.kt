package content.region.karamja.quest.roots.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.NPCs

class HoracioNPC(id: Int = 0,location: Location? = null) : AbstractNPC(id, location) {
    var target: Player? = null

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = HoracioNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.HORACIO_845)

    init {
        isWalks = false
        isRespawn = false
        isNeverWalks = true
    }
}