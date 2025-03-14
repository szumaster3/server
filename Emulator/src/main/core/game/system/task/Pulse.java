package core.game.system.task;

import core.game.node.Node;

/**
 * The type Pulse.
 */
public abstract class Pulse implements Runnable {

    /**
     * The Running.
     */
    public boolean running = true;

    private int delay;

    /**
     * The Ticks passed.
     */
    int ticksPassed;

    /**
     * The Checks.
     */
    protected Node[] checks;

    /**
     * Instantiates a new Pulse.
     */
    public Pulse() {
        this(1);
    }

    /**
     * Instantiates a new Pulse.
     *
     * @param delay the delay
     */
    public Pulse(int delay) {
        this.delay = delay;
    }

    /**
     * Instantiates a new Pulse.
     *
     * @param delay  the delay
     * @param checks the checks
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
     * Update boolean.
     *
     * @return the boolean
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
     * Has inactive node boolean.
     *
     * @return the boolean
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
     * Pulse boolean.
     *
     * @return the boolean
     */
    public abstract boolean pulse();

    /**
     * Remove for boolean.
     *
     * @param pulse the pulse
     * @return the boolean
     */
    public boolean removeFor(String pulse) {
        return true;
    }

    /**
     * Add node check.
     *
     * @param index the index
     * @param n     the n
     */
    public void addNodeCheck(int index, Node n) {
        checks[index] = n;
    }

    /**
     * Gets node check.
     *
     * @param index the index
     * @return the node check
     */
    public Node getNodeCheck(int index) {
        return checks[index];
    }

    /**
     * Stop.
     */
    public void stop() {
        running = false;
    }

    /**
     * Start.
     */
    public void start() {
        running = true;
    }

    /**
     * Restart.
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