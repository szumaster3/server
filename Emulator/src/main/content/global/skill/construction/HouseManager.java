package content.global.skill.construction;

import content.data.GameAttributes;
import content.global.skill.construction.servants.Servant;
import core.api.region.RegionSpecification;
import core.api.region.contracts.FillChunkContract;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.*;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.MinimapStateContext;
import core.net.packet.out.MinimapState;
import core.tools.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rs.consts.Components;
import org.rs.consts.Music;
import org.rs.consts.NPCs;

import java.awt.*;
import java.nio.ByteBuffer;

import static core.api.ContentAPIKt.*;
import static core.api.region.RegionSpecificationKt.fillWith;
import static core.api.region.RegionSpecificationKt.using;
import static core.api.ui.InterfaceAPIKt.setMinimapState;
import static core.tools.GlobalsKt.DARK_BLUE;

/**
 * The type House manager.
 */
public final class HouseManager {

    private DynamicRegion houseRegion;

    private DynamicRegion dungeonRegion;

    private HouseLocation location = HouseLocation.NOWHERE;

    private HousingStyle style = HousingStyle.BASIC_WOOD;

    private HouseZone zone = new HouseZone(this);

    private final Room[][][] rooms = new Room[4][8][8];

    private boolean buildingMode;

    private boolean locked;

    private Servant servant;

    private boolean hasDungeon;

    private Crests crest = Crests.ASGARNIA;

    /**
     * Instantiates a new House manager.
     */
    public HouseManager() {

    }

