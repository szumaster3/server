package core.game.node.entity.npc.agg;

import core.ServerConstants;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.game.world.GameWorld;
import core.tools.RandomFunction;

/**
 * The type Aggressive handler.
 */
public final class AggressiveHandler {
    private final Entity entity;
    private int radius = 4;
    private int pauseTicks = 0;
    private boolean targetSwitching;
    private final AggressiveBehavior behavior;
    private int chanceRatio = 2;
    private final int[] playerTolerance = new int[ServerConstants.MAX_PLAYERS];
    private boolean allowTolerance = true;

    /**
     * Instantiates a new Aggressive handler.
     *
     * @param entity   the entity
     * @param behavior the behavior
     */
    public AggressiveHandler(Entity entity, AggressiveBehavior behavior) {
        this.entity = entity;
        this.behavior = behavior;
    }

    /**
     * Select target boolean.
     *
     * @return the boolean
     */
    public boolean selectTarget() {
        if (pauseTicks > GameWorld.getTicks() || entity.getLocks().isInteractionLocked()) {
            return false;
        }
        if ((!targetSwitching && entity.getProperties().getCombatPulse().isAttacking()) || DeathTask.isDead(entity)) {
            return false;
        }
        if (RandomFunction.randomize(10) > chanceRatio) {
            return false;
        }
        Entity target = behavior.getLogicalTarget(entity, behavior.getPossibleTargets(entity, radius));
        if (target instanceof Player) {
            if (target.getAttribute("ignore_aggression", false)) {
                return false;
            }
            if (((Player) target).getRights().equals(Rights.ADMINISTRATOR) && !target.getAttribute("allow_admin_aggression", false)) {
                return false;
            }
        }
        if (target != null) {
            target.setAttribute("aggressor", entity);
            if (entity.getProperties().getCombatPulse().isAttacking()) {
                entity.getProperties().getCombatPulse().setVictim(target);
                entity.face(target);
            } else {
                entity.getProperties().getCombatPulse().attack(target);
            }
            return true;
        }
        return entity.getProperties().getCombatPulse().isAttacking();
    }

    /**
     * Remove tolerance.
     *
     * @param index the index
     */
    public synchronized void removeTolerance(int index) {
        playerTolerance[index] = 0;
    }

    /**
     * Gets radius.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets radius.
     *
     * @param radius the radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Gets pause ticks.
     *
     * @return the pause ticks
     */
    public int getPauseTicks() {
        return pauseTicks;
    }

    /**
     * Sets pause ticks.
     *
     * @param pauseTicks the pause ticks
     */
    public void setPauseTicks(int pauseTicks) {
        this.pauseTicks = GameWorld.getTicks() + pauseTicks;
    }

    /**
     * Get player tolerance int [ ].
     *
     * @return the int [ ]
     */
    public int[] getPlayerTolerance() {
        return playerTolerance;
    }

    /**
     * Is target switching boolean.
     *
     * @return the boolean
     */
    public boolean isTargetSwitching() {
        return targetSwitching;
    }

    /**
     * Sets target switching.
     *
     * @param targetSwitching the target switching
     */
    public void setTargetSwitching(boolean targetSwitching) {
        this.targetSwitching = targetSwitching;
    }

    /**
     * Gets chance ratio.
     *
     * @return the chance ratio
     */
    public int getChanceRatio() {
        return chanceRatio;
    }

    /**
     * Sets chance ratio.
     *
     * @param chanceRatio the chance ratio
     */
    public void setChanceRatio(int chanceRatio) {
        this.chanceRatio = chanceRatio;
    }

    /**
     * Is allow tolerance boolean.
     *
     * @return the boolean
     */
    public boolean isAllowTolerance() {
        boolean configSetting = true;
        if (entity instanceof NPC) {
            configSetting = ((NPC) entity).getDefinition().getConfiguration("can_tolerate", true);
        }
        return allowTolerance && configSetting;
    }

    /**
     * Sets allow tolerance.
     *
     * @param allowTolerance the allow tolerance
     */
    public void setAllowTolerance(boolean allowTolerance) {
        this.allowTolerance = allowTolerance;
    }

}
