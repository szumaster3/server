package core.game.node.entity.lock;

import core.game.node.Node;

/**
 * The interface Lock elapse.
 */
public interface LockElapse {

    /**
     * Elapse.
     *
     * @param node the node
     * @param lock the lock
     */
    public void elapse(Node node, Lock lock);

}