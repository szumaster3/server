package content.global.plugin.item

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * Plugin that allows players to convert [Items.CLAY_434] into [Items.SOFT_CLAY_1761].
 */
@Initializable
class SoftclayPlugin : UseWithHandler(Items.CLAY_434) {

    // Maps water container id to its empty version after use.
    private val waterToEmptyMap = mapOf(
        Items.BOWL_OF_WATER_1921 to Items.BOWL_1923,
        Items.BUCKET_OF_WATER_1929 to Items.BUCKET_1925,
        Items.JUG_OF_WATER_1937 to Items.JUG_1935,
    )

    private val clayId = Items.CLAY_434
    private val softClayId = Items.SOFT_CLAY_1761

    override fun newInstance(arg: Any?): Plugin<Any> {
        waterToEmptyMap.keys.forEach { addHandler(it, ITEM_TYPE, this) }
        return this
    }

    /**
     * Handles using clay with a water container. If the player has only one clay,
     * the conversion occurs instantly. Otherwise, a dialogue is opened to choose amount.
     */
    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player

        val waterId = getWaterId(event) ?: return false
        val clayAmount = player.inventory.getAmount(clayId)

        if (clayAmount <= 1) {
            create(player, waterId)
        } else {
            val softClayItem = Item(softClayId)
            object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, softClayItem) {
                override fun create(amount: Int, index: Int) {
                    submitIndividualPulse(player, object : Pulse(2, player) {
                        var count = 0
                        override fun pulse(): Boolean {
                            if (!create(player, waterId)) return true
                            return ++count >= amount
                        }
                    })
                }

                override fun getAll(index: Int): Int = clayAmount
            }.open()
        }
        return true
    }

    /**
     * Converts one clay and one water container into soft clay.
     * Adds the corresponding empty container back to the inventory.
     *
     * @param player The player performing the action
     * @param waterId The ID of the water container used
     * @return `true` if the conversion was successful
     */
    private fun create(player: Player, waterId: Int): Boolean {
        val emptyId = waterToEmptyMap[waterId] ?: return false
        if (!player.inventory.containsAll(clayId, waterId)) return false

        removeItem(player, clayId)
        removeItem(player, waterId)
        addItem(player, softClayId)
        addItemOrDrop(player, emptyId)

        sendMessages(
            player,
            "You mix the clay and water.",
            "You now have some soft, workable clay."
        )
        return true
    }

    /**
     * Determines which item in the event is the water container.
     */
    private fun getWaterId(event: NodeUsageEvent): Int? {
        return listOf(event.usedItem.id, event.baseItem.id).firstOrNull { it in waterToEmptyMap }
    }
}
