package content.global.skill.crafting.casting.silver

import core.api.addItem
import core.api.openInterface
import core.api.removeItem
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery

class SilverCraftingListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.SILVER_BAR_2355, *furnaceIDs) { player, _, with ->
            setAttribute(player, ATTRIBUTE_FURNACE_ID, with)
            openInterface(player, Components.CRAFTING_SILVER_CASTING_438)
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.BALL_OF_WOOL_1759, *unstrungIDs) { player, used, with ->
            Silver.forId(with.id)?.let {
                if (removeItem(player, with.id) && removeItem(player, used.id)) {
                    addItem(player, it.strung)
                }
            }
            return@onUseWith true
        }
    }

    companion object {
        private val furnaceIDs =
            intArrayOf(
                Scenery.FURNACE_2966,
                Scenery.FURNACE_3044,
                Scenery.FURNACE_3294,
                Scenery.FURNACE_4304,
                Scenery.FURNACE_6189,
                Scenery.FURNACE_11009,
                Scenery.FURNACE_11010,
                Scenery.FURNACE_11666,
                Scenery.FURNACE_12100,
                Scenery.FURNACE_12809,
                Scenery.FURNACE_18497,
                Scenery.FURNACE_18525,
                Scenery.FURNACE_18526,
                Scenery.FURNACE_21879,
                Scenery.FURNACE_22721,
                Scenery.FURNACE_26814,
                Scenery.FURNACE_28433,
                Scenery.FURNACE_28434,
                Scenery.FURNACE_30021,
                Scenery.FURNACE_30510,
                Scenery.FURNACE_36956,
                Scenery.FURNACE_37651,
            )
        private val unstrungIDs = intArrayOf(Items.UNSTRUNG_SYMBOL_1714, Items.UNSTRUNG_EMBLEM_1720)
        private const val ATTRIBUTE_FURNACE_ID = "crafting:silver:furnace_id"
    }
}
