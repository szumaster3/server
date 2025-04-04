package content.region.kandarin.quest.merlin.handlers

import content.region.kandarin.miniquest.knightwave.KnightWaveAttributes
import core.api.getAttribute
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Merlin NPC (Camelot Castle).
 */
@Initializable
class MerlinNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MerlinNPC(id, location)

    override fun isHidden(player: Player?): Boolean {
        if (player == null) return true

        val hasKingsRansom = hasRequirement(player, Quests.KINGS_RANSOM)
        val merlinCrystalComplete = isQuestComplete(player, Quests.MERLINS_CRYSTAL)
        val knightWaveComplete = getAttribute(player, KnightWaveAttributes.KW_COMPLETE, false)

        return !hasKingsRansom && !merlinCrystalComplete && knightWaveComplete
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MERLIN_213)
}