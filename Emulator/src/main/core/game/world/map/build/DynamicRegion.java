package core.game.world.map.build;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.music.MusicZone;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.scenery.Scenery;
import core.game.world.map.*;
import core.game.world.map.zone.RegionZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.impl.MultiwayCombatZone;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The type Dynamic region.
 */
public final class DynamicRegion extends Region {

    private static final List<ZoneBorders> RESERVED_AREAS = new ArrayList<>(20);

    private final int regionId;

    private final RegionChunk[][][] chunks;

    private ZoneBorders borders;

    private boolean multicombat;

    private boolean permanent;

    private DynamicRegion[] linked;

    private DynamicRegion parentRegion;

    /**
     * The Np cs.
     */
    public ArrayList<NPC> NPCs = new ArrayList<>(10);

    /**
     * Instantiates a new Dynamic region.
     *
     * @param regionId the region id
     * @param x        the x
     * @param y        the y
     */
    public DynamicRegion(int regionId, int x, int y) {
        super(x, y);
        this.regionId = regionId;
        this.chunks = new RegionChunk[4][SIZE >> 3][SIZE >> 3];
        RegionManager.resetFlags(getId());
    }

    /**
     * Instantiates a new Dynamic region.
     *
     * @param borders the borders
     */
    public DynamicRegion(@NotNull ZoneBorders borders) {
        this(-1, borders.getSouthWestX() >> 6, borders.getSouthWestY() >> 6);
        setBorders(borders);
        setUpdateAllPlanes(true);
        RegionManager.addRegion(getId(), this);
    }

    /**
     * Create dynamic region.
     *
     * @param regionId the region id
     * @return the dynamic region
     */
    public static DynamicRegion create(int regionId) {
        int x = (regionId >> 8) << 6;
        int y = (regionId & 0xFF) << 6;
        return create(new ZoneBorders(x, y, x + SIZE, y + SIZE))[0];
    }

    /**
     * Create dynamic region.
     *
     * @param regionOne the region one
     * @param regionTwo the region two
     * @return the dynamic region
     */
    public static DynamicRegion create(int regionOne, int regionTwo) {
        int x = (regionOne >> 8) << 6;
        int y = (regionOne & 0xFF) << 6;
        int x1 = (regionTwo >> 8) << 6;
        int y1 = (regionTwo & 0xFF) << 6;
        return create(new ZoneBorders(x, y, x1 + SIZE, y1 + SIZE))[0];
    }

    /**
     * Create dynamic region [ ].
     *
     * @param copy the copy
     * @return the dynamic region [ ]
     */
    public static DynamicRegion[] create(ZoneBorders copy) {
        int baseX = copy.getSouthWestX() >> 6;
        int baseY = copy.getSouthWestY() >> 6;

        ZoneBorders border = findZoneBorders((copy.getNorthEastX() - copy.getSouthWestX()) >> 3, (copy.getNorthEastY() - copy.getSouthWestY()) >> 3);
        RESERVED_AREAS.add(border);
        Location l = Location.create(border.getSouthWestX(), border.getSouthWestY(), 0);
        List<DynamicRegion> regions = new ArrayList<>(20);
        for (int x = copy.getSouthWestX() >> 6; x < copy.getNorthEastX() >> 6; x++) {
            for (int y = copy.getSouthWestY() >> 6; y < copy.getNorthEastY() >> 6; y++) {
                int regionId = x << 8 | y;
                Location loc = l.transform((x - baseX) << 6, (y - baseY) << 6, 0);
                DynamicRegion region = copy(regionId, loc);
                region.setBorders(border);
                Region r = RegionManager.forId(region.getId());
                if (r != null) {
                    for (int z = 0; z < 4; z++) {
                        region.getPlanes()[z].getPlayers().addAll(r.getPlanes()[z].getPlayers());
                        region.getPlanes()[z].getNpcs().addAll(r.getPlanes()[z].getNpcs());
                    }
                }
                RegionManager.addRegion(region.getId(), region);
                regions.add(region);
            }
        }
        for (Region r : regions) {
            for (int z = 0; z < 4; z++) {
                for (Player p : r.getPlanes()[z].getPlayers()) {
                    p.updateSceneGraph(false);
                }
            }
        }
        return regions.toArray(new DynamicRegion[regions.size()]);
    }

