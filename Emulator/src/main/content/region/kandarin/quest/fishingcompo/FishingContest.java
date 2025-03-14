package content.region.kandarin.quest.fishingcompo;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.plugin.Initializable;

import static core.api.ContentAPIKt.removeAttribute;

/**
 * The type Fishing contest.
 */
@Initializable
public class FishingContest extends Quest {

    /**
     * Instantiates a new Fishing contest.
     */
    public FishingContest() {
        super("Fishing Contest", 62, 61, 1, 11, 0, 1, 5);
    }

    /**
     * The constant FISHING_ROD.
     */
    public static final Item FISHING_ROD = new Item(307);

    /**
     * The constant FISHING_PASS.
     */
    public static final Item FISHING_PASS = new Item(27);

    /**
     * The constant RED_VINE_WORM.
     */
    public static final Item RED_VINE_WORM = new Item(25);

    /**
     * The constant RAW_GIANT_CARP.
     */
    public static final Item RAW_GIANT_CARP = new Item(338);

    /**
     * The constant GIANT_CARP.
     */
    public static final Item GIANT_CARP = new Item(337);

    /**
     * The constant FISHING_TROPHY.
     */
    public static final Item FISHING_TROPHY = new Item(26);

    /**
     * The constant GARLIC.
     */
    public static final Item GARLIC = new Item(1550);

    /**
     * The constant SPADE.
     */
    public static final Item SPADE = new Item(952);

    @Override
    public void drawJournal(Player player, int stage) {
        int line = 11;
        super.drawJournal(player, stage);
        if (stage < 10) {
            line(player, "I can start this quest by trying to take the !!shortcut??", line++);
            line(player, "near !!White Wolf Mountain??", line++);
            line(player, "I need level !!10 Fishing?? to start this quest.", line++, player.getSkills().getLevel(Skills.FISHING) >= 10);
        } else if (stage >= 10) {
            line(player, "The !!mountain Dwarves' home?? would be an ideal way to get across ", line++, stage >= 20);
            line(player, "White Wolf Mountain safely. However, the Dwarves aren't too", line++, stage >= 20);
            line(player, "fond of strangers. They will let you through if you can !!bring ", line++, stage >= 20);
            line(player, "!!them a trophy.?? The trophy is the prize for the annual Hemenster", line++, stage >= 20);
            line(player, "!!fishing competition.??", line++, stage >= 20);
            if (stage == 20)
                line(player, "I should return to !!Austri?? or !!Vestri??.", line++);

            if (stage >= 100) {
                line++;
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++);
            }
        }
    }

    @Override
    public void finish(Player player) {
        int ln = 10;
        super.finish(player);
        player.getPacketDispatch().sendItemZoomOnInterface(FISHING_TROPHY.getId(), 230, 277, 5);
        drawReward(player, "1 Quest Point", ln++);
        drawReward(player, "2437 Fishing XP.", ln++);
        drawReward(player, "Access to the White Wolf Mountain shortcut.", ln);
        removeAttribute(player, "fishing_contest:garlic");
        removeAttribute(player, "fishing_contest:won");
        removeAttribute(player, "fishing_contest:pass-shown");
        player.getSkills().addExperience(Skills.FISHING, 2437);
    }

    @Override
    public Quest newInstance(Object object) {
        return this;
    }

}
