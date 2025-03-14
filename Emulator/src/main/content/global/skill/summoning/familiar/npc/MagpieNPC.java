package content.global.skill.summoning.familiar.npc;

import content.global.skill.crafting.casting.Gem;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Magpie npc.
 */
@Initializable
public class MagpieNPC extends Forager {

    private static final Item[] ITEMS = new Item[]{Gem.SAPPHIRE.getUncut(), Gem.EMERALD.getUncut(), Gem.RUBY.getUncut(), Gem.DIAMOND.getUncut()};

    /**
     * Instantiates a new Magpie npc.
     */
    public MagpieNPC() {
        this(null, 6824);
    }

    /**
     * Instantiates a new Magpie npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public MagpieNPC(Player owner, int id) {
        super(owner, id, 3400, 12041, 3, ITEMS);
        boosts.add(new SkillBonus(Skills.THIEVING, 3));
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new MagpieNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        visualize(Animation.create(8020), Graphics.create(1336));
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.getSkills().updateLevel(Skills.THIEVING, 2);
        owner.visualize(new Animation(7660), new Graphics(1296));
    }

    @Override
    public int getRandom() {
        return 14;
    }

    @Override
    public int[] getIds() {
        return new int[]{6824};
    }

}
