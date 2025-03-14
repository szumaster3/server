package core.game.node.entity.combat.equipment.special;

import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.*;
import static core.api.combat.DamageCalculatorKt.calculateDragonFireMaxHit;

/**
 * The type Dragonfire swing handler.
 */
public class DragonfireSwingHandler extends CombatSwingHandler {

    private boolean meleeRange;

    private int maximumHit;

    private SwitchAttack attack;

    private boolean fire;

    /**
     * Instantiates a new Dragonfire swing handler.
     *
     * @param meleeRange the melee range
     * @param maximumHit the maximum hit
     * @param attack     the attack
     * @param fire       the fire
     */
    public DragonfireSwingHandler(boolean meleeRange, int maximumHit, SwitchAttack attack, boolean fire) {
        super(CombatStyle.MAGIC);
        this.meleeRange = meleeRange;
        this.maximumHit = maximumHit;
        this.attack = attack;
        this.fire = fire;
    }

    /**
     * Get switch attack.
     *
     * @param meleeRange    the melee range
     * @param maximumHit    the maximum hit
     * @param animation     the animation
     * @param startGraphics the start graphics
     * @param endGraphics   the end graphics
     * @param projectile    the projectile
     * @return the switch attack
     */
    public static SwitchAttack get(boolean meleeRange, int maximumHit, Animation animation, Graphics startGraphics, Graphics endGraphics, Projectile projectile) {
        SwitchAttack attack = new SwitchAttack(null, animation, startGraphics, endGraphics, projectile).setUseHandler(true);
        attack.setHandler(new DragonfireSwingHandler(meleeRange, maximumHit, attack, true));
        return attack;
    }

    /**
     * Get switch attack.
     *
     * @param meleeRange    the melee range
     * @param maximumHit    the maximum hit
     * @param animation     the animation
     * @param startGraphics the start graphics
     * @param endGraphics   the end graphics
     * @param projectile    the projectile
     * @param fire          the fire
     * @return the switch attack
     */
    public static SwitchAttack get(boolean meleeRange, int maximumHit, Animation animation, Graphics startGraphics, Graphics endGraphics, Projectile projectile, boolean fire) {
        SwitchAttack attack = new SwitchAttack(null, animation, startGraphics, endGraphics, projectile).setUseHandler(true);
        attack.setHandler(new DragonfireSwingHandler(meleeRange, maximumHit, attack, fire));
        return attack;
    }

    @Override
    public InteractionType canSwing(Entity entity, Entity victim) {
        if (meleeRange) {
            return CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim);
        }
        return CombatStyle.MAGIC.getSwingHandler().canSwing(entity, victim);
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        int max = calculateHit(entity, victim, 1.0);
        int hit = RandomFunction.random(max + 1);
        assert state != null;
        state.setMaximumHit(max);
        state.setStyle(CombatStyle.MAGIC);
        state.setEstimatedHit(hit);
        if (meleeRange) {
            return 1;
        }
        int ticks = 2 + (int) Math.floor(entity.getLocation().getDistance(victim.getLocation()) * 0.5);
        entity.setAttribute("fireBreath", GameWorld.getTicks() + (ticks + 2));
        return ticks;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(attack.getAnimation(), attack.getStartGraphics());
        if (attack.getProjectile() != null) {
            attack.getProjectile().copy(entity, victim, 5).send();
        }
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        if (entity instanceof NPC && victim instanceof Player) {
            Player p = (Player) victim;
            Item shield = p.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
            if (shield != null && (shield.getId() == 11283 || shield.getId() == 11284)) {
                if (shield.getId() == 11284) {
                    p.getEquipment().replace(new Item(11283), EquipmentContainer.SLOT_SHIELD);
                    shield = p.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
                    shield.setCharge(0);
                }
                if (shield.getCharge() < 1000) {
                    shield.setCharge(shield.getCharge() + 20);
                    EquipmentContainer.updateBonuses(p);
                    p.getPacketDispatch().sendMessage("Your dragonfire shield glows more brightly.");
                    playAudio(p, Sounds.DRAGONSLAYER_ABSORB_FIRE_3740);
                    p.faceLocation(entity.getCenterLocation());
                    victim.visualize(Animation.create(6695), Graphics.create(1163));
                } else {
                    p.getPacketDispatch().sendMessage("Your dragonfire shield is already fully charged.");
                }
                return;
            }
        }
        if (!fire && !hasTimerActive(victim, "frozen:immunity") && RandomFunction.random(4) == 2) {
            registerTimer(victim, spawnTimer("frozen", 16, true));
            victim.graphics(Graphics.create(502));
        }
        Graphics graphics = attack != null ? attack.getEndGraphics() : null;
        victim.visualize(victim.getProperties().getDefenceAnimation(), graphics);
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState state) {
        assert state != null;
        int hit = state.getEstimatedHit();
        if (hit > -1) {
            victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, state);
        }
        hit = state.getSecondaryHit();
        if (hit > -1) {
            victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, state);
        }
    }

    @Override
    public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
        if (victim.isPlayer() && !fire) {
            Item item = victim.asPlayer().getEquipment().get(EquipmentContainer.SLOT_SHIELD);
            if (item != null && (item.getId() == 2890 || item.getId() == 9731) && state.getEstimatedHit() > 10) {
                state.setEstimatedHit(RandomFunction.random(10));
            }
        }
        CombatStyle style = state.getStyle();
        super.adjustBattleState(entity, victim, state);
        state.setStyle(style);
    }

    @Override
    protected int getFormattedHit(Entity entity, Entity victim, BattleState state, int hit) {
        return formatHit(victim, hit);
    }

    @Override
    public int calculateAccuracy(Entity entity) {
        return 4000;
    }

    @Override
    public int calculateHit(Entity entity, Entity victim, double modifier) {
        return calculateDragonFireMaxHit(victim, maximumHit, !fire, 0, true);
    }

    @Override
    public int calculateDefence(Entity victim, Entity attacker) {
        return CombatStyle.MAGIC.getSwingHandler().calculateDefence(victim, attacker);
    }

    @Override
    public double getSetMultiplier(Entity e, int skillId) {
        return 1.0;
    }

}
