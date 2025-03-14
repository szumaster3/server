package content.region.asgarnia.quest.fortress.handlers

import core.api.finishDiaryTask
import core.api.hasDiaryTaskComplete
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BlackKnightNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return BlackKnightNPC(id, location)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val player = killer.asPlayer()
            if (!hasDiaryTaskComplete(player, DiaryType.FALADOR, 1, 3)) {
                finishDiaryTask(player, DiaryType.FALADOR, 1, 3)
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.BLACK_KNIGHT_178,
            NPCs.BLACK_KNIGHT_179,
            NPCs.BLACK_KNIGHT_CAPTAIN_610,
            NPCs.BLACK_KNIGHT_2698,
            NPCs.BLACK_KNIGHT_6189,
        )
    }
}
