package core.game.node.entity.combat

import core.game.node.entity.player.link.prayer.PrayerType

/**
 * Represents the different styles of combat.
 *
 * @author Emperor
 */
enum class CombatStyle(
    /**
     * The combat swing handler used by the combat style.
     */
    val swingHandler: CombatSwingHandler,
    /**
     * The protection prayer for this combat type.
     */
    val protectionPrayer: PrayerType
) {
    /**
     * The melee combat style.
     */
    MELEE(MeleeSwingHandler(), PrayerType.PROTECT_FROM_MELEE),

    /**
     * The range combat style.
     */
    RANGE(RangeSwingHandler(), PrayerType.PROTECT_FROM_MISSILES),

    /**
     * The magic combat style.
     */
    MAGIC(MagicSwingHandler(), PrayerType.PROTECT_FROM_MAGIC);

    init {
        swingHandler.type = this
    }
}