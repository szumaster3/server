package core.plugin.type

import core.game.node.entity.player.Player
import core.plugin.Plugin

object ExperiencePlugins {
    @JvmStatic
    private val plugins = ArrayList<ExperiencePlugin>()

    @JvmStatic
    fun add(plugin: ExperiencePlugin) {
        plugins.add(plugin)
    }

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

abstract class ExperiencePlugin : Plugin<Any> {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ExperiencePlugins.add(this)
        return this
    }

    abstract fun run(
        player: Player,
        skill: Int,
        amount: Double,
    )
}
