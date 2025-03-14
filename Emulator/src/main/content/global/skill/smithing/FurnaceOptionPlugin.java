package content.global.skill.smithing;

import content.global.skill.smithing.smelting.Bar;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Quests;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Furnace option plugin.
 */
@Initializable
public final class FurnaceOptionPlugin extends OptionHandler {

	private static final Animation ANIMATION = new Animation(833);

	private static final Item[] ITEMS = new Item[] { new Item(438, 1), new Item(436, 1) };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.setOptionHandler("smelt", this);
		SceneryDefinition.setOptionHandler("smelt-ore", this);
		SceneryDefinition.forId(3044).getHandlers().put("option:use", this);
		SceneryDefinition.forId(21303).getHandlers().put("option:use", this);
		new SmeltUseWithHandler().newInstance(arg);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		if (node.getId() == 26814 && !player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).isComplete(0)) {
			player.sendMessage("You need to have completed the easy tasks in the Varrock Diary in order to use this.");
			return true;
		}
		show(player);
		return true;
	}

	private static void show(final Player player) {
		player.getInterfaceManager().openChatbox(311);
		player.getPacketDispatch().sendItemZoomOnInterface(2349, 150, 311, 4);
		if (player.getQuestRepository().isComplete(Quests.THE_KNIGHTS_SWORD)) {
			player.getPacketDispatch().sendString("<br><br><br><br><col=000000>Blurite", 311, 20);
		}
		player.getPacketDispatch().sendItemZoomOnInterface(Bar.BLURITE.product.getId(), 150, 311, 5);
		player.getPacketDispatch().sendItemZoomOnInterface(Bar.IRON.product.getId(), 150, 311, 6);
		player.getPacketDispatch().sendItemZoomOnInterface(2355, 150, 311, 7);
		player.getPacketDispatch().sendItemZoomOnInterface(2353, 150, 311, 8);
		player.getPacketDispatch().sendItemZoomOnInterface(2357, 150, 311, 9);
		player.getPacketDispatch().sendItemZoomOnInterface(2359, 150, 311, 10);
		player.getPacketDispatch().sendItemZoomOnInterface(2361, 150, 311, 11);
		player.getPacketDispatch().sendItemZoomOnInterface(2363, 150, 311, 12);
	}

    /**
     * The type Smelt use with handler.
     */
    public final static class SmeltUseWithHandler extends UseWithHandler {

        /**
         * The constant furnaceIDS.
         */
        public static final int[] furnaceIDS = new int[] { 4304, 6189, 11010, 11666, 12100, 12809, 14921, 18497, 26814, 30021, 30510, 36956, 37651 };

        /**
         * Instantiates a new Smelt use with handler.
         */
        public SmeltUseWithHandler() {
			super(getIds());
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int i : furnaceIDS) {
				addHandler(i, OBJECT_TYPE, this);
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			if (event.getUsedWith().getId() == 26814 && !event.getPlayer().getAchievementDiaryManager().getDiary(DiaryType.VARROCK).isComplete(0)) {
				event.getPlayer().sendMessage("You need to have completed the easy tasks in the Varrock Diary in order to use this.");
				return true;
			}
			show(event.getPlayer());
			return true;
		}

        /**
         * Get ids int [ ].
         *
         * @return the int [ ]
         */
        public static final int[] getIds() {
			List<Integer> ids = new ArrayList<>(10);
			for (Bar bar : Bar.values()) {
				for (Item i : bar.ores) {
					ids.add(i.getId());
				}
			}
			int[] array = new int[ids.size()];
			for (int i = 0; i < ids.size(); i++) {
				array[i] = ids.get(i);
			}
			return array;
		}
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return null;
	}
}
