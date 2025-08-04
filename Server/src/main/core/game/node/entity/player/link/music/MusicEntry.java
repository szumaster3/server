package core.game.node.entity.player.link.music;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a music track entry that can be unlocked.
 */
public final class MusicEntry {

    /**
     * A static map of all available music entries, keyed by their unique id.
     */
    private static final Map<Integer, MusicEntry> SONGS = new HashMap<>();

    /**
     * The unique identifier of the music track.
     */
    private final int id;

    /**
     * The name of the music track as displayed to the player.
     */
    private final String name;

    /**
     * The index value associated with the music track.
     */
    private final int index;

    /**
     * Constructs a new {@code MusicEntry} with the given parameters.
     *
     * @param id    the unique id of the track
     * @param name  the name of the track
     * @param index the index used to order or reference the track
     */
    public MusicEntry(int id, String name, int index) {
        this.id = id;
        this.name = name;
        this.index = index;
    }

    /**
     * Retrieves a music entry by its unique ID.
     *
     * @param id the id of the music track to retrieve
     * @return the {@code MusicEntry} with the given ID, or {@code null} if not found
     */
    public static MusicEntry forId(int id) {
        return SONGS.get(id);
    }

    /**
     * Returns the id of the music track.
     *
     * @return the id of this music track
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the display name of the music track.
     *
     * @return the name of this music track
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the index associated with the music track.
     *
     * @return the index of this music track
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns a map of all available music entries.
     * The map is keyed by the track id and holds {@code MusicEntry} instances.
     *
     * @return the map of all music entries
     */
    public static Map<Integer, MusicEntry> getSongs() {
        return SONGS;
    }


    @Override
    public String toString() {
        return "MusicEntry[id=" + id + ", name='" + name + "', index=" + index + "]";
    }
}
