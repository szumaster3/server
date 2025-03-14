package core.api.region.contracts

import core.game.world.map.build.DynamicRegion

interface RegionSpecContract {
    fun instantiateRegion(): DynamicRegion
}
