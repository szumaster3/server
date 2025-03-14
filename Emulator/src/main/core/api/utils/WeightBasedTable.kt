package core.api.utils

import content.data.tables.*
import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScrollPlugin
import content.global.handlers.item.equipment.gloves.FOGGlovesListener
import core.api.inEquipment
import core.cache.def.impl.ItemDefinition
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items

/**
 * Represents a table of items with assigned weights that can be rolled
 * to generate a random selection of items based on their weight.
 * Items are inserted into the table and can be rolled multiple times,
 * with guaranteed items included in every roll.
 */
open class WeightBasedTable : ArrayList<WeightedItem>() {
    var totalWeight = 0.0 // Total weight of all non-guaranteed items
    val guaranteedItems = ArrayList<WeightedItem>() // List of guaranteed items

    /**
     * Adds an item to the table. Guaranteed items are added to the `guaranteedItems` list.
     * Non-guaranteed items are added with their weight affecting the total weight.
     *
     * @param element The [WeightedItem] to add.
     * @return True if the item was successfully added, false otherwise.
     */
    override fun add(element: WeightedItem): Boolean {
        return if (element.guaranteed) {
            guaranteedItems.add(element)
        } else {
            totalWeight += element.weight
            val randIndex = RandomFunction.random(0, size)
            val end = this.size
            super.add(element)

            val temp = this[randIndex]
            this[randIndex] = element
            this[end] = temp
            true
        }
    }

    /**
     * Rolls the table once to generate a list of items.
     * Includes any guaranteed items in the roll.
     *
     * @param receiver The entity that will receive the items. Default is null.
     * @return A list of items rolled.
     */
    open fun roll(receiver: Entity? = null): ArrayList<Item> {
        return roll(receiver, 1)
    }

    /**
     * Rolls the table multiple times to generate a list of items.
     * Includes any guaranteed items in each roll.
     *
     * @param receiver The entity that will receive the items. Default is null.
     * @param times The number of times to roll.
     * @return A list of items rolled.
     */
    open fun roll(
        receiver: Entity? = null,
        times: Int = 1,
    ): ArrayList<Item> {
        val items = ArrayList<WeightedItem>((guaranteedItems.size + 1) * times)

        for (i in 0 until times) {
            items.addAll(guaranteedItems)

            if (size == 1) {
                items.add(get(0))
            } else if (isNotEmpty()) {
                var rngWeight = RandomFunction.randomDouble(totalWeight)
                for (item in this) {
                    rngWeight -= item.weight
                    if (rngWeight <= 0) {
                        items.add(item)
                        break
                    }
                }
            }
        }

        return convertWeightedItems(items, receiver)
    }

    /**
     * Converts the rolled weighted items into actual items.
     *
     * @param weightedItems The list of weighted items.
     * @param receiver The entity that will receive the items.
     * @return A list of items corresponding to the weighted items.
     */
    fun convertWeightedItems(
        weightedItems: ArrayList<WeightedItem>,
        receiver: Entity?,
    ): ArrayList<Item> {
        val safeItems = ArrayList<Item>()
        for (e in weightedItems) {
            val safeItem =
                when (e.id) {
                    SLOT_CLUE_EASY -> ClueScrollPlugin.getClue(ClueLevel.EASY)
                    SLOT_CLUE_MEDIUM -> ClueScrollPlugin.getClue(ClueLevel.MEDIUM)
                    SLOT_CLUE_HARD -> ClueScrollPlugin.getClue(ClueLevel.HARD)
                    SLOT_RDT -> RareDropTable.retrieve(receiver)
                    SLOT_CELEDT -> CELEMinorTable.retrieve(receiver)
                    SLOT_USDT -> UncommonSeedDropTable.retrieve(receiver)
                    SLOT_HDT -> {
                        if (RandomFunction.nextBool() && receiver is Player) {
                            if (inEquipment(receiver, Items.IRIT_GLOVES_12856)) {
                                FOGGlovesListener.updateCharges(receiver)
                                Item(Items.GRIMY_IRIT_209)
                            } else if (inEquipment(receiver, Items.AVANTOE_GLOVES_12857)) {
                                FOGGlovesListener.updateCharges(receiver)
                                Item(Items.GRIMY_AVANTOE_211)
                            } else if (inEquipment(receiver, Items.KWUARM_GLOVES_12858)) {
                                FOGGlovesListener.updateCharges(receiver)
                                Item(Items.GRIMY_KWUARM_213)
                            } else if (inEquipment(receiver, Items.CADANTINE_GLOVES_12859)) {
                                FOGGlovesListener.updateCharges(receiver)
                                Item(Items.GRIMY_CADANTINE_215)
                            } else {
                                HerbDropTable.retrieve(receiver)
                            }
                        } else {
                            HerbDropTable.retrieve(receiver)
                        }
                    }

                    SLOT_GDT -> GemDropTable.retrieve(receiver)
                    SLOT_RSDT -> RareSeedDropTable.retrieve(receiver)
                    SLOT_ASDT -> AllotmentSeedDropTable.retrieve(receiver)
                    Items.DWARF_REMAINS_0 -> continue
                    else -> e.getItem()
                }
            safeItems.add(safeItem ?: continue)
        }
        return safeItems
    }

