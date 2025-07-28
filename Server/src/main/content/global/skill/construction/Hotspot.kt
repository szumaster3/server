package content.global.skill.construction

class Hotspot(val hotspot: BuildHotspot, val chunkX: Int, val chunkY: Int, var chunkX2: Int, var chunkY2: Int) {

    constructor(hotspot: BuildHotspot, chunkX: Int, chunkY: Int) : this(hotspot, chunkX, chunkY, -1, -1)

    var currentX: Int
    var currentY: Int
    var decorationIndex = -1

    fun copy(): Hotspot {
        return Hotspot(hotspot, chunkX, chunkY, chunkX2, chunkY2)
    }

    init {
        currentX = chunkX
        currentY = chunkY
    }
}