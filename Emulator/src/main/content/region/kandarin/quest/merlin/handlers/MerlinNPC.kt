package content.region.kandarin.quest.merlin.handlers

import core.api.quest.getQuestStage
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Merlin NPC after [MerlinCrystal] quest.
 * TODO:
 *  [ ] - Camelot teleport.
 *  [ ] - To early, he should spawn after Kenniths concerns.
 */
@Initializable
class MerlinNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return MerlinNPC(id, location)
    }

    override fun isHidden(player: Player): Boolean {
        return getQuestStage(player, Quests.MERLINS_CRYSTAL) in 0..99
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MERLIN_213)
    }
}
