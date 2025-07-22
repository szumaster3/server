package core.game.system.communication

/**
 * Represents a contact in the clan system.
 *
 * @property username the username of the contact
 * @property worldId the ID of the world the contact is in
 * @property rank the clan rank of the contact
 */
class Contact(
    @JvmField val username: String
) {
    /**
     * The world ID where the contact is currently located.
     */
    @JvmField
    var worldId: Int = 0

    /**
     * The clan rank of the contact.
     */
    @JvmField
    var rank: ClanRank = ClanRank.ANY_FRIEND
}