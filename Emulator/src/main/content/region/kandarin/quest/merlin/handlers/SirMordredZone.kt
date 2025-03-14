package content.region.kandarin.quest.merlin.handlers

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

@Initializable
class SirMordredZone :
    MapZone("SirMordredZone", true),
    Plugin<Any?> {
    override fun newInstance(arg: Any?): SirMordredZone {
        ZoneBuilder.configure(this)
        return this
    }

    override fun configure() {
        super.register(ZoneBorders(Location.create(2759, 3394, 2), Location.create(2777, 3413, 2)))
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any {
        return Unit
    }

    override fun startDeath(
        e: Entity?,
        killer: Entity?,
    ): Boolean {
        return !(e != null && e is NPC && e.asNpc().id == NPCs.SIR_MORDRED_247)
    }
}
