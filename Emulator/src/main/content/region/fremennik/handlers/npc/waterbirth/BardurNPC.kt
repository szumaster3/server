package content.region.fremennik.handlers.npc.waterbirth

import core.api.findLocalNPCs
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class BardurNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return BardurNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARDUR_2879)
    }

    override fun tick() {
        if (isActive && !inCombat() && RandomFunction.roll(10)) {
            val localfledgelings = findLocalNPCs(this, intArrayOf(NPCs.DAGANNOTH_FLEDGELING_2880))
            localfledgelings.forEach { fledglings ->
                if (fledglings.location.withinDistance(location, 6)) {
                    attack(fledglings)
                    return super.tick()
                }
            }
        }
        if (!isActive) {
            properties.combatPulse.stop()
        }
        return super.tick()
    }
}
