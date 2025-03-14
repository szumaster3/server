package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import content.global.skill.summoning.familiar.Forager;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;

/**
 * The type Evil turnip npc.
 */
@Initializable
public class EvilTurnipNPC extends Forager {

    private static final Item EVIL_TURNIP = new Item(12136);

    /**
     * Instantiates a new Evil turnip npc.
     */
    public EvilTurnipNPC() {
        this(null, 6833);
    }

    /**
     * Instantiates a new Evil turnip npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public EvilTurnipNPC(Player owner, int id) {
        super(owner, id, 3000, 12051, 6, WeaponInterface.STYLE_RANGE_ACCURATE, EVIL_TURNIP);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new EvilTurnipNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Entity target = (Entity) special.getNode();
        if (!canCombatSpecial(target)) {
            return false;
        }
        int ticks = 2 + (int) Math.floor(getLocation().getDistance(target.getLocation()) * 0.5);
        getSkills().heal(2);
        faceTemporary(target, 2);
        sendFamiliarHit(target, 10);
        animate(Animation.create(8251));
        target.graphics(Graphics.create(1329), ticks);
        target.getSkills().updateLevel(Skills.MAGIC, -1, 0);
        Projectile.magic(this, target, 1330, 40, 36, 51, 10).send();
        return true;
    }

    @Override
    public int getRandom() {
        return 20;
    }

    @Override
    public int[] getIds() {
        return new int[]{6833, 6834};
    }

}
