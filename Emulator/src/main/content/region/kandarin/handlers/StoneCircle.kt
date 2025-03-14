package content.region.kandarin.handlers

import core.api.setVarbit
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class StoneCircle :
    MapZone(STONE_CIRCLE, true),
    Plugin<Any> {
    override fun configure() {
        register(ZoneBorders(2558, 3219, 2559, 3225))
        register(ZoneBorders(2560, 3219, 2564, 3225))
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any? {
        return null
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            setVarbit(e, STONE_CIRCLE_ENTER_VARBIT, 1)
        }
        return super.enter(e)
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (!logout && e is Player) {
            setVarbit(e, STONE_CIRCLE_ENTER_VARBIT, 0)
        }
        return super.leave(e, logout)
    }

    companion object {
        val STONE_CIRCLE = "stone circle"
        val STONE_CIRCLE_ENTER_VARBIT = 4833
    }
}
