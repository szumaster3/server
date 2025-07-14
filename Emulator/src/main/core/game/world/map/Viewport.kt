package core.game.world.map

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

/**
 * Represents an entity viewport.
 */
class Viewport {

    companion object {
        /**
         * Represents the amount of chunks in a viewport (5x5).
         */
        const val CHUNK_SIZE = 5
    }

    /**
     * Represents the region this viewport belongs to.
     */
    var region: Region? = null

    /**
     * Represents the 2D array of chunks currently visible in the viewport.
     */
    var chunks: Array<Array<RegionChunk?>> = Array(CHUNK_SIZE) { arrayOfNulls(CHUNK_SIZE) }

    /**
     * Represents the current active plane (height level) the entity is on.
     */
    var currentPlane: RegionPlane? = null

    /**
     * Represents the list of region planes currently visible to the entity.
     */
    var viewingPlanes: MutableList<RegionPlane> = mutableListOf()

    /**
     * Updates the entity viewport by recalculating visible region chunks.
     *
     * @param entity The entity whose viewport should be updated.
     */
    fun updateViewport(entity: Entity) {
        val chunk = RegionManager.getRegionChunk(entity.location)
        val center = chunks.size shr 1
        if (chunks[center][center] == chunk) return

        val offset = center * -8
        val l = chunk.currentBase
        for (x in chunks.indices) {
            for (y in chunks[x].indices) {
                chunks[x][y] = RegionManager.getRegionChunk(
                    l.transform(offset + (8 * x), offset + (8 * y), 0)
                )
            }
        }
    }

    /**
     * Removes the entity from its current region and viewing planes.
     *
     * @param entity The entity to remove.
     */
    fun remove(entity: Entity) {
        val region = this.region ?: return

        if (entity is Player) {
            region.remove(entity)
            var lastRegion: Region? = null
            for (plane in viewingPlanes) {
                if (lastRegion != plane.region) {
                    lastRegion = plane.region
                    lastRegion.decrementViewAmount()
                    lastRegion.checkInactive()
                }
            }
        } else if (entity is NPC) {
            region.remove(entity)
        }
    }
}
