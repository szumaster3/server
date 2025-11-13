package content.global.skill.slayer.waterbirth_dungeon

import core.api.*
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles BardurNPC.
 */
class BardurNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    private var ticks = 0
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = BardurNPC(id, location)

    override fun tick() {
        ticks++
        if (ticks < 5) {
            super.tick()
            return
        }
        ticks = 0

        if (isActive && !inCombat() && RandomFunction.roll(10)) {
            val localFledglings = findLocalNPCs(this, intArrayOf(NPCs.DAGANNOTH_FLEDGELING_2880))
            for (fledgling in localFledglings) {
                if (fledgling.location.withinDistance(location, 6)) {
                    attack(fledgling)
                    return super.tick()
                }
            }
        }

        if (!isActive) {
            properties.combatPulse.stop()
        }
        super.tick()
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BARDUR_2879)
}
