package core.net.amsc;

import java.util.ArrayList;
import java.util.List;

/**
 * The type World statistics.
 */
public final class WorldStatistics {

    private final int id;

    private final List<String> players = new ArrayList<>(20);

    /**
     * Instantiates a new World statistics.
     *
     * @param id the id
     */
    public WorldStatistics(int id) {
        this.id = id;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }
}