    /**
     * Save.
     *
     * @param buffer the buffer
     */
    public void save(ByteBuffer buffer) {
        buffer.put((byte) location.ordinal());
        buffer.put((byte) style.ordinal());
        if (hasServant()) {
            servant.save(buffer);
        } else {
            buffer.put((byte) -1);
        }
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room room = rooms[z][x][y];
                    if (room != null) {
                        buffer.put((byte) z).put((byte) x).put((byte) y);
                        buffer.put((byte) room.getProperties().ordinal());
                        buffer.put((byte) room.getRotation().toInteger());
                        for (int i = 0; i < room.getHotspots().length; i++) {
                            if (room.getHotspots()[i].getDecorationIndex() > -1) {
                                buffer.put((byte) i);
                                buffer.put((byte) room.getHotspots()[i].getDecorationIndex());
                            }
                        }
                        buffer.put((byte) -1);
                    }
                }
            }
        }
        buffer.put((byte) -1);
    }

    /**
     * Parse.
     *
     * @param data the data
     */
    public void parse(JSONObject data) {
        location = HouseLocation.values()[Integer.parseInt(data.get("location").toString())];
        style = HousingStyle.values()[Integer.parseInt(data.get("style").toString())];
        java.lang.Object servRaw = data.get("servant");
        if (servRaw != null) {
            servant = Servant.parse((JSONObject) servRaw);
        }
        JSONArray rArray = (JSONArray) data.get("rooms");
        for (int i = 0; i < rArray.size(); i++) {
            JSONObject rm = (JSONObject) rArray.get(i);
            int z = Integer.parseInt(rm.get("z").toString());
            int x = Integer.parseInt(rm.get("x").toString());
            int y = Integer.parseInt(rm.get("y").toString());
            if (z == 3) hasDungeon = true;
            Room room = rooms[z][x][y] = new Room(RoomProperties.values()[Integer.parseInt(rm.get("properties").toString())]);
            room.configure(style);
            room.setRotation(Direction.get(Integer.parseInt(rm.get("rotation").toString())));
            JSONArray hotspots = (JSONArray) rm.get("hotspots");
            for (int j = 0; j < hotspots.size(); j++) {
                JSONObject spot = (JSONObject) hotspots.get(j);
                room.getHotspots()[Integer.parseInt(spot.get("hotspotIndex").toString())].setDecorationIndex(Integer.parseInt(spot.get("decorationIndex").toString()));
            }
        }
    }

    /**
     * Parse.
     *
     * @param buffer the buffer
     */
    public void parse(ByteBuffer buffer) {
        location = HouseLocation.values()[buffer.get() & 0xFF];
        style = HousingStyle.values()[buffer.get() & 0xFF];
        servant = Servant.parse(buffer);
        int z = 0;
        while ((z = buffer.get()) != -1) {
            if (z == 3) {
                hasDungeon = true;
            }
            int x = buffer.get();
            int y = buffer.get();
            Room room = rooms[z][x][y] = new Room(RoomProperties.values()[buffer.get() & 0xFF]);
            room.configure(style);
            room.setRotation(Direction.get(buffer.get() & 0xFF));
            int spot = 0;
            while ((spot = buffer.get()) != -1) {
                room.getHotspots()[spot].setDecorationIndex(buffer.get() & 0xFF);
            }
        }
    }

    /**
     * Pre enter.
     *
     * @param player       the player
     * @param buildingMode the building mode
     */
    public void preEnter(final Player player, boolean buildingMode) {
        if (this.buildingMode != buildingMode || !isLoaded()) {
            this.buildingMode = buildingMode;
            construct();
        }
        setAttribute(player, GameAttributes.POH_PORTAL, HouseManager.this);
        player.lock(1);
        if (player.isAdmin())
            player.sendMessage("House location: " + houseRegion.getBaseLocation() + ", entry: " + getEnterLocation());
    }

    /**
     * Enter.
     *
     * @param player       the player
     * @param buildingMode the building mode
     */
    public void enter(final Player player, boolean buildingMode) {
        player.lock();
        player.getLocks().lockTeleport(6);
        player.getLocks().lockMovement(6);
        player.getLocks().lockComponent(6);
        player.getInterfaceManager().closeSingleTab();
        preEnter(player, buildingMode);
        player.getProperties().setTeleportLocation(getEnterLocation());
        postEnter(player, buildingMode);
    }

    /**
     * Post enter.
     *
     * @param player       the player
     * @param buildingMode the building mode
     */
    public void postEnter(final Player player, boolean buildingMode) {
        openLoadInterface(player);
        checkForAndSpawnServant(player);
        updateVarbits(player, buildingMode);
        unlockMusicTrack(player);
    }

    private void openLoadInterface(Player player) {
        setMinimapState(player, 2);
        openInterface(player, Components.POH_HOUSE_LOADING_SCREEN_399);
        submitCloseLoadInterfacePulse(player);
    }

    private void submitCloseLoadInterfacePulse(Player player) {
        GameWorld.getPulser().submit(new Pulse(6, player) {
            @Override
            public boolean pulse() {
                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                player.getInterfaceManager().close();
                player.unlock();
                return true;
            }
        });
    }

    private void checkForAndSpawnServant(Player player) {
        if (!hasServant()) return;

        GameWorld.getPulser().submit(new Pulse(1, player) {
            @Override
            public boolean pulse() {
                spawnServant();
                if (servant.isGreet()) {
                    player.getDialogueInterpreter().sendDialogues(servant.getType().getNpcId(), servant.getType().getNpcId() == NPCs.DEMON_BUTLER_4243 ? FaceAnim.OLD_DEFAULT : FaceAnim.HALF_GUILTY, DARK_BLUE + "Welcome.");
                }
                return true;
            }
        });
    }

    private void updateVarbits(Player player, boolean build) {
        setVarp(player, 261, build ? 1 : 0);
        setVarp(player, 262, getRoomAmount());
    }

    private void unlockMusicTrack(Player player) {
        player.getMusicPlayer().unlock(Music.HOME_SWEET_HOME_454, true);
    }

    /**
     * Leave.
     *
     * @param player the player
     */
    public static void leave(Player player) {
        HouseManager house = player.getAttribute(GameAttributes.POH_PORTAL, player.getHouseManager());
        if (house.getHouseRegion() == null) {
            return;
        }
        player.lock(1);
        if (house.isInHouse(player)) {
            player.animate(Animation.RESET);
            player.getProperties().setTeleportLocation(house.location.getExitLocation());
        }
        if (player.getFamiliarManager().hasPet()) {
            player.getFamiliarManager().getFamiliar().unlock();
            player.getFamiliarManager().getFamiliar().call();
        }
    }

    /**
     * Toggle building mode.
     *
     * @param player the player
     * @param enable the enable
     */
    public void toggleBuildingMode(Player player, boolean enable) {

        if (!isInHouse(player)) {
            player.getPacketDispatch().sendMessage("Building mode really only helps if you're in a house.");
            return;
        }
        if (buildingMode != enable) {
            if (enable) {
                expelGuests(player);
            }
            enter(player, enable);
            if (player.isAdmin())
                player.getPacketDispatch().sendMessage("Building mode is now " + (buildingMode ? "on." : "off."));
        }
    }

    /**
     * Reload.
     *
     * @param player       the player
     * @param buildingMode the building mode
     */
    public void reload(Player player, boolean buildingMode) {
        int diffX = player.getLocation().getLocalX();
        int diffY = player.getLocation().getLocalY();
        int diffZ = player.getLocation().getZ();
        boolean inDungeon = player.getViewport().getRegion() == dungeonRegion;
        this.buildingMode = buildingMode;
        construct();
        Location newLoc = (dungeonRegion == null ? houseRegion : (inDungeon ? dungeonRegion : houseRegion)).getBaseLocation().transform(diffX, diffY, diffZ);
        player.getProperties().setTeleportLocation(newLoc);
    }

    /**
     * Expel guests.
     *
     * @param player the player
     */
    public void expelGuests(Player player) {
        if (isLoaded()) {
            for (RegionPlane plane : houseRegion.getPlanes()) {
                for (Player p : plane.getPlayers()) {
                    if (p != player) {
                        leave(p);
                    }
                }
            }
            if (dungeonRegion != null) {
                for (RegionPlane plane : dungeonRegion.getPlanes()) {
                    for (Player p : plane.getPlayers()) {
                        if (p != player) {
                            leave(p);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets enter location.
     *
     * @return the enter location
     */
    public Location getEnterLocation() {
        if (houseRegion == null) {
            log(this.getClass(), Log.ERR, "House wasn't constructed yet!");
            return null;
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Room room = rooms[0][x][y];
                if (room != null && (room.getProperties() == RoomProperties.GARDEN || room.getProperties() == RoomProperties.FORMAL_GARDEN)) {
                    for (Hotspot h : room.getHotspots()) {
                        if (h.getDecorationIndex() > -1) {
                            Decoration d = h.getHotspot().getDecorations()[h.getDecorationIndex()];
                            if (d == Decoration.PORTAL) {
                                return houseRegion.getBaseLocation().transform(x * 8 + h.getChunkX(), y * 8 + h.getChunkY() + 2, 0);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Redecorate.
     *
     * @param style the style
     */
    public void redecorate(HousingStyle style) {
        this.style = style;
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room room = rooms[z][x][y];
                    if (room != null) {
                        room.decorate(style);
                    }
                }
            }
        }
    }

    /**
     * Clear rooms.
     */
    @Deprecated
    public void clearRooms() {
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    rooms[z][x][y] = null;
                }
            }
        }
    }

    /**
     * Create new house at.
     *
     * @param location the location
     */
    public void createNewHouseAt(HouseLocation location) {
        clearRooms();
        Room room = rooms[0][4][3] = new Room(RoomProperties.GARDEN);
        room.configure(style);
        room.getHotspots()[0].setDecorationIndex(0);
        this.location = location;
    }

    /**
     * Construct dynamic region.
     *
     * @return the dynamic region
     */
    public DynamicRegion construct() {
        houseRegion = getPreparedRegion();
        configureRoofs();
        prepareHouseChunks(style, houseRegion, buildingMode, rooms);
        houseRegion.flagActive();

        if (hasDungeon()) {
            dungeonRegion = getPreparedRegion();
            prepareDungeonChunks(style, dungeonRegion, houseRegion, buildingMode, rooms[3]);
            dungeonRegion.flagActive();
        }

        ZoneBuilder.configure(zone);
        return houseRegion;
    }

    private DynamicRegion getPreparedRegion() {
        ZoneBorders borders = DynamicRegion.reserveArea(8, 8);
        DynamicRegion region = new DynamicRegion(-1, borders.getSouthWestX() >> 6, borders.getSouthWestY() >> 6);
        region.setBorders(borders);
        region.setUpdateAllPlanes(true);
        region.setBuild(true);
        RegionManager.addRegion(region.getId(), region);
        return region;
    }

    private class RoomLoadContract extends FillChunkContract {

        /**
         * The Rooms.
         */
        Room[][][] rooms;

        /**
         * The Manager.
         */
        HouseManager manager;

        /**
         * The Building mode.
         */
        boolean buildingMode;

        /**
         * Instantiates a new Room load contract.
         *
         * @param manager      the manager
         * @param buildingMode the building mode
         * @param rooms        the rooms
         */
        public RoomLoadContract(HouseManager manager, boolean buildingMode, Room[][][] rooms) {
            this.rooms = rooms;
            this.manager = manager;
            this.buildingMode = buildingMode;
        }

        @Override
        public BuildRegionChunk getChunk(int x, int y, int plane, @NotNull DynamicRegion dyn) {
            return rooms[plane][x][y].getChunk().copy(dyn.getPlanes()[plane]);
        }

        @Override
        public void afterSetting(@Nullable BuildRegionChunk chunk, int x, int y, int plane, @NotNull DynamicRegion dyn) {
            rooms[plane][x][y].loadDecorations(dyn != manager.dungeonRegion ? plane : 3, chunk, manager);
        }
    }

    private void prepareHouseChunks(HousingStyle style, DynamicRegion target, boolean buildingMode, Room[][][] rooms) {
        Region from = RegionManager.forId(style.getRegionId());
        Region.load(from, true);
        RegionChunk defaultChunk = from.getPlanes()[style.getPlane()].getRegionChunk(1, 0);
        RegionChunk defaultSkyChunk = from.getPlanes()[1].getRegionChunk(0, 0);

        RoomLoadContract loadRooms = new RoomLoadContract(this, buildingMode, rooms);
        RegionSpecification spec = new RegionSpecification(using(target), fillWith(defaultChunk).from(from).onPlanes(0), fillWith(defaultSkyChunk).from(from).onPlanes(1, 2, 3), loadRooms.from(from).onPlanes(0, 1, 2).onCondition((destX, destY, plane) -> rooms[plane][destX][destY] != null));

        spec.build();
    }

    private void prepareDungeonChunks(HousingStyle style, DynamicRegion target, DynamicRegion house, boolean buildingMode, Room[][] rooms) {
        Region from = RegionManager.forId(style.getRegionId());
        Region.load(from, true);
        RegionChunk defaultChunk = from.getPlanes()[style.getPlane()].getRegionChunk(3, 0);

        RoomLoadContract loadRooms = new RoomLoadContract(this, buildingMode, new Room[][][]{rooms});
        RegionSpecification spec = new RegionSpecification(using(target), fillWith((x, y, plane, region) -> buildingMode ? null : defaultChunk).from(from).onPlanes(0).onCondition((destX, destY, plane) -> rooms[destX][destY] == null), loadRooms.from(from).onPlanes(0).onCondition((destX, destY, plane) -> rooms[destX][destY] != null));

        spec.build();
        house.link(target);
    }

    /**
     * Configure roofs.
     */
    public void configureRoofs() {

    }

    /**
     * Gets room.
     *
     * @param l the l
     * @return the room
     */
    public Room getRoom(Location l) {
        int z = l.getZ();
        if (dungeonRegion != null && l.getRegionId() == dungeonRegion.getId()) {
            z = 3;
        }
        return rooms[z][l.getChunkX()][l.getChunkY()];
    }

    /**
     * Gets hotspot.
     *
     * @param scenery the scenery
     * @return the hotspot
     */
    public Hotspot getHotspot(Scenery scenery) {
        Room room = getRoom(scenery.getLocation());
        if (room == null) {
            return null;
        }
        int chunkX = scenery.getLocation().getChunkOffsetX();
        int chunkY = scenery.getLocation().getChunkOffsetY();
        switch (room.getRotation()) {
            case WEST: {
                int tempChunk = chunkY;
                chunkY = 7 - chunkX;
                chunkX = tempChunk;
                break;
            }
            case EAST: {

                int tempChunk = chunkX;
                chunkX = 7 - chunkY;
                chunkY = tempChunk;
                break;
            }
            case SOUTH: {
                chunkX = 7 - chunkX;
                chunkY = 7 - chunkY;
                break;
            }

            default: {

            }
        }
        for (Hotspot h : room.getHotspots()) {
            if ((h.getChunkX() == chunkX || h.getChunkX2() == chunkX) && (h.getChunkY() == chunkY || h.getChunkY2() == chunkY) && h.getHotspot().getObjectId(style) == scenery.getId()) {
                return h;
            }
        }
        return null;
    }

    /**
     * Has room at boolean.
     *
     * @param z     the z
     * @param roomX the room x
     * @param roomY the room y
     * @return the boolean
     */
    public boolean hasRoomAt(int z, int roomX, int roomY) {
        Room room = rooms[z][roomX][roomY];
        return room != null && !room.getProperties().isRoof();
    }

    /**
     * Enter dungeon.
     *
     * @param player the player
     */
    public void enterDungeon(Player player) {
        if (!hasDungeon()) {
            return;
        }
        int diffX = player.getLocation().getLocalX();
        int diffY = player.getLocation().getLocalY();
        player.getProperties().setTeleportLocation(dungeonRegion.getBaseLocation().transform(diffX, diffY, 0));
    }

    /**
     * Has exit boolean.
     *
     * @param z         the z
     * @param roomX     the room x
     * @param roomY     the room y
     * @param direction the direction
     * @return the boolean
     */
    public boolean hasExit(int z, int roomX, int roomY, Direction direction) {
        Room room = rooms[z][roomX][roomY];
        int index = (direction.toInteger() + 3) % 4;
        return room != null && room.getExits()[index];
    }

    /**
     * Gets room amount.
     *
     * @return the room amount
     */
    public int getRoomAmount() {
        int count = 0;
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room r = rooms[z][x][y];
                    if (r != null && !r.getProperties().isRoof()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Gets portal amount.
     *
     * @return the portal amount
     */
    public int getPortalAmount() {
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Room room = rooms[0][x][y];
                if (room != null && (room.getProperties() == RoomProperties.GARDEN || room.getProperties() == RoomProperties.FORMAL_GARDEN) && room.getHotspots()[0].getDecorationIndex() == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets boundaries.
     *
     * @return the boundaries
     */
    public Rectangle getBoundaries() {
        int startX = 99;
        int startY = 99;
        int endX = 0;
        int endY = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (rooms[0][x][y] != null) {
                    if (x < startX) startX = x;
                    if (y < startY) startY = y;
                    if (x > endX) endX = x;
                    if (y > endY) endY = y;
                }
            }
        }
        return new Rectangle(startX, startY, (endX - startX) + 1, (endY - startY) + 1);
    }

    /**
     * Gets maximum dimension.
     *
     * @param player the player
     * @return the maximum dimension
     */
    public int getMaximumDimension(Player player) {
        int level = player.getSkills().getStaticLevel(Skills.CONSTRUCTION);
        if (level >= 60) {
            return 7;
        }
        if (level >= 45) {
            return 6;
        }
        if (level >= 30) {
            return 5;
        }
        if (level >= 15) {
            return 4;
        }
        return 3;
    }

    /**
     * Gets maximum rooms.
     *
     * @param player the player
     * @return the maximum rooms
     */
    public int getMaximumRooms(Player player) {
        int level = player.getSkills().getStaticLevel(Skills.CONSTRUCTION);
        if (level >= 99) return 30;
        if (level >= 96) return 29;
        if (level >= 92) return 28;
        if (level >= 86) return 27;
        if (level >= 80) return 26;
        if (level >= 74) return 25;
        if (level >= 68) return 24;
        if (level >= 62) return 23;
        if (level >= 56) return 22;
        if (level >= 50) return 21;
        return 20;
    }

    private void spawnServant() {
        servant.setLocation(getEnterLocation());
        servant.setWalkRadius(getRoomAmount() * 2);
        servant.setWalks(true);
        servant.init();
    }

    /**
     * Has servant boolean.
     *
     * @return the boolean
     */
    public boolean hasServant() {
        return servant != null;
    }

    /**
     * Is in house boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isInHouse(Player player) {
        return isLoaded() && (player.getViewport().getRegion() == houseRegion || player.getViewport().getRegion() == dungeonRegion);
    }

    /**
     * Is in dungeon boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean isInDungeon(Player player) {
        return player.getViewport().getRegion() == player.getHouseManager().dungeonRegion;
    }

    /**
     * Is loaded boolean.
     *
     * @return the boolean
     */
    public boolean isLoaded() {
        return (houseRegion != null && houseRegion.isActive()) || (dungeonRegion != null && dungeonRegion.isActive());
    }

    /**
     * Has house boolean.
     *
     * @return the boolean
     */
    public boolean hasHouse() {
        return location != HouseLocation.NOWHERE;
    }

    /**
     * Has dungeon boolean.
     *
     * @return the boolean
     */
    public boolean hasDungeon() {
        return hasDungeon;
    }

    /**
     * Sets has dungeon.
     *
     * @param hasDungeon the has dungeon
     */
    public void setHasDungeon(boolean hasDungeon) {
        this.hasDungeon = hasDungeon;
    }

    /**
     * Get rooms room [ ] [ ] [ ].
     *
     * @return the room [ ] [ ] [ ]
     */
    public Room[][][] getRooms() {
        return rooms;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public HouseLocation getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(HouseLocation location) {
        this.location = location;
    }

    /**
     * Is building mode boolean.
     *
     * @return the boolean
     */
    public boolean isBuildingMode() {
        return buildingMode;
    }

    /**
     * Is locked boolean.
     *
     * @return the boolean
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets locked.
     *
     * @param locked the locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Gets house region.
     *
     * @return the house region
     */
    public DynamicRegion getHouseRegion() {
        return houseRegion;
    }

    /**
     * Gets dungeon region.
     *
     * @return the dungeon region
     */
    public Region getDungeonRegion() {
        return dungeonRegion;
    }

    /**
     * Gets style.
     *
     * @return the style
     */
    public HousingStyle getStyle() {
        return style;
    }

    /**
     * Sets style.
     *
     * @param style the style
     */
    public void setStyle(HousingStyle style) {
        this.style = style;
    }

    /**
     * Gets servant.
     *
     * @return the servant
     */
    public Servant getServant() {
        return servant;
    }

    /**
     * Sets servant.
     *
     * @param servant the servant
     */
    public void setServant(Servant servant) {
        this.servant = servant;
    }

    /**
     * Gets crest.
     *
     * @return the crest
     */
    public Crests getCrest() {
        return crest;
    }

    /**
     * Sets crest.
     *
     * @param crest the crest
     */
    public void setCrest(Crests crest) {
        this.crest = crest;
    }

    /**
     * Gets zone.
     *
     * @return the zone
     */
    public HouseZone getZone() {
        return zone;
    }

}
