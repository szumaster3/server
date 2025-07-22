package content.global.ame.eviltwin

import core.api.toIntArray
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * Represents the Suspect NPC for Evil twin random event.
 * @author szu
 */
@Initializable
class SuspectNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = SuspectNPC(id, location)

    override fun getIds(): IntArray = SUSPECT_IDS

    override fun shouldPreventStacking(mover: Entity?): Boolean = true

    companion object {
        val SUSPECT_IDS = (3852..3891).toIntArray()
    }
}
