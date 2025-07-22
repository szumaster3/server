package content.region.kandarin.plugin.barbtraining.plugin

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs.consts.NPCs

class BarbFishingSpot(var loc: Location? = null, var ttl: Int) : NPC(NPCs.FISHING_SPOT_1176) {

    init {
        location = loc
    }

    val locations = listOf(
        Location.create(2506, 3494, 0),
        Location.create(2504, 3497, 0),
        Location.create(2504, 3497, 0),
        Location.create(2500, 3506, 0),
        Location.create(2500, 3509, 0),
        Location.create(2500, 3512, 0),
        Location.create(2504, 3516, 0),
    )

    override fun handleTickActions() {
        if (location != loc) properties.teleportLocation = loc.also { ttl = BarbFishSpotManager.getNewTTL() }
        if (ttl-- <= 0) {
            BarbFishSpotManager.Companion.usedLocations.remove(location)
            loc = BarbFishSpotManager.Companion.getNewLoc()
        }
    }
}