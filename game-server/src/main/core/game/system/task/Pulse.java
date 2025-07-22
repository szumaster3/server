package core.game.system.task;

import core.game.node.Node;

/**
 * Represents a scheduled repeating task that runs every {@code delay} ticks.
 */
public abstract class Pulse implements Runnable {

    /**
     * Indicates whether this pulse is currently running.
     */
    public boolean running = true;

    /**
     * Number of ticks between executions.
     */
    private int delay;

    /**
     * Number of ticks passed since last execution.
     */
    int ticksPassed;

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
        if (update()) {
            //GameWorld.TASKS.remove(this);
        }
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
            return !isRunning();
        }
        return false;
    }

    /**
     * Checks whether any monitored nodes are inactive.
     *
     * @return {@code true} if any node is inactive; {@code false} otherwise.
     */
    public boolean hasInactiveNode() {
        if (checks != null) {
            int size = checks.length;
            for (int i = 0; i < size; i++) {
                Node n = checks[i];
                if (n != null && !n.isActive()) {
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
     * @param pulse The removal pulse.
     * @return {@code true} if the pulse should be removed; otherwise {@code false}.
     */
    public boolean removeFor(String pulse) {
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
     * Is running boolean.
     *
     * @return the boolean
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets delay.
     *
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Sets delay.
     *
     * @param delay the delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Sets ticks passed.
     *
     * @param ticks the ticks
     */
    public void setTicksPassed(int ticks) {
        this.ticksPassed = ticks;
    }

    /**
     * Gets ticks passed.
     *
     * @return the ticks passed
     */
    public int getTicksPassed() {
        return ticksPassed;
    }
}