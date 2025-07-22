package core.game.node.entity.npc.drop;

import java.util.Arrays;
import java.util.HashMap;

/**
 * The enum Drop frequency.
 */
public enum DropFrequency {
    /**
     * Always drop frequency.
     */
    ALWAYS,
    /**
     * Common drop frequency.
     */
    COMMON,
    /**
     * Uncommon drop frequency.
     */
    UNCOMMON,
    /**
     * Rare drop frequency.
     */
    RARE,
    /**
     * Very rare drop frequency.
     */
    VERY_RARE;

    /**
     * The Rates.
     */
    static int[] RATES = {1, 5, 15, 30, 60};

    /**
     * The constant rateMap.
     */
    final static HashMap<DropFrequency, Integer> rateMap = new HashMap<>();

    static {
        Arrays.stream(DropFrequency.values()).forEach(freq -> rateMap.putIfAbsent(freq, RATES[freq.ordinal()]));
    }

    /**
     * Rate int.
     *
     * @param frequency the frequency
     * @return the int
     */
    public static int rate(DropFrequency frequency) {
        return rateMap.get(frequency);
    }

}