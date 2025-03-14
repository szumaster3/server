package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Spirit mosquito npc.
 */
@Initializable
public class SpiritMosquitoNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Spirit mosquito npc.
     */
    public SpiritMosquitoNPC() {
        this(null, 7331);
    }

    /**
     * Instantiates a new Spirit mosquito npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public SpiritMosquitoNPC(Player owner, int id) {
        super(owner, id, 1200, 12778, 3, WeaponInterface.STYLE_ACCURATE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SpiritMosquitoNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Entity target = (Entity) special.getNode();
        if (!canAttack(target)) {
            return false;
        }
        visualize(Animation.create(8032), Graphics.create(1442));
        getProperties().getCombatPulse().attack(target);
        return true;
    }

    @Override
    public boolean isPoisonImmune() {
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{7331, 7332};
    }

}
