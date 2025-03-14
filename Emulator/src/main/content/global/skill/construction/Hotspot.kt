package content.global.skill.construction

class Hotspot(
    val hotspot: BuildHotspot,
    val chunkX: Int,
    val chunkY: Int,
    var chunkX2: Int = chunkX,
    var chunkY2: Int = chunkY,
) {
    var currentX: Int
    var currentY: Int
    var decorationIndex = -1

    init {
        currentX = chunkX
        currentY = chunkY
    }

    fun isValid(): Boolean {
        return chunkX2 != -1 && chunkY2 != -1
    }

    fun copy(): Hotspot {
        return Hotspot(hotspot, chunkX, chunkY, chunkX2, chunkY2)
    }
}
