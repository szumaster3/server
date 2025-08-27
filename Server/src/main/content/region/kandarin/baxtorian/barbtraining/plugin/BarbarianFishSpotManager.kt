package content.region.kandarin.baxtorian.barbtraining.plugin

import core.api.StartupListener
import core.api.TickListener
import core.game.world.map.Location
import core.tools.RandomFunction

class BarbarianFishSpotManager : TickListener, StartupListener {
    var ticks = 0
    val spots = ArrayList<BarbarianFishingSpot>()

    companion object {
        val usedLocations = arrayListOf<Location>()
        val locations = listOf(
            Location.create(2506, 3494, 0),
            Location.create(2504, 3497, 0),
            Location.create(2504, 3497, 0),
            Location.create(2500, 3506, 0),
            Location.create(2500, 3509, 0),
            Location.create(2500, 3512, 0),
            Location.create(2504, 3516, 0),
        )

        fun getNewTTL(): Int = RandomFunction.random(400, 2000)

        fun getNewLoc(): Location {
            val possibleLoc = ArrayList<Location>()
            for (loc in locations) if (usedLocations.contains(loc)) continue else possibleLoc.add(loc)
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