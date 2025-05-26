package core.game.node.entity.lock

import core.game.node.Node

/**
* Functional interface representing a callback that executes when a [Lock] expires.
*/
fun interface LockElapse {

    /**
     * Called when the associated [Lock] elapses.
     *
     * @param node The node associated with the lock.
     * @param lock The lock that has expired.
     */
    fun elapse(node: Node, lock: Lock)
}