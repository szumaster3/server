package content.region.kandarin.handlers;

import org.rs.consts.Items;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.plugin.ClassScanner;

/**
 * The type Sinclair flour barrel plugin.
 */
@Initializable
public final class SinclairFlourBarrelPlugin extends OptionHandler {
    private static final Item EMPTY_POT = new Item(Items.EMPTY_POT_1931);
    private static final Item FLOUR = new Item(Items.POT_OF_FLOUR_1933);
    private static final int DIARY_TASK_INDEX = 0;
    private static final int DIARY_TASK_ID = 5;
    private static final int MAX_FLOUR_COUNT = 4;

    @Override
    public boolean handle(Player player, Node node, String option) {
        return retrieveFlour(player, (Scenery) node);
    }

    private boolean retrieveFlour(final Player player, final Scenery barrel) {
        if (!player.getInventory().containsItem(EMPTY_POT)) {
            player.getPacketDispatch().sendMessage("I need an empty pot to hold the flour in.");
            return true;
        }

        if (player.getInventory().remove(EMPTY_POT)) {
            player.lock(3);
            player.getInventory().add(FLOUR);
            player.getPacketDispatch().sendMessage("You take some flour from the barrel.");
            updateDiaryProgress(player);
            player.getPacketDispatch().sendMessage("There's still plenty of flour left.");
            return true;
        }
        return false;
    }

    private void updateDiaryProgress(Player player) {
        if (!player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).isComplete(DIARY_TASK_INDEX, DIARY_TASK_ID)) {
            int currentFlourCount = player.getAttribute("diary:seers:sinclair-flour", 0);
            if (currentFlourCount >= MAX_FLOUR_COUNT) {
                player.setAttribute("/save:diary:seers:sinclair-flour", DIARY_TASK_ID);
                player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).updateTask(player, DIARY_TASK_INDEX, DIARY_TASK_ID, true);
            } else {
                player.setAttribute("/save:diary:seers:sinclair-flour", currentFlourCount + 1);
            }
        }
    }

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(26122).getHandlers().put("option:take from", this);
        ClassScanner.definePlugin(new FlourHandler());
        return this;
    }

    private class FlourHandler extends UseWithHandler {
        /**
         * Instantiates a new Flour handler.
         */
        public FlourHandler() {
            super(EMPTY_POT.getId());
        }

        @Override
        public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
            addHandler(org.rs.consts.Scenery.BARREL_OF_FLOUR_26122, OBJECT_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            return retrieveFlour(event.getPlayer(), event.getUsedWith().asScenery());
        }
    }
}
