package content.region.asgarnia.falador.plugin.party_room;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.GameWorld;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the drop party lever.
 *
 * @author Vexia
 */
public final class DropPartyLeverOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(shared.consts.Scenery.LEVER_26194).getHandlers().put("option:pull", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final Scenery object = (Scenery) node;
        if (player.getAttribute("delay:lever", -1) > GameWorld.getTicks()) return true;
        player.setAttribute("delay:picking", GameWorld.getTicks() + 3);
        player.lock(2);
        player.faceLocation(object.getLocation());
        player.getDialogueInterpreter().open(1 << 16 | 2);
        return true;
    }

}
