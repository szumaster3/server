package content.region.kandarin.quest.phoenix.handlers

import content.data.GameAttributes
import core.api.MapArea
import core.api.clearLogoutListener
import core.api.quest.isQuestComplete
import core.api.registerLogoutListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.NPCs
import org.rs.consts.Quests

val rebornWarriors =
    intArrayOf(
        NPCs.LESSER_REBORN_WARRIOR_8557,
        NPCs.LESSER_REBORN_WARRIOR_8558,
        NPCs.GREATER_REBORN_WARRIOR_8559,
        NPCs.GREATER_REBORN_WARRIOR_8560,
        NPCs.LESSER_REBORN_RANGER_8561,
        NPCs.LESSER_REBORN_RANGER_8562,
        NPCs.GREATER_REBORN_RANGER_8563,
        NPCs.GREATER_REBORN_RANGER_8564,
        NPCs.LESSER_REBORN_MAGE_8565,
        NPCs.LESSER_REBORN_MAGE_8566,
        NPCs.GREATER_REBORN_MAGE_8567,
        NPCs.GREATER_REBORN_MAGE_8568,
    )

class PhoenixLair : MapArea {
    private val spawnedNPC = mutableListOf<NPC>()

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders.forRegion(13905), ZoneBorders.forRegion(14161))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            val npcIdsToSpawn =
                if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {
                    listOf(
                        NPCs.LESSER_REBORN_MAGE_8573,
                        NPCs.LESSER_REBORN_RANGER_8571,
                        NPCs.LESSER_REBORN_WARRIOR_8569,
                    )
                } else {
                    rebornWarriors.toList()
                }

            respawnPoints.forEach { location ->
                val npcId = npcIdsToSpawn.random()
                val npc = NPC.create(npcId, location)

                npc.apply {
                    isNeverWalks = false
                    isWalks = true
                    isRespawn = false
                    isAggressive = true
                }

                npc.init()

                registerLogoutListener(player, logoutListener) {
                    npc.clear()
                    spawnedNPC.remove(npc)
                }

                player.incrementAttribute(GameAttributes.PHOENIX_LAIR_VISITED)
                spawnedNPC.add(npc)
            }
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)

        spawnedNPC.forEach { it.clear() }
        spawnedNPC.clear()

        if (logout) {
            clearLogoutListener(entity.asPlayer(), logoutListener)
        }
    }

    companion object {
        private val respawnPoints =
            arrayOf(
                Location.create(3464, 5242, 0),
                Location.create(3460, 5237, 0),
                Location.create(3484, 5234, 0),
                Location.create(3479, 5225, 0),
                Location.create(3475, 5220, 0),
                Location.create(3460, 5222, 0),
                Location.create(3494, 5241, 0),
                Location.create(3498, 5243, 0),
                Location.create(3504, 5241, 0),
                Location.create(3502, 5233, 0),
                Location.create(3492, 5224, 0),
                Location.create(3496, 5222, 0),
                Location.create(3509, 5223, 0),
                Location.create(3516, 5227, 0),
                Location.create(3515, 5242, 0),
                Location.create(3526, 5240, 0),
                Location.create(3526, 5244, 0),
                Location.create(3531, 5236, 0),
                Location.create(3534, 5233, 0),
                Location.create(3527, 5225, 0),
                Location.create(3535, 5220, 0),
                Location.create(3547, 5222, 0),
                Location.create(3547, 5232, 0),
                Location.create(3545, 5240, 0),
                Location.create(3465, 5210, 0),
                Location.create(3462, 5205, 0),
                Location.create(3466, 5204, 0),
                Location.create(3466, 5211, 0),
                Location.create(3471, 5210, 0),
                Location.create(3480, 5212, 0),
                Location.create(3482, 5205, 0),
                Location.create(3484, 5196, 0),
                Location.create(3475, 5190, 0),
                Location.create(3494, 5211, 0),
                Location.create(3498, 5202, 0),
                Location.create(3497, 5192, 0),
                Location.create(3493, 5188, 0),
                Location.create(3515, 5188, 0),
                Location.create(3513, 5196, 0),
                Location.create(3514, 5208, 0),
                Location.create(3505, 5203, 0),
                Location.create(3504, 5210, 0),
                Location.create(3474, 5242, 0),
                Location.create(3480, 5243, 0),
            )

        private const val logoutListener = "phoenix-lair"
    }
}
