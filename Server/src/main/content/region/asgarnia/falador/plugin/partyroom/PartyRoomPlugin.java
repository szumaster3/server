package content.region.asgarnia.falador.plugin.partyroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.api.ContentAPIKt.*;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.dialogue.DialogueAction;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import kotlin.Unit;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.plugin.ClassScanner;
import shared.consts.Animations;
import shared.consts.Components;
import shared.consts.Items;
import shared.consts.NPCs;

/**
 * Handles the party room.
 *
 * @author Vexia
 */
@Initializable
public final class PartyRoomPlugin extends OptionHandler {

    /**
     * The constants of the object ids.
     */
    private static final int CLOSED_CHEST = 26193, OPEN_CHEST = 2418, LEVER = 26194;

    /**
     * The queued chest.
     */
    private static final Container chestQueue = new Container(215);

    /**
     * The items currently being dropped.
     */
    private static final Container partyChest = new Container(215);

    /**
     * The mapping of chest viewers.
     */
    private static final Map<String, ChestViewer> viewers = new HashMap<>();

    /**
     * The balloon manager.
     */
    private static final BalloonManager balloonManager = new BalloonManager();

    /**
     * If the knight dance is commenced.
     */
    private static boolean dancing;

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(Animations.HUMAN_PARTY_ROOM_LEVER_6933);


    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(CLOSED_CHEST).getHandlers().put("option:open", this);
        SceneryDefinition.forId(OPEN_CHEST).getHandlers().put("option:deposit", this);
        SceneryDefinition.forId(OPEN_CHEST).getHandlers().put("option:shut", this);
        SceneryDefinition.forId(LEVER).getHandlers().put("option:pull", this);
        ClassScanner.definePlugin(new DepositInterfaceHandler());
        ClassScanner.definePlugin(new BalloonManager());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (node.getId()) {
            case CLOSED_CHEST:
                player.animate(Animation.create(536));
                SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(OPEN_CHEST));
                break;
            case OPEN_CHEST:
                switch (option) {
                    case "deposit":
                        deposit(player);
                        break;
                    case "shut":
                        player.animate(Animation.create(535));
                        SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(CLOSED_CHEST));
                        break;
                }
                break;
            case LEVER:
                handleLever(player, node.asScenery());
                break;
        }
        return true;
    }

    /**
     * Updates the chest viewers.
     *
     * @param type  the type.
     * @param event the event.
     */
    public static void update(int type, ContainerEvent event) {
        for (ChestViewer viewer : viewers.values()) {
            viewer.update(type, event);
        }
    }

    /**
     * Updates the party room.
     */
    public static void update() {
        update(0, null);
        update(1, null);
    }

    /**
     * Opens the deposit interface for the player.
     *
     * @param player the player.
     */
    private void deposit(Player player) {
        if (!viewers.containsKey(player.getName())) {
            viewers.put(player.getName(), new ChestViewer(player).view());
        } else {
            player.sendMessage("You are already viewing the chest!.");
        }
    }

    /**
     * Commences the knightly dance.
     */
    private void commenceDance() {
        dancing = true;
        final List<NPC> npcs = new ArrayList<NPC>();
        for (int i = 0; i < 6; i++) {
            NPC npc = NPC.create(NPCs.KNIGHT_660, Location.create(3043 + i, 3378, 0));
            npc.init();
            npcs.add(npc);
        }
        GameWorld.getPulser().submit(new Pulse(1) {
            int count = 0;

            @Override
            public boolean pulse() {
                switch (count) {
                    case 3:
                        npcs.get(3).sendChat("We're Knights of the Party Room");
                        break;
                    case 6:
                        npcs.get(3).sendChat("We dance round and round like a loon");
                        break;
                    case 8:
                        npcs.get(3).sendChat("Quite often we like to sing");
                        break;
                    case 11:
                        npcs.get(3).sendChat("Unfortunately we make a din");
                        break;
                    case 13:
                        npcs.get(3).sendChat("We're Knights of the Party Room");
                        break;
                    case 16:
                        npcs.get(3).sendChat("Do you like our helmet plumes?");
                        break;
                    case 18:
                        npcs.get(3).sendChat("Everyone's happy now we can move");
                        break;
                    case 20:
                        npcs.get(3).sendChat("Like a party animal in the groove");
                        break;
                    case 24:
                        dancing = false;
                        for (int i = 0; i < npcs.size(); i++) {
                            npcs.get(i).clear();
                        }
                        break;
                }
                count++;
                return false;
            }

        });

    }

    /**
     * Handles the lever pulling.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleLever(Player player, Scenery object) {
        player.getDialogueInterpreter().sendOptions("Select an Option", "Ballon Bonanza (1000 coins).", "Nightly Dance (500 coins).", "No reward.");
        player.getDialogueInterpreter().addAction(new DialogueAction() {
            @Override
            public void handle(Player player, int buttonId) {
                switch (buttonId) {
                    case 2:
                        if (isCluttered()) {
                            player.getDialogueInterpreter().sendDialogue("The floor is too cluttered at the moment.");
                        } else if (balloonManager.isCountingDown()) {
                            player.getDialogueInterpreter().sendDialogue("A count down has already begun.");
                        } else if (player.getInventory().contains(Items.COINS_995, 1000)) {
                            balloonManager.start();
                            player.getInventory().remove(new Item(Items.COINS_995, 1000));
                            animateLever(player, object);
                        } else {
                            player.getDialogueInterpreter().sendDialogue("Balloon Bonanza costs 1000 coins.");
                        }
                        break;
                    case 3:
                        if (isDancing()) {
                            player.getDialogueInterpreter().sendDialogue("The party room knights are already here!");
                        } else if (player.getInventory().contains(Items.COINS_995, 500)) {
                            commenceDance();
                            player.getInventory().remove(new Item(Items.COINS_995, 500));
                            animateLever(player, object);
                        } else {
                            player.getDialogueInterpreter().sendDialogue("Nightly Dance costs 500 coins.");
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Animates the player pulling the lever
     * and updates the scenery object.
     *
     * @param player the player.
     * @param object the object.
     */
    private void animateLever(Player player, Scenery object) {
        player.lock(3);

        // player.faceLocation(object.getLocation());
        // Not authentic. https://www.youtube.com/watch?v=giofl1mTP_M

        GameWorld.getPulser().submit(new Pulse(1) {
            private int ticks = 0;

            @Override
            public boolean pulse() {
                switch (ticks) {
                    case 0:
                        player.animate(ANIMATION);
                        break;
                    case 2: // Perfect synchronization.
                        animateScenery(player, object, 9386, true);
                        break;
                    case 5:
                        animateScenery(player, object, -1, true);
                        return true;
                }

                ticks++;
                return false;
            }
        });
    }

    /**
     * Checks if the knights are dancing.
     *
     * @return {@code True} if so.
     */
    private boolean isDancing() {
        return dancing;
    }

    /**
     * Checks if the floor is too cluttered.
     *
     * @return {@code True} if so.
     */
    private boolean isCluttered() {
        return balloonManager.isCluttered();
    }

    /**
     * Gets the chestQueue.
     *
     * @return the chestQueue
     */
    public static Container getChestQueue() {
        return chestQueue;
    }

    /**
     * Gets the partyChest.
     *
     * @return the partyChest
     */
    public static Container getPartyChest() {
        return partyChest;
    }

    /**
     * Gets the viewers.
     *
     * @return the viewers
     */
    public static Map<String, ChestViewer> getViewers() {
        return viewers;
    }

    /**
     * Handles the deposit interface.
     *
     * @author Vexia
     */
    public static final class DepositInterfaceHandler extends ComponentPlugin {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(Components.PARTY_ROOM_DEPOSIT_647, this);
            ComponentDefinition.put(Components.PARTY_ROOM_DEPOSIT_648, this);
            return this;
        }

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, final int slot, int itemId) {
            final ChestViewer viewer = player.getExtension(ChestViewer.class);
            if (viewer == null || viewer.getContainer() == null) {
                player.getInterfaceManager().close();
                return true;
            }
            switch (component.id) {
                case Components.PARTY_ROOM_DEPOSIT_648:
                    if (itemId == -1) {
                        if (player.getInventory().get(slot) != null) {
                            itemId = player.getInventory().get(slot).getId();
                        } else {
                            return true;
                        }
                    }
                    switch (opcode) {
                        case 155:
                            viewer.getContainer().addItem(slot, 1);
                            break;
                        case 196:
                            viewer.getContainer().addItem(slot, 5);
                            break;
                        case 124:
                            viewer.getContainer().addItem(slot, 10);
                            break;
                        case 199:
                            int amount = player.getInventory().getAmount(new Item(itemId));
                            viewer.getContainer().addItem(slot, amount);
                            break;
                        case 234:
                            sendInputDialogue(player, true, "Enter the amount:", (value) -> {
                                viewer.getContainer().addItem(slot, (int) value);
                                return Unit.INSTANCE;
                            });
                            break;
                    }
                    break;
                case Components.PARTY_ROOM_DEPOSIT_647:
                    switch (button) {
                        case 25:
                            viewer.accept();
                            return true;
                    }
                    if (itemId == -1 && viewer.getContainer().freeSlot() != 0) {
                        itemId = viewer.getContainer().get(slot).getId();
                    }
                    switch (opcode) {
                        case 155:
                            viewer.getContainer().takeItem(slot, 1);
                            break;
                        case 196:
                            viewer.getContainer().takeItem(slot, 5);
                            break;
                        case 124:
                            viewer.getContainer().takeItem(slot, 10);
                            break;
                        case 199:
                            int ammount = viewer.getContainer().getAmount(new Item(itemId));
                            viewer.getContainer().takeItem(slot, ammount);
                            break;
                        case 234:
                            sendInputDialogue(player, true, "Enter the amount:", (value) -> {
                                viewer.getContainer().takeItem(slot, (int) value);
                                return Unit.INSTANCE;
                            });
                            break;
                    }
                    break;
            }
            return true;
        }

    }

}
