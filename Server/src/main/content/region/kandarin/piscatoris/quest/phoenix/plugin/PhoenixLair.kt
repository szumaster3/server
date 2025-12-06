package content.region.kandarin.piscatoris.quest.phoenix.plugin

import content.data.GameAttributes
import content.region.kandarin.piscatoris.quest.phoenix.InPyreNeed
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryType
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Regions

/**
 * Represents the Phoenix Lair area.
 *
 * TODO:
 *  instanced per player
 */
class PhoenixLair : MapArea {

    private val spawnedNPC = mutableListOf<NPC>()
    private val currentTreeLocations = mutableMapOf<Int, Location>()

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders.forRegion(Regions.PHOENIX_LAIR_13905), ZoneBorders.forRegion(Regions.PHOENIX_LAIR_14161))
    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)

    init {
        respawnTreesGlobal()
    }


    private fun respawnAllTrees() {
        val locationsPool = InPyreNeed.TREE_LOCATION_MAP.toMutableSet()

        InPyreNeed.TREE_SCENERY_ID.forEach { treeId ->
            currentTreeLocations[treeId]?.let { oldLoc ->
                removeScenery(Scenery(treeId, oldLoc))
                locationsPool.add(oldLoc)
            }

            val newLoc = locationsPool.random()
            locationsPool.remove(newLoc)

            addScenery(treeId, newLoc, 0, SceneryType.CentrepieceStraight)
            currentTreeLocations[treeId] = newLoc
        }
    }

    private fun respawnTreesGlobal() {
        respawnAllTrees()
    }

    override fun areaEnter(entity: Entity) {
        if (entity !is Player) return
        val player = entity.asPlayer()

        val nearbyPlayers = RegionManager.getLocalPlayers(player, 20).filter { it != player }
        if (nearbyPlayers.isNotEmpty()) return

        if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {

            if (!spawnedNPC.any { it.id == InPyreNeed.WOUNDED_PHOENIX_ID.id }) {
                val wounded = InPyreNeed.WOUNDED_PHOENIX_ID

                wounded.apply {
                    isNeverWalks = true
                    isWalks = false
                    isAggressive = false
                    isRespawn = false
                }

                wounded.init()
                registerLogoutListener(player, InPyreNeed.LOGOUT_LISTENER) {
                    wounded.clear()
                    spawnedNPC.remove(wounded)
                }

                spawnedNPC.add(wounded)
            }
        }

        if (getAttribute(player, GameAttributes.PHOENIX_LAIR_ACTIVITY_REWARD, false)) {
            if (!spawnedNPC.any { it.id == NPCs.PHOENIX_8548 }) {

                val rewardNPC = NPC.create(NPCs.PHOENIX_8548, Location.create(3536, 5197, 0))

                rewardNPC.init()
                spawnedNPC.add(rewardNPC)
            }
        }


        val npcIdsToSpawn = if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {
            listOf(
                NPCs.LESSER_REBORN_MAGE_8573,
                NPCs.LESSER_REBORN_RANGER_8571,
                NPCs.LESSER_REBORN_WARRIOR_8569,
            )
        } else {
            InPyreNeed.REBORN_WARRIOR_ID.toList()
        }

        InPyreNeed.NPC_RESPAWNS.forEach { location ->
            val npcId = npcIdsToSpawn.random()
            val npc = NPC.create(npcId, location)

            npc.apply {
                isNeverWalks = false
                isWalks = true
                isRespawn = true
                isAggressive = true
            }

            npc.init()

            registerLogoutListener(player, InPyreNeed.LOGOUT_LISTENER) {
                npc.clear()
                spawnedNPC.remove(npc)
            }

            spawnedNPC.add(npc)
        }

        player.setAttribute(GameAttributes.PHOENIX_LAIR_VISITED, true)
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)

        spawnedNPC.forEach { it.clear() }
        spawnedNPC.clear()

        if (logout) {
            clearLogoutListener(entity.asPlayer(), InPyreNeed.LOGOUT_LISTENER)
        }
    }
}