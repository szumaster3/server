package content.region.misthalin.quest.romeo.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class JulietNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return JulietNPC(id, location)
    }

    override fun isHidden(player: Player): Boolean {
        return player
            .getQuestRepository()
            .getQuest(Quests.ROMEO_JULIET)
            .getStage(player) > 60 &&
            player.getQuestRepository().getQuest(Quests.ROMEO_JULIET).getStage(player) < 100
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        private val ID = intArrayOf(NPCs.JULIET_637)
    }
}
