package core.game.node.scenery

/**
 * Loc shapes.
 *
 * @see <a href="https://github.com/openrs2/openrs2/blob/master/share/doc/glossary.md">Source</a>
 */
object SceneryType {
    // Straight walls, fences etc
    val WallStraight = 0
    // Diagonal walls corner, fences, connectors
    val WallDiagonalCorner = 1
    // Entire walls, fences, corners
    val WallL = 2
    // Straight wall corners, fences, connectors
    val WallSquareCorner = 3
    // Straight inside wall decoration
    val WallDecorStraightNoOffset = 4
    // Straight outside wall decoration
    val WallDecorStraightOffset = 5
    // Diagonal outside wall decoration
    val WallDecorDiagonalOffset = 6
    // Diagonal inside wall decoration
    val WallDecorDiagonalNoOffset = 7
    // Diagonal in wall decoration
    val WallDecorDiagonalBoth = 8
    // Diagonal walls, fences etc
    val WallDiagonal = 9
    // All kinds of objects, trees, statues, signs, fountains etc
    val CentrepieceStraight = 10
    // Ground objects like daisies etc
    val CentrepieceDiagonal = 11
    // Straight sloped roofs
    val RoofStraight = 12
    // Diagonal sloped roofs
    val RoofDiagonalWithRoofEdge = 13
    // Diagonal slope connecting roofs
    val RoofDiagonal = 14
    // Straight sloped corner connecting roofs
    val RoofLConcave = 15
    // Straight sloped corner roof
    val RoofLConvex = 16
    // Straight flat top roofs
    val RoofFlat = 17
    // Straight bottom edge roofs
    val RoofEdgeStraight = 18
    // Diagonal bottom edge connecting roofs
    val RoofEdgeDiagonalCorner = 19
    // Straight bottom edge connecting roofs
    val RoofEdgeL = 20
    // Straight bottom edge connecting corner roofs
    val RoofEdgeSquareCorner = 21
    // Ground decoration + map signs (quests, water fountains, shops)
    val GroundDecor = 22

    private val map: Map<String, Int> = listOf(
        "WallStraight" to WallStraight,
        "WallDiagonalCorner" to WallDiagonalCorner,
        "WallL" to WallL,
        "WallSquareCorner" to WallSquareCorner,
        "WallDecorStraightNoOffset" to WallDecorStraightNoOffset,
        "WallDecorStraightOffset" to WallDecorStraightOffset,
        "WallDecorDiagonalOffset" to WallDecorDiagonalOffset,
        "WallDecorDiagonalNoOffset" to WallDecorDiagonalNoOffset,
        "WallDecorDiagonalBoth" to WallDecorDiagonalBoth,
        "WallDiagonal" to WallDiagonal,
        "CentrepieceStraight" to CentrepieceStraight,
        "CentrepieceDiagonal" to CentrepieceDiagonal,
        "RoofStraight" to RoofStraight,
        "RoofDiagonalWithRoofEdge" to RoofDiagonalWithRoofEdge,
        "RoofDiagonal" to RoofDiagonal,
        "RoofLConcave" to RoofLConcave,
        "RoofLConvex" to RoofLConvex,
        "RoofFlat" to RoofFlat,
        "RoofEdgeStraight" to RoofEdgeStraight,
        "RoofEdgeDiagonalCorner" to RoofEdgeDiagonalCorner,
        "RoofEdgeL" to RoofEdgeL,
        "RoofEdgeSquareCorner" to RoofEdgeSquareCorner,
        "GroundDecor" to GroundDecor
    ).toMap()

    /**
     * Gets the id of a tile type given its name.
     */
    fun getId(name: String): Int? = map[name]
}
