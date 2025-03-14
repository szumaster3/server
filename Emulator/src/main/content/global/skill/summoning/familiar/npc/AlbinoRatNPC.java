package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Albino rat npc.
 */
@Initializable
public class AlbinoRatNPC extends Forager {

    private static final Item CHEESE = new Item(1985, 4);

    /**
     * Instantiates a new Albino rat npc.
     */
    public AlbinoRatNPC() {
        this(null, 6847);
    }

    /**
     * Instantiates a new Albino rat npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public AlbinoRatNPC(Player owner, int id) {
        super(owner, id, 2200, 12067, 6, WeaponInterface.STYLE_ACCURATE, CHEESE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new AlbinoRatNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (produceItem(CHEESE)) {
            owner.lock(7);
            visualize(Animation.create(4934), Graphics.create(1384));
            return true;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{6847, 6848};
    }

}
