package core.plugin.type

import core.game.node.entity.player.Player
import core.plugin.Plugin

/**
 * Manages a collection of [ExperiencePlugin] instances that modify experience gain.
 */
object ExperiencePlugins {
    @JvmStatic
    private val plugins = ArrayList<ExperiencePlugin>()

    /**
     * Adds an [ExperiencePlugin] to the list of active plugins.
     *
     * @param plugin The experience plugin to be added.
     */
    @JvmStatic
    fun add(plugin: ExperiencePlugin) {
        plugins.add(plugin)
    }

    /**
     * Runs all registered [ExperiencePlugin] instances for a given experience gain event.
     *
     * @param player The player gaining experience.
     * @param skill The skill ID for which experience is being gained.
     * @param amount The amount of experience being gained.
     */
    @JvmStatic
    fun run(
        player: Player,
        skill: Int,
        amount: Double,
    ) {
        if (player.isArtificial) return
        plugins.forEach {
            it.run(player, skill, amount)
        }
    }
}

/**
 * Represents a plugin that modifies experience gain in some way.
 */
abstract class ExperiencePlugin : Plugin<Any> {
    /**
     * Registers the plugin by adding it to [ExperiencePlugins].
     *
     * @param arg Optional argument (not used in this implementation).
     * @return The initialized plugin instance.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        ExperiencePlugins.add(this)
        return this
    }

    /**
     * Defines the behavior when experience is gained.
     *
     * @param player The player gaining experience.
     * @param skill The skill ID for which experience is being gained.
     * @param amount The amount of experience being gained.
     */
    abstract fun run(
        player: Player,
        skill: Int,
        amount: Double,
    )
}