package core.game.node.item;

import core.game.bots.AIRepository;
import core.game.node.entity.player.Player;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.chunk.ItemUpdateFlag;
import core.net.packet.PacketRepository;
import core.net.packet.context.BuildItemContext;
import core.net.packet.out.UpdateGroundItemAmount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages the creation, destruction, and tracking of ground items within the game world.
 * This class is responsible for adding items to the world, removing them, updating their status,
 * and handling interactions with players.
 */
public final class GroundItemManager {

    private static final ArrayList<GroundItem> GROUND_ITEMS = new ArrayList<>(20);
    private static final HashMap<String, GroundItem> ITEM_LOOKUP_MAP = new HashMap<>();

    /**
     * Creates a ground item at the given location with the specified item.
     *
     * @param item     The item to be created as a ground item.
     * @param location The location where the ground item will be placed.
     * @return The created ground item.
     */
    public static GroundItem create(Item item, Location location) {
        return create(new GroundItem(item, location, null));
    }

    /**
     * Creates a ground item at the given location, associated with a specific player and respawn ticks.
     *
     * @param item      The item to be created as a ground item.
     * @param location  The location where the ground item will be placed.
     * @param playerUid The unique identifier for the player associated with the ground item.
     * @param ticks     The number of ticks until the ground item respawns.
     * @return The created ground item.
     */
    public static GroundItem create(Item item, Location location, int playerUid, int ticks) {
        return create(new GroundItem(item, location, playerUid, ticks));
    }

    /**
     * Creates a ground item at the player's location.
     *
     * @param item   The item to be created as a ground item.
     * @param player The player who owns the ground item.
     * @return The created ground item.
     */
    public static GroundItem create(Item item, final Player player) {
        return create(new GroundItem(item, player.getLocation(), player));
    }

    /**
     * Creates a ground item at a specific location with an associated player.
     *
     * @param item     The item to be created as a ground item.
     * @param location The location where the ground item will be placed.
     * @param player   The player who owns the ground item.
     * @return The created ground item.
     */
    public static GroundItem create(Item item, Location location, Player player) {
        return create(new GroundItem(item, location, player));
    }

    /**
     * Creates multiple ground items at a specified location for a specific player.
     *
     * @param items    The array of items to be created as ground items.
     * @param location The location where the ground items will be placed.
     * @param player   The player who owns the ground items.
     */
    public static void create(Item[] items, Location location, Player player) {
        for (Item item : items) {
            create(new GroundItem(item, location, player));
        }
    }

    /**
     * Creates a ground item from the given instance.
     *
     * @param item The ground item to be created.
     * @return The created ground item.
     */
    public static GroundItem create(GroundItem item) {
        if (!item.getDefinition().isTradeable()) {
            item.setRemainPrivate(true);
        }
        if (item.getDropper() != null && item.hasItemPlugin()) {
            item.getPlugin().remove(item.getDropper(), item, ItemPlugin.DROP);
        }
        item.setRemoved(false);
        RegionManager.getRegionPlane(item.getLocation()).add(item);
        if (GROUND_ITEMS.add(item)) {
            ITEM_LOOKUP_MAP.put(generateKey(item), item);  // Cache by itemId + location
            return item;
        }
        return null;
    }

    /**
     * Destroys the given ground item.
     *
     * @param item The ground item to be destroyed.
     * @return The destroyed ground item, or null if the item doesn't exist.
     */
    public static GroundItem destroy(GroundItem item) {
        if (item == null) {
            return null;
        }
        GROUND_ITEMS.remove(item);
        ITEM_LOOKUP_MAP.remove(generateKey(item));  // Remove from lookup map
        RegionManager.getRegionPlane(item.getLocation()).remove(item);
        if (item.isAutoSpawn()) {
            item.respawn();
        }
        return item;
    }

    /**
     * Retrieves a ground item by its item ID and location for a specific player.
     *
     * @param itemId   The ID of the item.
     * @param location The location of the ground item.
     * @param player   The player who may interact with the ground item.
     * @return The ground item found, or null if no matching item exists.
     */
    public static GroundItem get(int itemId, Location location, Player player) {
        return ITEM_LOOKUP_MAP.get(generateKey(itemId, location));
    }

    /**
     * Increases the amount of a specific ground item at a given location for a player.
     * If the item doesn't exist, it creates a new one.
     *
     * @param item     The item to increase.
     * @param location The location of the ground item.
     * @param p        The player interacting with the item.
     * @return The updated ground item.
     */
    public static GroundItem increase(Item item, Location location, Player p) {
        GroundItem g = get(item.getId(), location, p);
        if (g == null || !g.droppedBy(p) || !g.isPrivate() || g.isRemoved()) {
            return create(item, location, p);
        }
        int oldAmount = g.getAmount();
        g.setAmount(oldAmount + item.getAmount());
        PacketRepository.send(UpdateGroundItemAmount.class, new BuildItemContext(p, g, oldAmount));
        return g;
    }

    /**
     * Updates the status of all ground items. This includes handling respawns, removals, and private status checks.
     */
    public static void pulse() {
        for (int i = 0; i < GROUND_ITEMS.size(); i++) {
            GroundItem item = GROUND_ITEMS.get(i);
            if (item.isAutoSpawn()) {
                continue;
            }
            if (!item.isActive()) {
                GROUND_ITEMS.remove(i);
                ITEM_LOOKUP_MAP.remove(generateKey(item));
                if (item.getDropper() != null) {
                    if (item.getDropper().isArtificial()) {
                        ArrayList<GroundItem> val = AIRepository.getItems(item.getDropper());
                        if (val != null)
                            val.remove(item);
                    }
                }
                if (!item.isRemoved()) {
                    RegionManager.getRegionPlane(item.getLocation()).remove(item);
                }
            } else if (!item.isRemainPrivate() && item.getDecayTime() - GameWorld.getTicks() == 100) {
                RegionManager.getRegionChunk(item.getLocation()).flag(new ItemUpdateFlag(item, ItemUpdateFlag.CONSTRUCT_TYPE));
            }
        }
    }

    /**
     * Retrieves all ground items currently in the world.
     *
     * @return A list of all ground items.
     */
    public static List<GroundItem> getItems() {
        return GROUND_ITEMS;
    }

    /**
     * Generates a unique key for a ground item based on its item ID and location.
     *
     * @param item The ground item to generate a key for.
     * @return The generated unique key.
     */
    private static String generateKey(GroundItem item) {
        return generateKey(item.getId(), item.getLocation());
    }

    /**
     * Generates a unique key based on the item ID and location.
     *
     * @param itemId The item ID.
     * @param location The location of the item.
     * @return The generated unique key.
     */
    private static String generateKey(int itemId, Location location) {
        return itemId + ":" + location.toString();
    }
}
