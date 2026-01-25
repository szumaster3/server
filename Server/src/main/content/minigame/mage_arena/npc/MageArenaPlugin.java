package content.minigame.mage_arena.npc;

import content.minigame.mage_arena.MageArena;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the mage arena activity.
 *
 * @author Vexia
 */
@Initializable
public final class MageArenaPlugin extends OptionHandler {
    /**
     * The mage arena zone.
     */
    public static final MageArena MAGE_ARENA = new MageArena();

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ClassScanner.definePlugin(new MageArenaNPC());
        return this;
    }

    @Override
    public boolean handle(final Player player, final Node node, String option) {
        return true;
    }

    @Override
    public boolean isWalk(Player player, Node node) {
        if (node instanceof GroundItem) {
            return true;
        }
        return !(node instanceof Item);
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        return null;
    }
}
