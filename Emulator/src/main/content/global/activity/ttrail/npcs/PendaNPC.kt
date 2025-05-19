package content.global.activity.ttrail.npcs

import core.api.hasAnItem
import core.api.item.produceGroundItem
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Penda NPC.
 */
@Initializable
class PendaNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = PendaNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.PENDA_1087)

    /**
     * Handles drop clue key (medium) required to open locked chest (Treasure Trail).
     */
    override fun finalizeDeath(killer: Entity?) {
        (killer as? Player)?.takeIf {
            hasAnItem(it, Items.KEY_2836).container == null &&
                    it.inventory.containsItem(Item(Items.CLUE_SCROLL_10236, 1))
        }?.let {
            produceGroundItem(it, Items.KEY_2836, 1, location)
        }

        super.finalizeDeath(killer)
    }

}
