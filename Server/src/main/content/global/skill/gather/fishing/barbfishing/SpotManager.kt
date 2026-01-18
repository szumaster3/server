package content.global.skill.gather.fishing.barbfishing

import core.api.StartupListener
import core.api.TickListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * All potential spawn locations
 */
val locations = listOf(
    Location.create(2506, 3494, 0),
    Location.create(2504, 3497, 0),
    Location.create(2504, 3497, 0),
    Location.create(2500, 3506, 0),
    Location.create(2500, 3509, 0),
    Location.create(2500, 3512, 0),
    Location.create(2504, 3516, 0),
)

/**
 * Manages barbarian fishing spots.
 */
class BarbarianFishSpotManager : TickListener, StartupListener {
    /**
     * Global tick counter.
     */
    var ticks = 0

    /**
     * Currently active fishing spots.
     */
    val spots = ArrayList<BarbarianFishingSpot>()

    companion object {
        /**
         * Locations currently in use.
         */
        val usedLocations = arrayListOf<Location>()

        /**
         * Generates a random time-to-live for a fishing spot.
         */
        fun getNewTTL(): Int = RandomFunction.random(400, 2000)

        /**
         * Picks a new available location for a fishing spot.
         */
        fun getNewLoc(): Location {
            val possibleLoc = ArrayList<Location>()
            for (loc in locations) if (!usedLocations.contains(loc)) possibleLoc.add(loc)
            val loc = possibleLoc.random()
            usedLocations.add(loc)
            return loc
        }
    }

    override fun tick() {
        if (ticks % 50 == 0) {
            usedLocations.clear()
            for (spot in spots) usedLocations.add(spot.loc ?: Location(0, 0, 0))
        }
    }

    override fun startup() {
        for (i in 0 until 5) {
            spots.add(BarbarianFishingSpot(getNewLoc(), getNewTTL()).also { it.init() })
        }
    }
}

/**
 * Represents a single barbarian fishing spot NPC.
 *
 * @param loc initial location of the spot.
 * @param ttl time-to-live in ticks before respawn.
 */
class BarbarianFishingSpot(var loc: Location? = null, var ttl: Int) : NPC(NPCs.FISHING_SPOT_1176) {

    init {
        location = loc
    }

    /**
     * Handles per-tick behavior such as moving and respawning.
     */
    override fun handleTickActions() {
        if (location != loc) properties.teleportLocation = loc.also { ttl = BarbarianFishSpotManager.getNewTTL() }
        if (ttl-- <= 0) {
            BarbarianFishSpotManager.usedLocations.remove(location)
            loc = BarbarianFishSpotManager.getNewLoc()
        }
    }
}
