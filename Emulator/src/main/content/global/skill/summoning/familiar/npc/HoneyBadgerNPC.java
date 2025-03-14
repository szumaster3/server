package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Honey badger npc.
 */
@Initializable
public class HoneyBadgerNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Honey badger npc.
     */
    public HoneyBadgerNPC() {
        this(null, 6845);
    }

    /**
     * Instantiates a new Honey badger npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public HoneyBadgerNPC(Player owner, int id) {
        super(owner, id, 2500, 12065, 4, WeaponInterface.STYLE_AGGRESSIVE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new HoneyBadgerNPC(owner, id);
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1399));
    }

    @Override
    public String getText() {
        return "Raaaar!";
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (isCharged()) {
            return false;
        }
        charge();
        visualize(new Animation(7928, Priority.HIGH), Graphics.create(1397));
        return true;
    }

    @Override
    public boolean isCharged() {
        if (charged) {
            owner.getPacketDispatch().sendMessage("Your honey badger is already enraged!");
            return true;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{6845, 6846};
    }

}
