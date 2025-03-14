package core.api.region.contracts

import core.game.world.map.build.DynamicRegion

class EmptyChunkContract : ChunkSpecContract {
    override fun populateChunks(dyn: DynamicRegion) {}
}
