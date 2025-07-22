package core.api.utils

import core.game.node.entity.Entity
import core.game.node.item.Item

/**
 * Represents an NPC drop table with weighted drops,
 * including separate tables for charms and tertiary drops.
 */
class NPCDropTable : WeightBasedTable() {
    private val charmDrops = WeightBasedTable()
    private val tertiaryDrops = WeightBasedTable()

    /**
     * Adds an item to the charm drop table.
     *
     * @param element The weighted item to add.
     * @return True if added successfully, false otherwise.
     */
    fun addToCharms(element: WeightedItem): Boolean = charmDrops.add(element)

    /**
     * Adds an item to the tertiary drop table.
     *
     * @param element The weighted item to add.
     * @return True if added successfully, false otherwise.
     */
    fun addToTertiary(element: WeightedItem): Boolean = tertiaryDrops.add(element)

    /**
     * Rolls the drop tables to determine what items are dropped.
     * Rolls charm drops, tertiary drops, and the base drop table.
     *
     * @param receiver The entity receiving the drops (can be null).
     * @param times How many times to roll the drops.
     * @return A list of dropped items.
     */
    override fun roll(
        receiver: Entity?,
        times: Int,
    ): ArrayList<Item> {
        val items = ArrayList<Item>()
        items.addAll(charmDrops.roll(receiver, times))
        items.addAll(tertiaryDrops.roll(receiver, times))
        items.addAll(super.roll(receiver, times))
        return items
    }
}
