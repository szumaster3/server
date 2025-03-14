package core.game.node.entity.player.link.music;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Music entry.
 */
public final class MusicEntry {

    private static final Map<Integer, MusicEntry> SONGS = new HashMap<>();

    private final int id;

    private final String name;

    private final int index;

    /**
     * Instantiates a new Music entry.
     *
     * @param id    the id
     * @param name  the name
     * @param index the index
     */
    public MusicEntry(int id, String name, int index) {
        this.id = id;
        this.name = name;
        this.index = index;
    }

    /**
     * For id music entry.
     *
     * @param id the id
     * @return the music entry
     */
    public static MusicEntry forId(int id) {
        return SONGS.get(id);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static Map<Integer, MusicEntry> getSongs() {
        return SONGS;
    }
}