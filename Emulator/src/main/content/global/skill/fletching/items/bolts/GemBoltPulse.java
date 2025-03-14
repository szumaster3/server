package content.global.skill.fletching.items.bolts;

import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;

/**
 * Represents the attaching of a gem bolt to a premade bolt.
 *
 * @author Ceikry
 */
public final class GemBoltPulse extends SkillPulse<Item> {

    /**
     * Represents the gem bolt being made.
     */
    private GemBolt bolt;

    /**
     * Represents the sets to make.
     */
    private int sets = 0;

    /**
     * Represents the ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new {@code GemBoltPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param bolt   the bolt
     * @param sets   the sets.
     */
    public GemBoltPulse(Player player, Item node, GemBolt bolt, int sets) {
        super(player, node);
        this.bolt = bolt;
        this.sets = sets;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.FLETCHING) < bolt.getLevel()) {
            player.getDialogueInterpreter().sendDialogue("You need a Fletching level of " + bolt.getLevel() + " or above to do that.");
            return false;
        }
        if (!player.getInventory().containsItem(new Item(bolt.getBase())) || !player.getInventory().containsItem(new Item(bolt.getTip()))) {
            return false;
        }
        if (!player.getInventory().hasSpaceFor(new Item(bolt.getProduct()))) {
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
        if (++ticks % 3 != 0) {
            return false;
        }
        int baseAmount = player.getInventory().getAmount(bolt.getBase());
        int tipAmount = player.getInventory().getAmount(bolt.getTip());
        Item base = new Item(bolt.getBase());
        Item tip = new Item(bolt.getTip());
        Item product = new Item(bolt.getProduct());
        if (baseAmount >= 10 && tipAmount >= 10) {
            base.setAmount(10);
            tip.setAmount(10);
            product.setAmount(10);
        } else {
            int amount = Math.min(baseAmount, tipAmount);
            base.setAmount(amount);
            tip.setAmount(amount);
            product.setAmount(amount);
        }
        if (player.getInventory().remove(base, tip)) {
            player.getInventory().add(product);
            player.getSkills().addExperience(Skills.FLETCHING, bolt.getExperience() * product.getAmount(), true);
            player.getPacketDispatch().sendMessage(product.getAmount() == 1 ? "You attach the tip to the bolt." : "You fletch " + product.getAmount() + " bolts.");
        }
        sets--;
        return sets <= 0;
    }

}
