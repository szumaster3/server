package core.game.node.scenery;

import core.game.world.map.Location;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SceneryManager {

    private static final Map<Integer, Scenery> sceneryMap = new ConcurrentHashMap<>();
    private static final Map<Location, Scenery> tileMap = new ConcurrentHashMap<>();

    private SceneryManager() {
    }

    /**
     * Registers a scenery.
     */
    public static void register(Scenery scenery) {
        if (scenery != null) {
            sceneryMap.put(scenery.getId(), scenery);
        }
    }

    /**
     * Unregisters a scenery.
     */
    public static void unregister(Scenery scenery) {
        if (scenery != null) {
            sceneryMap.remove(scenery.getId());
        }
    }

    /**
     * Gets a scenery by id.
     *
     * @return the scenery or null if not found.
     */
    public static Scenery getById(int id) {
        return sceneryMap.get(id);
    }

    /**
     * Checks if a scenery with the given id exists.
     */
    public static boolean contains(int id) {
        return sceneryMap.containsKey(id);
    }

    /**
     * Registers a scenery on a specific tile.
     */
    public static void registerTile(Scenery scenery, Location loc) {
        if (scenery != null && loc != null) {
            tileMap.put(loc, scenery);
        }
    }

    /**
     * Unregisters a scenery from a specific tile.
     */
    public static void unregisterTile(Scenery scenery, Location loc) {
        if (scenery != null && loc != null) {
            Scenery current = tileMap.get(loc);
            if (current != null && current.equals(scenery)) {
                tileMap.remove(loc);
            }
        }
    }

    public static void register(Scenery scenery, Location loc) {
        if (scenery != null && loc != null) {
            sceneryMap.put(scenery.getId(), scenery);
            tileMap.put(loc, scenery);
        }
    }

    public static void unregister(Scenery scenery, Location loc) {
        if (scenery != null && loc != null) {
            sceneryMap.remove(scenery.getId());
            Scenery current = tileMap.get(loc);
            if (current != null && current.equals(scenery)) {
                tileMap.remove(loc);
            }
        }
    }
}
