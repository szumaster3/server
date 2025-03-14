package core.game.system.task;

import core.game.node.Node;

/**
 * The type Node task.
 */
public abstract class NodeTask {

    private final int ticks;

    private Pulse pulse;

    /**
     * Instantiates a new Node task.
     */
    public NodeTask() {
        this(-1);
    }

    /**
     * Instantiates a new Node task.
     *
     * @param ticks the ticks
     */
    public NodeTask(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Start.
     *
     * @param node the node
     * @param n    the n
     */
    public void start(Node node, Node... n) {

    }

    /**
     * Exec boolean.
     *
     * @param node the node
     * @param n    the n
     * @return the boolean
     */
    public abstract boolean exec(Node node, Node... n);

    /**
     * Stop.
     *
     * @param node the node
     * @param n    the n
     */
    public void stop(Node node, Node... n) {

    }

    /**
     * Remove for boolean.
     *
     * @param s    the s
     * @param node the node
     * @param n    the n
     * @return the boolean
     */
    public boolean removeFor(String s, Node node, Node... n) {
        return true;
    }

    /**
     * Schedule pulse.
     *
     * @param node the node
     * @param n    the n
     * @return the pulse
     */
    public Pulse schedule(final Node node, final Node... n) {
        pulse = new Pulse(ticks, node) {

            @Override
            public void start() {
                super.start();
                NodeTask.this.start(node, n);
            }

            @Override
            public boolean pulse() {
                return exec(node, n);
            }

            @Override
            public void stop() {
                super.stop();
                NodeTask.this.stop(node, n);
            }

            @Override
            public boolean removeFor(String s) {
                return NodeTask.this.removeFor(s, node, n);
            }
        };
        pulse.start();
        return pulse;
    }

    /**
     * Gets pulse.
     *
     * @return the pulse
     */
    public Pulse getPulse() {
        return pulse;
    }

    /**
     * Gets ticks.
     *
     * @return the ticks
     */
    public int getTicks() {
        return ticks;
    }

}