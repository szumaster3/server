package content.region.kandarin.pisc.quest.phoenix.plugin

import content.region.kandarin.pisc.quest.phoenix.InPyreNeed
import core.api.MapArea
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.RegionManager.getLocalPlayersMaxNorm
import core.game.world.map.build.DynamicRegion
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import java.util.concurrent.ConcurrentHashMap
import shared.consts.Regions

/** Manages the Phoenix Lair instances. */
object PhoenixLairInstance : MapArea {

    private const val BASE_REGION_1 = Regions.PHOENIX_LAIR_13905
    private const val BASE_REGION_2 = Regions.PHOENIX_LAIR_14161

    private val BASE_1 = Location.create(3456, 5184, 0)
    private val BASE_2 = Location.create(3520, 5184, 0)

    private var region1: DynamicRegion? = null
    private var region2: DynamicRegion? = null

    private val SAFE_EXIT = Location.create(3460, 5200, 0)
    private val playerRegions = ConcurrentHashMap<Player, DynamicRegion>()

    fun create() {
        if (region1 == null) region1 = DynamicRegion.create(BASE_REGION_1, BASE_REGION_2)
        if (region2 == null) region2 = DynamicRegion.create(BASE_REGION_2, BASE_REGION_1)

        region1!!.link(region2!!)

        spawnObjects(region1!!, InPyreNeed.NPC_RESPAWNS_REGION_0)
        spawnObjects(region2!!, InPyreNeed.NPC_RESPAWNS_REGION_1)
    }

    fun init(player: Player) {
        create()
        val region = region1!!
        player.teleport(region.baseLocation.transform(BASE_1.x - BASE_1.x, BASE_1.y - BASE_1.y, 0))
        playerRegions[player] = region
    }

    fun check(player: Player) {
        val currentRegion = playerRegions[player] ?: return
        val localPlayers = getLocalPlayersMaxNorm(player.location, 64)
        if (localPlayers.contains(player)) return
        val targetRegion = if (currentRegion == region1) region2!! else region1!!
        val newLocation =
            targetRegion.baseLocation.transform(
                player.location.x - currentRegion.baseLocation.x,
                player.location.y - currentRegion.baseLocation.y,
                player.location.z
            )
        player.teleport(newLocation)
        playerRegions[player] = targetRegion
    }

    private fun spawnObjects(region: DynamicRegion, NPCs: Array<Location>) {
        NPCs.forEach { loc ->
            val npcId = InPyreNeed.REBORN_WARRIOR_ID.random()
            val npc = RebornWarriorNPC(npcId)
            val tile = region.baseLocation.transform(loc.x - BASE_1.x, loc.y - BASE_1.y, 0)
            RegionManager.forId((tile.regionX shl 8) or tile.regionY).add(npc)
            npc.init()
        }

        InPyreNeed.TREE_LOCATION_MAP.forEach { loc ->
            val tile = region.baseLocation.transform(loc.x - BASE_1.x, loc.y - BASE_1.y, 0)
            InPyreNeed.TREE_SCENERY_ID.forEach { locID -> SceneryBuilder.add(Scenery(locID, tile)) }
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            entity.teleport(SAFE_EXIT)
            playerRegions.remove(entity)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(ZoneBorders.forRegion(BASE_REGION_1), ZoneBorders.forRegion(BASE_REGION_2))

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            playerRegions[entity] = region1!!
        }
    }
}
