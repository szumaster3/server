package content.global.skill.slayer.npc;

import content.global.skill.slayer.Tasks;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.MultiSwingHandler;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.combat.equipment.special.DragonfireSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * The type Mithril dragon npc.
 */
@Initializable
public class MithrilDragonNPC extends AbstractNPC {

    /**
     * Instantiates a new Mithril dragon npc.
     */
    public MithrilDragonNPC() {
        super(0, null);
    }

    private final CombatSwingHandler combatAction = new MultiSwingHandler(
        true,
        new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(80, Priority.HIGH)),
        new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(80, Priority.HIGH)),
        new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), new Animation(81, Priority.HIGH), null, null, Projectile.create((Entity) null, null, 500, 20, 20, 41, 40, 18, 255)), DRAGONFIRE,
        new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), new Animation(81, Priority.HIGH), null, null, Projectile.create((Entity) null, null, 16, 20, 20, 41, 40, 18, 255))
    );

    /**
     * Instantiates a new Mithril dragon npc.
     *
     * @param id       the id
     * @param location the location
     */
    public MithrilDragonNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new MithrilDragonNPC(id, location);
    }

    @Override
    public void sendImpact(BattleState state) {
        CombatStyle style = state.getStyle();
        if (style == null) {
            return;
        }
        int maxHit = state.getEstimatedHit();
        if (maxHit < 1) {
            return;
        }
        switch (style) {
            case MELEE:
                maxHit = 28;
                break;
            case MAGIC:
                maxHit = 18;
                break;
            case RANGE:
                maxHit = 18;
                break;
        }
        if (state.getEstimatedHit() > maxHit) {
            state.setEstimatedHit(RandomFunction.random(maxHit - 5, maxHit));
        }
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatAction;
    }

    @Override
    public int getDragonfireProtection(boolean fire) {
        return 0x2 | 0x4 | 0x8;
    }

    @Override
    public int[] getIds() {
        return Tasks.MITHRIL_DRAGONS.getNpcs();
    }

    private static final SwitchAttack DRAGONFIRE = DragonfireSwingHandler.get(false, 52, new Animation(81, Priority.HIGH), Graphics.create(1), null, null);
}
