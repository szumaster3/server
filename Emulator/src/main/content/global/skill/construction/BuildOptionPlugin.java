package content.global.skill.construction;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.Log;

import static core.api.ContentAPIKt.log;

/**
 * The type Build option plugin.
 */
@Initializable
public final class BuildOptionPlugin extends OptionHandler {

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.setOptionHandler("build", this);
        SceneryDefinition.setOptionHandler("remove", this);
        ClassScanner.definePlugin(new RemoveDialogue());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!player.getHouseManager().isBuildingMode()) {
            player.getPacketDispatch().sendMessage("You have to be in building mode to do this.");
            return true;
        }
        Scenery scenery = ((Scenery) node);
        if (option.equals("remove")) {
            Decoration decoration = Decoration.getDecoration(player, scenery);
            if (decoration == null || !scenery.isActive()) {
                return false;
            }
            player.getDialogueInterpreter().open("con:removedec", scenery);
            return true;
        }
        player.setAttribute("con:hsobject", node);
        if (BuildingUtils.isDoorHotspot(scenery)) {
            int[] pos = BuildingUtils.roomExists(player, scenery);
            if (pos != null) {
                player.getDialogueInterpreter().open("con:remove", "room", pos);
                return true;
            }
            if (player.getHouseManager().getRoomAmount() < player.getHouseManager().getMaximumRooms(player)) {
                player.getInterfaceManager().open(new Component(402));

            } else {
                player.getPacketDispatch().sendMessage("You currently have the maximum amount of rooms available.");
            }
            return true;
        }
        Hotspot hotspot = player.getHouseManager().getHotspot(scenery);
        if (hotspot == null || !isBuildable(player, scenery, hotspot)) {
            log(this.getClass(), Log.WARN, "Construction (building):  " + hotspot + " : " + scenery + " chunkX = " + scenery.getLocation().getChunkX() + ", chunkY = " + scenery.getLocation().getChunkY());
            return true;
        }

        player.setAttribute("con:hotspot", hotspot);
        BuildingUtils.openBuildInterface(player, hotspot.getHotspot());
        return true;
    }

    private static boolean isBuildable(Player player, Scenery scenery, Hotspot hotspot) {
        Room room = player.getHouseManager().getRoom(scenery.getLocation());
        if (room == null) {
            return false;
        }
        switch (hotspot.getHotspot()) {
            case STAIRWAYS:
            case STAIRS_DOWN:
            case STAIRWAYS_DUNGEON:
                if (room.isBuilt(BuildHotspot.HALL_RUG)) {
                    player.getPacketDispatch().sendMessage("You can't build a staircase on a rug.");
                    return false;
                }
                return true;
            case HALL_RUG:
            case HALL_RUG2:
            case HALL_RUG3:
                if (room.isBuilt(BuildHotspot.STAIRWAYS) || room.isBuilt(BuildHotspot.STAIRS_DOWN) || room.isBuilt(BuildHotspot.STAIRWAYS_DUNGEON)) {
                    player.getPacketDispatch().sendMessage("You can't build a rug under a staircase.");
                    return false;
                }
                return true;
            case QUEST_STAIRWAYS:
            case STAIRS_DOWN2:
                if (room.isBuilt(BuildHotspot.Q_HALL_RUG)) {
                    player.getPacketDispatch().sendMessage("You can't build a staircase on a rug.");
                    return false;
                }
                return true;
            case Q_HALL_RUG:
            case Q_HALL_RUG2:
            case Q_HALL_RUG3:
                if (room.isBuilt(BuildHotspot.QUEST_STAIRWAYS) || room.isBuilt(BuildHotspot.STAIRS_DOWN2)) {
                    player.getPacketDispatch().sendMessage("You can't build a rug under a staircase.");
                    return false;
                }
                return true;
            default:
                return true;
        }
    }

    private static class RemoveDialogue extends Dialogue {

        private Scenery scenery;

        /**
         * Instantiates a new Remove dialogue.
         */
        public RemoveDialogue() {
            super();
        }

        /**
         * Instantiates a new Remove dialogue.
         *
         * @param player the player
         */
        public RemoveDialogue(Player player) {
            super(player);
        }

        @Override
        public Dialogue newInstance(Player player) {
            return new RemoveDialogue(player);
        }

        @Override
        public boolean open(java.lang.Object... args) {
            interpreter.sendOptions("Really remove it?", "Yes", "No");
            scenery = (Scenery) args[0];
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (buttonId) {
                case 1:
                    BuildingUtils.removeDecoration(player, scenery);
                    break;
            }
            end();
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{DialogueInterpreter.getDialogueKey("con:removedec")};
        }

    }

}
