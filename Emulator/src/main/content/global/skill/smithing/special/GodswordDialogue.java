package content.global.skill.smithing.special;

import core.game.dialogue.Dialogue;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import org.rs.consts.Animations;
import org.rs.consts.Items;

/**
 * The Godsword dialogue.
 */
@Initializable
public final class GodswordDialogue extends Dialogue {

    private static final Animation ANIMATION = new Animation(Animations.SMITH_HAMMER_898);

    private static final Item BLADE = new Item(Items.GODSWORD_BLADE_11690);

    private int used;

    public GodswordDialogue() {

    }

    public GodswordDialogue(Player player) {
        super(player);
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new GodswordDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        used = (int) args[0];
        interpreter.sendDialogue("You set to work, trying to fix the ancient sword.");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                boolean passBlade = true;
                int remove = -1;
                if (used == Items.GODSWORD_SHARDS_11692 && player.getInventory().contains(Items.GODSWORD_SHARD_1_11710, 1)) {
                    passBlade = false;
                    remove = Items.GODSWORD_SHARD_1_11710;
                }
                if (used == Items.GODSWORD_SHARD_1_11710 && player.getInventory().containItems(Items.GODSWORD_SHARDS_11692)) {
                    passBlade = false;
                    remove = Items.GODSWORD_SHARDS_11692;
                }
                if (used == Items.GODSWORD_SHARDS_11688 && player.getInventory().contains(Items.GODSWORD_SHARD_2_11712, 1)) {
                    passBlade = false;
                    remove = Items.GODSWORD_SHARD_2_11712;
                }
                if (used == Items.GODSWORD_SHARD_2_11712 && player.getInventory().containItems(Items.GODSWORD_SHARDS_11688)) {
                    passBlade = false;
                    remove = Items.GODSWORD_SHARDS_11688;
                }
                if (used == Items.GODSWORD_SHARDS_11686 && player.getInventory().contains(Items.GODSWORD_SHARD_3_11714, 1)) {
                    passBlade = false;
                    remove = Items.GODSWORD_SHARD_3_11714;
                }
                if (used == Items.GODSWORD_SHARD_3_11714 && player.getInventory().containItems(Items.GODSWORD_SHARDS_11686)) {
                    passBlade = false;
                    remove = Items.GODSWORD_SHARDS_11686;
                }
                if (!passBlade) {
                    if (player.getInventory().remove(new Item(used)) && player.getInventory().remove(new Item(remove))) {
                        player.lock(5);
                        player.animate(ANIMATION);
                        interpreter.sendDialogue("Even for an experienced smith it is not an easy task, but eventually", "it is done.");
                        player.getSkills().addExperience(Skills.SMITHING, 100, true);
                        player.getInventory().add(BLADE);
                    }
                    return true;
                }
                int base = -1;
                if (used == Items.GODSWORD_SHARD_1_11710) {
                    if (player.getInventory().contains(Items.GODSWORD_SHARD_2_11712, 1)) {
                        base = Items.GODSWORD_SHARD_2_11712;
                    } else if (player.getInventory().contains(Items.GODSWORD_SHARD_3_11714, 1)) {
                        base = Items.GODSWORD_SHARD_3_11714;
                    }
                }
                if (used == Items.GODSWORD_SHARD_2_11712) {
                    if (player.getInventory().contains(Items.GODSWORD_SHARD_1_11710, 1)) {
                        base = Items.GODSWORD_SHARD_1_11710;
                    } else if (player.getInventory().contains(Items.GODSWORD_SHARD_3_11714, 1)) {
                        base = Items.GODSWORD_SHARD_3_11714;
                    }
                }
                if (used == Items.GODSWORD_SHARD_3_11714) {
                    if (player.getInventory().contains(Items.GODSWORD_SHARD_2_11712, 1)) {
                        base = Items.GODSWORD_SHARD_2_11712;
                    } else if (player.getInventory().contains(Items.GODSWORD_SHARD_1_11710, 1)) {
                        base = Items.GODSWORD_SHARD_1_11710;
                    }
                }
                if (base == -1) {
                    end();
                    player.getPacketDispatch().sendMessage("You didn't have all the required items.");
                    return true;
                }
                if (player.getInventory().remove(new Item(used)) && player.getInventory().remove(new Item(base))) {
                    int shard = -1;
                    if (used == Items.GODSWORD_SHARD_1_11710 && base == Items.GODSWORD_SHARD_2_11712 || used == Items.GODSWORD_SHARD_2_11712 && base == Items.GODSWORD_SHARD_1_11710) {
                        shard = Items.GODSWORD_SHARDS_11686;
                    } else if (used == Items.GODSWORD_SHARD_1_11710 && base == Items.GODSWORD_SHARD_3_11714 || used == Items.GODSWORD_SHARD_3_11714 && base == Items.GODSWORD_SHARD_1_11710) {
                        shard = Items.GODSWORD_SHARDS_11688;
                    }
                    if (used == Items.GODSWORD_SHARD_2_11712 && base == Items.GODSWORD_SHARD_3_11714 || used == Items.GODSWORD_SHARD_3_11714 && base == Items.GODSWORD_SHARD_2_11712) {
                        shard = Items.GODSWORD_SHARDS_11692;
                    }
                    player.lock(5);
                    player.animate(ANIMATION);
                    interpreter.sendDialogue("Even for an experienced smith it is not an easy task, but eventually", "it is done.");
                    player.getSkills().addExperience(Skills.SMITHING, 100);
                    player.getInventory().add(new Item(shard));
                }
                stage = 1;
                break;
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{62362};
    }
}
