package core.game.worldevents

import core.ServerStore
import core.api.ContentInterface
import core.plugin.ClassScanner
import core.plugin.Plugin
import core.tools.Log
import org.json.simple.JSONObject
import java.util.*

/**
 * Represents a world event that can occur in the game world.
 *
 * World events can have their own plugins and provide custom activation
 * conditions and initialization logic.
 *
 * @property name The name of the world event.
 *
 * @author Ceikry
 */
abstract class WorldEvent(
    var name: String,
) : ContentInterface {

    /**
     * A collection of plugins associated with this event.
     */
    var plugins = PluginSet()

    /**
     * Checks if the event is currently active, based on the given [Calendar] instance.
     *
     * Override this method to define custom activation logic.
     *
     * @param cal The calendar instance used to evaluate the current time/date.
     * @return `true` if the event is active, `false` otherwise.
     */
    open fun checkActive(cal: Calendar): Boolean = false

    /**
     * Initializes the event. Override to perform setup logic when the event starts.
     */
    open fun initEvent() {
        // Empty
    }

    /**
     * Logs a message under this event's context.
     *
     * @param message The message to log.
     */
    fun log(message: String) {
        core.api.log(this::class.java, Log.FINE, "[World Events($name)] $message")
    }
}

/**
 * Manages a list of plugins related to a world event.
 *
 * @param plugins Vararg list of [Plugin] instances to include initially.
 */
class PluginSet(
    vararg val plugins: Plugin<*>,
) {
    /**
     * The internal mutable list of plugins.
     */
    val set = ArrayList(plugins.asList())

    /**
     * Initializes all plugins in the set using the [ClassScanner].
     */
    fun initialize() {
        ClassScanner.definePlugins(*set.toTypedArray())
    }

    /**
     * Adds a plugin to the plugin set.
     *
     * @param plugin The plugin to add.
     */
    fun add(plugin: Plugin<*>) {
        set.add(plugin)
    }
}

/**
 * Singleton object managing registered [WorldEvent] instances.
 */
object WorldEvents {

    private var events = hashMapOf<String, WorldEvent>()

    /**
     * Registers a new [WorldEvent].
     *
     * @param event The world event to register.
     */
    @JvmStatic
    fun add(event: WorldEvent) {
        events[event.name.lowercase()] = event
    }

    /**
     * Retrieves a [WorldEvent] by name.
     *
     * @param name The name of the event to retrieve.
     * @return The corresponding [WorldEvent], or `null` if not found.
     */
    fun get(name: String): WorldEvent? = events[name.lowercase()]

    /**
     * Returns the archived status data for world events.
     *
     * @return A [JSONObject] representing the archive of world event status.
     */
    fun getArchive(): JSONObject = ServerStore.getArchive("world-event-status")
}
