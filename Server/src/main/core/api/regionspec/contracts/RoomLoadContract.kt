/*package core.api.regionspec.contracts

import content.global.skill.construction.HouseManager
import content.global.skill.construction.Room
import core.game.node.entity.player.Player
import core.game.world.map.build.DynamicRegion

class RoomLoadContract(
    private val manager: HouseManager,
    private val buildingMode: Boolean,
    private val rooms: Array<Array<Array<Room>>>
) : PlayerChunkSpecContract {
    override fun populateChunks(dyn: DynamicRegion, player: Player) {
        dyn.isBuild = player.houseManager.isBuildingMode
        for (plane in 0 until 4) {
            for (x in 0 until 8) {
                for (y in 0 until 8) {
                    val room = rooms.getOrNull(plane)?.getOrNull(x)?.getOrNull(y) ?: continue
                    val chunk = room.chunk.copy(dyn.planes[plane])
                    dyn.replaceChunk(plane, x, y, chunk, dyn)
                    room.loadDecorations(
                        if (dyn != player.houseManager.dungeonRegion) plane else 3,
                        chunk,
                        player.houseManager
                    )
                }
            }
        }
    }
}
*/