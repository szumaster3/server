package content.global.skill.construction.servants;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The type House servant plugin.
 */
@Initializable
public class HouseServantPlugin extends UseWithHandler {

    /**
     * The Ids.
     */
    final static int[] IDS = {1511, 1521, 6333, 6332};

    /**
     * Instantiates a new House servant plugin.
     */
    public HouseServantPlugin() {
        super(IDS);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(4235, NPC_TYPE, this);
        addHandler(4237, NPC_TYPE, this);
        addHandler(4239, NPC_TYPE, this);
        addHandler(4241, NPC_TYPE, this);
        addHandler(4243, NPC_TYPE, this);
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