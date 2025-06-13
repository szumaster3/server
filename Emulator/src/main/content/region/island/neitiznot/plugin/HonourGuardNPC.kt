package content.region.island.neitiznot.plugin

import core.api.findLocalNPCs
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class HonourGuardNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private var attackDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = HonourGuardNPC(id, location)

    override fun getIds(): IntArray = ID

    override fun handleTickActions() {
        if (!isActive || inCombat()) return

        if (--attackDelay <= 0) {
            if (RandomFunction.roll(10)) {
                val localTrolls = findLocalNPCs(this, TROLL_ID)
                val target = localTrolls.firstOrNull { it.location.withinDistance(location, 6) }
                if (target != null) {
                    attack(target)
                    attackDelay = randomDelay(5, 10)
                    return
                }
            }
            attackDelay = randomDelay(3, 6)
        }
    }

    private fun randomDelay(min: Int = 5, max: Int = 15) = RandomFunction.random(min, max)

    companion object {
        private val TROLL_ID = intArrayOf(
            NPCs.ICE_TROLL_FEMALE_5523,
            NPCs.ICE_TROLL_MALE_5522,
            NPCs.ICE_TROLL_RUNT_5521,
            NPCs.ICE_TROLL_GRUNT_5524
        )
        private val ID = intArrayOf(
            NPCs.HONOUR_GUARD_5514,
            NPCs.HONOUR_GUARD_5515,
            NPCs.HONOUR_GUARD_5516,
            NPCs.HONOUR_GUARD_5517
        )
    }
}
