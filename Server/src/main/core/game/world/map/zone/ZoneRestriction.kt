package core.game.world.map.zone

/**
 * The zone restrictions.
 *
 * @author Emperor
 */
enum class ZoneRestriction {
    FOLLOWERS,          // No followers allowed in this zone.
    RANDOM_EVENTS,      // No random events allowed.
    FIRES,              // No fires allowed.
    MEMBERS,            // Members only.
    CANNON,             // No cannons allowed.
    GRAVES,             // Do not spawn a grave if a player dies here.
    TELEPORT,           // No teleporting allowed.
    ;

    val flag: Int
        get() = 1 shl ordinal
}
