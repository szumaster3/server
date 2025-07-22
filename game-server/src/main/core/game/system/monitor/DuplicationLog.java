package core.game.system.monitor;

import core.ServerConstants;

/**
 * Tracks duplication-related flags and logs for a player.
 * Supports priority-based log writing depending on flag severity.
 */
public final class DuplicationLog extends MessageLog {

    /**
     * Duration (in milliseconds) for which an increase flag is considered active.
     * Currently set to 5 days.
     */
    public static final long LOGGING_DURATION = 5 * 24 * 60 * 60_000L;

    /**
     * Flag indicating duplication-related chat activity.
     */
    public static final int DUPE_TALK = 0x1;

    /**
     * Flag indicating suspicious increase in item/note count.
     */
    public static final int NW_INCREASE = 0x8;

    private int flag;

    private long lastIncreaseFlag;

    /**
     * Creates a DuplicationLog with unlimited capacity and no unique logging.
     */
    public DuplicationLog() {
        super(-1, false);
    }

    /**
     * Writes the log to a file based on the current duplication priority.
     * The path uses player's name and priority category (high, mid, low).
     *
     * @param playerName name of the player associated with this log
     */
    @Override
    public void write(String playerName) {
        String priority = "low";
        switch (getProbability()) {
            case 3:
                priority = "high";
                break;
            case 2:
                priority = "mid";
                break;
            default:
                priority = "low";
        }
        super.write(ServerConstants.LOGS_PATH + "duplicationflags/" + priority + "prior/" + playerName + ".log");
    }

    /**
     * Computes the duplication suspicion probability based on set flags.
     *
     * @return an integer probability value (0 to 3)
     */
    public int getProbability() {
        int probability = 0;
        if ((flag & NW_INCREASE) != 0) {
            probability += 2;
        }
        if ((flag & DUPE_TALK) != 0) {
            probability++;
        }
        return probability;
    }

    /**
     * Determines whether logging is currently flagged due to recent suspicious increase.
     *
     * @return true if increase flag is active within LOGGING_DURATION, false otherwise
     */
    public boolean isLoggingFlagged() {
        return (flag & NW_INCREASE) != 0 && (System.currentTimeMillis() - lastIncreaseFlag) < LOGGING_DURATION;
    }

    /**
     * Adds a flag to this log's internal flag bitmask.
     * If the flag is NW_INCREASE, updates the timestamp.
     *
     * @param flag the flag bit(s) to add
     */
    public void flag(int flag) {
        this.flag |= flag;
        if (flag == NW_INCREASE) {
            lastIncreaseFlag = System.currentTimeMillis();
        }
    }

    /**
     * Returns the current flag bitmask.
     *
     * @return the current flag integer
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Returns the timestamp of the last NW_INCREASE flag set.
     *
     * @return timestamp in milliseconds
     */
    public long getLastIncreaseFlag() {
        return lastIncreaseFlag;
    }

    /**
     * Sets the timestamp for the last NW_INCREASE flag.
     *
     * @param lastIncreaseFlag timestamp in milliseconds
     */
    public void setLastIncreaseFlag(long lastIncreaseFlag) {
        this.lastIncreaseFlag = lastIncreaseFlag;
    }
}