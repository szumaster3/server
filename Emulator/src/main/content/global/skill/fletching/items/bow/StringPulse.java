package content.global.skill.fletching.items.bow;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.update.flag.context.Animation;

/**
 * The type String pulse.
 */
public class StringPulse extends SkillPulse<Item> {

    private final Strings bow;

    private int amount;

    /**
     * Instantiates a new String pulse.
     *
     * @param player the player
     * @param node   the node
     * @param bow    the bow
     * @param amount the amount
     */
    public StringPulse(Player player, Item node, final Strings bow, int amount) {
        super(player, node);
        this.bow = bow;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (getDelay() == 1) {
            setDelay(2);
        }
        if (player.getSkills().getLevel(Skills.FLETCHING) < bow.getLevel()) {
            player.getDialogueInterpreter().sendDialogue("You need a fletching level of " + bow.getLevel() + " to string this bow.");
            return false;
        }
        if (!player.getInventory().containsItem(new Item(bow.getUnfinished()))) {
            return false;
        }
        if (!player.getInventory().containsItem(new Item(bow.getString()))) {
            player.getDialogueInterpreter().sendDialogue("You seem to have run out of bow strings.");
            return false;
        }
        animate();
        return true;
    }

    @Override
    public void animate() {
        player.animate(Animation.create(bow.getAnimation()));
    }

    @Override
    public boolean reward() {
        if (player.getInventory().remove(new Item(bow.getUnfinished()), new Item(bow.getString()))) {
            player.getInventory().add(new Item(bow.getProduct()));
            player.getSkills().addExperience(Skills.FLETCHING, bow.getExperience(), true);
            player.getPacketDispatch().sendMessage("You add a string to the bow.");

            if (bow == Strings.MAGIC_SHORTBOW
                    && (new ZoneBorders(2721, 3489, 2724, 3493, 0).insideBorder(player)
                    || new ZoneBorders(2727, 3487, 2730, 3490, 0).insideBorder(player))
                    && player.getAttribute("diary:seers:fletch-magic-short-bow", false)) {
                player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 2, 2);
            }
        }
        if (!player.getInventory().containsItem(new Item(bow.getString())) || !player.getInventory().containsItem(new Item(bow.getUnfinished()))) {
            return true;
        }
        amount--;
        return amount == 0;
    }

    @Override
    public void message(int type) {
    }

}