package content.region.karamja.handlers.tzhaar;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.game.world.map.MapDistance;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import org.rs.consts.NPCs;

/**
 * The type Tzhaar fight cave npc.
 */
public final class TzhaarFightCaveNPC extends AbstractNPC {
    private static final int[] NPC_IDS = {
            NPCs.TZ_KIH_2734,
            NPCs.TZ_KIH_2735,
            NPCs.TZ_KEK_2736,
            NPCs.TZ_KEK_2737,
            NPCs.TZ_KEK_2738,
            NPCs.TOK_XIL_2739,
            NPCs.TOK_XIL_2740,
            NPCs.YT_MEJKOT_2741,
            NPCs.YT_MEJKOT_2742,
            NPCs.KET_ZEK_2743,
            NPCs.KET_ZEK_2744,
            NPCs.TZTOK_JAD_2745,
            NPCs.YT_HURKOT_2746
    };

    private CombatAction combatAction;

    private TzhaarFightCavesPlugin activity;

    private boolean spawnedMinions;

    /**
     * Instantiates a new Tzhaar fight cave npc.
     *
     * @param id       the id
     * @param location the location
     * @param activity the activity
     */
    public TzhaarFightCaveNPC(int id, Location location, TzhaarFightCavesPlugin activity) {
        super(id, location);
        this.activity = activity;
    }

    /**
     * Instantiates a new Tzhaar fight cave npc.
     */
    public TzhaarFightCaveNPC() {
        super(NPCs.TZ_KIH_2734, null);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatAction;
    }

    @Override
    public void tick() {
        super.tick();
        if (getId() != NPCs.YT_HURKOT_2746 && activity != null && !getProperties().getCombatPulse().isAttacking()) {
            getProperties().getCombatPulse().attack(activity.getPlayer());
            face(activity.getPlayer());
        }
    }

    @Override
    public void configure() {
        super.configure();
        CombatStyle style = CombatStyle.MELEE;
        if (getId() == NPCs.TOK_XIL_2739 || getId() == NPCs.TOK_XIL_2740) {
            style = CombatStyle.RANGE;
        } else if (getId() == NPCs.KET_ZEK_2743 || getId() == NPCs.KET_ZEK_2744 || getId() == NPCs.TZTOK_JAD_2745) {
            style = CombatStyle.MAGIC;
        }
        super.setAggressive(false);
        super.aggressiveHandler = null;
        combatAction = new CombatAction(this, style);
        getProperties().setCombatTimeOut(Integer.MAX_VALUE);
        super.setWalkRadius(64);
        if (activity != null) {
            activity.activeNPCs.add(this);
            if (getId() != NPCs.YT_HURKOT_2746) {
                getProperties().getCombatPulse().attack(activity.getPlayer());
            }
        }
    }

    @Override
    public boolean shouldPreventStacking(Entity mover) {
        return mover instanceof TzhaarFightCaveNPC;
    }

    /**
     * Heal.
     *
     * @param amount the amount
     */
    public void heal(int amount) {
        if (getSkills().heal(amount) > 0 && getId() == 2745) {
            spawnedMinions = false;
        }
    }

    @Override
    public void onImpact(final Entity entity, final BattleState state) {
        if (getId() == 2746) {
            getPulseManager().run(new Pulse(1, entity) {
                @Override
                public boolean pulse() {
                    getProperties().getCombatPulse().attack(entity);
                    return true;
                }
            });
            return;
        }
        super.onImpact(entity, state);
        if (getId() == 2736 || getId() == 2737) {
            if (state.getStyle() == CombatStyle.MELEE && getSkills().getLifepoints() > 0) {
                entity.getImpactHandler().manualHit(this, 1 + (state.getEstimatedHit() / 10), HitsplatType.NORMAL, 1);
            }
        } else if (getId() == 2745 && !spawnedMinions && getSkills().getLifepoints() < (getSkills().getMaximumLifepoints() >> 1)) {
            spawnedMinions = true;
            if (activity != null) {
                for (int i = activity.activeNPCs.size() - 1; i < 4; i++) {
                    TzhaarFightCaveNPC npc = activity.spawn(2746);
                    npc.getProperties().getCombatPulse().attack(this);
                    npc.setAttribute("fc_jad", this);
                }
            }
        }
    }

    @Override
    public int getWalkRadius() {
        return 64;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new TzhaarFightCaveNPC(id, location, null);
    }

    @Override
    public int[] getIds() {
        return NPC_IDS;
    }

    /**
     * The type Combat action.
     */
    static class CombatAction extends CombatSwingHandler {
        private final TzhaarFightCaveNPC npc;
        private CombatStyle main;
        private CombatStyle style;

        /**
         * The Jad.
         */
        boolean jad;

        /**
         * Instantiates a new Combat action.
         *
         * @param npc  the npc
         * @param main the main
         */
        public CombatAction(TzhaarFightCaveNPC npc, CombatStyle main) {
            super(main);
            this.npc = npc;
            this.jad = npc.getId() == 2745;
            this.main = main;
            this.style = getType();
        }

