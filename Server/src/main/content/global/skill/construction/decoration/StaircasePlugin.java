package content.global.skill.construction.decoration;

import content.data.GameAttributes;
import content.global.skill.construction.*;
import core.cache.def.impl.SceneryDefinition;
import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Staircase plugin.
 */
@Initializable
public final class StaircasePlugin extends OptionHandler {

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        ClassScanner.definePlugin(new BuildDialogue());
        ClassScanner.definePlugin(new ClimbPohLadder());
        for (int i = 13497; i < 13507; i++) {
            SceneryDefinition.forId(i).getHandlers().put("option:climb", this);
            SceneryDefinition.forId(i).getHandlers().put("option:climb-up", this);
            SceneryDefinition.forId(i).getHandlers().put("option:climb-down", this);
            SceneryDefinition.forId(i).getHandlers().put("option:remove-room", this);
        }
        SceneryDefinition.forId(13409).getHandlers().put("option:enter", this);
        SceneryDefinition.forId(13409).getHandlers().put("option:remove-room", this);
        for (int id = 13328; id < 13331; id++) {
            SceneryDefinition.forId(id).getHandlers().put("option:climb", this);
            SceneryDefinition.forId(id).getHandlers().put("option:remove-room", this);
        }
        for (int id = 13675; id <= 13680; id++) {
            if (id < 13678) {
                SceneryDefinition.forId(id).getHandlers().put("option:open", this);
            } else {
                SceneryDefinition.forId(id).getHandlers().put("option:go-down", this);
                SceneryDefinition.forId(id).getHandlers().put("option:close", this);
            }
            SceneryDefinition.forId(id).getHandlers().put("option:remove-room", this);
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        HouseManager house = player.getAttribute(GameAttributes.POH_PORTAL, null);
        if (house == null) {
            player.getPacketDispatch().sendMessage("You're not in your house right now (REPORT).");
            return true;
        }
        Scenery scenery = (Scenery) node;
        switch (option) {
            case "open":
                SceneryBuilder.replace(scenery, scenery.transform(scenery.getId() + 3), 200);
                return true;
            case "close":
                SceneryBuilder.replace(scenery, scenery.transform(scenery.getId() - 3));
                return true;
            case "remove-room":
                if (player.getLocation().getZ() != 0) {
                    player.getPacketDispatch().sendMessage("The room below is supporting this room!");
                    return true;
                }
                return false;
            case "climb":
                if (house.getDungeonRegion() == player.getViewport().getRegion()) {
                    climb(player, 1, house, scenery);
                    return true;
                }
                if (scenery.getLocation().getZ() > 0) {
                    climb(player, -1, house, scenery);
                    return true;
                }
                player.getDialogueInterpreter().open("con:climbdial", house, scenery);
                return true;
            case "climb-up":
                climb(player, 1, house, scenery);
                return true;
            case "enter":
            case "climb-down":
            case "go-down":
                climb(player, -1, house, scenery);
                return true;
        }
        return false;
    }

    private static void climb(Player player, int z, HouseManager house, Scenery scenery) {
        Location l = player.getLocation();
        int plane = l.getZ() + z;
        int roomX = l.getChunkX();
        int roomY = l.getChunkY();
        Room current = house.getRooms()[l.getZ()][roomX][roomY];
        if (plane < 0) {
            plane = 3;
        } else if (player.getViewport().getRegion() == house.getDungeonRegion() && plane == 1) {
            plane = 0;
        }
        Room room = house.getRooms()[plane][roomX][roomY];

        if (room == null || room.getProperties().isRoof()) {
            if (player.getHouseManager().isInHouse(player) && player.getHouseManager().isBuildingMode()) {
                player.getDialogueInterpreter().open("con:nfroom", plane, roomX, roomY, current, scenery);
            } else {
                player.getPacketDispatch().sendMessage("This doesn't seem to lead anywhere.");
            }
        } else {
            Location destination = l.transform(0, 0, z);
            if (player.getViewport().getRegion() == house.getDungeonRegion()) {
                destination = house.getHouseRegion().getBaseLocation().transform(l.getLocalX(), l.getLocalY(), 0);
            } else if (plane == 3) {
                destination = house.getDungeonRegion().getBaseLocation().transform(l.getLocalX(), l.getLocalY(), 0);
            }
            Room r = house.getRoom(destination);
            Hotspot h = r.getStairs();
            if (h != null && h.getDecorationIndex() > -1) {
                player.getProperties().setTeleportLocation(destination);
            } else {
                player.getPacketDispatch().sendMessage("This doesn't seem to lead anywhere.");
            }
        }
    }

    /**
     * The type Climb poh ladder.
     */
    static final class ClimbPohLadder extends Dialogue {

        private HouseManager house;

        private Scenery ladder;

        /**
         * Instantiates a new Climb poh ladder.
         */
        public ClimbPohLadder() {
            super();
        }

        /**
         * Instantiates a new Climb poh ladder.
         *
         * @param player the player
         */
        public ClimbPohLadder(final Player player) {
            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new ClimbPohLadder(player);
        }

        @Override
        public boolean open(java.lang.Object... args) {
            house = (HouseManager) args[0];
            ladder = (Scenery) args[1];
            interpreter.sendOptions("What would you like to do?", "Climb Up.", "Climb Down.");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    switch (buttonId) {
                        case 1:
                            player.lock(1);
                            GameWorld.getPulser().submit(new Pulse(1) {
                                @Override
                                public boolean pulse() {
                                    climb(player, 1, house, ladder);
                                    return true;
                                }
                            });
                            end();
                            break;
                        case 2:
                            player.lock(1);
                            GameWorld.getPulser().submit(new Pulse(1) {
                                @Override
                                public boolean pulse() {
                                    climb(player, -1, house, ladder);
                                    return true;
                                }
                            });
                            end();
                            break;

                    }
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{DialogueInterpreter.getDialogueKey("con:climbdial")};
        }

    }

