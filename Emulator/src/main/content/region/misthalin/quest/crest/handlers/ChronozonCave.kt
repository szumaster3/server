package content.region.misthalin.quest.crest.handlers

import core.api.hasAnItem
import core.api.quest.getQuestStage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ChronozonCave :
    MapZone("FC ChronozoneZone", true),
    Plugin<Unit> {
    val spawnLoc = Location(3086, 9936, 0)
    var chronozon = ChronozonNPC(NPCs.CHRONOZON_667, spawnLoc)

    override fun configure() {
        register(ZoneBorders(3079, 9927, 3095, 9944))
    }

    override fun move(
        e: Entity?,
        from: Location?,
        to: Location?,
    ): Boolean {
        return super.move(e, from, to)
    }

    override fun enter(e: Entity?): Boolean {
        if (e != null) {
            if (e.isPlayer) {
                val player = e as Player
                if (getQuestStage(player, Quests.FAMILY_CREST) in (19..99) &&
                    !hasAnItem(
                        player,
                        Items.CREST_PART_781,
                    ).exists()
                ) {
                    if (!RegionManager.getLocalNpcs(spawnLoc, 5).contains(chronozon)) {
                        chronozon.setPlayer(e)
                        chronozon.isRespawn = false
                        chronozon.location = spawnLoc
                        chronozon.init()
                    }
                }
            }
            return true
        }
        return false
    }

    override fun leave(
        e: Entity?,
        logout: Boolean,
    ): Boolean {
        if (e!!.isPlayer) {
            if (RegionManager.getLocalPlayers(spawnLoc, 5).size <= 0) {
                chronozon.clear()
            }
        }
        return super.leave(e, logout)
    }

    override fun newInstance(arg: Unit?): Plugin<Unit> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any {
        return UInt
    }
}