        @Override
        public InteractionType canSwing(Entity entity, Entity victim) {
            if (getType() != CombatStyle.MELEE) {
                if (!isProjectileClipped(entity, victim, false)) {
                    return InteractionType.NO_INTERACT;
                }
                int distance = MapDistance.RENDERING.getDistance();
                if (victim.getCenterLocation().withinDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, distance)) && super.canSwing(entity, victim) != InteractionType.NO_INTERACT) {
                    entity.getWalkingQueue().reset();
                    return InteractionType.STILL_INTERACT;
                }
                return InteractionType.NO_INTERACT;
            }
            if (getType() != null)
                return getType().getSwingHandler().canSwing(entity, victim);
            else return InteractionType.NO_INTERACT;
        }

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            if (super.getType() != null)
                style = super.getType();
            int ticks = 1;
            if (jad) {
                main = CombatStyle.values()[1 + RandomFunction.RANDOM.nextInt(2)];
            }
            if (main != CombatStyle.MELEE) {
                style = main;
                if (CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim) != InteractionType.NO_INTERACT && RandomFunction.random(10) < 4) {
                    style = CombatStyle.MELEE;
                } else {
                    ticks += (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.4);
                }
            }
            int max = calculateHit(entity, victim, 1.0);
            int hit = 0;
            boolean heal = victim instanceof NPC || ((npc.getId() == 2741 || npc.getId() == 2742) && RandomFunction.RANDOM.nextInt(4) < 1);
            if (entity.getId() == 2746) {
                NPC j = (NPC) entity.getAttribute("fc_jad", entity);
                if (entity.getLocation().withinDistance(j.getCenterLocation(), (j.size() >> 1) + 1) && RandomFunction.random(4) < 2) {
                    heal = true;
                    state.setVictim(j);
                    victim = j;
                }
            }
            state.setStyle(heal ? null : style);
            if (heal || isAccurateImpact(entity, victim)) {
                state.setMaximumHit(max);
                hit = RandomFunction.random(max) + (heal ? 5 : 0);
            }
            state.setEstimatedHit(hit);
            return ticks;
        }

        @Override
        public void adjustBattleState(Entity entity, Entity victim, BattleState state) {

        }

        @Override
        public void visualize(Entity entity, Entity victim, BattleState state) {
            if (state == null || state.getStyle() == null) {
                return;
            }
            switch (state.getStyle()) {
                case MELEE:
                    entity.animate(entity.getProperties().getAttackAnimation());
                    break;
                case RANGE:
                    if (jad) {
                        entity.visualize(new Animation(9276, Priority.HIGH), Graphics.create(1625));
                    } else {
                        Projectile.ranged(entity, victim, 1616, 41, 36, 50, 15).send();
                        entity.animate(new Animation(9243, Priority.HIGH));
                    }
                    break;
                case MAGIC:
                    if (jad) {
                        Projectile.create(entity, victim, 1627, 96, 36, 70, (int) (victim.getLocation().getDistance(entity.getLocation()) * 10 + 52)).send();
                        entity.visualize(new Animation(9300, Priority.HIGH), Graphics.create(1626));
                    } else {
                        Projectile.magic(entity, victim, 1623, 41, 36, 50, 15).send();
                        entity.visualize(new Animation(9266, Priority.HIGH), Graphics.create(1622));
                    }
                    break;
            }
        }

        @Override
        public void impact(Entity entity, Entity victim, BattleState state) {
            if (state != null && state.getStyle() != null && victim.hasProtectionPrayer(state.getStyle())) {
                state.setEstimatedHit(0);
            }
            if (state != null && state.getStyle() == null) {
                NPC n = victim instanceof NPC ? ((NPC) victim) : (NPC) npc.activity.getPlayer().getProperties().getCombatPulse().getVictim();
                if (!(n instanceof TzhaarFightCaveNPC)) {
                    n = npc;
                }
                ((TzhaarFightCaveNPC) n).heal(state.getEstimatedHit());
                n.graphics(new Graphics(444, 96));
                return;
            }
            if (state != null && state.getEstimatedHit() > 0) {
                state.setEstimatedHit(formatHit(victim, state.getEstimatedHit()));
                if (entity.getId() == 2734 || entity.getId() == 2735) {
                    victim.getSkills().decrementPrayerPoints(state.getEstimatedHit());
                } else if (jad && state.getStyle() != CombatStyle.MELEE) {
                    victim.graphics(Graphics.create(157));
                }
            }
            style.getSwingHandler().impact(entity, victim, state);
        }

        @Override
        public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
            if (state != null && state.getStyle() == null) {
                return;
            } else if (state != null && state.getStyle() == CombatStyle.MAGIC && !jad) {
                victim.graphics(Graphics.create(1624));
            } else if (state != null && jad && state.getStyle() == CombatStyle.RANGE) {
                victim.graphics(Graphics.create(451));
            }
            style.getSwingHandler().visualizeImpact(entity, victim, state);
        }

        @Override
        public int calculateAccuracy(Entity entity) {
            return style.getSwingHandler().calculateAccuracy(entity);
        }

        @Override
        public int calculateDefence(Entity victim, Entity attacker) {
            return style.getSwingHandler().calculateDefence(victim, attacker);
        }

        @Override
        public int calculateHit(Entity entity, Entity victim, double modifier) {
            return style.getSwingHandler().calculateHit(entity, victim, modifier);
        }

        @Override
        public ArmourSet getArmourSet(Entity e) {
            return style.getSwingHandler().getArmourSet(e);
        }

        @Override
        public double getSetMultiplier(Entity e, int skillId) {
            return style.getSwingHandler().getSetMultiplier(e, skillId);
        }

    }
}
