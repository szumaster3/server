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
 * The type Inventory listener.
 */
public final class InventoryListener implements ContainerListener {

    private final Player player;

    /**
     * Instantiates a new Inventory listener.
     *
     * @param player the player
     */
    public InventoryListener(Player player) {
        this.player = player;
    }

    private void updatePlayerState(Container container) {
        player.getSettings().updateWeight();

        boolean pouch = player.getFamiliarManager().isHasPouch();
        boolean summoningPouch = false;

        for (Item item : container.toArray()) {
            if (item != null && SummoningPouch.get(item.getId()) != null) {
                summoningPouch = true;
                break;
            }
        }

        player.getFamiliarManager().setHasPouch(summoningPouch);

        if (pouch != summoningPouch) {
            player.getAppearance().sync();
        }
    }

    @Override
    public void refresh(Container container) {
        PacketRepository.send(
            ContainerPacket.class,
            new ContainerContext(player, Components.INVENTORY_149, 0, 93, container, false)
        );
        updatePlayerState(container);
    }

    @Override
    public void update(Container container, ContainerEvent event) {
        PacketRepository.send(
            ContainerPacket.class,
            new ContainerContext(player, Components.INVENTORY_149, 0, 93, event.getItems(), false, event.getSlots())
        );
        updatePlayerState(container);
    }
}
