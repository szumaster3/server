package core.game.node.entity.skill;

import content.data.skill.SkillingTool;
import content.global.skill.gather.SkillingResource;
import core.game.node.Node;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;

/**
 * The type Skill pulse.
 *
 * @param <T> the type parameter
 */
public abstract class SkillPulse<T extends Node> extends Pulse {

    /**
     * The Player.
     */
    protected final Player player;

    /**
     * The Node.
     */
    protected T node;

    /**
     * The Tool.
     */
    protected SkillingTool tool;

    /**
     * The Resource.
     */
    protected SkillingResource resource;

    /**
     * The Reset animation.
     */
    protected boolean resetAnimation = true;

    /**
     * Instantiates a new Skill pulse.
     *
     * @param player the player
     * @param node   the node
     */
    public SkillPulse(Player player, T node) {
        super(1, player, node);
        this.player = player;
        this.node = node;
        super.stop();
    }

    @Override
    public void start() {
        if (checkRequirements()) {
            super.start();
            message(0);
        }
    }

    @Override
    public boolean pulse() {
        if (!checkRequirements()) {
            return true;
        }
        animate();
        return reward();
    }

    @Override
    public void stop() {
        if (resetAnimation) {
            player.animate(new Animation(-1, Priority.HIGH));
        }
        super.stop();
        message(1);
    }

    /**
     * Check requirements boolean.
     *
     * @return the boolean
     */
    public abstract boolean checkRequirements();

    /**
     * Animate.
     */
    public abstract void animate();

    /**
     * Reward boolean.
     *
     * @return the boolean
     */
    public abstract boolean reward();

    /**
     * Message.
     *
     * @param type the type
     */
    public void message(int type) {

    }
}
