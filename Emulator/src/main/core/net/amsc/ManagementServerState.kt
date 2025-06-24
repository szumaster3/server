package core.net.amsc

import core.game.world.repository.Repository
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContactPackets

/**
 * The enum Management server state.
 */
enum class ManagementServerState(val value: Int) {

    /**
     * Not available management server state.
     */
    NOT_AVAILABLE(2),

    /**
     * Connecting management server state.
     */
    CONNECTING(1),

    /**
     * Available management server state.
     */
    AVAILABLE(2);

    /**
     * Updates the state for all players.
     */
    fun set() {
        for (player in Repository.players) {
            PacketRepository.send(
                ContactPackets::class.java,
                OutgoingContext.Contact(player, OutgoingContext.Contact.UPDATE_STATE_TYPE)
            )
        }
    }
}
