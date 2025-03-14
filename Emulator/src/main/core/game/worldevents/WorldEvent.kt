package core.game.worldevents

import core.ServerStore
import core.api.ContentInterface
import core.plugin.ClassScanner
import core.plugin.Plugin
import core.tools.Log
import org.json.simple.JSONObject
import java.util.*

abstract class WorldEvent(
    var name: String,
) : ContentInterface {
    var plugins = PluginSet()

    open fun checkActive(cal: Calendar): Boolean {
        return false
    }

    open fun initEvent() {
        // Empty
    }

    fun log(message: String) {
        core.api.log(this::class.java, Log.FINE, "[World Events($name)] $message")
    }
}

class PluginSet(
    vararg val plugins: Plugin<*>,
) {
    val set = ArrayList(plugins.asList())

    fun initialize() {
        ClassScanner.definePlugins(*set.toTypedArray())
    }

    fun add(plugin: Plugin<*>) {
        set.add(plugin)
    }
}

object WorldEvents {
    private var events = hashMapOf<String, WorldEvent>()

    @JvmStatic
    fun add(event: WorldEvent) {
        events[event.name.lowercase()] = event
    }

    fun get(name: String): WorldEvent? {
        return events[name.lowercase()]
    }

    fun getArchive(): JSONObject {
        return ServerStore.getArchive("world-event-status")
    }
}
