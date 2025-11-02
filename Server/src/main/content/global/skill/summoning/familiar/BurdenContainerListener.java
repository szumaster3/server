package content.global.skill.summoning.familiar;

import core.api.ContainerListener;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.node.entity.player.Player;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

/**
 * The Beast of Burden container listener.
 */
public final class BurdenContainerListener implements ContainerListener {

	private final Player player;

    /**
     * Instantiates a new Burden container listener.
     *
     * @param player the player
     */
    public BurdenContainerListener(Player player) {
		this.player = player;
	}

	/**
	 * Sends a partial container update to the client with only the changed slots.
	 *
	 * @param c The container being updated.
	 * @param event The container event containing updated slots and items.
	 */
	@Override
	public void update(Container c, ContainerEvent event) {
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 30, event.getItems(), false, event.getSlots()));
	}

	/**
	 * Sends a full container refresh to the client, updating all items.
	 *
	 * @param c The container to refresh.
	 */
	@Override
	public void refresh(Container c) {
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 30, c.toArray(), c.capacity(), false));
	}

}