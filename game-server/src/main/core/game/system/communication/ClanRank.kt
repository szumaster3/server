package core.game.system.communication

/**
 * Represents the different ranks within a clan.
 *
 * @property value The numeric value associated with the rank.
 * @property info A user-friendly description of the rank.
 */
enum class ClanRank(
    val value: Int,
    val info: String
) {
    /**
     * Rank for anyone.
     */
    ANYONE(-1, "Anyone"),

    /**
     * Rank for any friends.
     */
    ANY_FRIEND(0, "Any friends"),

    /**
     * Recruit rank and above.
     */
    RECRUIT(1, "Recruit+"),

    /**
     * Corporal rank and above.
     */
    CORPORAL(2, "Corporal+"),

    /**
     * Sergeant rank and above.
     */
    SERGEANT(3, "Sergeant+"),

    /**
     * Lieutenant rank and above.
     */
    LIEUTENANT(4, "Lieutenant+"),

    /**
     * Captain rank and above.
     */
    CAPTAIN(5, "Captain+"),

    /**
     * General rank and above.
     */
    GENERAL(6, "General+"),

    /**
     * Rank for only the player themselves.
     */
    ONLY_ME(7, "Only me"),

    /**
     * Rank for no one.
     */
    NO_ONE(127, "No-one");

    override fun toString(): String = "Rank=[$name], Info=[$info]"
}
