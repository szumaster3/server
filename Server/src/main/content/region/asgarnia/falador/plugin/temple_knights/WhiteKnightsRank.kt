package content.region.asgarnia.falador.plugin.temple_knights

/**
 * Represents the White Knights ranking.
 */
enum class WhiteKnightsRank(val killCount: Int) {
    UNRANKED(0), NOVICE(100), PEON(200), PAGE(300), NOBLE(500), ADEPT(800), MASTER(1300)
}