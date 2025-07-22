package content.global.activity.shootingstar

/**
 * Enum represents the star types (tiered).
 */
enum class ShootingStarType(
    val objectId: Int,
    val exp: Int,
    val totalStardust: Int,
    val rate: Double,
) {
    LEVEL_1(38668, 14, 1200, 0.05),
    LEVEL_2(38667, 25, 700, 0.1),
    LEVEL_3(38666, 29, 439, 0.3),
    LEVEL_4(38665, 32, 250, 0.4),
    LEVEL_5(38664, 47, 175, 0.5),
    LEVEL_6(38663, 71, 80, 0.70),
    LEVEL_7(38662, 114, 40, 0.80),
    LEVEL_8(38661, 145, 25, 0.85),
    LEVEL_9(38660, 210, 15, 0.95),
}