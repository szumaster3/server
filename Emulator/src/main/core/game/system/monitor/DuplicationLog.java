package core.game.system.monitor;

import core.ServerConstants;

/**
 * The type Duplication log.
 */
public final class DuplicationLog extends MessageLog {

    /**
     * The constant LOGGING_DURATION.
     */
    public static long LOGGING_DURATION = 5 * 24 * 60 * 60_000;

    /**
     * The constant DUPE_TALK.
     */
    public static final int DUPE_TALK = 0x1;

    /**
     * The constant NW_INCREASE.
     */
    public static final int NW_INCREASE = 0x8;

    private int flag;

    private long lastIncreaseFlag;

    /**
     * Instantiates a new Duplication log.
     */
    public DuplicationLog() {
        super(-1, false);
    }

    @Override
    public void write(String playerName) {
        String priority = "low";
        switch (getProbability()) {
            case 3:
                priority = "high";
                break;
            case 2:
                priority = "mid";
        }
        super.write(ServerConstants.LOGS_PATH + "duplicationflags/" + priority + "prior/" + playerName + ".log");
    }

    /**
     * Gets probability.
     *
     * @return the probability
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
     * Is logging flagged boolean.
     *
     * @return the boolean
     */
    public boolean isLoggingFlagged() {
        return (flag & NW_INCREASE) != 0 && (System.currentTimeMillis() - lastIncreaseFlag) < LOGGING_DURATION;
    }

    /**
     * Flag.
     *
     * @param flag the flag
     */
    public void flag(int flag) {
        this.flag |= flag;
        if (flag == NW_INCREASE) {
            lastIncreaseFlag = System.currentTimeMillis();
        }
    }

    /**
     * Gets flag.
     *
     * @return the flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Gets last increase flag.
     *
     * @return the last increase flag
     */
    public long getLastIncreaseFlag() {
        return lastIncreaseFlag;
    }

    /**
     * Sets last increase flag.
     *
     * @param lastIncreaseFlag the last increase flag
     */
    public void setLastIncreaseFlag(long lastIncreaseFlag) {
        this.lastIncreaseFlag = lastIncreaseFlag;
    }

}