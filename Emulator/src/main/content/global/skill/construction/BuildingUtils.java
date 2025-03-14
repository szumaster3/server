package content.global.skill.construction;

import content.global.skill.construction.item.Nails;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.map.BuildRegionChunk;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.Region;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;
import core.tools.Log;
import org.jetbrains.annotations.NotNull;
import org.rs.consts.Items;

import java.util.ArrayList;
import java.util.Arrays;

import static core.api.ContentAPIKt.*;

/**
 * The type Building utils.
 */
public final class BuildingUtils {

    /**
     * The constant DIRECTIONS.
     */
    public static final Direction[] DIRECTIONS = {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };

    /**
     * The constant BUILD_LOW_ANIM.
     */
    public static final Animation BUILD_LOW_ANIM = Animation.create(3683);

    /**
     * The constant BUILD_MID_ANIM.
     */
    public static final Animation BUILD_MID_ANIM = Animation.create(3676);

    /**
     * The constant BUILD_HIGH_ANIM.
     */
    public static final Animation BUILD_HIGH_ANIM = Animation.create(3684);

    /**
     * The constant PLANT_ANIM.
     */
    public static final Animation PLANT_ANIM = Animation.create(3691);

    private static final Animation REMOVE_ANIMATION = Animation.create(3685);

    /**
     * The constant PLANK.
     */
    public static final Item PLANK = new Item(Items.PLANK_960);

    /**
     * The constant WATERING_CAN.
     */
    public static final int WATERING_CAN = 5340;

    private static final int[] BUILD_INDEXES = { 0, 2, 4, 6, 1, 3, 5 };

