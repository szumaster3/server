package core.game.world.map.build;

import core.cache.def.impl.SceneryDefinition;
import core.cache.misc.buffer.ByteBufferUtils;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.map.Region;
import core.game.world.map.RegionManager;
import core.game.world.map.RegionPlane;

import java.nio.ByteBuffer;

/**
 * The type Landscape parser.
 */
public final class LandscapeParser {

    /**
     * Parse.
     *
     * @param r            the r
     * @param mapscape     the mapscape
     * @param buffer       the buffer
     * @param storeObjects the store objects
     */
    public static void parse(Region r, byte[][][] mapscape, ByteBuffer buffer, boolean storeObjects) {
        int objectId = -1;
        for (; ; ) {
            int offset = ByteBufferUtils.getBigSmart(buffer);
            if (offset == 0) {
                break;
            }
            objectId += offset;
            int location = 0;
            for (; ; ) {
                offset = ByteBufferUtils.getSmart(buffer);
                if (offset == 0) {
                    break;
                }
                location += offset - 1;
                int y = location & 0x3f;
                int x = location >> 6 & 0x3f;
                int configuration = buffer.get() & 0xFF;
                int rotation = configuration & 0x3;
                int type = configuration >> 2;
                int z = location >> 12;
                r.setObjectCount(r.getObjectCount() + 1);
                if (x < 0 || y < 0 || x >= 64 || y >= 64) {

                } else {
                    if ((mapscape[1][x][y] & 0x2) == 2) {
                        z--;
                    }
                    if (z >= 0 && z <= 3) {
                        Scenery scenery = new Scenery(objectId, Location.create((r.getX() << 6) + x, (r.getY() << 6) + y, z), type, rotation);
                        flagScenery(r.getPlanes()[z], x, y, scenery, true, storeObjects);
                    }
                }
            }
        }
    }

    /**
     * Add scenery.
     *
     * @param scenery the scenery
     */
    public static void addScenery(Scenery scenery) {
        addScenery(scenery, false);
    }

    /**
     * Add scenery.
     *
     * @param scenery   the scenery
     * @param landscape the landscape
     */
    public static void addScenery(Scenery scenery, boolean landscape) {
        Location l = scenery.getLocation();
        flagScenery(RegionManager.getRegionPlane(l), l.getLocalX(), l.getLocalY(), scenery, landscape, false);
    }

    /**
     * Flag scenery.
     *
     * @param plane        the plane
     * @param localX       the local x
     * @param localY       the local y
     * @param scenery      the scenery
     * @param landscape    the landscape
     * @param storeObjects the store objects
     */
    public static void flagScenery(RegionPlane plane, int localX, int localY, Scenery scenery, boolean landscape, boolean storeObjects) {
        Region.load(plane.getRegion());
        SceneryDefinition def = scenery.getDefinition();
        scenery.setActive(true);
        boolean add = storeObjects || !landscape || def.getChildObject(null).hasActions();
        if (add) {
            addPlaneObject(plane, scenery, localX, localY, landscape, storeObjects);
        }

        if (!applyClippingFlagsFor(plane, localX, localY, scenery))
            return;

        if (!storeObjects && !add && (!def.getChildObject(null).getName().equals("null"))) {
            addPlaneObject(plane, scenery, localX, localY, landscape, false);
        }
    }

    /**
     * Apply clipping flags for boolean.
     *
     * @param plane   the plane
     * @param localX  the local x
     * @param localY  the local y
     * @param scenery the scenery
     * @return the boolean
     */
    public static boolean applyClippingFlagsFor(RegionPlane plane, int localX, int localY, Scenery scenery) {
        SceneryDefinition def = scenery.getDefinition();
        int sizeX;
        int sizeY;
        if (scenery.getRotation() % 2 == 0) {
            sizeX = def.sizeX;
            sizeY = def.sizeY;
        } else {
            sizeX = def.sizeY;
            sizeY = def.sizeX;
        }
        int type = scenery.getType();
        if (type == 22) { //Tile
            plane.getFlags().getLandscape()[localX][localY] = true;
            if (def.interactive != 0 || def.solid == 1 || def.isBlocksLand()) {
                if (def.solid == 1) {
                    plane.getFlags().flagTileObject(localX, localY);
                    if (def.isProjectileClipped()) {
                        plane.getProjectileFlags().flagTileObject(localX, localY);
                    }
                }
            }
        } else if (type >= 9) { //Default objects
            if (def.solid != 0) {
                plane.getFlags().flagSolidObject(localX, localY, sizeX, sizeY, def.isProjectileClipped());
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().flagSolidObject(localX, localY, sizeX, sizeY, def.isProjectileClipped());
                }
            }
        } else if (type >= 0 && type <= 3) { //Doors/walls
            if (def.solid != 0) {
                plane.getFlags().flagDoorObject(localX, localY, scenery.getRotation(), type, def.isProjectileClipped());
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().flagDoorObject(localX, localY, scenery.getRotation(), type, def.isProjectileClipped());
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private static void addPlaneObject(RegionPlane plane, Scenery scenery, int localX, int localY, boolean landscape, boolean storeAll) {
        if (landscape && !storeAll) {
            Scenery current = plane.getObjects()[localX][localY];
            if (current != null && current.getDefinition().getChildObject(null).hasOptions(!scenery.getDefinition().getChildObject(null).hasOptions(false))) {
                return;
            }
        }
        plane.add(scenery, localX, localY, landscape && !storeAll);
    }

    /**
     * Remove scenery scenery.
     *
     * @param scenery the scenery
     * @return the scenery
     */
    public static Scenery removeScenery(Scenery scenery) {
        if (!scenery.isRenderable()) {
            return null;
        }
        RegionPlane plane = RegionManager.getRegionPlane(scenery.getLocation());
        Region.load(plane.getRegion());
        int localX = scenery.getLocation().getLocalX();
        int localY = scenery.getLocation().getLocalY();
        Scenery current = plane.getChunkObject(localX, localY, scenery.getId());
        if (current == null || current.getId() != scenery.getId()) {
            return null;
        }
        current.setActive(false);
        scenery.setActive(false);
        plane.remove(localX, localY, scenery.getId());
        SceneryDefinition def = scenery.getDefinition();
        int sizeX;
        int sizeY;
        if (scenery.getRotation() % 2 == 0) {
            sizeX = def.sizeX;
            sizeY = def.sizeY;
        } else {
            sizeX = def.sizeY;
            sizeY = def.sizeX;
        }
        int type = scenery.getType();
        if (type == 22) { //Tile
            if (def.interactive != 0 || def.solid == 1 || def.isBlocksLand()) {
                if (def.solid == 1) {
                    plane.getFlags().unflagTileObject(localX, localY);
                    if (def.isProjectileClipped()) {
                        plane.getProjectileFlags().unflagTileObject(localX, localY);
                    }
                }
            }
        } else if (type >= 9) { //Default objects
            if (def.solid != 0) {
                plane.getFlags().unflagSolidObject(localX, localY, sizeX, sizeY, def.isProjectileClipped());
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().unflagSolidObject(localX, localY, sizeX, sizeY, def.isProjectileClipped());
                }
            }
        } else if (type >= 0 && type <= 3) { //Doors/walls
            if (def.solid != 0) {
                plane.getFlags().unflagDoorObject(localX, localY, scenery.getRotation(), type, def.isProjectileClipped());
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().unflagDoorObject(localX, localY, scenery.getRotation(), type, def.isProjectileClipped());
                }
            }
        }
        return current;
    }
}
