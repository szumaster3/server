package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.BurdenBeast;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Abyssal lurker npc.
 */
@Initializable
public class AbyssalLurkerNPC extends BurdenBeast {

    /**
     * Instantiates a new Abyssal lurker npc.
     */
    public AbyssalLurkerNPC() {
        this(null, 6820);
    }

    /**
     * Instantiates a new Abyssal lurker npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public AbyssalLurkerNPC(Player owner, int id) {
        super(owner, id, 4100, 12037, 3, 7, WeaponInterface.STYLE_CAST);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new AbyssalLurkerNPC(owner, id);
    }

    @Override
    public boolean isAllowed(Player owner, Item item) {
        if (item.getId() != 1436 && item.getId() != 7936) {
            owner.getPacketDispatch().sendMessage("Your familiar can only hold unnoted essence.");
            return false;
        }
        return super.isAllowed(owner, item);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        visualize(Animation.create(7682), Graphics.create(0));
        owner.getSkills().updateLevel(Skills.AGILITY, 4);
        owner.getSkills().updateLevel(Skills.THIEVING, 4);
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(new Animation(7660), new Graphics(1296));
    }

    @Override
    public int[] getIds() {
        return new int[]{6820, 6821};
    }

}
