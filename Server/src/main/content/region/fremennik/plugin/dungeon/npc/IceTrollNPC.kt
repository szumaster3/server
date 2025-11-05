package content.region.fremennik.plugin.dungeon.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles Ice trolls NPC.
 */
@Initializable
class IceTrollNPC : AbstractNPC {

    private var tickDelay = RandomFunction.random(30)

    constructor() : super(NPCs.ICE_TROLL_MALE_5474, null, true)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC = IceTrollNPC(id, location)

    override fun tick() {
        tickDelay++
        if (tickDelay < 5) {
            super.tick()
            return
        }
        tickDelay = 0

        if (!inCombat()) {
            val nearbyMiners = RegionManager.getLocalNpcs(this, 8).filter { it.id == NPCs.MINER_5497 }
            val nearbyMiner = nearbyMiners.firstOrNull() ?: return super.tick()

            attack(nearbyMiner)
        }
        super.tick()
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.ICE_TROLL_MALE_5474,
        NPCs.ICE_TROLL_RUNT_5473,
        NPCs.ICE_TROLL_FEMALE_5475
    )
}
