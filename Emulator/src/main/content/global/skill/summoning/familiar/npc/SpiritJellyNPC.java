package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;

/**
 * The type Spirit jelly npc.
 */
@Initializable
public class SpiritJellyNPC extends content.global.skill.summoning.familiar.Familiar {

    /**
     * Instantiates a new Spirit jelly npc.
     */
    public SpiritJellyNPC() {
        this(null, 6992);
    }

    /**
     * Instantiates a new Spirit jelly npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public SpiritJellyNPC(Player owner, int id) {
        super(owner, id, 4300, 12027, 6, WeaponInterface.STYLE_AGGRESSIVE);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SpiritJellyNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Entity target = (Entity) special.getNode();
        if (!canCombatSpecial(target)) {
            return false;
        }
        faceTemporary(target, 2);
        sendFamiliarHit(target, 13);
        animate(Animation.create(8575));
        Projectile.magic(this, target, 1360, 40, 36, 51, 10).send();
        target.getSkills().updateLevel(Skills.ATTACK, -3, target.getSkills().getStaticLevel(Skills.ATTACK) - 3);
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{6992, 6993};
    }

}
