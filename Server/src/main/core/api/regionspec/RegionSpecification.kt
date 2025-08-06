package core.api.regionspec

import core.api.regionspec.contracts.*
import core.game.world.map.BuildRegionChunk
import core.game.world.map.Region
import core.game.world.map.build.DynamicRegion

class RegionSpecification(
    val regionContract: RegionSpecContract = EmptyRegionContract(),
    vararg val chunkContracts: ChunkSpecContract = arrayOf(EmptyChunkContract()),
) {
    constructor(vararg chunkContracts: ChunkSpecContract) : this(EmptyRegionContract(), *chunkContracts)

    fun build(): DynamicRegion {
        val dyn = regionContract.instantiateRegion()
        Region.load(dyn)
        chunkContracts.forEach { it.populateChunks(dyn) }
        return dyn
    }
}

fun fillWith(chunk: BuildRegionChunk?): FillChunkContract = FillChunkContract(chunk)

fun fillWith(delegate: (Int, Int, Int, Region) -> BuildRegionChunk?): FillChunkContract = FillChunkContract(delegate)

fun copyOf(regionId: Int): RegionSpecContract = CloneRegionContract(regionId)

fun using(region: DynamicRegion): UseExistingRegionContract = UseExistingRegionContract(region)
