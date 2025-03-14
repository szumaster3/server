package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Animate interface context.
 */
public class AnimateInterfaceContext implements Context {

    private Player player;

    private int animationId;

    private int interfaceId;

    private int childId;

    /**
     * Instantiates a new Animate interface context.
     *
     * @param player      the player
     * @param animationId the animation id
     * @param interfaceId the interface id
     * @param childId     the child id
     */
    public AnimateInterfaceContext(Player player, int animationId, int interfaceId, int childId) {
        this.player = player;
        this.animationId = animationId;
        this.interfaceId = interfaceId;
        this.childId = childId;
    }

    /**
     * Gets animation id.
     *
     * @return the animation id
     */
    public int getAnimationId() {
        return animationId;
    }

    /**
     * Gets interface id.
     *
     * @return the interface id
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets child id.
     *
     * @return the child id
     */
    public int getChildId() {
        return childId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
