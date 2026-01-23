package content.region.misthalin.varrock.quest.surok.plugin

import core.game.activity.ActivityManager
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles what lies below options.
 */
@Initializable
class WhatLiesBelowPlugin : OptionHandler() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ActivityManager.register(WhatLiesBelowCutscene())
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        return true
    }
}