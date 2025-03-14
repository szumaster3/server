package content.global.skill.summoning.familiar.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Spirit graahk option plugin.
 */
@Initializable
public final class SpiritGraahkOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(7363).getHandlers().put("option:interact", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        player.getDialogueInterpreter().open(7353, node.asNpc());
        return true;
    }

}
