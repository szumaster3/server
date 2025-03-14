package core.game.node.entity.player.link.request.trade;

import core.api.ContainerListener;
import core.cache.def.impl.ItemDefinition;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.container.ContainerType;
import core.game.container.SortType;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

import static core.api.ContentAPIKt.sendMessage;
import static core.tools.GlobalsKt.colorize;

/**
 * The type Trade container.
 */
public final class TradeContainer extends Container {

    private final Player player;

    /**
     * Instantiates a new Trade container.
     *
     * @param player the player
     */
    public TradeContainer(final Player player) {
        super(28, ContainerType.DEFAULT, SortType.ID);
        this.player = player;
        this.getListeners().add(new TradeListener());
    }

    /**
     * Offer.
     *
     * @param slot   the slot
     * @param amount the amount
     */
    public void offer(final int slot, int amount) {
        final Item item = getItem(slot, player.getInventory());
        if (!canUse()) {
            return;
        }
        if (!validatedItem(item, slot, amount, player.getInventory())) {
            return;
        }
        if (!tradeable(item) && !GameWorld.getSettings().isDevMode()) {
            player.getPacketDispatch().sendMessage("You can't trade this item.");
            return;
        }
        Item remove = new Item(item.getId(), amount);
        remove.setAmount(stabalizeAmount(remove, amount, player.getInventory()));
        if (!checkCapacity(remove, this)) {
            player.getPacketDispatch().sendMessage("You must accept the current items before adding any more.");
            return;
        }
        if (player.getInventory().remove(remove, slot, true) && super.add(remove)) {
            if (TradeModule.getExtension(player).isAccepted() || TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).isAccepted()) {
                TradeModule.getExtension(player).setAccepted(false, true);
                TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).setAccepted(false, true);
            }
            TradeModule.getExtension(player).update();
            checkAlert();
        }
    }

    /**
     * Withdraw.
     *
     * @param slot   the slot
     * @param amount the amount
     */
    public void withdraw(final int slot, int amount) {
        Item item = getItem(slot, this);
        if (!canUse()) {
            return;
        }
        if (!validatedItem(item, slot, amount, this)) {
            return;
        }
        Item remove = new Item(item.getId(), amount);
        remove.setAmount(stabalizeAmount(remove, amount, this));
        if (!checkCapacity(remove, player.getInventory())) {
            player.getPacketDispatch().sendMessage("You don't have enough space in your inventory.");
            return;
        }
        if (super.remove(remove, slot, true) && player.getInventory().add(remove)) {
            shift();
            TradeModule.getExtension(player).setModified(true);
            if (TradeModule.getExtension(player).isAccepted() || TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).isAccepted()) {
                TradeModule.getExtension(player).setAccepted(false, true);
                TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).setAccepted(false, true);
                TradeModule.getExtension(player).update();
            } else {
                TradeModule.getExtension(player).update();
            }
        }
        if (TradeModule.getExtension(player).isModified()) {
            alert(slot, true);
        }
    }

    @Override
    public void shift() {
        super.shift();
    }

    private Item getItem(int slot, final Container container) {
        return container.get(slot);
    }

    private boolean validatedItem(final Item item, final int slot, final int amount, final Container container) {
        if (item == null) {
            return false;
        }
        return !(slot < 0 || slot > player.getInventory().capacity() || amount < 1);
    }

    private int stabalizeAmount(final Item item, int amount, final Container container) {
        int maximum = container.getAmount(item);
        return (amount > maximum ? maximum : amount);
    }

    private boolean tradeable(final Item item) {
        final ItemDefinition definition = ItemDefinition.forId(item.getId());
        Player target = TradeModule.getExtension(player).getTarget();
        boolean playerIsBot = player.getAttribute("not_on_highscores", false);
        boolean targetIsBot = target.getAttribute("not_on_highscores", false);
        String playerIP = player.getDetails().getIpAddress();
        String targetIP = target.getDetails().getIpAddress();
        String playerMac = player.getDetails().getMacAddress();
        String targetMac = target.getDetails().getMacAddress();
        String playerHost = player.getDetails().getCompName();
        String targetHost = target.getDetails().getCompName();
        if (item.getId() == 11174 || item.getId() == 11173 || item.getId() == 759) {
            return true;
        }
        if (player.getIronmanManager().isIronman() || target != null && target.getIronmanManager().isIronman()) {
            return false;
        }
        if ((playerIsBot || targetIsBot) && (playerIP.equals(targetIP) || playerMac.equals(targetMac) || playerHost.equals(targetHost))) {
            sendMessage(player, colorize("%RYou can not trade items with your own bot accounts."));
            return false;
        }
        if (item.getName().equals("Coins") && item.getId() != 995) {
            return false;
        }
        return definition.isTradeable();
    }

    private boolean canUse() {
        if (!player.getInterfaceManager().isOpened() || TradeModule.getExtension(player) == null || TradeModule.getExtension(TradeModule.getExtension(player).getTarget()) == null || TradeModule.getExtension(player).getStage() != 0) {
            return false;
        }
        return true;
    }

    private boolean checkCapacity(final Item item, final Container container) {
        if (item.getAmount() > container.getMaximumAdd(item)) {
            item.setAmount(container.getMaximumAdd(item));
            if (item.getAmount() < 1) {
                return false;
            }
        }
        return true;
    }

    private void alert(final int slot, final boolean save) {
        GameWorld.getPulser().submit(new Pulse(1, player) {
            @Override
            public boolean pulse() {
                if (TradeModule.getExtension(player) != null) {
                    TradeModule.getExtension(player).alert(slot);
                }
                return true;
            }
        });
        if (save) {
            player.setAttribute("alert", GameWorld.getTicks() + 8);
            player.setAttribute("alertSlot", slot);
        }
    }

    private void checkAlert() {
        if (player.getAttribute("alert", 0) > GameWorld.getTicks()) {
            alert(player.getAttribute("alertSlot", 0), false);
        }
    }

    /**
     * The type Trade listener.
     */
    public final class TradeListener implements ContainerListener {

        @Override
        public void update(Container c, ContainerEvent event) {//type 541 - loaning
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 64212, 90, c.toArray(), c.capacity(), false));
            if (TradeModule.getExtension(player) != null && TradeModule.getExtension(TradeModule.getExtension(player).getTarget()) != null) {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 60981, -2, 91, TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).getContainer().toArray(), 27, false));
            }
            checkAlert();
        }

        @Override
        public void refresh(Container c) {
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 64209, 93, player.getInventory(), false));
        }

    }
}