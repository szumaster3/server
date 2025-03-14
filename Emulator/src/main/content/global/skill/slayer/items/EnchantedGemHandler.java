package content.global.skill.slayer.items;

import content.global.skill.slayer.SlayerManager;
import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type Enchanted gem handler.
 */
@Initializable
public final class EnchantedGemHandler extends OptionHandler {

    /**
     * Instantiates a new Enchanted gem handler.
     */
    public EnchantedGemHandler() {

    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!SlayerManager.getInstance(player).hasStarted()) {
            player.getPacketDispatch().sendMessage("You try to activate the gem...");
            return true;
        }
        player.getDialogueInterpreter().open(77777);
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(4155).getHandlers().put("option:activate", this);
        return this;
    }
}
