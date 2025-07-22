package content.global.skill.construction;

import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.node.scenery.SceneryManager;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionChunk;
import core.plugin.Initializable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the build room dialogue.
 */
@Initializable
public final class BuildRoomDialogue extends Dialogue {

    private Scenery door;

    private Direction[] directions;

    private boolean[] exits;

    private int index;
    private List<Scenery> boundaries = new ArrayList<>(20);
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
        this.exits = room.getExits(Direction.NORTH);
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
        stage = 1;
        drawGhostRoom();
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
        for (Scenery scenery : boundaries) {
            SceneryBuilder.remove(scenery);
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
                        rotate(buttonId == 2);
                        options("Rotate clockwise", "Rotate anticlockwise", "Build", "Cancel");
                        return true;
                    case 3:
                        if (player.getInventory().remove(new Item(995, room.getProperties().getCost()))) {
                            room.setRotation(directions[index]);
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
        Direction direction = null;
        while ((direction = directions[index = (index + (counter ? 3 : 1)) % 4]) == null) {
        }
        room.setRotation(direction);
        drawGhostRoom();
    }

    private void drawGhostRoom() {
        for (Scenery scenery : boundaries) {
            SceneryBuilder.remove(scenery);
        }
        int rotation = directions[index].toInteger();
        boundaries.clear();
        Location base = player.getViewport().getRegion().getBaseLocation().transform(roomX << 3, roomY << 3, player.getLocation().getZ());
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Scenery[] sceneries = room.getChunk().getObjects(x, y);
                for (Scenery scenery : sceneries) {
                    if (scenery != null && scenery.getDefinition().hasAction("build")) {
                        int[] pos = RegionChunk.getRotatedPosition(x, y, scenery.getDefinition().sizeX, scenery.getDefinition().sizeY, scenery.getRotation(), rotation);
                        Scenery obj = scenery.transform(scenery.getId(), (scenery.getRotation() + rotation) % 4, base.transform(pos[0], pos[1], 0));
                        boundaries.add(SceneryBuilder.add(obj));
                    }
                }
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{DialogueInterpreter.getDialogueKey("con:room")};
    }

}