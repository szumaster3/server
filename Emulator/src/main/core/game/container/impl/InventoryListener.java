package core.game.container.impl;

import content.global.skill.summoning.SummoningPouch;
import core.api.ContainerListener;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;
import org.rs.consts.Components;

/**
 * Handles the inventory container listening.
 * @author Emperor
 */
public final class InventoryListener implements ContainerListener {

    /**
     * The player associated with this inventory listener.
     */
    private final Player player;

    /**
     * Creates a new inventory listener for the specified player.
     *
     * @param player the player whose inventory changes will be monitored
     */
    public InventoryListener(Player player) {
        this.player = player;
    }

    /**
     * Updates the player's state based on the contents of their inventory.
     * This includes weight calculations and determining if the player has a summoning pouch.
     *
     * @param container the inventory container
     */
    private void updatePlayerState(Container container) {
        // Update the player's weight based on the inventory contents.
        player.getSettings().updateWeight();

        // Check if the player has a summoning pouch in their inventory.
        boolean pouch = player.getFamiliarManager().isHasPouch();
        boolean summoningPouch = false;

        for (Item item : container.toArray()) {
            if (item != null && SummoningPouch.get(item.getId()) != null) {
                summoningPouch = true;
                break;
            }
        }

        // Update the player's familiar manager state.
        player.getFamiliarManager().setHasPouch(summoningPouch);

        // If the pouch state changed, synchronize the player's appearance.
        if (pouch != summoningPouch) {
            player.getAppearance().sync();
        }
    }

    /**
     * Refreshes the entire inventory container and sends an update packet to the client.
     *
     * @param container the inventory container to refresh
     */
    @Override
    public void refresh(Container container) {
        PacketRepository.send(
                ContainerPacket.class,
                new ContainerContext(player, Components.INVENTORY_149, 0, 93, container, false)
        );
        updatePlayerState(container);
    }

    /**
     * Updates specific items in the inventory based on a container event.
     * Sends an update packet to the client and ensures the player's state remains consistent.
     *
     * @param container the inventory container
     * @param event     the container event containing the updated items
     */
    @Override
    public void update(Container container, ContainerEvent event) {
        PacketRepository.send(
                ContainerPacket.class,
                new ContainerContext(player, Components.INVENTORY_149, 0, 93, event.getItems(), false, event.getSlots())
        );
        updatePlayerState(container);
    }
}