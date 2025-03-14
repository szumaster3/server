package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Barker toad npc.
 */
@Initializable
public class BarkerToadNPC extends Familiar {

    /**
     * Instantiates a new Barker toad npc.
     */
    public BarkerToadNPC() {
        this(null, 6889);
    }

    /**
     * Instantiates a new Barker toad npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public BarkerToadNPC(Player owner, int id) {
        super(owner, id, 800, 12123, 6, WeaponInterface.STYLE_AGGRESSIVE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new BarkerToadNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Entity target = special.getTarget();
        if (!canCombatSpecial(target)) {
            return false;
        }
        animate(getProperties().getAttackAnimation());
        graphics(Graphics.create(1403));
        sendFamiliarHit(target, 8, Graphics.create(1404));
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{6889, 6890};
    }

}
