package content.minigame.fistofguthix.handlers

import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin
import java.util.*

class FOGWaitingZone :
    MapZone("Fog Waiting Room", true),
    Plugin<Any?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(e: Entity): Boolean {
        return super.enter(e)
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        return super.interact(e, target, option)
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun configure() {
        super.registerRegion(6487)
    }
}