    /**
     * Reserve area zone borders.
     *
     * @param sizeX the size x
     * @param sizeY the size y
     * @return the zone borders
     */
    public static ZoneBorders reserveArea(int sizeX, int sizeY) {
        ZoneBorders borders = findZoneBorders(sizeX, sizeY);
        RESERVED_AREAS.add(borders);
        return borders;
    }

    /**
     * Find zone borders zone borders.
     *
     * @param sizeX the size x
     * @param sizeY the size y
     * @return the zone borders
     */
    public static ZoneBorders findZoneBorders(int sizeX, int sizeY) {
        int x = 0;
        int y = 0;
        int count = 0;
        int width = (sizeX >> 3) << 6;
        int height = (sizeY >> 3) << 6;
        if (width < 64) {
            width = 64;
        }
        if (height < 64) {
            height = 64;
        }
        while (true) {
            int endX = x + width;
            int endY = y + height;
            boolean reserved = false;
            for (ZoneBorders b : RESERVED_AREAS) {
                if (b.insideBorder(x, y) || b.insideBorder(endX, endY)
                    || b.insideBorder(x, endY) || b.insideBorder(endX, y)) {
                    reserved = true;
                    break;
                }
            }
            if (!reserved) {
                return new ZoneBorders(x, y, endX, endY);
            }
            if (++count % 15 == 0) {
                y += 64;
                x = 0;
            } else {
                x += 64;
            }
        }
    }

    /**
     * Copy dynamic region.
     *
     * @param regionId the region id
     * @param to       the to
     * @return the dynamic region
     */
    public static DynamicRegion copy(int regionId, Location to) {
        int regionX = ((regionId >> 8) & 0xFF) << 6;
        int regionY = (regionId & 0xFF) << 6;
        DynamicRegion region = new DynamicRegion(regionId, to.getRegionX() >> 3, to.getRegionY() >> 3);
        Region base = RegionManager.forId(regionId);
        Region.load(base);
        for (int offsetX = 0; offsetX < 8; offsetX++) {
            for (int offsetY = 0; offsetY < 8; offsetY++) {
                int x = regionX + (offsetX << 3);
                int y = regionY + (offsetY << 3);
                for (int plane = 0; plane < 4; plane++) {
                    RegionChunk c = base.getPlanes()[plane].getRegionChunk(offsetX, offsetY);
                    if (c == null) {
                        region.chunks[plane][offsetX][offsetY] = c = new RegionChunk(Location.create(0, 0, 0), 0, region.getPlanes()[plane]);
                    } else {
                        region.replaceChunk(plane, offsetX, offsetY, (c = c.copy(region.getPlanes()[plane])), base);
                    }
                    c.setRotation(0);
                    c.setBase(Location.create(x, y, plane));
                }
            }
        }
        return region;
    }

    /**
     * Link.
     *
     * @param regions the regions
     */
    public void link(DynamicRegion... regions) {
        for (DynamicRegion r : regions) {
            r.parentRegion = this;
        }
        this.linked = regions;
        flagActive();
    }

    /**
     * Toggle multicombat.
     */
    public void toggleMulticombat() {
        if (multicombat) {
            for (Iterator<RegionZone> it = getRegionZones().iterator(); it.hasNext(); ) {
                if (it.next().getZone() == MultiwayCombatZone.Companion.getInstance()) {
                    it.remove();
                }
            }
            multicombat = false;
            return;
        }
        getRegionZones().add(new RegionZone(MultiwayCombatZone.Companion.getInstance(), borders));
        multicombat = true;
    }

    /**
     * Sets music id.
     *
     * @param musicId the music id
     */
    public void setMusicId(int musicId) {
        getMusicZones().add(new MusicZone(musicId, borders));
    }

