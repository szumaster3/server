package content.region.asgarnia.falador.plugin.temple_knights

import core.api.hasRequirement
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the White knight NPCs.
 */
@Initializable
class WhiteKnightNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = WhiteKnightNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            if(!hasRequirement(killer, Quests.WANTED, false)) return
            val penaltyPoint = 1
            val currentKills = WhiteKnightRankManager.getKillCount(killer)
            val newKills = (currentKills - penaltyPoint).coerceAtLeast(0)
            WhiteKnightRankManager.addToKillCount(killer, newKills - currentKills)
        }
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(
            NPCs.WHITE_KNIGHT_19,
            NPCs.WHITE_KNIGHT_1092,
            NPCs.WHITE_KNIGHT_3348,
            NPCs.WHITE_KNIGHT_3349,
            NPCs.WHITE_KNIGHT_3350,
        )
    }
}