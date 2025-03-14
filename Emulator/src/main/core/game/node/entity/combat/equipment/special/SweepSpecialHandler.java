package core.game.node.entity.combat.equipment.special;

import content.global.skill.summoning.familiar.Familiar;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.Animations;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * The type Sweep special handler.
 */
@Initializable
public final class SweepSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 30;

    private static final Animation ANIMATION = new Animation(Animations.ATTACK_WEAPON_1203, Priority.HIGH);

    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.DRAGON_HALBERD_SPECIAL_282, 96);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_HALBERD_3204, this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        BattleState[] targets = getTargets(entity, victim, state);
        state.setTargets(targets);
        for (BattleState s : targets) {
            s.setStyle(CombatStyle.MELEE);
            int hit = 0;
            if (isAccurateImpact(entity, s.getVictim(), CombatStyle.MELEE)) {
                hit = RandomFunction.random(calculateHit(entity, s.getVictim(), 1.1) + 1);
            }

            s.setEstimatedHit(hit);
            if (s.getVictim().size() > 1) {
                hit = 0;
                if (isAccurateImpact(entity, s.getVictim(), CombatStyle.MELEE, 0.75, 1.0)) {
                    hit = RandomFunction.random(calculateHit(entity, s.getVictim(), 1.1) + 1);
                }

                s.setSecondaryHit(hit);
            }
        }
        return 1;
    }

    private BattleState[] getTargets(Entity entity, Entity victim, BattleState state) {
        if (!entity.getProperties().isMultiZone() || !victim.getProperties().isMultiZone()) {
            return new BattleState[]{state};
        }
        Location vl = victim.getLocation();
        int x = vl.getX();
        int y = vl.getY();
        Direction dir = Direction.getDirection(x - entity.getLocation().getX(), y - entity.getLocation().getY());
        List<BattleState> l = new ArrayList<>(20);
        l.add(new BattleState(entity, victim));
        for (Entity n : victim instanceof NPC ? RegionManager.getSurroundingNPCs(victim, 9, entity, victim) : RegionManager.getSurroundingPlayers(victim, 9, entity, victim)) {
            if (n instanceof Familiar) {
                continue;
            }
            if (n.getLocation().equals(vl.transform(dir.getStepY(), dir.getStepX(), 0)) || n.getLocation().equals(vl.transform(-dir.getStepY(), -dir.getStepX(), 0))) {
                l.add(new BattleState(entity, n));
                if (l.size() >= 3) {
                    break;
                }
            }
        }
        return l.toArray(new BattleState[l.size()]);
    }

    @Override
    public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
        if (state.getTargets() != null) {
            for (BattleState s : state.getTargets()) {
                if (s != null) {
                    super.adjustBattleState(entity, s.getVictim(), s);
                }
            }
            return;
        }
        super.adjustBattleState(entity, victim, state);
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState state) {
        if (state.getTargets() != null) {
            for (BattleState s : state.getTargets()) {
                if (s != null) {
                    s.getVictim().getImpactHandler().handleImpact(entity, s.getEstimatedHit(), CombatStyle.MELEE, s);
                    if (s.getSecondaryHit() > -1) {
                        s.getVictim().getImpactHandler().handleImpact(entity, s.getSecondaryHit(), CombatStyle.MELEE, s);
                    }
                }
            }
            return;
        }
        victim.getImpactHandler().handleImpact(entity, state.getEstimatedHit(), CombatStyle.MELEE, state);
        if (state.getSecondaryHit() > -1) {
            victim.getImpactHandler().handleImpact(entity, state.getSecondaryHit(), CombatStyle.MELEE, state);
        }
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        if (state.getTargets() != null) {
            for (BattleState s : state.getTargets()) {
                if (s != null) {
                    s.getVictim().animate(victim.getProperties().getDefenceAnimation());
                }
            }
            return;
        }
        victim.animate(victim.getProperties().getDefenceAnimation());
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.HALBERD_SWIPE_2533);
        entity.visualize(ANIMATION, GRAPHICS);
    }

}
