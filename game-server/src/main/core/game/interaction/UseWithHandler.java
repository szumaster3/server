package core.game.interaction;

import core.cache.def.impl.SceneryDefinition;
import core.game.event.UseWithEvent;
import core.game.node.Node;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.plugin.Plugin;
import core.tools.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.api.ContentAPIKt.log;

/**
 * Abstract base class for handling "use item with" interactions in the game.
 */
public abstract class UseWithHandler implements Plugin<Object> {

    /**
     * Represents an item node type.
     */
    public static final int ITEM_TYPE = 0;

    /**
     * Represents an NPC node type.
     */
    public static final int NPC_TYPE = 1;

    /**
     * Represents an object/scenery node type.
     */
    public static final int OBJECT_TYPE = 2;

    /**
     * Represents a player node type.
     */
    public static final int PLAYER_TYPE = 3;

    /**
     * Mapping of combined node type and id to a list of applicable handlers.
     */
    private static final Map<Integer, List<UseWithHandler>> HANDLERS = new HashMap<>();

    /**
     * The node ids this handler supports (if not dynamic).
     */
    private int[] allowedNodes;

    /**
     * Creates a handler for specific node ids.
     *
     * @param allowedNodes The node ids this handler applies to.
     */
    public UseWithHandler(int... allowedNodes) {
        this.allowedNodes = allowedNodes;
    }