    /**
     * Open build interface.
     *
     * @param player  the player
     * @param hotspot the hotspot
     */
    public static void openBuildInterface(Player player, BuildHotspot hotspot) {
        player.getInterfaceManager().open(new Component(396));
        Item[] items = new Item[7];

        int c261Value = 0;

        for (int menuIndex = 0; menuIndex < 7; menuIndex++) {
            int itemsStringOffset = 97 + (menuIndex * 5);

            //97 +
            if (menuIndex >= hotspot.getDecorations().length || (hotspot.getDecorations()[menuIndex] != null && hotspot.getDecorations()[menuIndex].isInvisibleNode())) {
                for (int j = 0; j < 5; j++) {
                    player.getPacketDispatch().sendString("", 396, itemsStringOffset + j);
                }
                player.getPacketDispatch().sendString("", 396, 140 + menuIndex);
                c261Value += (1 << (menuIndex + 1));
                continue;
            }

            Decoration decoration = hotspot.getDecorations()[menuIndex];
            items[BUILD_INDEXES[menuIndex]] = new Item(decoration.getInterfaceItem());
            player.getPacketDispatch().sendString(ItemDefinition.forId(decoration.getInterfaceItem()).getName(), 396, itemsStringOffset);
            boolean hasRequirements = player.getSkills().getLevel(Skills.CONSTRUCTION) >= decoration.getLevel();
            for (int j = 0; j < 4; j++) {
                if (j >= decoration.getItems().length) {
                    if (j == decoration.getItems().length && decoration.getNailAmount() > 0) {
                        player.getPacketDispatch().sendString("Nails: " + decoration.getNailAmount(), 396, (itemsStringOffset + 1) + j);
                    } else {
                        player.getPacketDispatch().sendString("", 396, (itemsStringOffset + 1) + j);
                    }
                } else {
                    Item item = decoration.getItems()[j];
                    if (!player.getInventory().containsItem(item)) {
                        hasRequirements = false;
                    }
                    String s = item.getName() + ": " + item.getAmount();
                    /*if (j > 1 && (decoration == Decoration.RUNE_CASE1 || decoration == Decoration.RUNE_CASE2)) {
                        if (j == 3) {
                            offset--;
                            item = decoration.getItems()[++j];
                            s = item.getName() + ": " + item.getAmount();
                        }
                        item = decoration.getItems()[j + 1];
                        s += ", " + item.getName() + ": " + item.getAmount();
                        player.getPacketDispatch().sendString(s, 396, 15 + offset + j);
                        continue;
                    }*/
                    player.getPacketDispatch().sendString(s, 396, (itemsStringOffset + 1) + j);
                }
            }
            if (hasRequirements) {
                c261Value += (1 << (menuIndex + 1));
            }
            setVarp(player, 1485 + menuIndex, hasRequirements || player.isStaff() ? 1 : 0);
            player.getPacketDispatch().sendString("Level " + decoration.getLevel(), 396, 140 + menuIndex);
            //player.getPacketDispatch().sendItemZoomOnInterface(items[i].protocol(), 50000, 396, 49 + i);
        }

        setVarp(player, 261, c261Value);
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 396, 132, 8, items, false));
    }

    /**
     * Is door hotspot boolean.
     *
     * @param object the object
     * @return the boolean
     */
    public static boolean isDoorHotspot(Scenery object) {
        return object.getId() >= 15305 && object.getId() <= 15322;
    }

    /**
     * Build decoration.
     *
     * @param player  the player
     * @param hotspot the hotspot
     * @param deco    the deco
     * @param object  the object
     */
    public static void buildDecoration(final Player player, final Hotspot hotspot, final Decoration deco, final Scenery object) {
        buildDecoration(player, hotspot, deco, object, false);
    }

    /**
     * Build decoration.
     *
     * @param player        the player
     * @param hotspot       the hotspot
     * @param deco          the deco
     * @param object        the object
     * @param usingFlatpack the using flatpack
     */
    public static void buildDecoration(final Player player, final Hotspot hotspot, final Decoration deco, final Scenery object, final boolean usingFlatpack) {

        final int nailAmount = deco.getNailAmount();
        final Nails type = nailAmount > 0 ? Nails.get(player, nailAmount) : null;
        if (nailAmount > 0 && type == null && !usingFlatpack && !player.isAdmin()) {
            player.getPacketDispatch().sendMessage("You don't have the right materials.");
            return;
        }
        int roomX = object.getLocation().getChunkX();
        int roomY = object.getLocation().getChunkY();
        int z = object.getLocation().getZ();
        Region region = player.getHouseManager().getHouseRegion();
        if (HouseManager.isInDungeon(player)) {
            region = player.getHouseManager().getDungeonRegion();
            z = 3;
        }
        final Room room = player.getHouseManager().getRooms()[z][roomX][roomY];
        if (!canBuildDecoration(player, room, deco, object)) {
            return;
        }
        player.lock(3);
        player.animate(hotspot.getHotspot().getBuildingAnimation());
        final Region r = region;
        if (!usingFlatpack) {
            player.getPulseManager().run(new Pulse(3, player, object) {
                int nails = nailAmount;
                Nails nail = type;

                @Override
                public boolean pulse() {
                    if (nails > 0) {
                        if (!type.isBend()) {
                            player.getPacketDispatch().sendMessage("You use a nail.");
                            nails--;
                        } else {
                            player.getPacketDispatch().sendMessage("You accidently bend a nail.");
                        }
                        player.animate(hotspot.getHotspot().getBuildingAnimation());
                        if (!player.getInventory().remove(new Item(nail.getItemId(), 1))) {
                            return true;
                        }
                        if (nails > 0) {
                            player.lock(4);
                            return false;
                        }
                    } else if (deco.getTools()[0] == WATERING_CAN) {
                        for (int i = 7; i >= 0; i--) {
                            Item can = player.getInventory().getItem(new Item(WATERING_CAN - i, 1));
                            if (can != null && can.getSlot() > -1) {
                                player.getInventory().replace(new Item(WATERING_CAN - (i == 7 ? i + 2 : i + 1), 1), can.getSlot());
                                break;
                            }
                        }
                    }
                    if (player.getInventory().remove(deco.getItems()) || player.isAdmin()) {
                        setDecoration(player, r, room, hotspot, object, deco);
                        player.getSkills().addExperience(Skills.CONSTRUCTION, deco.getExperience(), true);

                        if (getObjectIdsThatGiveFarmingExperience().contains(deco.getObjectId())) {
                            player.getSkills().addExperience(Skills.FARMING, deco.getExperience(), true);
                        }
                        player.unlock();
                    }
                    return true;
                }

                @NotNull
                private ArrayList<Integer> getObjectIdsThatGiveFarmingExperience() {
                    return new ArrayList<>(
                            // Garden big tree decorations.
                            Arrays.asList(13411, 13412, 13413, 13414, 13415, 13416, 13417,
                                    // Garden tree decorations.
                                    13418, 13419, 13420, 13421, 13422, 13423, 13424,
                                    // Garden big plant 1 decorations.
                                    13425, 13426, 13427,
                                    // Garden big plant 2 decorations.
                                    13428, 13429, 13430,
                                    // Garden small plant 1 decorations.
                                    13431, 13432, 13433,
                                    // Garden small plant 2 decorations.
                                    13434, 13435, 13436));
                }
            });
        } else {
            if (player.getInventory().remove(new Item(deco.getFlatpackItemID()))) {
                setDecoration(player, r, room, hotspot, object, deco);
            }
        }
    }

    /**
     * Create flatpack.
     *
     * @param player the player
     * @param deco   the deco
     * @param debug  the debug
     */
    public static void createFlatpack(final Player player, final Decoration deco, final Boolean debug) {
        System.out.println("Building flatpack in BuildingUtils for item: " + deco.name());
//		System.out.println(deco.name());
        if (!player.skills.hasLevel(Skills.CONSTRUCTION, deco.getLevel())) {
            player.sendMessage("You need to have a Construction level of " + deco.getLevel() + " to build that.");
            return;
        }
        if (deco.getLevel()>player.getAttribute("maxFlatpackLevel",0)) {
            player.sendMessage("You need a better workbench to build that item.");
            return;
        }
        System.out.println("Checking if player has the right materials for flatpack: " + deco.getItems().toString());
        for (Item item : deco.getItems()) {
            player.sendMessage(item.getName());
        }
        if( player.getInventory().remove(deco.getItems()) || debug){
            addItemOrDrop(player, deco.getFlatpackItemID(), 1);
            player.skills.addExperience(Skills.CONSTRUCTION, deco.getExperience());
            player.animate(new Animation(4110));
            player.animate(new Animation(4110),3);
            player.animate(new Animation(4110),5);
            lock(player,8);
        }
    }


    private static void setDecoration(Player player, Region region, Room room, Hotspot hotspot, Scenery object, Decoration deco) {
        Location l = object.getLocation();
        HousingStyle style = player.getHouseManager().getStyle();
        int decIndex = hotspot.getHotspot().getDecorationIndex(deco);
        switch(hotspot.getHotspot().getType()) {
            case STAIRCASE:
                int z = l.getZ();
                if (region == player.getHouseManager().getDungeonRegion()) {
                    z = 3;
                }
                for (int i = -1; i <= 1; i++) {
                    int plane = (z + (i == -1 ? 3 : i)) % 4;
                    Room r = player.getHouseManager().getRooms()[plane][l.getChunkX()][l.getChunkY()];
                    if (r != null && r.getRotation() == room.getRotation() && !r.getProperties().isLand()) {
                        Hotspot h = r.getStairs();
                        if (h != null) {
                            h.setDecorationIndex(decIndex);
                            Region reg = plane == 3 ? player.getHouseManager().getDungeonRegion() : player.getHouseManager().getHouseRegion();
                            if (reg == null) {
                                continue;
                            }
                            BuildRegionChunk chunk = (BuildRegionChunk) reg.getPlanes()[plane % 3].getChunks()[l.getChunkX()][l.getChunkY()];
                            Scenery[] objects = chunk.getObjects(h.getCurrentX(), h.getCurrentY());
                            for (Scenery o : objects) {
                                if (o != null && o.getType() == object.getType()) {
                                    SceneryBuilder.replace(o, o.transform(h.getHotspot().getDecorations()[decIndex].getObjectId(style)));
                                    if (plane == 1) {
                                        if (r.getProperties() == RoomProperties.SKILL_HALL) {
                                            r.updateProperties(player, RoomProperties.SKILL_HALL_2);
                                        } else if (r.getProperties() == RoomProperties.QUEST_HALL) {
                                            r.updateProperties(player, RoomProperties.QUEST_HALL_2);
                                        } else {
                                            break;
                                        }
                                        player.getHouseManager().reload(player, true);
                                    }
                                    break;
                                }
                            }
                        } else {
                            log(BuildingUtils.class, Log.ERR, "Couldn't find stairs! " + plane);
                        }
                    }
                }
                break;
            case CREST:
                SceneryBuilder.replace(object, object.transform(deco.getObjectId(style) + player.getHouseManager().getCrest().ordinal()));
                hotspot.setDecorationIndex(decIndex);
                break;
            case INDIVIDUAL:
                SceneryBuilder.replace(object, object.transform(deco.getObjectId(style)));
                hotspot.setDecorationIndex(decIndex);
                break;
            case RECURSIVE:
                BuildRegionChunk chunk = (BuildRegionChunk) region.getPlanes()[l.getZ()].getChunks()[l.getChunkX()][l.getChunkY()];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Hotspot h = room.getHotspot(hotspot.getHotspot(), x, y);
                        if (h != null) {
                            h.setDecorationIndex(decIndex);
                            int objectId = hotspot.getHotspot().getObjectId(style);
                            Scenery o = chunk.get(x, y, chunk.getIndex(x, y, objectId));
                            if (o != null && objectId == o.getId()) {
                                SceneryBuilder.replace(o, o.transform(hotspot.getHotspot().getDecorations()[decIndex].getObjectId(style)));
                            }
                        }
                    }
                }
                break;
            case LINKED:
                BuildHotspot[] linkedHotspots = BuildHotspot.getLinkedHotspots(hotspot.getHotspot());
                chunk = (BuildRegionChunk) region.getPlanes()[l.getZ()].getChunks()[l.getChunkX()][l.getChunkY()];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        for (BuildHotspot bh : linkedHotspots) {
                            Hotspot h = room.getHotspot(bh, x, y);
                            if (h != null) {
                                h.setDecorationIndex(decIndex);
                                int objectId = bh.getObjectId(style);
                                Scenery o = chunk.get(x, y, chunk.getIndex(x, y, objectId));
                                if (o != null && objectId == o.getId()) {
                                    SceneryBuilder.replace(o, o.transform(bh.getDecorations()[decIndex].getObjectId(style)));
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    /**
     * Remove decoration.
     *
     * @param player the player
     * @param object the object
     */
    public static void removeDecoration(Player player, Scenery object) {
        if (object.getId() == Decoration.PORTAL.getObjectId() && player.getHouseManager().getPortalAmount() <= 1) {
            sendMessage(player, "You need at least one portal, how else would you leave your house?");
            return;
        }
        Location l = object.getLocation();
        Room room = player.getHouseManager().getRooms()[l.getZ()][l.getChunkX()][l.getChunkY()];
        Region region = player.getHouseManager().getHouseRegion();
        if (HouseManager.isInDungeon(player)) {
            region = player.getHouseManager().getDungeonRegion();
            room = player.getHouseManager().getRooms()[3][l.getChunkX()][l.getChunkY()];
        }
        HousingStyle style = player.getHouseManager().getStyle();
        for (int i = 0; i < room.getHotspots().length; i++) {
            Hotspot hotspot = room.getHotspots()[i];
            int objectId = hotspot.getDecorationIndex() < 0 ? -1 : hotspot.getHotspot().getDecorations()[hotspot.getDecorationIndex()].getObjectId(style);
            if (hotspot.getHotspot().getType() == BuildHotspotType.CREST) {
                objectId += player.getHouseManager().getCrest().ordinal();
            }
            if (objectId == object.getId() && hotspot.getCurrentX() == l.getChunkOffsetX() && hotspot.getCurrentY() == l.getChunkOffsetY()) {
                player.animate(REMOVE_ANIMATION);
                removeDecoration(player, region, room, hotspot, object, style);
                Decoration decoration = Decoration.forObjectId(object.getId());
                for (Item item : decoration.getRefundItems()) {
                    addItemOrDrop(player, item.getId(), item.getAmount());
                }
                break;
            }
        }
    }

    private static void removeDecoration(Player player, Region region, Room room, Hotspot hotspot, Scenery object, HousingStyle style) {
        Location l = object.getLocation();
        switch (hotspot.getHotspot().getType()) {
            case STAIRCASE:
                int z = l.getZ();
                if (region == player.getHouseManager().getDungeonRegion()) {
                    z = 3;
                }
                for (int i = -1; i <= 1; i++) {
                    int plane = (z + (i == -1 ? 3 : i)) % 4;
                    Room r = player.getHouseManager().getRooms()[plane][l.getChunkX()][l.getChunkY()];
                    if (r != null && r.getRotation() == room.getRotation()) {
                        Hotspot h = r.getStairs();
                        if (h != null) {
                            h.setDecorationIndex(-1);
                            Region reg = plane == 3 ? player.getHouseManager().getDungeonRegion() : player.getHouseManager().getHouseRegion();
                            if (reg == null) {
                                continue;
                            }
                            BuildRegionChunk chunk = (BuildRegionChunk) reg.getPlanes()[plane % 3].getChunks()[l.getChunkX()][l.getChunkY()];
                            Scenery[] objects = chunk.getObjects(h.getCurrentX(), h.getCurrentY());
                            for (Scenery o : objects) {
                                if (o != null && o.getType() == object.getType()) {
                                    SceneryBuilder.replace(o, o.transform(h.getHotspot().getObjectId(style)));
                                    break;
                                }
                            }
                        } else {
                            log(BuildingUtils.class, Log.ERR, "Couldn't find stairs! " + plane);
                        }
                    }
                }
                if (l.getZ() == 1) {
                    if (room.getProperties() == RoomProperties.SKILL_HALL_2) {
                        room.updateProperties(player, RoomProperties.SKILL_HALL);
                    } else if (room.getProperties() == RoomProperties.QUEST_HALL_2) {
                        room.updateProperties(player, RoomProperties.QUEST_HALL);
                    } else {
                        break;
                    }
                    player.getHouseManager().reload(player, true);
                }
                break;
            case INDIVIDUAL:
            case CREST:
                SceneryBuilder.replace(object, object.transform(hotspot.getHotspot().getObjectId(style)));
                hotspot.setDecorationIndex(-1);
                break;
            case RECURSIVE:
                BuildRegionChunk chunk = (BuildRegionChunk) region.getPlanes()[l.getZ()].getChunks()[l.getChunkX()][l.getChunkY()];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Hotspot h = room.getHotspot(hotspot.getHotspot(), x, y);
                        if (h != null) {
                            int objectId = hotspot.getHotspot().getDecorations()[h.getDecorationIndex()].getObjectId(style);
                            Scenery o = chunk.get(x, y, chunk.getIndex(x, y, objectId));
                            h.setDecorationIndex(-1);
                            if (o != null && objectId == o.getId()) {
                                SceneryBuilder.replace(o, o.transform(hotspot.getHotspot().getObjectId(style)));
                            }
                        }
                    }
                }
                break;
            case LINKED:
                BuildHotspot[] linkedHotspots = BuildHotspot.getLinkedHotspots(hotspot.getHotspot());
                chunk = (BuildRegionChunk) region.getPlanes()[l.getZ()].getChunks()[l.getChunkX()][l.getChunkY()];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        for (BuildHotspot bh : linkedHotspots) {
                            Hotspot h = room.getHotspot(bh, x, y);
                            if (h != null) {
                                int objectId = bh.getDecorations()[h.getDecorationIndex()].getObjectId(style);
                                Scenery o = chunk.get(x, y, chunk.getIndex(x, y, objectId));
                                h.setDecorationIndex(-1);
                                if (o != null && objectId == o.getId()) {
                                    SceneryBuilder.replace(o, o.transform(bh.getObjectId(style)));
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    private static boolean canBuildDecoration(Player player, Room room, Decoration deco, Scenery object) {
        switch (deco) {
            case TENTACLE_MID:
            case TENTACLE_SIDE:
            case TENTACLE_CORNER:
            case TENTACLE_FL:
                if (!room.isBuilt(BuildHotspot.PRISON)) {
                    player.getPacketDispatch().sendMessage("You can't build a tentacle pool without a cage.");
                    return false;
                }
                return true;
            default:
                return true;
        }
    }

    @SuppressWarnings("unused")
    private static boolean setLinkedHotspot(Player player, Room room, Hotspot hotspot, int decorationIndex, Scenery object) {
        Location l = object.getLocation();
        int z = l.getZ();
        switch (hotspot.getHotspot()) {
            case STAIRWAYS:
            case QUEST_STAIRWAYS:
            case STAIRWAYS_DUNGEON:
                BuildHotspot[] stairs = { BuildHotspot.STAIRS_DOWN, BuildHotspot.STAIRS_DOWN2, BuildHotspot.STAIRWAYS, BuildHotspot.QUEST_STAIRWAYS, BuildHotspot.STAIRWAYS_DUNGEON };
                for (int i = 0; i < 2; i++) {
                    int plane = (z + 1 + (i * 2)) % 4;
                    Room r = player.getHouseManager().getRooms()[plane][l.getChunkX()][l.getChunkY()];
                    if (r != null) {
                        for (BuildHotspot h : stairs) {
                            Hotspot hs = r.getHotspot(h, hotspot.getCurrentX(), hotspot.getCurrentY());
                            if (hs != null) {
                                hs.setDecorationIndex(decorationIndex);
                                SceneryBuilder.replace(object, object.transform(h.getDecorations()[decorationIndex].getObjectId(player.getHouseManager().getStyle())));
                                break;
                            }
                        }
                    }
                }
                return true;
            default:
                return true;
        }
    }

    /**
     * Build room.
     *
     * @param player the player
     * @param room   the room
     * @param z      the z
     * @param x      the x
     * @param y      the y
     * @param exits  the exits
     * @param reload the reload
     */
    public static void buildRoom(Player player, Room room, int z, int x, int y, boolean[] exits, boolean reload) {
        player.getHouseManager().getRooms()[z][x][y] = room;
        player.getPacketDispatch().sendMessage("Building room " + room.getProperties() + ".");
        if (z == 3) {
            player.getHouseManager().setHasDungeon(true);
        }
        if (room.getProperties().isChamber() && z < 2) {
            int count = 0;
            int index = 0;
            for (int i = 0; i < exits.length; i++) {
                if (exits[i]) {
                    count++;
                } else {
                    index = i;
                }
            }
            //(0=east, 1=south, 2=west, 3=north).

            if (count == 4) {
                room = Room.create(player, RoomProperties.ROOF_4_EXIT);
            } else if (count == 3) {
                room = Room.create(player, RoomProperties.ROOF_3_EXIT);
            } else if ((exits[0] != exits[2]) || (exits[1] != exits[3])) {
                room = Room.create(player, RoomProperties.ROOF_3_EXIT);
            } else {
                room = Room.create(player, RoomProperties.ROOF_2_EXIT);
            }
            player.getHouseManager().getRooms()[z + 1][x][y] = room;
        }
        if (reload) {
            player.getHouseManager().reload(player, true);
        }
    }

    /**
     * Room exists int [ ].
     *
     * @param player the player
     * @param door   the door
     * @return the int [ ]
     */
    public static int[] roomExists(Player player, Scenery door) {
        int[] location = getRoomPosition(player, door);
        int z = player.getLocation().getZ();
        if (HouseManager.isInDungeon(player)) {
            z = 3;
        }
        if (player.getHouseManager().hasRoomAt(z, location[0], location[1])) {
            return location;
        }
        return null;
    }

    /**
     * Get room position int [ ].
     *
     * @param player the player
     * @param door   the door
     * @return the int [ ]
     */
    public static int[] getRoomPosition(Player player, Scenery door) {
        Location l = door.getLocation();
        int rotation = door.getRotation();
        if (player.getLocation().getChunkX() != l.getLocation().getChunkX()
                || player.getLocation().getChunkY() != l.getLocation().getChunkY()) {
            return new int[] { l.getChunkX(), l.getChunkY() };
        }
        switch (rotation) {
            case 0: //West
                return new int[] { l.getChunkX() - 1, l.getChunkY() };
            case 1: //North
                return new int[] { l.getChunkX(), l.getChunkY() + 1};
            case 2: //East
                return new int[] { l.getChunkX() + 1, l.getChunkY() };
            case 3: //South
                return new int[] { l.getChunkX(), l.getChunkY() - 1};
        }
        return null;
    }

    /**
     * Get available rotations direction [ ].
     *
     * @param player the player
     * @param exits  the exits
     * @param z      the z
     * @param roomX  the room x
     * @param roomY  the room y
     * @return the direction [ ]
     */
    public static Direction[] getAvailableRotations(Player player, boolean[] exits, int z, int roomX, int roomY) {
        Direction[] directions = new Direction[4];
        boolean[] exit = Arrays.copyOf(exits, exits.length); //(0=east, 1=south, 2=west, 3=north)
        int[] info = getExitRequirements(player, z, roomX, roomY);//(0=west, 1=north, 2=east, 3=south)
        //		SystemLogger.logErr(BuildingUtils.class, "Available exits - [east=" + exit[0] + ", south=" + exit[1] + ", west=" + exit[2] + ", north=" + exit[3] + "]!");
        //		SystemLogger.logErr(BuildingUtils.class, "Required exits - [east=" + info[0] + ", south=" + info[1] + ", west=" + info[2] + ", north=" + info[3] + "]!");
        for (int i = 0; i < 4; i++) {
            boolean success = true;
            for (int j = 0; j < 4; j++) {
                if ((info[j] == 1 && !exit[j]) || (info[j] == -1 && exit[j])) {
                    success = false;
                    break;
                }
            }
            if (success) {
                directions[i] = DIRECTIONS[i];
            }
            for (int j = 0; j < exit.length; j++) {
                exit[(j + i + 1) % exit.length] = exits[j];
            }
        }
        return directions;
    }

    private static int[] getExitRequirements(Player player, int z, int roomX, int roomY) {
        int[] exits = new int[4];
        int deltaX = roomX - player.getLocation().getChunkX();
        int deltaY = roomY - player.getLocation().getChunkY();
        if (deltaX > 0) {
            // player is to our west
            exits[2] = 1;
        } else if (deltaX < 0) {
            // player is to our east
            exits[0] = 1;
        } else if (deltaY > 0) {
            // player is to our south
            exits[1] = 1;
        } else if (deltaY < 0) {
            // player is to our north
            exits[3] = 1;
        }
        return exits;
    }
}
