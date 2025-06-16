package content.region.asgarnia.falador.plugin.wkrank

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BlackKnightNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = BlackKnightNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val killsToAdd = if (id > 8000) 12 else 1
            WhiteKnightRankManager.addKills(killer, killsToAdd)
        }
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(
            NPCs.BLACK_KNIGHT_178,
            NPCs.BLACK_KNIGHT_179,
            NPCs.BLACK_KNIGHT_2698,
            NPCs.BLACK_KNIGHT_2777,
            NPCs.BLACK_KNIGHT_6189,
            NPCs.ELITE_BLACK_KNIGHT_8324,
            NPCs.ELITE_BLACK_KNIGHT_8325,
            NPCs.ELITE_BLACK_KNIGHT_8326,
            NPCs.ELITE_BLACK_KNIGHT_8327,
            NPCs.ELITE_BLACK_KNIGHT_8330,
        )
    }
}
