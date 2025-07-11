package core.game.system.task;

import core.game.node.Node;

/**
 * Abstract task operating on Node(s), executed on a Pulse schedule.
 */
public abstract class NodeTask {

    private final int ticks;
    private Pulse pulse;

    /**
     * Constructs a NodeTask with default ticks (-1).
     */
    public NodeTask() {
        this(-1);
    }

    /**
     * Constructs a NodeTask with a specific tick interval.
     *
     * @param ticks The number of ticks between executions.
     */
    public NodeTask(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Called once when the task starts.
     *
     * @param node The primary node.
     * @param others Optional other nodes.
     */
    public void start(Node node, Node... others) {}

    /**
     * Called on each pulse tick.
     *
     * @param node The primary node.
     * @param others Optional other nodes.
     * @return True if the task should stop, false otherwise.
     */
    public abstract boolean exec(Node node, Node... others);

    /**
     * Called once when the task stops.
     *
     * @param node The primary node.
     * @param others Optional other nodes.
     */
    public void stop(Node node, Node... others) {}

    /**
     * Check whether to remove the task for a given reason.
     *
     * @param reason The reason string.
     * @param node The primary node.
     * @param others Optional other nodes.
     * @return True if the task should be removed, false otherwise.
     */
    public boolean removeFor(String reason, Node node, Node... others) {
        return true;
    }

    /**
     * Schedules this task as a Pulse running at configured ticks.
     *
     * @param node The primary node.
     * @param others Optional other nodes.
     * @return The scheduled Pulse instance.
     */
    public Pulse schedule(final Node node, final Node... others) {
        pulse = new Pulse(ticks, node) {

            @Override
            public void start() {
                super.start();
                NodeTask.this.start(node, others);
            }

            @Override
            public boolean pulse() {
                return exec(node, others);
            }

            @Override
            public void stop() {
                super.stop();
                NodeTask.this.stop(node, others);
            }

            @Override
            public boolean removeFor(String s) {
                return NodeTask.this.removeFor(s, node, others);
            }
        };
        pulse.start();
        return pulse;
    }

    /**
     * Gets the scheduled pulse instance, or null if not scheduled.
     */
    public Pulse getPulse() {
        return pulse;
    }

    /**
     * Gets the configured tick interval for this task.
     */
    public int getTicks() {
        return ticks;
    }
}