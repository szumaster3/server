package core.game.system.task;

import core.game.node.Node;

/**
 * Represents a scheduled repeating task that runs every {@code delay} ticks.
 */
public abstract class Pulse implements Runnable {

    /**
     * Indicates whether this pulse is currently running.
     */
    private boolean running = true;

    /**
     * Number of ticks between executions.
     */
    private int delay;

    /**
     * Number of ticks passed since last execution.
     */
    private int ticksPassed;

    /**
     * Nodes monitored by this pulse; pulse stops if any become inactive.
     */
    protected Node[] checks;

    /**
     * Creates a Pulse with default delay of 1 tick.
     */
    public Pulse() {
        this(1);
    }

    /**
     * Creates a Pulse with the specified delay.
     *
     * @param delay Number of ticks between pulse executions.
     */
    public Pulse(int delay) {
        this.delay = delay;
    }

    /**
     * Creates a Pulse with specified delay and associated nodes.
     *
     * @param delay  Number of ticks between pulse executions.
     * @param checks Nodes to monitor for activity.
     */
    public Pulse(int delay, Node... checks) {
        this.delay = delay;
        this.checks = checks;
    }

    @Override
    public void run() {
        update();
    }

    /**
     * Updates the pulse state; executes pulse logic if delay elapsed.
     *
     * @return {@code true} if the pulse has stopped and should be removed; {@code false} otherwise.
     */
    public boolean update() {
        if (hasInactiveNode()) {
            stop();
            return true;
        }
        if (!isRunning()) {
            return true;
        }

        if (++ticksPassed >= delay) {
            ticksPassed = 0;
            try {
                if (pulse()) {
                    stop();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                stop();
                return true;
            }
        }
        return !isRunning();
    }

    /**
     * Checks whether any monitored nodes are inactive.
     *
     * @return {@code true} if any node is inactive; {@code false} otherwise.
     */
    public boolean hasInactiveNode() {
        if (checks != null) {
            for (Node node : checks) {
                if (node != null && !node.isActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The logic executed each time the pulse triggers.
     *
     * @return {@code true} to stop the pulse; {@code false} to continue.
     */
    public abstract boolean pulse();

    /**
     * Determines if the pulse should be removed for a given reason.
     *
     * @param reason The removal reason.
     * @return {@code true} if the pulse should be removed; otherwise {@code false}.
     */
    public boolean removeFor(String reason) {
        return true;
    }

    /**
     * Replaces the node checked at the given index.
     *
     * @param index Index of the node to replace.
     * @param node  New node to set.
     */
    public void addNodeCheck(int index, Node node) {
        checks[index] = node;
    }

    /**
     * Gets the node at the specified index.
     *
     * @param index The index of the node.
     * @return The node at the specified index.
     */
    public Node getNodeCheck(int index) {
        return checks[index];
    }

    /**
     * Stops the pulse from running.
     */
    public void stop() {
        running = false;
    }

    /**
     * Starts or resumes the pulse.
     */
    public void start() {
        running = true;
    }

    /**
     * Resets the pulse tick counter.
     */
    public void restart() {
        ticksPassed = 0;
    }

    /**
     * Checks if the pulse is currently running.
     *
     * @return {@code true} if running, {@code false} otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets the delay between pulse executions.
     *
     * @return The delay in ticks.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Sets the delay between pulse executions.
     *
     * @param delay The delay in ticks.
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Sets the number of ticks that have passed since last execution.
     *
     * @param ticks The number of ticks.
     */
    public void setTicksPassed(int ticks) {
        this.ticksPassed = ticks;
    }

    /**
     * Gets the number of ticks passed since the last execution.
     *
     * @return The number of ticks passed.
     */
    public int getTicksPassed() {
        return ticksPassed;
    }
}
