package content.region.kandarin.handlers.seers;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Camelot plugin.
 */
@Initializable
public final class CamelotPlugin extends OptionHandler {

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(26017).getHandlers().put("option:climb-down", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node instanceof Scenery) {
            int id = ((Scenery) node).getId();
            if (id == 26017) {
                player.getPacketDispatch().sendMessage("Court is not in session.");
                return true;
            }
        }
        return false;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof Scenery) {
            int id = ((Scenery) n).getId();
            if (id == 993) {
                return determineLocation(node);
            }
        }
        return null;
    }

    private Location determineLocation(Node node) {
        return node.getLocation().getX() <= 2638
            ? Location.create(2637, 3350, 0)
            : Location.create(2640, 3350, 0);
    }
}
