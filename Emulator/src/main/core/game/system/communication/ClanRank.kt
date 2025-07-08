package core.game.system.communication

/**
 * The enum Clan rank.
 */
enum class ClanRank(
    /**
     * Gets value.
     *
     * @return the value
     */
    val value: Int,
    /**
     * Gets info.
     *
     * @return the info
     */
    val info: String
) {
    /**
     * Anyone clan rank.
     */
    ANYONE(-1, "Anyone"),

    /**
     * The Any friend.
     */
    ANY_FRIEND(0, "Any friends"),

    /**
     * Recruit clan rank.
     */
    RECRUIT(1, "Recruit+"),

    /**
     * Corporal clan rank.
     */
    CORPORAL(2, "Corporal+"),

    /**
     * Sergeant clan rank.
     */
    SERGEANT(3, "Sergeant+"),

    /**
     * Lieutenant clan rank.
     */
    LIEUTENANT(4, "Lieutenant+"),

    /**
     * Captain clan rank.
     */
    CAPTAIN(5, "Captain+"),

    /**
     * General clan rank.
     */
    GENERAL(6, "General+"),

    /**
     * The Only me.
     */
    ONLY_ME(7, "Only me"),

    /**
     * No one clan rank.
     */
    NO_ONE(127, "No-one");

    override fun toString(): String {
        return "Rank=[$name], Info=[$info]"
    }
}