    /**
     * Creates a handler for specific node ids (using a list).
     *
     * @param allowedNodes The node ids this handler applies to.
     */
    public UseWithHandler(ArrayList<Integer> allowedNodes) {
        this.allowedNodes = allowedNodes.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Updates the allowed node list for this handler.
     *
     * @param allowedNodes List of new allowed node ids.
     */
    public void setAllowedNodes(ArrayList<Integer> allowedNodes) {
        this.allowedNodes = allowedNodes.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Registers a new handler for a specific node id and type.
     *
     * @param id      The node id (item, object, etc.).
     * @param type    The type of node (ITEM_TYPE, NPC_TYPE, etc.).
     * @param handler The handler to register.
     */
    public static void addHandler(int id, int type, UseWithHandler handler) {
        int key = id | type << 16;
        List<UseWithHandler> handlers = HANDLERS.get(key);
        if (handlers == null) {
            HANDLERS.put(key, handlers = new ArrayList<>(20));
        }
        if (type == PLAYER_TYPE) {
            if (handler.allowedNodes == null) {
                handler.allowedNodes = new int[]{id};
            } else {
                int[] array = handler.allowedNodes;
                handler.allowedNodes = new int[handler.allowedNodes.length + 1];
                System.arraycopy(array, 0, handler.allowedNodes, 0, array.length);
                handler.allowedNodes[handler.allowedNodes.length - 1] = id;
            }
        }
        handlers.add(handler);
    }

    /**
     * Executes a "use with" interaction based on the event.
     *
     * @param event The interaction event.
     */
    public static void run(final NodeUsageEvent event) {
        try {
            if (event.getPlayer() != null) {
                event.getPlayer().getInterfaceManager().close();
            }
            Node n = event.getUsedWith();
            List<UseWithHandler> handler = null;

            if (n instanceof Item) {
                handler = HANDLERS.get(((Item) event.getUsed()).getId());
                if (handler == null) {
                    handler = HANDLERS.get(((Item) event.getUsedWith()).getId());
                }
            } else if (n instanceof NPC) {
                handler = HANDLERS.get(((NPC) n).getId() | NPC_TYPE << 16);
            } else if (n instanceof Scenery) {
                handler = HANDLERS.get(((Scenery) n).getId() | OBJECT_TYPE << 16);
            } else if (n instanceof Player) {
                handler = HANDLERS.get(((Item) event.getUsed()).getId() | PLAYER_TYPE << 16);
            } else {
                handler = HANDLERS.get(((NPC) n).getId() | NPC_TYPE << 16);
            }

            if (handler == null) {
                if (n instanceof Item && !(event.getUsed() instanceof Player)) {
                    event.getPlayer().getPulseManager().runUnhandledAction(event.getPlayer(), PulseType.STANDARD);
                } else {
                    event.getPlayer().getPulseManager().run(new MovementPulse(event.getPlayer(), event.getUsedWith()) {
                        @Override
                        public boolean pulse() {
                            Player player = event.getPlayer();
                            player.debug("Unhandled use with interaction: ");
                                    player.debug("item used =" + event.getUsed().getName()  + " [" + event.getUsed().getId() + "]");
                            player.debug("with =" + event.getUsedWith().getName()   + " [" + event.getUsedWith().getId() + "]");
                            player.getPacketDispatch().sendMessage("Nothing interesting happens.");
                            return true;
                        }
                    }, PulseType.STANDARD);
                }
                return;
            }

            final List<UseWithHandler> handlers = handler;
            if (n instanceof Item && !(event.getUsed() instanceof Player)) {
                event.getPlayer().getPulseManager().run(new Pulse(1, event.getPlayer(), event.getUsed(), event.getUsedWith()) {
                    @Override
                    public boolean pulse() {
                        event.getPlayer().dispatch(new UseWithEvent(event.getUsed().getId(), event.getUsedWith().getId()));
                        boolean handled = false;
                        if (event.getPlayer() != null) {
                            event.getPlayer().getInterfaceManager().close();
                        }
                        for (UseWithHandler h : handlers) {
                            if (!h.nodeAllowed(((Item) event.getUsedWith()).getId()) && !h.nodeAllowed(event.getUsedItem().getId()) || !h.handle(event)) {
                                continue;
                            }
                            event.getPlayer().debug("Handler=" + h.getClass().getSimpleName() +
                                    ", used item=" + event.getUsedItem().getName() + " [" + event.getUsedItem().getId() + "]" +
                                    ", used with=" + event.getUsedWith().getName() + " [" + event.getUsedWith().getId() + "]");                            handled = true;
                            break;
                        }
                        if (!handled) {
                            event.getPlayer().debug("Handler=none, used item=" + event.getUsedItem());
                            event.getPlayer().debug("used with=" + event.getUsedWith());
                            event.getPlayer().getPacketDispatch().sendMessage("Nothing interesting happens.");
                        }
                        return true;
                    }
                }, PulseType.STANDARD);
                return;
            }

            event.getPlayer().getPulseManager().run(new MovementPulse(event.getPlayer(), event.getUsedWith(), handler.get(0)) {
                @Override
                public boolean pulse() {
                    event.getPlayer().dispatch(new UseWithEvent(event.getUsed().getId(), event.getUsedWith().getId()));
                    event.getPlayer().faceLocation(event.getUsedWith().getFaceLocation(event.getPlayer().getLocation()));
                    boolean handled = false;
                    Item used = (Item) event.getUsed();
                    for (UseWithHandler h : handlers) {
                        if ((used != null && !h.nodeAllowed(used.getId())) || !h.handle(event)) {
                            continue;
                        }
                        event.getPlayer().debug("Handler=" + h.getClass().getSimpleName() + ", used item=" + event.getUsedItem() + ", used with=" + event.getUsedWith());
                        handled = true;
                        break;
                    }
                    if (!handled) {
                        event.getPlayer().debug("Handler=none, used item=" + event.getUsedItem() + ", used with=" + event.getUsedWith());
                        event.getPlayer().getPacketDispatch().sendMessage("Nothing interesting happens.");
                    }
                    return true;
                }
            }, PulseType.STANDARD);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.lang.Object fireEvent(String identifier, java.lang.Object... args) {
        return null;
    }

    /**
     * Returns a list of all child object ids linked to a wrapper object, based on its config.
     *
     * @param wrapper The wrapper object id.
     * @return Array of valid child object ids.
     */
    public int[] getValidChildren(int wrapper) {
        final SceneryDefinition definition = SceneryDefinition.forId(wrapper);
        final List<Integer> list = new ArrayList<>(20);
        if (definition.childrenIds == null) {
            log(this.getClass(), Log.ERR, "Null child wrapper in option handler wrapperId=" + wrapper);
            return new int[]{wrapper};
        }
        for (int child : definition.childrenIds) {
            if (child != -1 && !list.contains(child)) {
                list.add(child);
            }
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Determines the destination a player should move to before executing the action.
     *
     * @param player The player.
     * @param with   The target node.
     * @return The destination location, or null if no movement required.
     */
    public Location getDestination(Player player, Node with) {
        return null;
    }

    /**
     * Checks whether a given node id is allowed for this handler.
     *
     * @param nodeId The node id.
     * @return True if allowed or if handler is dynamic; false otherwise.
     */
    public boolean nodeAllowed(int nodeId) {
        if (isDynamic()) {
            return true;
        }
        for (int id : allowedNodes) {
            if (nodeId == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the actual interaction logic.
     *
     * @param event The interaction event.
     * @return True if the interaction was handled; false otherwise.
     */
    public abstract boolean handle(NodeUsageEvent event);

    /**
     * Determines if this handler dynamically accepts any node id.
     *
     * @return True if dynamic; false if node list restricted.
     */
    public boolean isDynamic() {
        return false;
    }
}