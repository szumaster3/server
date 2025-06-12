package content.region.islands.neitiznot.plugin

import core.api.findLocalNPCs
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class HonourGuardNPC : AbstractNPC {

    constructor() : super(NPCs.HONOUR_GUARD_5514, null, true)

    private constructor(id: Int, location: Location) : super(id, location)

    private var nextAttack = 0L

    override fun construct(id: Int, location: Location, vararg objects: Any?, ): AbstractNPC =
        HonourGuardNPC(id, location)

    override fun tick() {
        val now = System.currentTimeMillis()

        if (isActive && !inCombat() && now >= nextAttack) {
            if (RandomFunction.roll(10)) {
                val localTrolls = findLocalNPCs(this, trollIDs)
                for (troll in localTrolls) {
                    if (troll.location.withinDistance(location, 6)) {
                        attack(troll)
                        nextAttack = now + 5000L
                        break
                    }
                }
            } else {
                nextAttack = now + 3000L
            }
        }

        if (!isActive) {
            properties.combatPulse.stop()
        }

        super.tick()
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val trollIDs = intArrayOf(
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