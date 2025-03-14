package content.global.skill.summoning.familiar.npc;

import content.global.skill.summoning.familiar.BurdenBeast;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

/**
 * The type Thorny snail npc.
 */
@Initializable
public class ThornySnailNPC extends BurdenBeast {

    /**
     * Instantiates a new Thorny snail npc.
     */
    public ThornySnailNPC() {
        this(null, NPCs.THORNY_SNAIL_6806);
    }

    /**
     * Instantiates a new Thorny snail npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    public ThornySnailNPC(Player owner, int id) {
        super(owner, id, 1600, Items.THORNY_SNAIL_POUCH_12019, 3, 3);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new ThornySnailNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Entity target = (Entity) special.getNode();
        if (!canCombatSpecial(target)) {
            return false;
        }
        visualizeSpecialMove();
        visualize(new Animation(8148, Priority.HIGH), Graphics.create(1385));
        Projectile.magic(this, target, 1386, 40, 36, 51, 10).send();
        int ticks = 2 + (int) Math.floor(getLocation().getDistance(target.getLocation()) * 0.5);
        getProperties().getCombatPulse().setNextAttack(4);
        faceTemporary(target, 2);
        GameWorld.getPulser().submit(new Pulse(ticks, this, target) {
            @Override
            public boolean pulse() {
                BattleState state = new BattleState(ThornySnailNPC.this, target);
                int hit = 0;
                if (CombatStyle.MAGIC.getSwingHandler().isAccurateImpact(ThornySnailNPC.this, target)) {
                    hit = RandomFunction.randomize(8);
                }
                state.setEstimatedHit(hit);
                target.getImpactHandler().handleImpact(owner, hit, CombatStyle.MAGIC, state);
                target.graphics(new Graphics(1387));
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean isPoisonImmune() {
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.THORNY_SNAIL_6806, NPCs.THORNY_SNAIL_6807};
    }

}
