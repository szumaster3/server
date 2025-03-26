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
 * The type Use with handler.
 */
public abstract class UseWithHandler implements Plugin<java.lang.Object> {

    /**
     * The constant ITEM_TYPE.
     */
    public static final int ITEM_TYPE = 0;

    /**
     * The constant NPC_TYPE.
     */
    public static final int NPC_TYPE = 1;

    /**
     * The constant OBJECT_TYPE.
     */
    public static final int OBJECT_TYPE = 2;

    /**
     * The constant PLAYER_TYPE.
     */
    public static final int PLAYER_TYPE = 3;

    private static final Map<Integer, List<UseWithHandler>> HANDLERS = new HashMap<>();

    private int[] allowedNodes;

    /**
     * Instantiates a new Use with handler.
     *
     * @param allowedNodes the allowed nodes
     */
    public UseWithHandler(int... allowedNodes) {
        this.allowedNodes = allowedNodes;
    }

    /**
     * Instantiates a new Use with handler.
     *
     * @param allowedNodes the allowed nodes
     */
    public UseWithHandler(ArrayList<Integer> allowedNodes) {
        this.allowedNodes = allowedNodes.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Sets allowed nodes.
     *
     * @param allowedNodes the allowed nodes
     */
    public void setAllowedNodes(ArrayList<Integer> allowedNodes) {
        this.allowedNodes = allowedNodes.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Add handler.
     *
     * @param id      the id
     * @param type    the type
     * @param handler the handler
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
     * Run.
     *
     * @param event the event
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
                            event.getPlayer().debug("Unhandled use with interaction: item used: " + event.getUsed() + " with: " + event.getUsedWith());
                            event.getPlayer().getPacketDispatch().sendMessage("Nothing interesting happens.");
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
                            event.getPlayer().debug("Handler=" + h.getClass().getSimpleName() + ", used item=" + event.getUsedItem() + ", used with=" + event.getUsedWith());
                            handled = true;
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

    @Override
    public java.lang.Object fireEvent(String identifier, java.lang.Object... args) {
        return null;
    }

    /**
     * Get valid children int.
     *
     * @param wrapper the wrapper
     * @return the int.
     */
    public int[] getValidChildren(int wrapper) {
        final SceneryDefinition definition = SceneryDefinition.forId(wrapper);
        final List<Integer> list = new ArrayList<>(20);
        if (definition.configObjectIds == null) {
            log(this.getClass(), Log.ERR, "Null child wrapper in option handler wrapperId=" + wrapper);
            return new int[]{wrapper};
        }
        for (int child : definition.configObjectIds) {
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
     * Gets destination.
     *
     * @param player the player
     * @param with   the with
     * @return the destination
     */
    public Location getDestination(Player player, Node with) {
        return null;
    }

    /**
     * Node allowed boolean.
     *
     * @param nodeId the node id
     * @return the boolean
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
     * Handle boolean.
     *
     * @param event the event
     * @return the boolean
     */
    public abstract boolean handle(NodeUsageEvent event);

    /**
     * Is dynamic boolean.
     *
     * @return the boolean
     */
    public boolean isDynamic() {
        return false;
    }
}
