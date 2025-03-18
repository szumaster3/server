package content.global.handlers.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import kotlin.random.Random

@Initializable
class IceSpiderNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = IceSpiderNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val random = Random.nextInt(1, 10)
            if (random == 1) {
                GroundItemManager.create(Item(Items.SHARDS_OF_ARMADYL_14701, 1), getLocation(), killer.asPlayer())
            }
        }
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.ICE_SPIDER_64,
        )
}
