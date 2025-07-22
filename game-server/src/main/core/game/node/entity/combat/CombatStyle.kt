package core.game.node.entity.combat

import core.game.node.entity.player.link.prayer.PrayerType

/**
 * Represents different combat styles, each with its swing handler and protection prayer.
 */
enum class CombatStyle(
    /**
     * The combat swing handler used by this style.
     */
    val swingHandler: CombatSwingHandler,
    /**
     * The protection prayer associated with this combat style.
     */
    val protectionPrayer: PrayerType
) {
    MELEE(MeleeSwingHandler(), PrayerType.PROTECT_FROM_MELEE),
    RANGE(RangeSwingHandler(), PrayerType.PROTECT_FROM_MISSILES),
    MAGIC(MagicSwingHandler(), PrayerType.PROTECT_FROM_MAGIC);

    init {
        swingHandler.type = this
    }
}