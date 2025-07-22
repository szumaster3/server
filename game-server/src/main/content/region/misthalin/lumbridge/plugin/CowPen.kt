package content.region.misthalin.lumbridge.plugin

import core.GlobalStatistics.incrementDailyCowDeaths
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class CowPen : MapZone("lumbridge cows", true), Plugin<Any?> {
    private val COW_PEN_AREA = ZoneBorders(3242, 3255, 3265, 3297)

    override fun fireEvent(identifier: String, vararg args: Any, ): Any? = null

    override fun configure() {
        super.register(COW_PEN_AREA)
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun death(e: Entity, killer: Entity): Boolean {
        if (killer is Player && e is NPC) {
            incrementDailyCowDeaths()
        }
        return false
    }
}