    /**
     * Rotate.
     */
    public void rotate() {
        for (int z = 0; z < 4; z++) {
            RegionChunk[][] c = Arrays.copyOf(chunks[z], 8);
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    RegionChunk r = chunks[z][x][y] = c[8 - y - 1][x];
                    if (r != null) {
                        r.setRotation(r.getRotation() + 1);
                    }
                }
            }
        }
    }

    /**
     * Sets chunk.
     *
     * @param z     the z
     * @param x     the x
     * @param y     the y
     * @param chunk the chunk
     */
    public void setChunk(int z, int x, int y, RegionChunk chunk) {
        chunks[z][x][y] = chunk;
        getPlanes()[z].getChunks()[x][y] = chunk;
        if (chunk != null) {
            chunk.setCurrentBase(getBaseLocation().transform(x << 3, y << 3, 0));
        }
    }

    /**
     * Replace chunk.
     *
     * @param z          the z
     * @param x          the x
     * @param y          the y
     * @param chunk      the chunk
     * @param fromRegion the from region
     */
    public void replaceChunk(int z, int x, int y, RegionChunk chunk, Region fromRegion) {
        Region.load(DynamicRegion.this);
        RegionPlane p = getPlanes()[z];
        chunks[z][x][y] = chunk;
        p.getChunks()[x][y] = chunk;
        if (chunk == null) {
            for (int i = x << 3; i < (x + 1) << 3; i++) {
                for (int j = y << 3; j < (y + 1) << 3; j++) {
                    p.getFlags().invalidateFlag(i, j);
                    p.getProjectileFlags().invalidateFlag(i, j);
                    Scenery scenery = p.getObjects()[i][j];
                    if (scenery != null) {
                        LandscapeParser.removeScenery(scenery);
                    } else {
                        p.add(null, i, j, true);
                    }
                }
            }
        } else {
            Region.load(fromRegion);
            Location l = chunk.getBase();
            RegionPlane rp = fromRegion.getPlanes()[l.getZ()];
            chunk.setCurrentBase(getBaseLocation().transform(x << 3, y << 3, z));
            chunk.rebuildFlags(rp);
        }
    }

    @Override
    public boolean flagInactive(boolean force) {
        if (!permanent) {
            if (parentRegion != null && parentRegion.isActive()) {
                parentRegion.checkInactive();
                return false;
            }
            if (linked != null) {
                for (DynamicRegion r : linked) {
                    if (!r.isInactive(false)) {
                        return false;
                    }
                }
            }
            if (!super.flagInactive(force)) {
                return false;
            }
            for (RegionPlane plane : getPlanes()) {
                for (int i = 0; i < plane.getNpcs().size(); i++) {
                    NPC npc = plane.getNpcs().get(0);
                    npc.clear();
                }
                for (RegionChunk[] chunks : getChunks()[plane.getPlane()]) {
                    for (RegionChunk chunk : chunks) {
                        if (chunk != null) {
                            for (Iterator<GroundItem> it = chunk.getItems().iterator(); it.hasNext(); ) {
                                GroundItemManager.getItems().remove(it.next());
                            }
                        }
                    }
                }
            }
            RESERVED_AREAS.remove(borders);
            if (multicombat) {
                toggleMulticombat();
            }
            boolean allLinkedInactive = true;
            if (linked != null) {
                for (DynamicRegion r : linked) {
                    allLinkedInactive &= r.flagInactive(force);
                }
            }
            return allLinkedInactive;
        } else {
            return true;
        }
    }

    @Override
    public int getRegionId() {
        return regionId;
    }

    /**
     * Get chunks region chunk [ ] [ ] [ ].
     *
     * @return the region chunk [ ] [ ] [ ]
     */
    public RegionChunk[][][] getChunks() {
        return chunks;
    }

    /**
     * Gets borders.
     *
     * @return the borders
     */
    public ZoneBorders getBorders() {
        return borders;
    }

    /**
     * Sets borders.
     *
     * @param borders the borders
     */
    public void setBorders(ZoneBorders borders) {
        this.borders = borders;
    }

    /**
     * Is multicombat boolean.
     *
     * @return the boolean
     */
    public boolean isMulticombat() {
        return multicombat;
    }

    /**
     * Sets multicombat.
     *
     * @param multicombat the multicombat
     */
    public void setMulticombat(boolean multicombat) {
        this.multicombat = multicombat;
    }

    /**
     * Is permanent boolean.
     *
     * @return the boolean
     */
    public boolean isPermanent() {
        return permanent;
    }

    /**
     * Sets permanent.
     *
     * @param permanent the permanent
     */
    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public DynamicRegion getParent() {
        return parentRegion;
    }

    /**
     * Clear.
     */
    public void clear() {
        for (NPC n : NPCs) {
            n.clear();
        }
        NPCs.clear();
    }
}