    /**
     * The type Build dialogue.
     */
    static final class BuildDialogue extends Dialogue {

        private int plane;

        private int roomX;

        private int roomY;

        private Room room;

        private Scenery stairs;

        /**
         * Instantiates a new Build dialogue.
         */
        public BuildDialogue() {

        }

        /**
         * Instantiates a new Build dialogue.
         *
         * @param player the player
         */
        public BuildDialogue(final Player player) {
            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new BuildDialogue(player);
        }

        @Override
        public boolean open(java.lang.Object... args) {
            plane = (Integer) args[0];
            roomX = (Integer) args[1];
            roomY = (Integer) args[2];
            room = (Room) args[3];
            stairs = (Scenery) args[4];
            stage = 0;
            if (stairs.getId() >= 13328 && stairs.getId() <= 13330) {
                interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build a throne room upstairs?");
                stage = 5;
                return true;
            }
            if (plane == 3) {
                if (room.getProperties() == RoomProperties.THRONE_ROOM) {
                    interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build an Oubilette?");
                } else {
                    interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build a dungeon room?");
                }
                return true;
            }
            interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build a room at the top?");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            boolean dungeon = plane == 3;
            switch (stage) {
                case 0:
                    interpreter.sendOptions("Select an Option", "Yes", "No");
                    stage = 1;
                    break;
                case 1:
                    switch (buttonId) {
                        case 1:
                            if (room.getProperties() == RoomProperties.THRONE_ROOM) {
                                Room r = Room.create(player, RoomProperties.OUBILETTE);
                                Direction[] dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
                                for (Direction d : dirs) {
                                    if (d.toInteger() == room.getRotation()) {
                                        r.setRotation(d.toInteger());
                                        Hotspot stairs = room.getStairs();
                                        int index = stairs != null ? stairs.getDecorationIndex() : -1;
                                        BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
                                        r.getStairs().setDecorationIndex(index);
                                        end();
                                        return true;
                                    }
                                }
                                interpreter.sendPlainMessage(false, "The room you're trying to build doesn't fit.");
                                stage = 4;
                                return true;
                            }
                            if (dungeon) {
                                interpreter.sendOptions("Select an Option", "Skill Hall", "Quest Hall", "Dungeon Stairs");
                            } else {
                                interpreter.sendOptions("Select an Option", "Skill Hall", "Quest Hall");
                            }
                            stage = 2;
                            return true;
                    }
                    end();
                    return true;
                case 2:
                    if (dungeon && buttonId == 3) {
                        stage = 3;
                        return handle(interfaceId, 1);
                    }
                    RoomProperties props = buttonId == 2 ? RoomProperties.QUEST_HALL_2 : RoomProperties.SKILL_HALL_2;
                    if (plane == 3) {
                        props = buttonId == 2 ? RoomProperties.QUEST_HALL : RoomProperties.SKILL_HALL;
                    }
                    Room r = Room.create(player, props);
                    Direction[] dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
                    for (Direction d : dirs) {
                        if (d.toInteger() == room.getRotation()) {
                            r.setRotation(d.toInteger());
                            Hotspot stairs = room.getStairs();
                            int index = stairs != null ? stairs.getDecorationIndex() : -1;
                            r.getStairs().setDecorationIndex(index);
                            BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
                            end();
                            return true;
                        }
                    }
                    interpreter.sendPlainMessage(false, "The room you're trying to build doesn't seem to fit.");
                    stage = 4;
                    return true;
                case 3:
                    switch (buttonId) {
                        case 1:
                            r = Room.create(player, RoomProperties.DUNGEON_STAIRS);
                            dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
                            for (Direction d : dirs) {
                                if (d.toInteger() == room.getRotation()) {
                                    r.setRotation(d.toInteger());
                                    Hotspot stairs = room.getStairs();
                                    int index = stairs != null ? stairs.getDecorationIndex() : -1;
                                    BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
                                    r.getStairs().setDecorationIndex(index);
                                    end();
                                    return true;
                                }
                            }
                            interpreter.sendPlainMessage(false, "The room you're trying to build doesn't fit.");
                            stage = 4;
                            return true;
                        case 2:
                            end();
                            return true;
                    }
                    return true;
                case 4:
                    end();
                    return true;
                case 5:
                    interpreter.sendOptions("Select an Option", "Yes", "No");
                    stage = 6;
                    return true;
                case 6:
                    switch (buttonId) {
                        case 1:
                            r = Room.create(player, RoomProperties.THRONE_ROOM);
                            dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
                            for (Direction d : dirs) {
                                if (d.toInteger() == room.getRotation()) {
                                    r.setRotation(d.toInteger());
                                    Hotspot stairs = room.getStairs();
                                    int index = stairs != null ? stairs.getDecorationIndex() : -1;
                                    BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
                                    r.getStairs().setDecorationIndex(index);
                                    end();
                                    return true;
                                }
                            }
                            interpreter.sendPlainMessage(false, "The room you're trying to build doesn't fit.");
                            stage = 4;
                            return true;
                        case 2:
                            end();
                            return true;
                    }
                    return true;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{DialogueInterpreter.getDialogueKey("con:nfroom")};
        }

    }
}