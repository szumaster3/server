package content.global.skill.construction;

import core.cache.def.impl.SceneryDefinition;
import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionChunk;
import core.plugin.Initializable;
import core.tools.Log;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.log;

/**
 * Represents the build room dialogue.
 */
@Initializable
public final class BuildRoomDialogue extends Dialogue {
    private List<Scenery> boundaries = new ArrayList<>();
    private Scenery door;
    private Direction[] directions;
    private boolean[] exits;
    private int index;
    private Room room;
    private int roomX;
    private int roomY;
    private int roomZ;

    /**
     * Instantiates a new Build room dialogue.
     */
    public BuildRoomDialogue() {
        super();
    }

    /**
     * Instantiates a new Build room dialogue.
     *
     * @param player the player
     */
    public BuildRoomDialogue(Player player) {
        super(player);
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new BuildRoomDialogue(player);
    }

    @Override
    public boolean open(java.lang.Object... args) {
        player.getInterfaceManager().close();
        RoomProperties props = (RoomProperties) args[0];
        if (player.getSkills().getStaticLevel(Skills.CONSTRUCTION) < props.getLevel()) {
            interpreter.sendPlainMessage(false, "You need a Construction level of " + props.getLevel() + " to buy this room.");
            stage = 2;
            return true;
        }
        if (!player.getInventory().contains(995, props.getCost())) {
            interpreter.sendPlainMessage(false, "You need " + props.getCost() + " coins to buy this room.");
            stage = 2;
            return true;
        }
        this.door = (Scenery) player.getAttribute("con:hsobject");
        int[] pos = BuildingUtils.getRoomPosition(player, door);
        roomX = pos[0];
        roomY = pos[1];
        if (!inBounds()) {
            interpreter.sendPlainMessage(false, "Your house is too large. TODO: correct message");
            stage = 2;
            return true;
        }
        roomZ = player.getLocation().getZ();
        if (roomZ != 0) {
            Room r = player.getHouseManager().getRooms()[roomZ - 1][roomX][roomY];
            if (r == null || r.getProperties().isLand()) {
                interpreter.sendPlainMessage(false, "You can't build a room here, you need a room to build on.");
                stage = 2;
                return true;
            }
        }
        if (HouseManager.isInDungeon(player)) {
            if (props.isLand()) {
                interpreter.sendPlainMessage(false, "You can't build this room inside your dungeon.");
                stage = 2;
                return true;
            }
            roomZ = 3;
        } else if (props.isDungeon()) {
            interpreter.sendPlainMessage(false, "You can only build this room in your dungeon.");
            stage = 2;
            return true;
        }
        if (props.isLand() && roomZ != 0) {
            interpreter.sendPlainMessage(false, "A garden can only be on ground floor.");
            stage = 2;
            return true;
        }
        this.room = Room.create(player, props);
        this.exits = room.getExits();
        this.index = 0;
        this.directions = BuildingUtils.getAvailableRotations(player, exits, roomZ, roomX, roomY);
        while (directions[index] == null) {
            if (++index == directions.length) {
                interpreter.sendPlainMessage(false, "There's no space to build this room.");
                stage = 2;
                return true;
            }
        }
        options("Rotate clockwise", "Rotate anticlockwise", "Build", "Cancel");
        drawGhostRoom();
        stage = 1;
        return true;
    }

    private boolean inBounds() {
        Rectangle bounds = player.getHouseManager().getBoundaries();
        int max = player.getHouseManager().getMaximumDimension(player);
        if ((roomX < bounds.x || roomX >= bounds.x + bounds.width) && bounds.width >= max) {
            return false;
        }
        if ((roomY < bounds.y || roomY >= bounds.y + bounds.height) && bounds.height >= max) {
            return false;
        }
        return true;
    }

    @Override
    public boolean close() {
        for (Scenery object : boundaries) {
            SceneryBuilder.remove(object);
        }
        boundaries.clear();
        return super.close();
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 1:
                switch (buttonId) {
                    case 1:
                    case 2:
                        options("Rotate clockwise", "Rotate anticlockwise", "Build", "Cancel");
                        rotate(buttonId == 2);
                        return true;
                    case 3:
                        if (player.getInventory().remove(new Item(995, room.getProperties().getCost()))) {
                            room.setRotation(directions[index].toInteger());
                            boolean[] exit = new boolean[exits.length];
                            for (int i = 0; i < exit.length; i++) {
                                exit[(i + index) % exit.length] = exits[i];
                            }
                            BuildingUtils.buildRoom(player, room, roomZ, roomX, roomY, exit, true);
                            end();
                            return true;
                        }
                        interpreter.sendPlainMessage(false, "You need " + room.getProperties().getCost() + " coins to buy this room.");
                        stage = 2;
                        return true;
                    case 4:
                        end();
                        return true;
                }
                break;
            case 2:
                end();
                return true;
        }
        return false;
    }

    private void rotate(boolean counter) {
        final int totalDirections = directions.length;
        int attempts = 0;

        do {
            index = (index + (counter ? -1 : 1) + totalDirections) % totalDirections;
            attempts++;
            if (attempts > totalDirections) {
                log(this.getClass(), Log.DEBUG, "Direction = [null] Cannot rotate.");
                return;
            }
        } while (directions[index] == null);
        Integer direction = directions[index].toInteger();
        room.setRotation(direction);
        drawGhostRoom();
    }

    /**
     * Draws the current boundaries of the room to build.
     */
    private void drawGhostRoom() {
        for (Scenery scenery : boundaries) {
            SceneryBuilder.remove(scenery);
        }
        boundaries.clear();

        int rotation = directions[index].toInteger();
        Location base = player.getViewport().getRegion().getBaseLocation()
                .transform(roomX << 3, roomY << 3, player.getLocation().getZ());

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Scenery[] scenery = room.getChunk().getObjects(x, y);
                if (scenery == null) continue;

                for (Scenery object : scenery) {
                    if (object == null) continue;

                    SceneryDefinition obj = object.getDefinition();
                    if (obj == null || !obj.hasAction("build")) continue;

                    int[] pos = RegionChunk.getRotatedPosition(
                            x, y,
                            obj.sizeX, obj.sizeY,
                            object.getRotation(), rotation
                    );

                    Location transformedLoc = base.transform(pos[0], pos[1], 0);
                    int rotatedRot = (object.getRotation() + rotation) % 4;

                    Scenery transformed = object.transform(object.getId(), rotatedRot, transformedLoc);
                    boundaries.add(SceneryBuilder.add(transformed));
                }
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{DialogueInterpreter.getDialogueKey("con:room")};
    }

}