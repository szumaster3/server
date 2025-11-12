package content.global.skill.gather.fishing

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction

// List of all NPC IDs that are fishing spots.
private val fishingSpots = FishingSpot.getAllIds()

/**
 * Handles the behavior of fishing spot NPCs, including movement between locations and visibility toggling.
 */
class FishingNPC : NPCBehavior(*fishingSpots) {

    /**
     * Called when the NPC is created.
     */
    override fun onCreation(self: NPC) {
        setAttribute(self, "fishing:spot", FishingSpots.forLocation(self.location))
        setAttribute(self, "fishing:switchdelay", 0)
    }

    /**
     * Called on every game tick.
     */
    override fun tick(self: NPC): Boolean {
        if (getSpot(self) == null) {
            return false
        }
        if (getAttribute(self, "fishing:switchdelay", 0) < getWorldTicks()) {
            moveSpot(self)
            return false
        }
        return false
    }

    /**
     * Moves the fishing spot to a different location within its defined list.
     */
    private fun moveSpot(self: NPC) {
        when (val spot = getSpot(self)) {
            null -> {
                self.isInvisible = !self.isInvisible
                setAttribute(self, "fishing:switchdelay", getWorldTicks() + getRandomDelay())
                return
            }

            FishingSpots.TUTORIAL_ISLAND -> {
                // Tutorial Island spots do not move.
                return
            }

            else -> {
                val randLoc = spot.locations[RandomFunction.random(spot.locations.size)]
                if (findLocalNPCs(randLoc, 0).isEmpty()) {
                    teleport(self, randLoc)
                }
                setAttribute(self, "fishing:switchdelay", getWorldTicks() + getRandomDelay())
            }
        }
    }

    /**
     * Generates a random delay in ticks for the next spot movement.
     */
    private fun getRandomDelay(): Int = RandomFunction.random(200, 390)

    /**
     * Gets the [FishingSpots] enum value assigned to the NPC.
     */
    private fun getSpot(npc: NPC): FishingSpots? = getAttribute(npc, "fishing:spot", null)
}
