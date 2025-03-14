package content.global.skill.fletching;

import core.game.dialogue.SkillDialogueHandler;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;

/**
 * The type Fletching logs.
 */
@Initializable
public class FletchingLogs extends UseWithHandler {

    /**
     * Instantiates a new Fletching logs.
     */
    public FletchingLogs() {
        super(Items.LOGS_1511, Items.OAK_LOGS_1521, Items.WILLOW_LOGS_1519, Items.MAPLE_LOGS_1517, Items.YEW_LOGS_1515, Items.MAGIC_LOGS_1513, Items.ACHEY_TREE_LOGS_2862, Items.MAHOGANY_LOGS_6332, Items.TEAK_LOGS_6333);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(Items.KNIFE_946, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(final NodeUsageEvent event) {
        final Player player = event.getPlayer();

        if (Fletching.isLog(event.getUsedItem().getId()) && event.getUsedWith().getId() == 946) {
            final Item log = event.getUsedItem();
            Item[] items = Fletching.getItems(log.getId());
            SkillDialogueHandler.SkillDialogue dialLength = SkillDialogueHandler.SkillDialogue.ONE_OPTION;
            switch (items.length) {
                case 2:
                    dialLength = SkillDialogueHandler.SkillDialogue.TWO_OPTION;
                    break;
                case 3:
                    dialLength = SkillDialogueHandler.SkillDialogue.THREE_OPTION;
                    break;
                case 4:
                    dialLength = SkillDialogueHandler.SkillDialogue.FOUR_OPTION;
                    break;
            }
            SkillDialogueHandler handler = new SkillDialogueHandler(player, dialLength, items) {

                @Override
                public void create(final int amount, int index) {
                    final Fletching.FletchingItems item = Fletching.getEntries(log.getId())[index];
                    player.getPulseManager().run(new FletchingPulse(player, log, amount, item));
                }

                @Override
                public int getAll(int index) {
                    return player.getInventory().getAmount(log);
                }

            };
            handler.open();
            return true;
        }

        return false;
    }

}