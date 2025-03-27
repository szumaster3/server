package content.global.skill.construction.servants;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The type House servant plugin.
 */
@Initializable
public class HouseServantPlugin extends UseWithHandler {

    /**
     * The Ids.
     */
    final static int[] IDS = {Items.LOGS_1511, Items.OAK_LOGS_1521, Items.TEAK_LOGS_6333, Items.MAHOGANY_LOGS_6332};

    /**
     * Instantiates a new House servant plugin.
     */
    public HouseServantPlugin() {
        super(IDS);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(NPCs.RICK_4235, NPC_TYPE, this);
        addHandler(NPCs.MAID_4237, NPC_TYPE, this);
        addHandler(NPCs.COOK_4239, NPC_TYPE, this);
        addHandler(NPCs.BUTLER_4241, NPC_TYPE, this);
        addHandler(NPCs.DEMON_BUTLER_4243, NPC_TYPE, this);
        ClassScanner.definePlugin(new HouseServantDialogue());
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        if (event.getUsedItem() == null || event.getUsedWith() == null) {
            return true;
        }
        event.getPlayer().getDialogueInterpreter().open(event.getUsedWith().asNpc().getId(), event.getUsedWith().asNpc(), true, event.getUsedItem());
        return true;
    }

}
