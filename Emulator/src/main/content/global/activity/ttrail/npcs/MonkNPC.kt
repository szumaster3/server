package content.global.activity.ttrail.npcs

import core.api.hasAnItem
import core.api.inBorders
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
 * Represents the Monk (Monastery) NPC.
 */
@Initializable
class MonkNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MonkNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.MONK_281)

    /**
     * Handles drop clue key (medium) required to open locked chest (Treasure Trail).
     */
    override fun finalizeDeath(killer: Entity?) {
        (killer as? Player)?.takeIf {
            inBorders(it, 2590, 3203, 2622, 3221) &&
                    hasAnItem(it, Items.KEY_2834).container == null &&
                    it.inventory.containsItem(Item(Items.CLUE_SCROLL_10236, 1))
        }?.let {
            produceGroundItem(it, Items.KEY_2834, 1, location)
        }
        super.finalizeDeath(killer)
    }

}