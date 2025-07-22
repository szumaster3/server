package content.region.desert.bedabin.plugin;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The Bedabin plugin.
 */
@Initializable
public final class BedabinPlugin extends OptionHandler {

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        registerHandlers();
        return this;
    }

    private void registerHandlers() {
        SceneryDefinition.forId(2700).getHandlers().put("option:walk-through", this);
        SceneryDefinition.forId(2672).getHandlers().put("option:use", this);
        NPCDefinition.forId(834).getHandlers().put("option:talk-to", this);
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "talk-to":
                return handleTalkTo(player, node);
            case "walk-through":
                return handleWalkThrough(player, node);
            case "use":
                return handleUse(player, node);
            default:
                return false;
        }
    }

    private boolean handleTalkTo(Player player, Node node) {
        player.getDialogueInterpreter().open(node.getId(), node);
        return true;
    }

    private boolean handleWalkThrough(Player player, Node node) {
        final int id = node.getId();
        if (id == 2700) {
            if (player.getLocation().getY() >= 3046) {
                walkThroughTent(player);
            } else {
                player.getDialogueInterpreter().open(834, RegionManager.getNpc(player, 834));
            }
            return true;
        }
        return false;
    }

    private void walkThroughTent(Player player) {
        final Scenery door = RegionManager.getObject(new Location(3169, 3046, 0));
        SceneryBuilder.replace(door, door.transform(2701), 2);
        player.getWalkingQueue().reset();
        player.getWalkingQueue().addPath(3169, 3045);
        player.getPacketDispatch().sendMessage("You walk back out the tent.");
    }

    private boolean handleUse(Player player, Node node) {
        if (node.getId() == 2672) {
            player.getPacketDispatch().sendMessage("To forge items use the metal you wish to work with the anvil.");
            return true;
        }
        return false;
    }

    @Override
    public Location getDestination(Node n, Node node) {
        if (node instanceof NPC && node.getId() == 834) {
            return new Location(3169, 3045, 0);
        }
        return null;
    }
}
