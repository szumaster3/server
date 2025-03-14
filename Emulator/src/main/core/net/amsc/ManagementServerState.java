package core.net.amsc;

import core.game.node.entity.player.Player;
import core.game.world.repository.Repository;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContactContext;
import core.net.packet.out.ContactPackets;

/**
 * The enum Management server state.
 */
public enum ManagementServerState {

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

    private final int value;

    private ManagementServerState(int value) {
        this.value = value;
    }

    /**
     * Set.
     */
    public void set() {
        for (Player player : Repository.getPlayers()) {
            PacketRepository.send(ContactPackets.class, new ContactContext(player, ContactContext.UPDATE_STATE_TYPE));
        }
    }

    /**
     * Value int.
     *
     * @return the int
     */
    public int value() {
        return value;
    }
}