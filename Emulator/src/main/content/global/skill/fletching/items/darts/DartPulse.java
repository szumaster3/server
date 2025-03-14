package content.global.skill.fletching.items.darts;

import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import org.rs.consts.Quests;

import static core.api.ContentAPIKt.hasSpaceFor;
import static core.api.ContentAPIKt.sendDialogue;

/**
 * The type Dart pulse.
 */
public final class DartPulse extends SkillPulse<Item> {

	private static final Item FEATHER = new Item(314);

	private final Dart dart;

	private int sets;

	/**
	 * Instantiates a new Dart pulse.
	 *
	 * @param player the player
	 * @param node   the node
	 * @param dart   the dart
	 * @param sets   the sets
	 */
	public DartPulse(Player player, Item node, Dart dart, int sets) {
		super(player, node);
		this.dart = dart;
		this.sets = sets;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getLevel(Skills.FLETCHING) < dart.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a fletching level of " + dart.getLevel() + " to do this.");
			return false;
		}
		if (!player.getQuestRepository().isComplete(Quests.THE_TOURIST_TRAP)){
			player.getDialogueInterpreter().sendDialogue("You need to have completed Tourist Trap to fletch darts.");
			return false;
		}
		if (!hasSpaceFor(player, new Item(dart.getFinished()))) {
			sendDialogue(player, "You do not have enough inventory space.");
			return false;
		}
		return true;
	}

	@Override
	public void animate() {

	}

	@Override
	public boolean reward() {
		if (getDelay() == 1) {
			super.setDelay(3);
		}
		final Item unfinished = new Item(dart.getUnfinished());
		final int dartAmount = player.getInventory().getAmount(unfinished);
		final int featherAmount = player.getInventory().getAmount(FEATHER);
		if (dartAmount >= 10 && featherAmount >= 10) {
			FEATHER.setAmount(10);
			unfinished.setAmount(10);
			player.getPacketDispatch().sendMessage("You attach feathers to 10 darts.");
		} else {
			int amount = featherAmount > dartAmount ? dartAmount : featherAmount;
			FEATHER.setAmount(amount);
			unfinished.setAmount(amount);
			player.getPacketDispatch().sendMessage(amount == 1 ? "You attach a feather to a dart." : "You attach feathers to " + amount + " darts.");
		}
		if (player.getInventory().remove(FEATHER, unfinished)) {
			Item product = new Item(dart.getFinished());
			product.setAmount(FEATHER.getAmount());
			player.getSkills().addExperience(Skills.FLETCHING, dart.getExperience() * product.getAmount(), true);
			player.getInventory().add(product);
		}
		FEATHER.setAmount(1);
		if (!player.getInventory().containsItem(FEATHER)) {
			return true;
		}
		if (!player.getInventory().containsItem(new Item(dart.getUnfinished()))) {
			return true;
		}
		sets--;
		return sets == 0;
	}

	@Override
	public void message(int type) {
	}

}
