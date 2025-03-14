package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Ice titan npc.
 */
@Initializable
public class IceTitanNPC extends ElementalTitanNPC {

    /**
     * Instantiates a new Ice titan npc.
     */
    public IceTitanNPC() {
        this(null, 7359);
    }

    /**
     * Instantiates a new Ice titan npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public IceTitanNPC(Player owner, int id) {
        super(owner, id, 6400, 12806, 20, WeaponInterface.STYLE_ACCURATE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new IceTitanNPC(owner, id);
    }


    @Override
    public void visualizeSpecialMove() {
        owner.visualize(new Animation(7660), new Graphics(1306));
    }

    @Override
    public int[] getIds() {
        return new int[]{7359, 7360};
    }

}
