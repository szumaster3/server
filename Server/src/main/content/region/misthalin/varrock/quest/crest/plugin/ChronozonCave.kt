package content.region.misthalin.varrock.quest.crest.plugin

import core.api.getQuestStage
import core.api.hasAnItem
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class ChronozonCave : MapZone("FC_Chronozon_Zone", true), Plugin<Unit> {

    private val spawnLoc = Location(3086, 9936, 0)
    private var chronozon = ChronozonNPC(NPCs.CHRONOZON_667, spawnLoc)

    override fun configure() {
        register(ZoneBorders(3079, 9927, 3095, 9944))
    }

    override fun enter(e: Entity): Boolean {
        if (!e.isPlayer) return true
        val player = e as Player
        val stage = getQuestStage(player, Quests.FAMILY_CREST)
        val crestPart = hasAnItem(player, Items.CREST_PART_781).container != null
        val shouldSpawn = stage in 19..99 && !crestPart

        if (shouldSpawn) {
            val local = RegionManager.getLocalNpcs(spawnLoc, 5)
            val exists = local.any { it.id == NPCs.CHRONOZON_667 }

            if (!exists) {
                chronozon.setPlayer(player)
                chronozon.isRespawn = false
                chronozon.location = spawnLoc
                chronozon.init()
            }
        }

        return true
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e.isPlayer) {
            val playersNearby = RegionManager.getLocalPlayers(spawnLoc, 5)
            if (playersNearby.isEmpty()) {
                chronozon.clear()
            }
        }
        return super.leave(e, logout)
    }

    override fun newInstance(arg: Unit?): Plugin<Unit> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any? {
        return null
    }
}