    /**
     * Handles herb drop based on the equipment of the player.
     *
     * @param receiver The entity (player) that is receiving the item.
     * @return The herb item to be dropped.
     */
    private fun handleHerbDrop(receiver: Entity?): Item? {
        return if (receiver is Player && RandomFunction.nextBool()) {
            when {
                inEquipment(receiver, Items.IRIT_GLOVES_12856) -> {
                    FOGGlovesListener.updateCharges(receiver)
                    Item(Items.GRIMY_IRIT_209)
                }

                inEquipment(receiver, Items.AVANTOE_GLOVES_12857) -> {
                    FOGGlovesListener.updateCharges(receiver)
                    Item(Items.GRIMY_AVANTOE_211)
                }

                inEquipment(receiver, Items.KWUARM_GLOVES_12858) -> {
                    FOGGlovesListener.updateCharges(receiver)
                    Item(Items.GRIMY_KWUARM_213)
                }

                inEquipment(receiver, Items.CADANTINE_GLOVES_12859) -> {
                    FOGGlovesListener.updateCharges(receiver)
                    Item(Items.GRIMY_CADANTINE_215)
                }

                else -> HerbDropTable.retrieve(receiver)
            }
        } else {
            HerbDropTable.retrieve(receiver)
        }
    }

    /**
     * Checks if the player can roll the table based on their inventory space.
     *
     * @param player The player attempting to roll the table.
     * @return True if the player has space for the guaranteed items, otherwise false.
     */
    open fun canRoll(player: Player): Boolean {
        val guaranteed = guaranteedItems.map { it.getItem() }.toTypedArray()
        return (guaranteed.isNotEmpty() && player.inventory.hasSpaceFor(*guaranteed)) || !player.inventory.isFull
    }

    // Methods for inserting specific rolls into the table
    fun insertEasyClue(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_CLUE_EASY, 1, 1, weight, false))
        return this
    }

    fun insertMediumClue(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_CLUE_MEDIUM, 1, 1, weight, false))
        return this
    }

    fun insertHardClue(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_CLUE_HARD, 1, 1, weight, false))
        return this
    }

    fun insertRDTRoll(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_RDT, 1, 1, weight, false))
        return this
    }

    fun insertCELEDTRoll(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_CELEDT, 1, 1, weight, false))
        return this
    }

    fun insertSEEDDTRoll(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_USDT, 1, 1, weight, false))
        return this
    }

    fun insertHERBDTRoll(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_HDT, 1, 1, weight, false))
        return this
    }

    fun insertGDTRoll(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_GDT, 1, 1, weight, false))
        return this
    }

    fun insertRSDTRoll(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_RSDT, 1, 1, weight, false))
        return this
    }

    fun insertASDTRoll(weight: Double): WeightBasedTable {
        this.add(WeightedItem(SLOT_ASDT, 1, 1, weight, false))
        return this
    }

    companion object {
        /**
         * Creates a new [WeightBasedTable] from the specified items.
         *
         * @param items A variable number of [WeightedItem]s to add to the table.
         * @return A new [WeightBasedTable] instance.
         */
        @JvmStatic
        fun create(vararg items: WeightedItem): WeightBasedTable {
            val table = WeightBasedTable()
            items.forEach {
                table.add(it)
            }
            return table
        }

        // Slot identifiers for different types of items
        @JvmField
        val SLOT_RDT = Items.TINDERBOX_31
        val SLOT_CLUE_EASY = Items.TOOLKIT_1
        val SLOT_CLUE_MEDIUM = Items.ROTTEN_POTATO_5733
        val SLOT_CLUE_HARD = Items.GRANITE_LOBSTER_POUCH_12070
        val SLOT_CELEDT = Items.NULL_799
        val SLOT_USDT = Items.SACRED_CLAY_POUCH_CLASS_1_14422
        val SLOT_HDT = Items.SACRED_CLAY_POUCH_CLASS_2_14424
        val SLOT_GDT = Items.SACRED_CLAY_POUCH_CLASS_3_14426
        val SLOT_RSDT = Items.SACRED_CLAY_POUCH_CLASS_4_14428
        val SLOT_ASDT = Items.SACRED_CLAY_POUCH_CLASS_5_14430
    }

    /**
     * Returns a string representation of the table with the details of each item.
     *
     * @return A string description of the table.
     */
    override fun toString(): String {
        val builder = StringBuilder()
        for (item in this) {
            builder.append(
                "${ItemDefinition.forId(
                    item.id,
                ).name} || Weight: ${item.weight} || MinAmt: ${item.minAmt} || maxAmt: ${item.maxAmt}",
            )
            builder.appendLine()
        }
        return builder.toString()
    }
}
