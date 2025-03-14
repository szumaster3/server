package content.global.skill.smithing;

import content.global.skill.smithing.smelting.Bar;
import core.game.component.Component;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.tools.StringUtils;

/**
 * The type Smithing builder.
 */
public final class SmithingBuilder {

	private BarType type;

	private Bar bar;

    /**
     * Instantiates a new Smithing builder.
     *
     * @param item the item
     */
    public SmithingBuilder(Item item) {
		this.bar = Bar.forId(item.getId());
		this.type = BarType.getBarTypeForId(item.getId());
	}

    /**
     * Build.
     *
     * @param player the player
     */
    public void build(Player player) {
		player.getGameAttributes().removeAttribute("smith-type");
		player.getGameAttributes().setAttribute("smith-type", type);
		if (type.name().equals("BLURITE")) {
			int[] values = {17, 25, 33, 41, 57, 65, 73, 105, 113, 129, 137, 145, 153, 177, 185, 193, 201, 217, 225, 233, 241,
			};
			for (int childId : values) {
				player.getPacketDispatch().sendInterfaceConfig(300, childId, true);

			}
		}
		else {
			player.getPacketDispatch().sendInterfaceConfig(300, 267, false);// pickaxe
		}
		final Bars bars[] = Bars.getBars(type);
		for (int i = 0; i < bars.length; i++) {
			if (bars[i].getSmithingType() == SmithingType.TYPE_GRAPPLE_TIP) {
				player.getPacketDispatch().sendInterfaceConfig(300, 169, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_DART_TIP) {
				player.getPacketDispatch().sendInterfaceConfig(300, 65, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_WIRE){
				player.getPacketDispatch().sendInterfaceConfig(300, 81, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_SPIT_IRON){
				player.getPacketDispatch().sendInterfaceConfig(300, 89, false);
			}
			if ( bars[i].getSmithingType() == SmithingType.TYPE_BULLSEYE) {
				player.getPacketDispatch().sendInterfaceConfig(300, 161, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_CLAWS) {
				player.getPacketDispatch().sendInterfaceConfig(300, 209, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_OIL_LANTERN) {
				player.getPacketDispatch().sendInterfaceConfig(300, 161, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_STUDS) {
				player.getPacketDispatch().sendInterfaceConfig(300, 97, false);
			}
			if (type.name().equals("BLURITE") && bars[i].getSmithingType() == SmithingType.TYPE_Crossbow_Bolt || bars[i].getSmithingType() == SmithingType.TYPE_Crossbow_Limb) {
				player.getPacketDispatch().sendInterfaceConfig(300, 249, false);
				player.getPacketDispatch().sendInterfaceConfig(300, 15, true);
			}
			String color = "";
			if (player.getSkills().getLevel(Skills.SMITHING) < bars[i].level) {
			} else {
				color = "<col=FFFFFF>";
			}
			player.getPacketDispatch().sendString(color + StringUtils.formatDisplayName(bars[i].getSmithingType().name().replace("TYPE_", "")), 300, bars[i].getSmithingType().getDisplayName());
			if (player.getInventory().contains(bars[i].getBarType().getBarType(), bars[i].getSmithingType().required)) {
				color = "<col=2DE120>";
			} else {
				color = null;
			}
			if (color != null) {
				String amt = bars[i].getSmithingType().required > 1 ? "s" : "";
				player.getPacketDispatch().sendString(color + String.valueOf(bars[i].getSmithingType().required) + " Bar" + amt, 300, bars[i].getSmithingType().getDisplayName() + 1);
			}
			InterfaceContainer.generateItems(player, new Item[] { new Item(bars[i].product, bars[i].getSmithingType().getProductAmount()) }, new String[] { "" }, 300, bars[i].getSmithingType().child - 1);
		}
		player.getPacketDispatch().sendString(type.getBarName(), 300, 14);
		player.getInterfaceManager().open(new Component(300));
	}

    /**
     * Gets type.
     *
     * @return the type
     */
    public BarType getType() {
		return type;
	}

    /**
     * Gets bar.
     *
     * @return the bar
     */
    public Bar getBar() {
		return bar;
	}

}
