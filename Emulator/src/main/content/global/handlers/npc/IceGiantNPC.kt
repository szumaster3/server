package content.global.handlers.npc

import core.api.finishDiaryTask
import core.api.hasDiaryTaskComplete
import core.api.withinDistance
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class IceGiantNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return IceGiantNPC(id, location)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val player = killer.asPlayer()
            if (withinDistance(player, Location(3052, 9573, 0), 100) &&
                !hasDiaryTaskComplete(player, DiaryType.FALADOR, 1, 4)
            ) {
                finishDiaryTask(player, DiaryType.FALADOR, 1, 4)
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.ICE_GIANT_111,
            NPCs.ICE_GIANT_3072,
            NPCs.ICE_GIANT_4685,
            NPCs.ICE_GIANT_4686,
            NPCs.ICE_GIANT_4687,
        )
    }
}
