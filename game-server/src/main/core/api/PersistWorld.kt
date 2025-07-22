package core.api

/**
 * Interface for world persistence management.
 * Implementations should handle saving and parsing world data.
 */
interface PersistWorld : ContentInterface {
    /**
     * Saves the current world state to persistent storage.
     */
    fun save()

    /**
     * Parses and loads the world data from persistent storage.
     */
    fun parse()
}
