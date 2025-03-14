package core.api.skill

import content.data.items.SkillingTool
import core.api.amountInInventory
import core.api.getWorldTicks
import core.game.dialogue.SkillDialogueHandler
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * A builder class for constructing a skill dialogue, allowing customization of the items displayed,
 * as well as the logic for item creation and maximum amount calculations.
 */
class SkillDialogueBuilder {
    internal lateinit var player: Player
    internal var items: Array<Item> = arrayOf<Item>()
    internal var creationCallback: (itemId: Int, amount: Int) -> Unit = { _, _ -> }
    internal var totalAmountCallback: (itemId: Int) -> Int = { id -> amountInInventory(player, id) }

    /**
     * Sets the items to be included in the skill dialogue.
     *
     * @param item The items to be added to the dialogue.
     */
    fun withItems(vararg item: Item) {
        items = arrayOf(*item)
    }

    /**
     * Sets the items to be included in the skill dialogue by their item ids.
     *
     * @param item The item ids to be added to the dialogue.
     */
    fun withItems(vararg item: Int) {
        items = item.map { Item(it) }.toTypedArray()
    }

    /**
     * Sets the callback method that will be invoked when an item is created.
     *
     * @param method The method that takes an item id and the amount of that item.
     */
    fun create(method: (itemId: Int, amount: Int) -> Unit) {
        creationCallback = method
    }

    /**
     * Sets the callback method that calculates the total amount of an item available for the player.
     *
     * @param method The method that calculates the amount of an item.
     */
    fun calculateMaxAmount(method: (itemId: Int) -> Int) {
        totalAmountCallback = method
    }
}

/**
 * Sends a skill dialogue to the player, allowing the player to choose from a set of items for a skill interaction.
 *
 * The skill dialogue is constructed based on the provided builder, and validation is performed to ensure
 * the correct number of items is passed.
 *
 * @param player The player who will receive the skill dialogue.
 * @param init The lambda block to initialize the skill dialogue using the builder.
 * @throws IllegalStateException if the number of items in the dialogue is not between 1 and 5.
 */
fun sendSkillDialogue(
    player: Player,
    init: SkillDialogueBuilder.() -> Unit,
) {
    val builder = SkillDialogueBuilder()
    builder.player = player
    builder.init()

    if (builder.items.size !in 1..5) {
        throw IllegalStateException(
            "Invalid number of items passed to skill dialogue (min 1, max 5): ${builder.items.size}",
        )
    }

    val type =
        when (builder.items.size) {
            1 -> SkillDialogueHandler.SkillDialogue.ONE_OPTION
            2 -> SkillDialogueHandler.SkillDialogue.TWO_OPTION
            3 -> SkillDialogueHandler.SkillDialogue.THREE_OPTION
            4 -> SkillDialogueHandler.SkillDialogue.FOUR_OPTION
            5 -> SkillDialogueHandler.SkillDialogue.FIVE_OPTION
            else -> null
        }

    object : SkillDialogueHandler(player, type, *builder.items) {
        /**
         * Handles the creation of an item when selected in the dialogue.
         *
         * @param amount The amount of the selected item to be created.
         * @param index The index of the selected item.
         */
        override fun create(
            amount: Int,
            index: Int,
        ) {
            builder.creationCallback(builder.items[index].id, amount)
        }

        /**
         * Retrieves the total amount of the selected item available for the player.
         *
         * @param index The index of the selected item.
         * @return The total amount of the item available for the player.
         */
        override fun getAll(index: Int): Int {
            return builder.totalAmountCallback(builder.items[index].id)
        }
    }.open()
}

/**
 * Retrieves the appropriate skilling tool for the player.
 **
 * @param player The player for whom the tool is being retrieved.
 * @param pickaxe If true, the function returns a pickaxe; otherwise, it returns a hatchet.
 * @return The appropriate [SkillingTool] for the player, or null if none is available.
 */
fun getTool(
    player: Player,
    pickaxe: Boolean,
): SkillingTool? {
    return if (pickaxe) SkillingTool.getPickaxe(player) else SkillingTool.getHatchet(player)
}

/**
 * Delays an entity's clock by a specified number of ticks.
 *
 * @param entity The entity whose clock is being delayed.
 * @param clock The clock ID to update (defines which action or event is delayed).
 * @param ticks The number of game ticks to delay the clock by.
 * @return Always returns false.
 */
fun delayClock(
    entity: Entity,
    clock: Int,
    ticks: Int,
): Boolean {
    entity.clocks[clock] = getWorldTicks() + ticks
    return false
}

/**
 * Checks if the specified clock for an entity has expired and is ready for use.
 *
 * @param entity The entity whose clock is being checked.
 * @param clock The clock ID to check.
 * @return True if the clock is ready, otherwise false.
 */
fun clockReady(
    entity: Entity,
    clock: Int,
): Boolean {
    return entity.clocks[clock] <= getWorldTicks()
}

private class SkillAPI
