package content.region.kandarin.handlers.gutanoth;

import content.region.kandarin.dialogue.gutanoth.BogrogDialogue;
import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Unit;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.sendItemSelect;

/**
 * The type Bogrog plugin.
 */
@Initializable
public final class BogrogPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        registerPlugin();
        return this;
    }

    private void registerPlugin() {
        NPCDefinition.forId(NPCs.BOGROG_4472).getHandlers().put("option:swap", this);
        ClassScanner.definePlugin(new BogrogDialogue());
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if ("swap".equals(option)) {
            openSwap(player);
        }
        return true;
    }

    /**
     * Open swap.
     *
     * @param player the player
     */
    public static void openSwap(Player player) {
        if (player.getSkills().getStaticLevel(Skills.SUMMONING) < 21) {
            player.sendMessage("You need a Summoning level of at least 21 in order to do that.");
        } else {
            sendItemSelect(player, new String[]{"Value", "Swap 1", "Swap 5", "Swap 10", "Swap X"}, true, (slot, index) -> {
                BogrogPouchSwapper.handle(player, index, slot);
                return Unit.INSTANCE;
            });
        }
    }

}