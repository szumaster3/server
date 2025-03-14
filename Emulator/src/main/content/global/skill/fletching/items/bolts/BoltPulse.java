package content.global.skill.fletching.items.bolts;

import content.global.skill.slayer.SlayerManager;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import org.rs.consts.Items;

/**
 * The type Bolt pulse.
 */
public final class BoltPulse extends SkillPulse<Item> {

	private Item feather;

	private static final Item[] FEATHER = new Item[] {
			new Item(Items.FEATHER_314),
			new Item(Items.STRIPY_FEATHER_10087),
			new Item(Items.RED_FEATHER_10088),
			new Item(Items.BLUE_FEATHER_10089),
			new Item(Items.YELLOW_FEATHER_10090),
			new Item(Items.ORANGE_FEATHER_10091)
	};

	private final Bolt bolt;

	private int sets;

	private boolean useSets = false;

	/**
	 * Instantiates a new Bolt pulse.
	 *
	 * @param player  the player
	 * @param node    the node
	 * @param bolt    the bolt
	 * @param feather the feather
	 * @param sets    the sets
	 */
	public BoltPulse(Player player, Item node, final Bolt bolt, final Item feather, final int sets) {
		super(player, node);
		this.bolt = bolt;
		this.sets = sets;
		this.feather = feather;
	}

	@Override
	public boolean checkRequirements() {
		if (bolt.getUnfinished() == 13279) {
			if (!SlayerManager.getInstance(player).flags.isBroadsUnlocked()) {
				player.getDialogueInterpreter().sendDialogue("You need to unlock the ability to create broad bolts.");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.FLETCHING) < bolt.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a fletching level of " + bolt.getLevel() + " in order to do this.");
			return false;
		}
		if (!player.getInventory().containsItem(feather)) {
			return false;
		}
		if (!player.getInventory().containsItem(new Item(bolt.getUnfinished()))) {
			return false;
		}
		if (!player.getInventory().hasSpaceFor(new Item(bolt.getFinished()))) {
			player.getDialogueInterpreter().sendDialogue("You do not have enough inventory space.");
			return false;
		}

		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		int featherAmount = player.getInventory().getAmount(feather);
		int boltAmount = player.getInventory().getAmount(bolt.getUnfinished());
		if (getDelay() == 1) {
			super.setDelay(3);
		}
		final Item unfinished = new Item(bolt.getUnfinished());
		if (featherAmount >= 10 && boltAmount >= 10) {
			feather.setAmount(10);
			unfinished.setAmount(10);
			player.getPacketDispatch().sendMessage("You fletch 10 bolts.");
		} else {
			int amount = featherAmount > boltAmount ? boltAmount : featherAmount;
			feather.setAmount(amount);
			unfinished.setAmount(amount);
			player.getPacketDispatch().sendMessage(amount == 1 ? "You attach a feather to a bolt." : "You fletch " + amount + " bolts");
		}
		if (player.getInventory().remove(feather, unfinished)) {
			Item product = new Item(bolt.getFinished());
			product.setAmount(feather.getAmount());
			player.getSkills().addExperience(Skills.FLETCHING, product.getAmount() * bolt.getExperience(), true);
			player.getInventory().add(product);
		}
		feather.setAmount(1);
		if (!player.getInventory().containsItem(feather)) {
			return true;
		}
		if (!player.getInventory().containsItem(new Item(bolt.getUnfinished()))) {
			return true;
		}
		sets--;
		return sets <= 0;
	}

	@Override
	public void message(int type) {
	}

}
