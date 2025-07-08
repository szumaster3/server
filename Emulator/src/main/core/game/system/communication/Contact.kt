package core.game.system.communication

/**
 * The type Contact.
 */
class Contact(@JvmField val username: String) {
    /**
     * Gets world id.
     *
     * @return the world id
     */
    @JvmField
    var worldId: Int = 0

    /**
     * Gets rank.
     *
     * @return the rank
     */
    @JvmField
    var rank: ClanRank = ClanRank.ANY_FRIEND
}