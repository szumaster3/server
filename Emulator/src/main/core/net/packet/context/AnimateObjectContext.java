package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.net.packet.Context;

/**
 * The type Animate object context.
 */
public class AnimateObjectContext implements Context {

    private final Player player;

    private final Animation animation;

    /**
     * Instantiates a new Animate object context.
     *
     * @param player    the player
     * @param animation the animation
     */
    public AnimateObjectContext(Player player, Animation animation) {
        this.player = player;
        this.animation = animation;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets animation.
     *
     * @return the animation
     */
    public Animation getAnimation() {
        return animation;
    }
}