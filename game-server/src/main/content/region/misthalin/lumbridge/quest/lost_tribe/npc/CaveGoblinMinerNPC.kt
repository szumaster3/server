package content.region.misthalin.lumbridge.quest.lost_tribe.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class CaveGoblinMinerNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var mining = false
    var originallyMiner = false
    val forceChat = arrayOf("Nooo!", "Eeek!")

    init {
        originallyMiner = id > NPCs.CAVE_GOBLIN_GUARD_2073
    }

    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC = CaveGoblinMinerNPC(id, location)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.CAVE_GOBLIN_MINER_2078,
            NPCs.CAVE_GOBLIN_MINER_2077,
            NPCs.CAVE_GOBLIN_MINER_2076,
            NPCs.CAVE_GOBLIN_MINER_2075,
            NPCs.CAVE_GOBLIN_MINER_2072,
            NPCs.CAVE_GOBLIN_MINER_2071,
            NPCs.CAVE_GOBLIN_MINER_2070,
            NPCs.CAVE_GOBLIN_MINER_2069,
        )

    override fun tick() {
        mining = (id > 2074)

        if (properties.combatPulse.isAttacking && mining) {
            transform(id - 6)
            this.isWalks = true
            this.walkRadius = 4
            this.isNeverWalks = false
            sendChat(forceChat.random())
        }
        super.tick()
    }

    override fun finalizeDeath(killer: Entity?) {
        if (!mining && originallyMiner) {
            transform(id + 6)
            this.isWalks = false
            this.walkRadius = 0
            this.isNeverWalks = true
        }
        super.finalizeDeath(killer)
    }
}
