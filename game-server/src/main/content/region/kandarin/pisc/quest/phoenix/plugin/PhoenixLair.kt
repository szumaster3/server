package content.region.kandarin.pisc.quest.phoenix.plugin

import content.data.GameAttributes
import content.region.kandarin.pisc.quest.phoenix.InPyreNeed
import core.api.*
import core.api.isQuestComplete
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Phoenix Lair area.
 *
 * TODO:
 *  instanced per player
 */
class PhoenixLair : MapArea {

    private val spawnedNPC = mutableListOf<NPC>()
    private val currentTreeLocations = mutableMapOf<Int, Location>()

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders.forRegion(13905), ZoneBorders.forRegion(14161))
    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)

    init {
        respawnTreesGlobal()
        InPyreNeed.WOUNDED_PHOENIX_ID.init()
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

            addScenery(treeId, newLoc, 0, 22)
            currentTreeLocations[treeId] = newLoc
        }
    }

    private fun respawnTreesGlobal() {
        respawnAllTrees()
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
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
                    isRespawn = false
                    isAggressive = true
                }

                npc.init()

                registerLogoutListener(player, InPyreNeed.LOGOUT_LISTENER) {
                    npc.clear()
                    spawnedNPC.remove(npc)
                }

                player.incrementAttribute(GameAttributes.PHOENIX_LAIR_VISITED)
                spawnedNPC.add(npc)
            }
        }
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